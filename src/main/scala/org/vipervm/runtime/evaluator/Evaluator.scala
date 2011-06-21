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

package org.vipervm.runtime.evaluator

import org.vipervm.runtime.Data

/**
 * Evaluate a functional task graph (sequential)
 */
class Evaluator {
  def eval(term:Term):Term = term match {
    case DataTerm(_) => term
    case KernelApply(k,as) => {
      if (k.paramCount < as.length) {
        throw new InvalidProgram("Too many parameter for kernel %s".format(k))
      }
      else if (k.paramCount != as.length) {
        //TODO: currying
        throw new InvalidProgram("Currying for kernel %s isn't supported yet".format(k))
      }
      else {
        val eas = as.map(eval _)
        if (eas.exists(a => !isData(a))) throw new InvalidProgram("Evaluation of kernel parameter to something other than a data")
        val datas = eas.map(asData _)


        val task = k.createTask(datas)

        //TODO
        println("Scheduling execution %s = %s(%s)".format(task.output,k,task.input.mkString(",")))

        DataTerm(task.output)
      }
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
