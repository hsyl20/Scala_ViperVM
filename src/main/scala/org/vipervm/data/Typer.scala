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

package org.vipervm.data

object Typer {

  def apply(e:Expr): TypedExpr = e match {
    case a@TypedExpr(_,_) => a
    case ValInt(_)    => TypedExpr(e, Int32Type)
    case ValLong(_)   => TypedExpr(e, Int64Type)
    case ValFloat(_)  => TypedExpr(e, FloatType)
    case ValDouble(_) => TypedExpr(e, DoubleType)
    case Var(_)       => sys.error("Unknown expression type")
    case Add(l,r)     => mustEquals(l,r, Add(_,_))
    case Sub(l,r)     => mustEquals(l,r, Sub(_,_))
    case Mul(l,r)     => mustEquals(l,r, Mul(_,_))
    case Div(l,r)     => mustEquals(l,r, Div(_,_))
    case Tuple(elems) => {
      val ne = elems.map(apply)
      TypedExpr(Tuple(ne), TupleType(elems.size, ne.map(_.typ)))
    }
    case TupleExtractor(t,i) => {
      val tt = apply(t)
      TypedExpr(TupleExtractor(tt,i), tt.e.asInstanceOf[Tuple].elems(i).asInstanceOf[TypedExpr].typ)
    }
  }

  def mustEquals(e1:Expr,e2:Expr,f:(Expr,Expr) => Expr):TypedExpr = {
    val t1 = apply(e1)
    val t2 = apply(e2)
    if (t1.typ != t2.typ) sys.error("Types must be the same") else TypedExpr(f(t1,t2), t1.typ)
  }
}
