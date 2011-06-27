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

package org.vipervm.library

import org.vipervm.runtime.FunctionalKernel

/**
 * Kernel library
 *
 * TODO: support packages
 */
class Library {

  private var functions: Map[String,FunctionalKernel] = Map.empty

  /** Try to retrieve a kernel by name */
  def retrieve(name:String): Option[FunctionalKernel] = {
    functions.get(name)
  }

  /** Add a kernel to the library */
  def add(name:String, kernel:FunctionalKernel): Unit = {
    functions += name -> kernel
  }

}
