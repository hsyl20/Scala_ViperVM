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

package org.vipervm

import org.vipervm.platform.{Buffer,DummyEvent}
import org.vipervm.runtime.interpreter.{FutureValue,Value}

package object runtime {

  implicit def bufferWrapper(buffer:Buffer) = new BufferWrapper(buffer)

  implicit def value2futureValue(value:Value):FutureValue = new FutureValue(value,DummyEvent)
}

