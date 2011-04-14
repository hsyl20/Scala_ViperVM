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

import fr.hsyl20.vipervm.platform.opencl._
import fr.hsyl20.vipervm.platform._

class RuntimeSpec extends FlatSpec with ShouldMatchers {

  "A OpenCL kernel" should "be instantiable" in {
    val program = new OpenCLProgram(source)

    val kernel = new OpenCLKernel(program, "dummy") {
  
      val modes = ReadOnly :: ReadOnly :: ReadWrite :: ReadOnly :: Nil

      def configure(device:OpenCLDevice) = {
        case LongKernelParameter(size) :: BufferKernelParameter(in) :: BufferKernelParameter(out) :: IntKernelParameter(factor) :: Nil => Some(new OpenCLKernelConfig {
          val globalWorkSize = List(size, 1, 1)
          val parameters = List(BufferKernelParameter(in), BufferKernelParameter(out), IntKernelParameter(factor))
        })
      }
    }

  }

  val source = """__kernel void dummy(float * in, float * out, int a) {\n
                    int i = get_global_id(0);\n
                    out[i] = in[i] * a;\n
                  }\n"""
}
