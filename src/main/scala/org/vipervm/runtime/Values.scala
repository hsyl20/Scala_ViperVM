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

import org.vipervm.platform.{Event,FutureEvent,DummyEvent}

sealed abstract class Value

case class DoubleValue(value:DataRef[Double]) extends Value
case class FloatValue(value:DataRef[Float]) extends Value
case class IntValue(value:DataRef[Int]) extends Value
case class DataValue(value:Data) extends Value

class DataRef[T] {
  
  private var fvalue:T = null.asInstanceOf[T]

  def value_=(value:T):Unit = fvalue = value

  def apply():T = fvalue

  def value:T = fvalue
}

class FutureValue(val value:Value, event:Event) extends FutureEvent(value,event) {
  def this(value:Value) = this(value,DummyEvent)
}
