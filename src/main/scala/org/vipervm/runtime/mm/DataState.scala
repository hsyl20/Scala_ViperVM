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

package org.vipervm.runtime.mm

/**
 * State of a data in a memory
 *
 * @param available Data is available in memory
 * @param uploading Data is being uploaded into memory
 * @param users Number of users of this data
 * @param futureUsers Number of future users of this data
 */
case class DataState(available:Boolean=false, uploading:Boolean=false, users:Int=0, futureUsers:Int=0)
