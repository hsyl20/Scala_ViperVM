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

package org.vipervm.dsl.fp

/**
 * A data that is or will be computed
 */
trait Data

/**
 * A memory buffer (mutable)
 */
trait Buffer


/**
 * A function (pure)
 */
trait Function


/**
 * Enqueue functions for execution
 */
trait Executor {
  /** Enqueue function for execution */
  def execute(f:Function, args:Seq[Data]): Seq[Data]
}

/** 
 * A "function" only working on primitive data types
 */
trait Kernel {
  def dataToBuffer(ds:Seq[Data]): Seq[Buffer]
}
