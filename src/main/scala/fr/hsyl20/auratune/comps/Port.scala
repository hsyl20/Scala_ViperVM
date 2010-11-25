package fr.hsyl20.auratune

object Direction extends Enumeration {
   val In = Value
   val Out = Value
}

/* A port of a component */
class Port(val mandatory:Boolean = true, val direction:Direction.Value = Direction.In)(implicit var component:Component) {
   component.ports ::= this
}

abstract class Component {
   var ports:List[Port] = Nil

   /* Implicit assignation of port component */
   implicit val self = this
}

abstract class CompositeComponent extends Component {
   var bindings:List[(Port,Port)] = Nil

   def bind(from:Port, to:Port): Unit = {
      //TODO: typechecking

      /* Save binding */
      bindings ::= (from -> to)
   }

   implicit def portex(p:Port) = new {
      def --> (to:Port) = bind(p, to)
   }
}
