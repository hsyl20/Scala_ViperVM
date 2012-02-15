package org.vipervm.dsl

import org.vipervm.utils._
import org.vipervm.runtime.data.Primitives._
import org.vipervm.runtime.data
import org.vipervm.runtime.{TmVar,TmApp,TmKernel,Term,Context,DataValue}

import org.vipervm.library._

abstract class Matrix2D[A:Primitive] {
  val term:Term
  val context:Context
  val peer:Option[data.Matrix2D[A]]

  lazy val addkernel = new FMatAddKernel
  lazy val mulkernel = new FMatMulKernel

  def +(m:Matrix2D[A]):Matrix2D[A] = {
    val myt = term
    val myc = context
    new Matrix2D[A] {
      val term = TmApp(TmKernel("matadd"), Vector(myt, m.term))
      val context = Context(myc.values ++ m.context.values, myc.kernels ++ m.context.kernels + ("matadd" -> addkernel))
      val peer = None
    }
  }

  def *(m:Matrix2D[A]):Matrix2D[A] = {
    val myt = term
    val myc = context
    new Matrix2D[A] {
      val term = TmApp(TmKernel("matmul"), Vector(myt, m.term))
      val context = Context(myc.values ++ m.context.values, myc.kernels ++ m.context.kernels + ("matmul" -> mulkernel))
      val peer = None
    }
  }
}

object Matrix2D {
  var id = -1

  def apply[A:Primitive](width:Long,height:Long):Matrix2D[A] = {
    id += 1
    val name = "m%d".format(id)
    new Matrix2D[A] {
      val term = TmVar(name)
      val peer = Some(new data.Matrix2D[A](width,height))
      val context = Context(Map(name -> DataValue(peer.get)), Map.empty)
    }
  }
}
