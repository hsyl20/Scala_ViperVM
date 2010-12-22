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
 * A runtime system
 *
 * A runtime system is made of
 *  - a platform
 *  - a task scheduler
 *  - a data scheduler
 */
class Runtime {
  val platform:Platform
  val taskScheduler:Scheduler
  val dataScheduler:DataScheduler
}
