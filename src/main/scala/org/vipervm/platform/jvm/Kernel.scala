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

package org.vipervm.platform.jvm

import org.vipervm.platform._

/**
 * Kernel that can be executed on the JVM (Scala,Java,Groovy...)
 */

abstract class JVMKernel(val fun:Seq[Any] => Unit) extends Kernel {
  
  def canExecuteOn(proc:Processor): Boolean = proc match {
    case _:JVMProcessor => true
    case _ => false
  }
}
