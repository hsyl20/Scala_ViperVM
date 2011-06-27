package org.vipervm.dsl.linearalgebra

trait Addable[A] {
  def -(a:A):A
  def +(a:A):A
}

trait Multipliable[A] {
  def *(a:A):A
}

trait Divisable[A] {
  def /(a:A):A
}
