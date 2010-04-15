package com.sun.jna

/**
 * 'size_t' C type (32 bits on 32 bits platforms, 64 bits on 64 bits platforms).
 * Can be also used to model the 'long' C type for libraries known to be compiled with GCC or LLVM even on Windows.
 * (NativeLong on Windows is only okay with MSVC++ libraries, as 'long' on Windows 64 bits will be 32 bits with MSVC++ and 64 bits with GCC/mingw)
 *   (Adapated from JNAerator)
 */
class NativeSize(value: Long) extends IntegerType(Native.SIZE_T_SIZE, value) {
   def this() = this(0)
}

object NativeSize {
   val SIZE = Native.SIZE_T_SIZE

   def apply(value: Long) = new NativeSize(value)
}
