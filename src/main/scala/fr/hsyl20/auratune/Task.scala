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
**
*/

package fr.hsyl20.auratune

import fr.hsyl20.opencl.Device

/**
 * A task ready to be executed. This class contains references to codelet arguments
 * and various parameters (group size...)
 */
class Task {
   def executableOn(d:Device): Boolean = true
}
