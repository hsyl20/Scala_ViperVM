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

package fr.hsyl20.vipervm.dsl.linearalgebra

import fr.hsyl20.vipervm.dsl._

abstract class Matrix {
  def *(m:Matrix) = MatrixMultiplication(this,m)
  def +(m:Matrix) = MatrixAddition(this,m)
  def -(m:Matrix) = MatrixSubtraction(this,m)

  def saveToFile(filename:String)(implicit engine:LinearAlgebraEngine) = engine.submit(MatrixSaveToFile(this,filename))
}

case class MatrixSaveToFile(matrix:Matrix, filename:String) extends Action

case class MatrixAddition(m:Matrix,n:Matrix) extends Matrix
case class MatrixSubtraction(m:Matrix,n:Matrix) extends Matrix
case class MatrixMultiplication(m:Matrix,n:Matrix) extends Matrix
case class MatrixLoadFromFile(filename:String) extends Matrix
case class MatrixProduct(m:Matrix, n:Matrix) extends Matrix
case class MatrixFilled[A](value:A,dims:Long*) extends Matrix

object Matrix {
  def loadFromFile(filename:String) = MatrixLoadFromFile(filename)
  def filled[A](value:A,dims:Long*) = MatrixFilled(value, dims:_*)
}
