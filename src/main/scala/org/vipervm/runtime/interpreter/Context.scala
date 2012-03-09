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

package org.vipervm.runtime.interpreter

case class Context(bindings:List[(String,Binding)]) {
  def length = bindings.length

  def pickFreshName(name:String, hint:Int=0):(Context,String) = {
    val x = if (hint == 0) name else name+hint
    if (bindings.map(_._1).exists(_ == x)) pickFreshName(name,hint+1) else {
      val ctx = Context((x -> NameBind) :: bindings)
      (ctx,x)
    }
  }

  def indexToName(index:Int):String = bindings(index)._1
}

sealed abstract class Binding
case object NameBind extends Binding
