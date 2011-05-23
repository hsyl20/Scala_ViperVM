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

package org.vipervm.platform

/**
 * Data transfer
 *
 * @param link    Link handling the transfer
 * @param source  Source buffer view
 * @param target  Target buffer view
 * @param event   Event indicating data transfer completion
 */
class DataTransfer[+V <: BufferView](link:Link,source:V,target:V,event:Event) extends BindedEvent(event)
