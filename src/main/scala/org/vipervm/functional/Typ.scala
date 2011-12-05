package org.vipervm.functional

sealed abstract class Typ {

  def isConcrete:Boolean = this match {
    case Vect(Vect(_)) => false
    case _ => true
  }

}

case object Float extends Typ
case object Double extends Typ
case object Int extends Typ
case class Vect(v:Typ) extends Typ
