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

package org.vipervm.runtime

sealed abstract class Value extends FutureValue {
  def isAvailable = true

  def get = this
}

case class DoubleValue(value:Double) extends Value
case class FloatValue(value:Float) extends Value
case class IntValue(value:Int) extends Value
case class DataValue(value:Data) extends Value


trait FutureValue {
  def isAvailable: Boolean

  def get: Value
}
