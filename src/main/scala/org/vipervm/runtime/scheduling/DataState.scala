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

package org.vipervm.runtime.scheduling

sealed abstract class DataState
/** Data is present in memory */
case object DataAvailable extends DataState
/** Data isn't present in memory */
case object DataUnavailable extends DataState
/** Data is being transferred into memory */
case object DataIncoming extends DataState
/** Data is being transferred into another memory to prepare its eviction */
case object DataOutgoing extends DataState

