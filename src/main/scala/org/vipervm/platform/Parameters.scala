package org.vipervm

package object platform {
  abstract class ParameterType[A : Manifest] {
    def name:String = manifest[A].toString
  }
  implicit object IntParameter extends ParameterType[Int]
  implicit object LongParameter extends ParameterType[Long]
  implicit object FloatParameter extends ParameterType[Float]
  implicit object DoubleParameter extends ParameterType[Double]
  implicit object BufferParameter extends ParameterType[Buffer]
}
