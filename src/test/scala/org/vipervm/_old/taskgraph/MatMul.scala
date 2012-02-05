/*                                                  *\
** \ \     / _)                   \ \     /   \  |  **
**  \ \   /   |  __ \    _ \   __| \ \   /   |\/ |  **
**   \ \ /    |  |   |   __/  |     \ \ /    |   |  **
**    \_/    _|  .__/  \___| _|      \_/    _|  _|  **
**              _|                                  **
**                                                  **
**       ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~          **
**                                                  **
**            http://www.vipervm.org                **
**                     GPLv3                        **
\*                                                  */

package org.vipervm.tests

import org.vipervm.platform.{ReadOnly,ReadWrite}
import org.vipervm.taskgraph._
import scalaz._
import Scalaz._

class MatMul(val a:Data, val b:Data, val c:Data) extends Task{
  val name = "matmul"
  val args = Seq(a,b,c)
  val argModes = Seq(ReadOnly,ReadOnly,ReadWrite)


  def lineSplit = {
    val ls = new LineSplit(10)
    val as = ls(a)
    val cs = ls(c)
    val tasks = (as zip cs) map (t => new MatMul(t._1, b, t._2))
    new TaskGraph(tasks, Nil)
  }

  def columnSplit = {
    val cols = new ColumnSplit(10)
    val bs = cols(b)
    val cs = cols(c)
    val tasks = (bs zip cs) map (t => new MatMul(a, t._1, t._2))
    new TaskGraph(tasks, Nil)
  }

  def blockSplit:TaskGraph = {
    val blocks = new BlockSplit(3,3)
    val as = blocks(a)
    val bs = blocks(b) //TODO: should be b.transpose
    val cs = blocks(c)
    def red(as:Seq[Data],bs:Seq[Data],c:Data) = {
      val lc = as zip bs
      val mulTasks = lc map (a => new MatMul(a._1,a._2, new TemporaryData(a._1.desc)))
      val red = new Reduction((a,b,c) => new MatAdd(a,b,c), mulTasks.map(_.c), c)
      val deps = mulTasks.map(t => (red,t))
      new TaskGraph(red +: mulTasks, deps)
    }
    val linecols = (as.zipWithIndex <|*|> bs.zipWithIndex)
    val gs = linecols.map(a => red(a._1._1, a._2._1, cs(a._1._2)(a._2._2)))
    new TaskGraph(gs.flatMap(_.tasks), gs.flatMap(_.deps))
  }

  override val splits = Map(
    'lines -> lineSplit _,
    'columns -> columnSplit _,
    'blocks -> blockSplit _
    )

  val source = """
  static void cpu_mult(void *descr[], __attribute__((unused))  void *arg)
  {
    float *subA = (float *)STARPU_MATRIX_GET_PTR(descr[0]);
    float *subB = (float *)STARPU_MATRIX_GET_PTR(descr[1]);
    float *subC = (float *)STARPU_MATRIX_GET_PTR(descr[2]);

    unsigned nxC = STARPU_MATRIX_GET_NX(descr[2]);
    unsigned nyC = STARPU_MATRIX_GET_NY(descr[2]);
    unsigned nyA = STARPU_MATRIX_GET_NY(descr[0]);

    unsigned ldA = STARPU_MATRIX_GET_LD(descr[0]);
    unsigned ldB = STARPU_MATRIX_GET_LD(descr[1]);
    unsigned ldC = STARPU_MATRIX_GET_LD(descr[2]);

    printf("SGEMM: not implemented\n");
  //  CPU_GEMM("N", "N", nxC, nyC, nyA, (TYPE)1.0, subA, ldA, subB, ldB, (TYPE)0.0, subC, ldC);
  }

  static starpu_codelet codelet_matmul = {
    .where = STARPU_CPU,
    .cpu_func = cpu_mult,
    .nbuffers = 3,
  };
  """
}

