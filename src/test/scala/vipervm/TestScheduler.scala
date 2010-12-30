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

import org.junit._
import fr.hsyl20.vipervm._

/*class TestScheduler {
   @Test def scheduler {
      
      val src = """
         _kernel void dummy(float *in, float *out, float*inout) {
            out[get_global_id(0)] = in[get_global_id(0)];
            inout[get_global_id(0)] = inout[get_global_id(0)] * 2.0;
         } 
         _kernel void dummy2(float *in, float *out, float*inout) {
            out[get_global_id(0)] = in[get_global_id(0)];
            inout[get_global_id(0)] = inout[get_global_id(0)] * 5.0;
         }
      """

      val p = new Program(src)

      val k = new Kernel(p, "dummy") {
         input(dt.Matrix(Float), name = "in")
         output(dt.Matrix(Float), SizeOf('in))
         inout(dt.Matrix(Float))
      }

      val d = new Matrix(Float, 10000)
      val d2 = new Matrix(Float, 10000).fill(10.0)

      val (od, iod) = k(d, d2)

   }  
}*/
