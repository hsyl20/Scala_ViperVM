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

/**
 * Kernel being executed on a device
 *
 * @param kernel  Kernel and its parameters
 * @param device  Device executing the kernel
 * @param event   Event indicating kernel execution completion
 */
case class RunningKernel(kernel:ConfiguredKernel,device:Device,event:Event)
