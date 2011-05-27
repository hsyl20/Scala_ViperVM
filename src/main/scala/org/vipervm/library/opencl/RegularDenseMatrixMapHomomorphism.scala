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

package org.vipervm.library.opencl

import org.vipervm.data.RegularDenseMatrix
import org.vipervm.data.morphisms.Homomorphism

/**
 * Homomorphism map operation on a regular dense matrix
 *
 * This operation can be performed in-place
 */
class RegularDenseMatrixMapHomomorphism(matrix:RegularDenseMatrix, f:Homomorphism) {
  assert(f.domain == matrix.elementType)
}
