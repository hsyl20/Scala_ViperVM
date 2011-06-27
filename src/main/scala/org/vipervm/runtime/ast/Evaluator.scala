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

package org.vipervm.runtime.ast

import org.vipervm.runtime.Data

/**
 * Evaluate a functional task graph (sequential)
 */
class Evaluator {
  def eval(term:Term):Term = term match {

    case DataTerm(_) => term

    case AppTerm(KernelTerm(k),as) => {
      val eas = eval(as)
      if (!isData(eas)) throw new InvalidProgram("Evaluation of kernel parameter to something other than a data")
      val data = asData(eas)

      //FIXME: multi-arg kernels
      val task = k.createTask(List(data))

      //TODO
      println("Scheduling execution %s = %s(%s)".format(task.output,k,task.input.mkString(",")))

      DataTerm(task.output)
    }
  }

  private def isData(term:Term):Boolean = term match {
    case DataTerm(_) => true
    case _ => false
  }

  private def asData(term:Term):Data = term match {
    case DataTerm(d) => d
    case _ => throw new Exception("Invalid conversion: term isn't a data")
  }

}


class InvalidProgram(str:String) extends Exception(str)
