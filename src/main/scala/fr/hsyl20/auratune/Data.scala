/*
**
**      \    |  | _ \    \ __ __| |  |  \ |  __| 
**     _ \   |  |   /   _ \   |   |  | .  |  _|  
**   _/  _\ \__/ _|_\ _/  _\ _|  \__/ _|\_| ___| 
**
**       ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
**
**      OpenCL binding (and more) for Scala
**
**         http://www.hsyl20.fr/auratune
**                     GPLv3
*/

package fr.hsyl20.auratune

import scala.collection.mutable.Queue
import scala.collection.mutable
import java.nio.ByteBuffer
import fr.hsyl20.auratune.datatype.DataType


class Data(val size:Int) {
   /* Data instances on different devices */
   private var buffers: mutable.Map[Device, Buffer] = mutable.Map.empty
   
   /* Host-side buffer */
   var hostBuffer: Option[ByteBuffer] = None
   var hostValid: Boolean = false 

   /* Queue of requests for this data */
   val activeRequest: List[Request] = List.empty
   val pendingRequests: Queue[Request] = new Queue[Request]

   var computed: Boolean = false

   /**
    * Indicates that this data is required by a device with the
    * specified access mode
    *
    * When the buffer is ready on the device, returned event is triggered
    */
   def postTransferRequest(device:Device): TransferRequest = {
      val req = TransferRequest(device)
      pendingRequests += req

      req
   }

   def allocate(device:Device): Unit = {
      //TODO
   }

   def lockInDevice(device:Device): Boolean = buffers.get(device).map(_.retain).isDefined

   def unlockInDevice(device:Device): Boolean = buffers.get(device).map(_.release).isDefined

   def getBuffer(device:Device): Option[Buffer] = buffers.get(device)
}

object Data {
   def map(buffer:ByteBuffer): Data = new Data(buffer.capacity) {
      hostBuffer = Some(buffer)
      hostValid = true
   }

   /* Create a new Data from the specified Buffer.
    * Remove buffer from the origin Data */
   def fromBuffer(buffer:Buffer): Data = {
      val d = Data.fromData(buffer.data)
      
      val b = new Buffer(d, buffer.device) {
         override val peer = buffer.peer
      }

      d.buffers += (buffer.device -> b)
      buffer.data.buffers -= buffer.device
      d
   }

   /* Create a new Data with no active buffer
    * f with the same size as "data" argument
    */
   def fromData(data:Data): Data = {
      new Data(data.size)
   }

   def apply(datatype:DataType, size:Size): Data = size match {
      case NoSize => throw new Exception("Cannot create data with undefined size")
      case SizeValue(b,dims@_*) => new Data((b /: dims)(_*_))
   }

   def createBuffer(device:Device): BufferType
}

sealed abstract class Request extends Event
case class TransferRequest(device:Device) extends Request
