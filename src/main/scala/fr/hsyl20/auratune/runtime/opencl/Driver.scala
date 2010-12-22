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

package fr.hsyl20.auratune.runtime.opencl

import fr.hsyl20.auratune.runtime.{Driver,Device}
import fr.hsyl20.{opencl => cl}

/** OpenCL Driver
 */
class OpenCLDriver extends Driver {
   
   def devices:Seq[Device] = for (p <- cl.OpenCL.platforms ; peer <- p.devices())
      yield new OpenCLDevice(peer)

}
