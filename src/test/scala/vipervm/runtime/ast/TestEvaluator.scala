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

import org.scalatest.FunSuite

import org.vipervm.runtime.data.RawData
import org.vipervm.runtime.ast._
import org.vipervm.runtime.{FunctionalKernel,Data,KernelParameter}
import org.vipervm.platform.{Kernel,ReadOnly,ReadWrite,Processor,AccessMode}

class TestAstEvaluator extends FunSuite {

  test("Data are returned as-is") {
    val d = new RawData(1024)
    val prog = DataTerm(d)
    val evaluator = new Evaluator
    assert( evaluator.eval(prog) == prog )
  }

  test("Kernel applications return data") {
    val k = new DummyFunctionalKernel(new DummyKernel)
    val d1 = DataTerm(new RawData(1024))
    val d2 = DataTerm(new RawData(512))
    val prog = AppTerm(AppTerm(KernelTerm(k),d1),d2)
    val evaluator = new Evaluator
    assert( evaluator.eval(prog).isInstanceOf[DataTerm] )
  }
}


class DummyFunctionalKernel(kernel:Kernel) extends FunctionalKernel(kernel) {
  val paramCount = 2
  
  def pre(input:List[Data],output:Data):Seq[KernelParameter] = Nil

  def post(ks:Seq[KernelParameter],output:Data):Unit = {}

  protected def createOutput(input:List[Data]):Data = new RawData(10)
}

class DummyKernel extends Kernel {
  def canExecuteOn(proc:Processor): Boolean = true

  val param_modes: Array[AccessMode] = Array(ReadOnly,ReadOnly,ReadWrite)
}
