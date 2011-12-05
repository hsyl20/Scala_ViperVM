
import org.scalatest.FunSuite
import org.vipervm.bindings.opencl._
import java.nio.{ByteBuffer, ByteOrder}

class TestMatrix extends FunSuite {
   
   val src = """__kernel void matrix_add(__global float *s1, __global float *s2, __global float *d) {
                    int x = get_global_id(0);
                    int y = get_global_id(1);
                    int w = get_global_size(0);
                    d[w*y+x] = s1[w*y+x] + s2[w*y+x];
                }"""


   test("OpenCL program") {
      OpenCL.platforms match {
         case Nil => println("No OpenCL platform to test!")
         case platform::_ => platform.devices() match {
            case Nil => println("No device to test!")
            case devices => {
               val ctx = new Context(devices)
               val prog = new Program(ctx, src)
               val dev = devices.head
               prog.build(List(dev))
               
               val bldInfo = prog.buildInfo(dev)
               println("   Build status: " + bldInfo.status)
               println("   Build options: " + bldInfo.options)
               println("   Build log: " + bldInfo.log)

               val ker = new Kernel(prog, "matrix_add")
               println("   Kernel num args: " + ker.numArgs)
               println("   Work group max size: " + ker.workGroupInfo(dev).size)

               val cq = new CommandQueue(ctx, dev, profiling = true)
               println(cq.properties)

               val m0 = ByteBuffer.allocateDirect(1024*4)
               val m1 = ByteBuffer.allocateDirect(1024*4)
               val m2 = ByteBuffer.allocateDirect(1024*4)
               
               m0.order(ByteOrder.LITTLE_ENDIAN)
               m1.order(ByteOrder.LITTLE_ENDIAN)
               m2.order(ByteOrder.LITTLE_ENDIAN)

               val fb0 = m0.asFloatBuffer
               val fb1 = m1.asFloatBuffer
               for (i <- 0 until 1024) {
                  fb0.put(i, 1.0f)
                  fb1.put(i, 2.0f)
               }
               for (i <- 0 until 1024)
                  print(fb0.get(i)+ " ")
               println()
               println()
               for (i <- 0 until 1024)
                  print(fb1.get(i)+ " ")
               println()
               println()
               
               val b0 = ctx.useBuffer(m0)
               val b1 = ctx.useBuffer(m1)
               val b2 = ctx.useBuffer(m2)

               ker.setArg(0, b0)
               ker.setArg(1, b1)
               ker.setArg(2, b2)
               
               val ev = cq.enqueueKernel(ker, List(128,8,1), Some(List(64,1,1)), Nil)

               val (ptr,_) = cq.enqueueMapBuffer(b2, false, CommandQueue.CL_MAP_READ, 0, b2.size, Nil)

               cq.finish

               println(ev.executionStatus)
               println("Start: " + ev.profilingInfo.start)
               println("End: " + ev.profilingInfo.end)
               println("Duration: " + (ev.profilingInfo.end - ev.profilingInfo.start) + " ns")

               val fb2 = ptr.getByteBuffer(0,b2.size).asFloatBuffer
               for (i <- 0 until 1024)
                  print(fb2.get(i)+ " ")
               println()

            }
         }
      }
   }
}

