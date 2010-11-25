package fr.hsyl20.auratune

import fr.hsyl20.auratune.dsl._

object Graph {
   def leaves(ds:Data): List[Data] = dfs(ds) {
      case d:Matrix2D => d
   }

   def dfs[A](ds:Data)(f:PartialFunction[Data,A]): List[A] = {
      val current = if (f.isDefinedAt(ds)) List(f(ds)) else Nil

      val children = ds match {
         /* Dummy expression */
         case DummyExpr => Nil

         /* Leaves */
         case d:Matrix2D => Nil

         /* Data-parallel operations */
         case Map(d1,g) => dfs (d1) (f)  ::: dfs (g(DummyExpr)) (f)

         /* Expr operations */
         case Plus(d1,d2) => dfs (d1) (f) ::: dfs (d2) (f)
         case Sub(d1,d2) => dfs (d1) (f) ::: dfs (d2) (f)
         case Mul(d1,d2) => dfs (d1) (f) ::: dfs (d2) (f)
         case Div(d1,d2) => dfs (d1) (f) ::: dfs (d2) (f)

         case _ => error("Unhandled data type %s".format(ds))
      }

      current ::: children
   }
}
