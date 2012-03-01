/*                                                  *\
** \ \     / _)                   \ \     /   \  |  **
**  \ \   /   |  __ \    _ \   __| \ \   /   |\/ |  **
**   \ \ /    |  |   |   __/  |     \ \ /    |   |  **
**    \_/    _|  .__/  \___| _|      \_/    _|  _|  **
**              _|                                  **
**                                                  **
**       ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~          **
**                                                  **
**            http://www.vipervm.org                **
**                     GPLv3                        **
\*                                                  */

package org.vipervm.library.linearalgebra.kernels.opencl

import org.vipervm.library.linearalgebra.kernels.prototypes._

import org.vipervm.platform.opencl._

object FloatMatrixMultiplicationOpenCL extends OpenCLKernel with FloatMatrixMultiplicationPrototype {
  val source = """
    #define BS 32
    __kernel void matrixMul(
       const int N,
       __global float* A,
       __global float* B, 
       __global float* C) {
        __local float atile[BS+1][BS+1];
        __local float btile[BS+1][BS+1];
        int bx = get_group_id(0); 
        int by = get_group_id(1); 
        int gx = get_global_id(0);
        int gy = get_global_id(1);
        int tx = get_local_id(0);
        int ty = get_local_id(1);

        float sum = 0.0;
        const int UT = N / BS;
        int k,t,i;

        for (t=0; t<UT; t++) {
          btile[tx][ty] = B[gx + BS*N*t + N*ty];
          btile[tx][ty+BS/2] = B[gx + BS*N*t + (BS/2)*N + N*ty];
          atile[ty][tx] = A[N*gy+t*BS+tx];
          barrier(CLK_LOCAL_MEM_FENCE);

          for (k=0; k<BS; k++) {		
           sum += atile[ty][k] * btile[tx][k];
          }	   

        }	    

        C[gy*N+gx] = sum;
    }
  """

  val program = new OpenCLProgram(source)

  def configure(device:OpenCLProcessor, params:Seq[Any]) = {

    val config = OpenCLKernelConfig(
      kernelName = "matrixMul",
      globalWorkSize = List(params(n), params(n), 1),
      localWorkSize = Some(List(32, 32/2, 1)),
      parameters = IndexedSeq(params(n),params(a), params(b), params(c))
    )

    Some(config)
  }
}
