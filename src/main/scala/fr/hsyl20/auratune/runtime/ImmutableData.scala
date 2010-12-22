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

package fr.hsyl20.auratune.runtime

import fr.hsyl20.auratune.runtime.AccessMode._
import scala.collection._

/** This represents an immutable data.
 *
 * Every buffer must contain the same data.
 * Data can be available or unavailable on each memory node
 */
abstract class ImmutableData extends Data {
  import Data._

  override def notifyBeforeAccess(buffer:Buffer, mode:AccessMode): Unit = mode match {
    case ReadWrite | WriteOnly => throw new Exception("Trying to access immutable data in write mode")
    case ReadOnly => ()
  }
}

