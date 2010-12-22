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

/**
 * Group of Data to access with the specified access mode.
 *
 * Support device constraints (include, exclude) and piorities
 *
 * @param data Data and their associated access modes
 */
class DefaultDataState(val data:List[(Data,AccessMode)]) extends DataState {

  def this(data:(Data,AccessMode)*) = this(data.toList)

  /**
   * if "includedDevices" is not Nil, the DataState must only be
   * configured on a device contained in it. Otherwise any device
   * on the platform can be used.
   */
  val includedDevices:List[Device] = Nil

  /**
   * Devices that must not be used
   */
  val excludedDevices:List[Device] = Nil

  /**
   * Device priorities. 0 is default. Negative is lower priority and positive is higher priority
   */
  val priorities: Map[Device, Int] = Map.empty
}
