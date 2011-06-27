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

package org.vipervm.runtime.ast

import scala.util.Random

class Context(bindings:List[(String,Binding)]) {

  def this() = this(Nil)

  lazy val length: Int = bindings.length

  private lazy val rand = new Random

  /**
   * Find a string similar to "name" that is not already present in this context
   * @return Name found and context containing this new binding
   */
  def pickFreshName(name:String):(Context,String) = {
    val orig = if (name != "") name else "r"
    val name2 = freshName(orig,orig)
    (new Context((name2,NameBind) :: bindings), name2)
  }

  protected def freshName(name:String, orig:String):String = {
    if (!bindings.exists(a => a._1 == name))
      name
    else
      freshName(orig+rand.nextInt(1000),orig)
  }

  def indexToName(index:Int):String = {
    val (name,_) = bindings(index)
    name
  }
}
