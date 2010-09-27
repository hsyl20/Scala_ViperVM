import org.junit._
import fr.hsyl20.auratune._

class TestScheduler {
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

      val p = Program(src)

      val k = new Kernel(p, "dummy") {
         input(Matrix(Float), name = "in"),
         output(Matrix(Float), SizeOf('in)),
         inout(Matrix(Float))
      }

      val d = Matrix(Float, 10000)
      val d2 = Matrix(Float, 10000).fill(10.0)

      val (od, iod) = k(d, d2)

   }  
}
