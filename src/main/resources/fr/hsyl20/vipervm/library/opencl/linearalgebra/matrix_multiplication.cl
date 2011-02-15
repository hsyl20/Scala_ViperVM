
#define AS(i, j) As[j * BLOCK_SIZE + i]
#define BS(i, j) Bs[j * BLOCK_SIZE + i]
#define min(a,b) ((a)<(b)?(a):(b))
#define max(a,b) ((a)>(b)?(a):(b))

__kernel void matrixMul( __global TYPE* A, __global TYPE* B, __global TYPE* C,
    __local TYPE* As, __local TYPE* Bs, unsigned int wa, unsigned int ha, unsigned int wb)
{
  int bx = get_group_id(0);
  int by = get_group_id(1);
  
  int tx = get_local_id(0);
  int ty = get_local_id(1);
  
  int gx = get_global_id(0);
  int gy = get_global_id(1);
  
  unsigned int block_w = min(wb - bx * BLOCK_SIZE, BLOCK_SIZE);
  unsigned int block_h = min(ha - by * BLOCK_SIZE, BLOCK_SIZE);
  
  int valid = (gx < wb && gy < ha);
  
  TYPE Csub = (TYPE)0.0;
  
  unsigned int pos = 0;
  while (pos < wa) {
    unsigned int size = min(wa-pos, BLOCK_SIZE);
    if (tx < size && gy < ha)
      AS(tx, ty) = A[pos + tx + wa * gy];
    if (ty < size && gx < wb)
      BS(tx, ty) = B[gx + wb * (pos+ty)];
    
    barrier(CLK_LOCAL_MEM_FENCE);
    
    if (valid) {
      for (int k = 0; k < size; ++k)
        Csub += AS(k, ty) * BS(tx, k);
    }
    pos += size;
    barrier(CLK_LOCAL_MEM_FENCE);
  }
  
  if (valid)
    C[wb * gy + gx] = Csub;
}
