/*                                                  *\
** \ \     / _)                   \ \     /   \  |  **
**  \ \   /   |  __ \    _ \   __| \ \   /   |\/ |  **
**   \ \ /    |  |   |   __/  |     \ \ /    |   |  **
**    \_/    _|  .__/  \___| _|      \_/    _|  _|  **
**              _|                                  **
**                                                  **
**       ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~          **
**                                                  **
**         http://www.hsyl20.fr/vipervm             **
**                     GPLv3                        **
\*                                                  */

package fr.hsyl20.vipervm.runtime

import fr.hsyl20.vipervm.runtime.AccessMode._
import scala.collection._

/** This represents a mutable data. That is, a data where
 * coherency between buffers needs to be managed.
 */
abstract class MutableData extends Data {
  import Data._

   /**
    * Indicate that a buffer now contain valid data (typically after a copy)
    */
   def valid(buffer:Buffer): Unit = status.update(buffer, Ready)

   /**
    * This method is called before an access is to be made
    * on buffer associated to this data
    */
   override def notifyBeforeAccess(buffer:Buffer, mode:AccessMode): Unit = {

      val currentState = status(buffer)

      (currentState,mode) match {
         case (Invalid,_) => throw new Exception("Trying to access buffer in invalid state")
         case (Ready,ReadOnly) => status.update(buffer,Shared(1))
         case (Ready,_) => {
            status.mapValues(_ => Invalid)
            status.update(buffer,Exclusive)
         }
         case (Shared(n),ReadOnly) => status.update(buffer,Shared(n+1))
         case (Shared(_),_) => throw new Exception("Trying to access buffer in Write mode while it is in Read mode")
         case (Exclusive,_) => throw new Exception("Trying to access buffer in exclusive mode")
      }
   }

   /**
    * This method is called after an access is to be made
    * on buffer associated to this data
    */
   override def notifyAfterAccess(buffer:Buffer, mode:AccessMode): Unit = {

      val currentState = status(buffer)

      (currentState,mode) match {
         case (Shared(n),ReadOnly) if n > 1 => status.update(buffer,Shared(n-1))
         case (Shared(n),ReadOnly)  => status.update(buffer,Ready)
         case (Exclusive,_) => status.update(buffer,Ready)
         case _ => error("Buffer got an invalid state while being processed")
      }
   }

   override def addBuffer(buffer:Buffer): Unit = {
      super.addBuffer(buffer)
      status += (buffer -> Invalid)
   }

   override def removeBuffer(buffer:Buffer): Option[Buffer] = {
      status.remove(buffer)
      super.removeBuffer(buffer)
   }
}


