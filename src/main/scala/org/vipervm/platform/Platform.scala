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

package org.vipervm.platform

/** This class gives a unified representation of the runtime platform.
 *
 * Drivers are to be registered into the platform to be used
 */
class Platform(val drivers:Driver*) {

  /* Detect host driver */
  val hostDriver = drivers.toList.filter(_.isInstanceOf[HostDriver]) match {
    case d :: Nil => d.asInstanceOf[HostDriver]
    case Nil => throw new Exception("Platform must be configured to use an host driver")
    case _ => throw new Exception("Platform has been configured to use more than one host driver")
  }

  /** Host memories (useful for NUMA hosts) */
  val hostMemories = hostDriver.memories

  /** Host memory
   * 
   * Use hostMemories on NUMA hosts
   */
  val hostMemory = hostMemories.head
   
  /**
   * Memory nodes (including host memories)
   */
  def memories: Seq[MemoryNode] = drivers.flatMap(_.memories)

  /**
   * Networks
   */
  def networks: Seq[Network] = drivers.flatMap(_.networks)

  /**
   * Processors
   */
  def processors: Seq[Processor] = drivers.flatMap(_.processors)

  /**
   * Processors that can work in the given memory
   */
  def processorsThatCanWorkIn(memory:MemoryNode) = processors.filter(_.memories contains memory)

  /**
   * Direct links between two memory nodes, if any
   */
  def linksBetween(source:MemoryNode,target:MemoryNode):Seq[Link] = 
    networks.flatMap(_.link(source,target))

  /**
   * Direct links between two buffers, if any
   */
  def linksBetween(source:Buffer,target:Buffer):Seq[Link] = 
    linksBetween(source.memory, target.memory)

  /**
   * First direct link between two memory nodes, if any
   */
  def linkBetween(source:MemoryNode,target:MemoryNode):Option[Link] = 
    linksBetween(source,target).headOption

  /**
   * First direct link between two buffers, if any
   */
  def linkBetween(source:Buffer,target:Buffer):Option[Link] = 
    linksBetween(source,target).headOption

  /**
   * First direct link between two views, if any
   */
  def linkBetween(source:BufferView,target:BufferView):Option[Link] = 
    linksBetween(source.buffer,target.buffer).headOption

  /** Shutdown drivers */
  def shutdown:Unit = drivers.foreach(_.shutdown)
}

object Platform {
  def apply(drivers:Driver*):Platform = new Platform(drivers:_*)
}
