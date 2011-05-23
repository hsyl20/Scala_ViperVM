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

package org.vipervm.components

abstract class ExprComponent extends CompositeComponent {

   implicit def portex2(p:Port) = new {
      def + (p2:Port): Port = opbind(new PlusComponent,p,p2)
      def - (p2:Port): Port = opbind(new SubComponent,p,p2)
      def * (p2:Port): Port = opbind(new MulComponent,p,p2)
      def / (p2:Port): Port = opbind(new DivComponent,p,p2)

      private def opbind(c:OpComponent,e1:Port,e2:Port): Port = {
         e1 --> c.in1
         e2 --> c.in2
         c.out
      }
   }
}

abstract class OpComponent extends Component {
   val in1 = new Port
   val in2 = new Port
   val out = new Port
}


class PlusComponent extends OpComponent
class SubComponent extends OpComponent
class MulComponent extends OpComponent
class DivComponent extends OpComponent
