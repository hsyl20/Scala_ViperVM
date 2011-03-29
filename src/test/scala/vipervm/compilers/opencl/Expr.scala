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

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import fr.hsyl20.vipervm.codegen.opencl._

class ExprSpec extends FlatSpec with ShouldMatchers {

  "A symbol" can "be declared as a variable with a specified type" in {
      val code = new CLCode {
         'a :- CFloat
      }
      println(code)
  }

  "A symbol" can "be declared as a variable with specified type and space" in {
      val code = new CLCode {
         'a :- Global @@ CFloat
      }
      println(code)
  }

  "An expression" should "be compiled in 3-variables C code" in {
      val code = new CLCode {
         'a :- CFloat
         'b :- Global @@ CFloat
         'e :- Local @@ CFloat := ('a + 'b) * 'a
         'f := 'e
      }
      println(code)
  }


  "A variable" should "be assignable" in {
      val code = new CLCode {
         'a :- CFloat
         'b :- CFloat
         'c :- CFloat

         'c := ('a + 'b) * 'a
      }
      println(code)
  }

  "A variable" should "support address spaces in declaration" in {

      val code = new CLCode {
         'a :- Global @@ CFloat
         'b :- Private @@ CFloat := 'a
         'c :- Local @@ CFloat := "raw init"
      }
      println(code)
  }

  "A variable" should "support address spaces in function declaration" in {

      val code = new CLCode {
         val a = Variable(CFloat, Global)
         val b = Variable(CFloat)
         val f1 = CFunction(CFloat, a, b) { case List(v,w) =>
            val d = Variable(CFloat)
            d + v
         }
         declareFun(f1)
      }

      println(code)
  }

  "A function" should "be declared correctly" in {
      val code = new CLCode {
         val f1 = CFunction(CFloat, Variable(CFloat)) { case List(v) =>
            val d = Variable(CFloat)
            d + v
         }

         declareFun(f1)
      }

      println(code)
  }

  "A kernel" should "be declared correctly" in {
      val code = new CLCode {
         kernel("dummy", VarType(CFloat*, Global), VarType(CInt), VarType(CFloat*, Global)) { case List(in,pos,out) =>
            out(pos) := in(pos)
         }
      }

      println(code)
  }

  "A function" should "be callable" in {
      val code2 = new CLCode {
         val f1 = CFunction(CFloat, Variable(CFloat)) { case List(v) =>
            val d = Variable(CFloat)
            d + v
         }

         val v = Variable(CFloat)
         declare(v)
         declareFun(f1)
         call(f1, Array(v))
      }

      println(code2)
  }

}

