/*                                                  *\
** \ \     / _)                   \ \     /   \  |  **
**  \ \   /   |  __ \    _ \   __| \ \   /   |\/ |  **
**   \ \ /    |  |   |   __/  |     \ \ /    |   |  **
**    \_/    _|  .__/  \___| _|      \_/    _|  _|  **
**              _|                                  **
**                                                  **
**       ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~          **
**                                                  **
**         http://www.hsyl20.fr/vipervm             **
**                     GPLv3                        **
\*                                                  */

package fr.hsyl20.vipervm.dsl.linearalgebra

import fr.hsyl20.vipervm.dsl._
import scala.collection.mutable.{HashSet,SynchronizedSet}

class LinearAlgebraEngine extends Engine {
  var actions = new HashSet[Action] with SynchronizedSet[Action]

  def terminated(action:Action) = actions.contains(action)

  def submit(action:Action):ActionStatus = {
    actions += action
    new LinearAlgebraActionStatus(this,action)
  }
}

class LinearAlgebraActionStatus(engine:LinearAlgebraEngine,action:Action) extends ActionStatus(action) {
  def terminated = engine.terminated(action)
}
