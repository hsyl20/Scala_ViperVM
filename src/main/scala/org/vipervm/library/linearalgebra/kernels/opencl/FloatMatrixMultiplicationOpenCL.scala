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

  val blockSize = 16

  val source = """
    #define BLOCK_SIZE """ + blockSize + """
      
    __kernel void
    matrixMul(__global float* A, 
              __global float* B, 
              __global float* C, long wA, long wB)
    {
        int bx = get_group_id(0) * BLOCK_SIZE;
        int by = get_group_id(1) * BLOCK_SIZE;
     
        int tx = get_local_id(0);
        int ty = get_local_id(1);
     
        long aBegin = wA * by;
        long aEnd   = aBegin + wA;
        long aStep  = BLOCK_SIZE;
     
        long bBegin = bx;
        long bStep  = BLOCK_SIZE * wB;

        float Csub = 0.0f;
     
        for (long a = aBegin, b = bBegin;
                 a < aEnd;
                 a += aStep, b += bStep) 
        {

            __local float As[BLOCK_SIZE][BLOCK_SIZE];
            __local float Bs[BLOCK_SIZE][BLOCK_SIZE];

            As[ty][tx] = A[a + wA * ty + tx];
            Bs[ty][tx] = B[b + wB * ty + tx];
     
            barrier(CLK_LOCAL_MEM_FENCE);
     
            for (long k = 0; k < BLOCK_SIZE; ++k)
                Csub += As[ty][k] * Bs[k][tx];
     
            barrier(CLK_LOCAL_MEM_FENCE);
     
        }
     
        long c = wB * by + bx;
        C[c + wB * ty + tx] = Csub;
    }
  """

  val program = new OpenCLProgram(source)

  def configure(device:OpenCLProcessor, params:Seq[Any]) = {

    val config = OpenCLKernelConfig(
      kernelName = "matrixMul",
      globalWorkSize = List(params(widthB), params(heightA), 1),
      localWorkSize = Some(List(blockSize, blockSize, 1)),
      parameters = IndexedSeq(params(a), params(b), params(c), params(widthA), params(widthB))
    )

    Some(config)
  }
}
