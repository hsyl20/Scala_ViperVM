__kernel void matrixAdd_same_shape( __global TYPE* A, __global TYPE* B, __global TYPE* C, unsigned long width, unsigned long height, int padding) {
  int x = get_global_id(0);
  int y = get_global_id(1);

  unsigned long pos = y*(width+padding) + x;

  if (x < width && y < height)
    C[pos] = A[pos] + B[pos];
}
