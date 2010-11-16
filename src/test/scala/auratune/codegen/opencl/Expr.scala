import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import fr.hsyl20.auratune.codegen.opencl._

class ExprSpec extends FlatSpec with ShouldMatchers {

  "An expression" should "be compiled in 3-variables C code" in {
      val a = Variable(CFloat)
      val b = Variable(CFloat)

      val e = (a + b) * a

      val code = new CCode {
         compute(e)
      }
      println(code)
  }

  "A variable" should "be assignable" in {
      val a = Variable(CFloat)
      val b = Variable(CFloat)
      val c = Variable(CFloat)

      val e = (a + b) * a

      val code = new CCode {
         c := compute(e)
      }
      println(code)
  }

  "A variable" should "support address spaces in declaration" in {
      val a = Variable(CFloat, "global")
      val b = Variable(CFloat, "local")
      val c = Variable(CFloat, "shared")

      val code = new CCode {
         declare(a)
         declareInit(b,a)
         declareInitRaw(c, "raw init")
      }
      println(code)
  }

  "A variable" should "support address spaces in function declaration" in {
      val a = Variable(CFloat, "global")
      val b = Variable(CFloat)

      val f1 = CFunction(CFloat, a, b) { case List(v,w) =>
         val d = Variable(CFloat)
         d + v
      }

      val code = new CCode {
         declareFun(f1)
      }

      println(code)
  }

  "A function" should "be declared correctly" in {
      val f1 = CFunction(CFloat, Variable(CFloat)) { case List(v) =>
         val d = Variable(CFloat)
         d + v
      }

      val code = new CCode {
         declareFun(f1)
      }

      println(code)
  }

  "A kernel" should "be declared correctly" in {
      val code = new CCode {
         val in = Variable(CFloat*, "global")
         val pos = Variable(CInt)
         val out = Variable(CFloat*, "global")
         kernel("dummy", in, pos, out) {
            out(pos) := in(pos)
         }
      }

      println(code)
  }

  "A function" should "be callable" in {
      val f1 = CFunction(CFloat, Variable(CFloat)) { case List(v) =>
         val d = Variable(CFloat)
         d + v
      }

      val code2 = new CCode {
         val v = Variable(CFloat)
         declare(v)
         declareFun(f1)
         call(f1, Array(v))
      }

      println(code2)
  }

   "A map" should "be computed correctly" in {
      val m = Variable(CFloat*)
      val n = Variable(CFloat*)

      val j = Variable(CInt)

      val f1 = CFunction(CFloat, Variable(CFloat)) { case List(v) =>
         val d = Variable(CFloat)
         d + v
      }

      val map = CMap(f1,j, m, n)

      println(map.code)
   }

}

