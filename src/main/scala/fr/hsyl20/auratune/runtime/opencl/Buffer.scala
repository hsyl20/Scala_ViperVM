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

import fr.hsyl20.auratune.runtime.Buffer
import fr.hsyl20.{opencl => cl}

/**
 * OpenCL buffer
 */
abstract class OpenCLBuffer extends Buffer {
   type MemoryNodeType <: OpenCLMemoryNode

   val peer: cl.Buffer
}
