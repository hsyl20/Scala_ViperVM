package fr.hsyl20.auratune.dsl

abstract class Data

case class Map(d:Data, f:Expr => Expr) extends Data
