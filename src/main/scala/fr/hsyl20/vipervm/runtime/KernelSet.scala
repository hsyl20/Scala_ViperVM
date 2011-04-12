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

package fr.hsyl20.vipervm.runtime

import fr.hsyl20.vipervm.platform.Kernel

/**
 * A kernel set is a set of kernels accomplishing the same task and
 * that can be run on different devices
 *
 * Added kernels must support kernel set prototype (i.e. kernel parameters)
 * @param prototype Prototype for every kernel in this set
 */
abstract class KernelSet(kernels:Seq[Kernel]) extends MetaKernel
