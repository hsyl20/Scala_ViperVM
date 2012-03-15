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

package org.vipervm.library

import org.vipervm.runtime.{Function,Rule}

class Library(functions:Seq[Function],rules:Seq[Rule]) {

  lazy val functionNames = functions.groupBy(_.prototype.name)
  lazy val ruleNames = rules.groupBy(_.prototype.name)

  def this(functions:Function*)(rules:Rule*) = this(functions.toList,rules.toList)

  def byName(name:String):Seq[Function] = functionNames.getOrElse(name, Seq.empty)

  def rulesByName(name:String):Seq[Rule] = ruleNames.getOrElse(name, Seq.empty)
}
