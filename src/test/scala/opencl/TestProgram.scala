import fr.hsyl20.opencl._
import org.junit._

class TestProgram {
   
   val src = """__kernel void dummy(__global float *s, __global float *d) {
                    int x = get_global_id(0);
                    d[x] = s[x];
                }"""

   val src2 = """__kernel void dummy1(__global float *s, __global float *d) {
                    int x = get_global_id(0);
                    d[x] = s[x];
                }
                __kernel void dummy2(__global float *s, __global float *d) {
                    int x = get_global_id(0);
                    d[x] = s[x];
                }"""

   @Test def program {
      OpenCL.platforms match {
         case Nil => println("No OpenCL platform to test!")
         case p::_ => p.devices() match {
            case Nil => println("No device to test!")
            case l => {
               val c = new Context(l)
               val prog = new Program(c, src)
               prog.build(p.devices())
               val bi = prog.buildInfo(p.devices().head)
               println("   Build status: " + bi.status)
               println("   Build options: " + bi.options)
               println("   Build log: " + bi.log)
               val k = new Kernel(prog, "dummy")
               println("   Kernel num args: " + k.numArgs)

               val prog2 = new Program(c, src2)
               prog2.build(p.devices())
               val ks = prog2.createKernels
               for (k <- ks)
                  println(k.name)
            }
         }
      }
   }
}

