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
    val program = new Program(source)

    val kernel = new OpenCLKernel(program, "dummy") {
      case class Params(in: Buffer, out: Buffer, factor:Int, size:Long)
      type ParamsType = Params
  
      def getParams(args:Seq[KernelParameter]): Option[Params] = args match {
        case LongKernelParameter(size) :: BufferKernelParameter(in) :: BufferKernelParameter(out) :: IntKernelParameter(factor) :: Nil => Some(Params(in,out,factor,size))
        case _ => None
      }

      def configure(device:OpenCLDevice,params:ParamsType):Some[OpenCLKernelConfig] = Some(new OpenCLKernelConfig {
        val globalWorkSize = List(params.size, 1, 1)
        val parameters = List(BufferKernelParameter(params.in), BufferKernelParameter(params.out), IntKernelParameter(params.factor))
      })
    }

  }

  val source = """__kernel void dummy(float * in, float * out, int a) {\n
                    int i = get_global_id(0);\n
                    out[i] = in[i] * a;\n
                  }\n"""
}
