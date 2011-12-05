package org.vipervm.functional

/**
 * Base class for vectors
 */
sealed abstract class Vector[E] {
  /** Domain of definition */
  val domain:Range
}

/**
 * A vector whose values are stored in a buffer
 */
class StoredVector[E](val domain:Range,ptr:Pointer) extends Vector[E]

/**
 * A vector whose values may not be stored in a buffer
 */
class VirtualVector[E](val domain:Range,f:Int=>E) extends Vector[E]
