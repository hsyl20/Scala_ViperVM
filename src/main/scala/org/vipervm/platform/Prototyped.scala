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

package org.vipervm.platform

import org.vipervm.runtime.mm.{Data,DataInstance}

trait Prototyped {

  val prototype:List[Parameter[_]]

  protected implicit def paramExtractor(params:Seq[Any]) = new {
    def apply[A : Manifest](param:Parameter[A]) = try {
      params(prototype.indexOf(param)).asInstanceOf[A]
    }
    catch {
      case e => throw new Exception("Invalid parameter for \"%s\" at position %d. You should pass a %s that corresponds to the following description: %s)".format(param.name, prototype.indexOf(param), manifest[A].toString, param.description))
    }
  }

}
