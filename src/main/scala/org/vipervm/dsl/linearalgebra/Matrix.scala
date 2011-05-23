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

package org.vipervm.dsl.linearalgebra
/*
import org.vipervm.data._
import org.vipervm.dsl._

import java.io._

/** Wrapper adding Linear Algebra methods to matrix */
class LAMatrix[A <: PrimitiveType](that:Matrix[A]) {

  /** Matrix multiplication */
  def *(m:Matrix[A]) = {
    assume(that.width == m.height)
    MatrixMultiplication(that,m)
  }
  
  /** Matrix addition */
  def +(m:Matrix[A]) = {
    assume(that.width == m.width && that.height == m.height)
    MatrixAddition(that,m)
  }
  
  /** Matrix subtraction */
  def -(m:Matrix[A]) = {
    assume(that.width == m.width && that.height == m.height)
    MatrixSubtraction(that,m)
  }

  def saveToFile(filename:String)(implicit engine:LinearAlgebraEngine) = {
    engine.submit(MatrixSaveToFile(that,filename))
  }
}

case class MatrixSaveToFile[A<:PrimitiveType](matrix:Matrix[A], filename:String) extends Action

case class MatrixAddition[A<:PrimitiveType](m:Matrix[A],n:Matrix[A]) extends Matrix[A] {
  val width = m.width
  val height = m.height
}

case class MatrixSubtraction[A<:PrimitiveType](m:Matrix[A],n:Matrix[A]) extends Matrix[A] {
  val width = m.width
  val height = m.height
}

case class MatrixMultiplication[A<:PrimitiveType](m:Matrix[A],n:Matrix[A]) extends Matrix[A] {
  val width = n.width
  val height = m.height
}

case class MatrixFilled[A<:PrimitiveType](value:A, val width:Long, val height:Long) extends Matrix[A]

class MatrixFile[A<:PrimitiveType](stream:InputStream) extends Matrix[A] {
  private val s = new DataInputStream(stream)

  /* Check header */
  private val id = Array[Byte]('M','2','D','\0')
  s.readFully(id)
  if (!(id zip Array[Byte]('M','2','D','\0')).forall(a => a._1 == a._2))
    throw new Exception("Invalid file")

  val width = s.readLong
  val height = s.readLong
}


object Matrix {
  /** Load a matrix from a file */
  def loadFromStream[A<:PrimitiveType](stream:InputStream) = new MatrixFile[A](stream)

  /** Create a new matrix filled with a value */
  def filled[A<:PrimitiveType](value:A,width:Long,height:Long) = MatrixFilled[A](value,width,height)
}
*/
