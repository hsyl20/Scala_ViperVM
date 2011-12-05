package org.vipervm.functional

/**
 * A program
 */
class Program(val exprs:Map[String,Expr]) {

  def this(tuples:(String,Expr)*) = this(tuples.toMap)

}
