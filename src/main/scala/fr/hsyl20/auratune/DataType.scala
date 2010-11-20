package fr.hsyl20.auratune.opencl.datatype

trait DataType

trait Primitive extends DataType

case object Float extends Primitive
case object Double extends Primitive
case object Int extends Primitive
case object Char extends Primitive

class Matrix(base:Primitive) extends DataType

object Matrix {
   def apply(dt:Primitive): DataType = new Matrix(dt)
}
