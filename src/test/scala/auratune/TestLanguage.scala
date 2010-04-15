import org.junit._
import fr.hsyl20.auratune._
import fr.hsyl20.opencl._
import fr.hsyl20.auratune.Conversions._

class TestLanguage {
   @Test def language {

      val s = {
         'c := 'a + 'b
      }
      println(s)

      val fun = 'a --> 'a + 10
      println(fun)

      println(fun(5))

      val fm = new FloatMatrix
      val src = fm.forAll(fun).cl("pouet")
      
      println(src)
   
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

               val ker = new Kernel(prog, "pouet")
               println("   Kernel num args: " + ker.numArgs)
               println("   Work group max size: " + ker.workGroupInfo(dev).size)

               val cq = new CommandQueue(ctx, dev, profiling = true)
               println(cq.properties)

               val b0 = ctx.bufferAlloc(1024*4)   
               val fb0 = b0.byteBuffer.asFloatBuffer

               for (i <- 0 until 1024) {
                  fb0.put(i, 1.0f)
               }
               println()
               println()
               

               ker.setArg(0, b0)
               
               val ev = cq.enqueueKernel(ker, List(1024,1,1), Some(List(64,1,1)), Nil)

               val (ptr,_) = cq.enqueueMapBuffer(b0, false, CommandQueue.CL_MAP_READ, 0, b0.size, Nil)

               cq.finish

               println(ev.executionStatus)
               println("Start: " + ev.profilingInfo.start)
               println("End: " + ev.profilingInfo.end)
               println("Duration: " + (ev.profilingInfo.end - ev.profilingInfo.start) + " ns")

               val fb1 = ptr.getByteBuffer(0,b0.size).asFloatBuffer
               for (i <- 0 until 1024)
                  print(fb1.get(i)+ " ")
               println()
            }
         }
      }
   }  
}
