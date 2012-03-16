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

import org.vipervm.platform.HostBuffer

package object mm {

  implicit object FloatPrim extends PrimType[Float](FloatType) {

    def set(hostBuffer:HostBuffer,offset:Long,value:Float):Unit = {
      hostBuffer.peer.setFloat(offset, value)
    }

    def get(hostBuffer:HostBuffer,offset:Long):Float = {
      hostBuffer.peer.getFloat(offset)
    }
  }

  implicit object DoublePrim extends PrimType[Double](DoubleType) {

    def set(hostBuffer:HostBuffer,offset:Long,value:Double):Unit = {
      hostBuffer.peer.setDouble(offset, value)
    }

    def get(hostBuffer:HostBuffer,offset:Long):Double = {
      hostBuffer.peer.getDouble(offset)
    }
  }

}
