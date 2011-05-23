import org.vipervm.bindings.opencl._

import org.junit._

class TestMem {
   @Test def context {
      OpenCL.platforms match {
         case Nil => println("No OpenCL platform to test!")
         case p::_ => p.devices() match {
            case Nil => println("No device to test!")
            case l => {
               val c = new Context(l)
               val cq = new CommandQueue(c, p.devices().head)
               val b0 = c.allocBuffer(100000)
               println("Buffer info:")
               println("   Mem type: " + b0.memType)
               println("   Flags: " + b0.flags)
               println("   Size: " + b0.size)
               println("   HostPtr: " + b0.hostPtr)
               println("   Map count: " + b0.mapCount)
               println("   Reference count: " + b0.referenceCount)
            }
         }
      }
   }
}
