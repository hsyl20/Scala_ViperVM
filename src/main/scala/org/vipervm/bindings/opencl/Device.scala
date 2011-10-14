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

package org.vipervm.bindings.opencl

import com.sun.jna.ptr.{IntByReference, PointerByReference}
import com.sun.jna.{Pointer, Structure, PointerType, NativeLong, Memory}
import com.sun.jna.Pointer.NULL
import scala.collection.immutable._
import scala.collection.immutable.BitSet.BitSet1
import java.nio.ByteOrder

/**
 * An OpenCL device
 */
class Device(val platform: Platform, val peer: Pointer) extends Entity with Info {
   import Wrapper._
   import Device._

   override protected val infoFunc = clGetDeviceInfo(peer, _:Int, _:Int, _:Pointer, _:Pointer)

   lazy val deviceType: BitSet = new BitSet.BitSet1(getLongInfo(CL_DEVICE_TYPE))
   lazy val vendorID: Int = getIntInfo(CL_DEVICE_VENDOR_ID)
   lazy val maxComputeUnits: Int = getIntInfo(CL_DEVICE_MAX_COMPUTE_UNITS)
   lazy val maxWorkItemDimensions: Int = getIntInfo(CL_DEVICE_MAX_WORK_ITEM_DIMENSIONS)
   lazy val maxWorkItemSizes: Seq[Long] = getNativeSizeArrayInfo(CL_DEVICE_MAX_WORK_ITEM_SIZES)
   lazy val maxWorkGroupSize: Int = getIntInfo(CL_DEVICE_MAX_WORK_GROUP_SIZE)
   lazy val preferredCharVectorWidth: Int = getIntInfo(CL_DEVICE_PREFERRED_VECTOR_WIDTH_CHAR)
   lazy val preferredShortVectorWidth: Int = getIntInfo(CL_DEVICE_PREFERRED_VECTOR_WIDTH_SHORT)
   lazy val preferredIntVectorWidth: Int = getIntInfo(CL_DEVICE_PREFERRED_VECTOR_WIDTH_INT)
   lazy val preferredLongVectorWidth: Int = getIntInfo(CL_DEVICE_PREFERRED_VECTOR_WIDTH_LONG)
   lazy val preferredFloatVectorWidth: Int = getIntInfo(CL_DEVICE_PREFERRED_VECTOR_WIDTH_FLOAT)
   lazy val preferredDoubleVectorWidth: Int = getIntInfo(CL_DEVICE_PREFERRED_VECTOR_WIDTH_DOUBLE)
   lazy val maxClockFrequency: Int = getIntInfo(CL_DEVICE_MAX_CLOCK_FREQUENCY)
   lazy val addressBits: Int = getIntInfo(CL_DEVICE_ADDRESS_BITS)
   lazy val maxMemAllocSize: Long = getLongInfo(CL_DEVICE_MAX_MEM_ALLOC_SIZE)
   lazy val imageSupport: Boolean = getBoolInfo(CL_DEVICE_IMAGE_SUPPORT)


   lazy val maxReadImageArgs: Int = getIntInfo(CL_DEVICE_MAX_READ_IMAGE_ARGS)
   lazy val maxWriteImageArgs: Int = getIntInfo(CL_DEVICE_MAX_WRITE_IMAGE_ARGS)

   lazy val max2DImageWidth: Long = getNativeSizeInfo(CL_DEVICE_IMAGE2D_MAX_WIDTH)
   lazy val max2DImageHeight: Long = getNativeSizeInfo(CL_DEVICE_IMAGE2D_MAX_HEIGHT)
   lazy val max3DImageWidth: Long = getNativeSizeInfo(CL_DEVICE_IMAGE3D_MAX_WIDTH)
   lazy val max3DImageHeight: Long = getNativeSizeInfo(CL_DEVICE_IMAGE3D_MAX_HEIGHT)
   lazy val max3DImageDepth: Long = getNativeSizeInfo(CL_DEVICE_IMAGE3D_MAX_DEPTH)

   lazy val maxSamplers: Int = getIntInfo(CL_DEVICE_MAX_SAMPLERS)

   lazy val maxParameterSize: Long = getNativeSizeInfo(CL_DEVICE_MAX_PARAMETER_SIZE)
   lazy val memBaseAddrAlign: Int = getIntInfo(CL_DEVICE_MEM_BASE_ADDR_ALIGN)
   lazy val minDataTypeAlignSize: Int = getIntInfo(CL_DEVICE_MIN_DATA_TYPE_ALIGN_SIZE)

   lazy val singleFloatingPointConfig: BitSet = new BitSet.BitSet1(getLongInfo(CL_DEVICE_SINGLE_FP_CONFIG))

   lazy val globalMemCacheType: Int = getIntInfo(CL_DEVICE_GLOBAL_MEM_CACHE_TYPE)
   lazy val globalMemCacheLineSize: Int = getIntInfo(CL_DEVICE_GLOBAL_MEM_CACHELINE_SIZE)
   lazy val globalMemCacheSize: Long = getLongInfo(CL_DEVICE_GLOBAL_MEM_CACHE_SIZE)
   lazy val globalMemSize: Long = getLongInfo(CL_DEVICE_GLOBAL_MEM_SIZE)

   lazy val maxConstantBufferSize: Long = getLongInfo(CL_DEVICE_MAX_CONSTANT_BUFFER_SIZE)
   lazy val maxConstantArgs: Int = getIntInfo(CL_DEVICE_MAX_CONSTANT_ARGS)

   lazy val localMemType: Int = getIntInfo(CL_DEVICE_LOCAL_MEM_TYPE)
   lazy val localMemSize: Long = getLongInfo(CL_DEVICE_LOCAL_MEM_SIZE)

   lazy val errorCorrectionSupport: Boolean = getBoolInfo(CL_DEVICE_ERROR_CORRECTION_SUPPORT)
   lazy val profilingTimerResolution: Long = getNativeSizeInfo(CL_DEVICE_PROFILING_TIMER_RESOLUTION)

   lazy val endianness: ByteOrder = if(getBoolInfo(CL_DEVICE_ENDIAN_LITTLE)) ByteOrder.LITTLE_ENDIAN else ByteOrder.BIG_ENDIAN
   def available: Boolean = getBoolInfo(CL_DEVICE_AVAILABLE)
   lazy val compilerAvailable: Boolean = getBoolInfo(CL_DEVICE_COMPILER_AVAILABLE)

   lazy val executionCapabilities: BitSet1 = new BitSet.BitSet1(getLongInfo(CL_DEVICE_EXECUTION_CAPABILITIES))
   lazy val queueProperties: BitSet1 = new BitSet.BitSet1(getLongInfo(CL_DEVICE_QUEUE_PROPERTIES))

   def queueProfilingSupport  = (queueProperties.elems & CommandQueue.CL_QUEUE_PROFILING_ENABLE) != 0
   def queueOutOfOrderSupport = (queueProperties.elems & CommandQueue.CL_QUEUE_OUT_OF_ORDER_EXEC_MODE_ENABLE) != 0

   //platform field is already available
   lazy val name: String   = getStringInfo(CL_DEVICE_NAME)
   lazy val vendor: String = getStringInfo(CL_DEVICE_VENDOR)
   lazy val driverVersion: String = getStringInfo(CL_DRIVER_VERSION)
   lazy val profile: String = getStringInfo(CL_DEVICE_PROFILE)
   lazy val version: String = getStringInfo(CL_DEVICE_VERSION)
   lazy val extensions: Seq[String] = getStringInfo(CL_DEVICE_EXTENSIONS).split(' ').toList
}

object Device {
   val CL_DEVICE_TYPE_DEFAULT                   =   (1 << 0)
   val CL_DEVICE_TYPE_CPU                       =   (1 << 1)
   val CL_DEVICE_TYPE_GPU                       =   (1 << 2)
   val CL_DEVICE_TYPE_ACCELERATOR               =   (1 << 3)
   val CL_DEVICE_TYPE_ALL                       =   0xFFFFFFFF
   val CL_DEVICE_TYPE                           =   0x1000
   val CL_DEVICE_VENDOR_ID                      =   0x1001
   val CL_DEVICE_MAX_COMPUTE_UNITS              =   0x1002
   val CL_DEVICE_MAX_WORK_ITEM_DIMENSIONS       =   0x1003
   val CL_DEVICE_MAX_WORK_GROUP_SIZE            =   0x1004
   val CL_DEVICE_MAX_WORK_ITEM_SIZES            =   0x1005
   val CL_DEVICE_PREFERRED_VECTOR_WIDTH_CHAR    =   0x1006
   val CL_DEVICE_PREFERRED_VECTOR_WIDTH_SHORT   =   0x1007
   val CL_DEVICE_PREFERRED_VECTOR_WIDTH_INT     =   0x1008
   val CL_DEVICE_PREFERRED_VECTOR_WIDTH_LONG    =   0x1009
   val CL_DEVICE_PREFERRED_VECTOR_WIDTH_FLOAT   =   0x100A
   val CL_DEVICE_PREFERRED_VECTOR_WIDTH_DOUBLE  =   0x100B
   val CL_DEVICE_MAX_CLOCK_FREQUENCY            =   0x100C
   val CL_DEVICE_ADDRESS_BITS                   =   0x100D
   val CL_DEVICE_MAX_READ_IMAGE_ARGS            =   0x100E
   val CL_DEVICE_MAX_WRITE_IMAGE_ARGS           =   0x100F
   val CL_DEVICE_MAX_MEM_ALLOC_SIZE             =   0x1010
   val CL_DEVICE_IMAGE2D_MAX_WIDTH              =   0x1011
   val CL_DEVICE_IMAGE2D_MAX_HEIGHT             =   0x1012
   val CL_DEVICE_IMAGE3D_MAX_WIDTH              =   0x1013
   val CL_DEVICE_IMAGE3D_MAX_HEIGHT             =   0x1014
   val CL_DEVICE_IMAGE3D_MAX_DEPTH              =   0x1015
   val CL_DEVICE_IMAGE_SUPPORT                  =   0x1016
   val CL_DEVICE_MAX_PARAMETER_SIZE             =   0x1017
   val CL_DEVICE_MAX_SAMPLERS                   =   0x1018
   val CL_DEVICE_MEM_BASE_ADDR_ALIGN            =   0x1019
   val CL_DEVICE_MIN_DATA_TYPE_ALIGN_SIZE       =   0x101A
   val CL_DEVICE_SINGLE_FP_CONFIG               =   0x101B
   val CL_DEVICE_GLOBAL_MEM_CACHE_TYPE          =   0x101C
   val CL_DEVICE_GLOBAL_MEM_CACHELINE_SIZE      =   0x101D
   val CL_DEVICE_GLOBAL_MEM_CACHE_SIZE          =   0x101E
   val CL_DEVICE_GLOBAL_MEM_SIZE                =   0x101F
   val CL_DEVICE_MAX_CONSTANT_BUFFER_SIZE       =   0x1020
   val CL_DEVICE_MAX_CONSTANT_ARGS              =   0x1021
   val CL_DEVICE_LOCAL_MEM_TYPE                 =   0x1022
   val CL_DEVICE_LOCAL_MEM_SIZE                 =   0x1023
   val CL_DEVICE_ERROR_CORRECTION_SUPPORT       =   0x1024
   val CL_DEVICE_PROFILING_TIMER_RESOLUTION     =   0x1025
   val CL_DEVICE_ENDIAN_LITTLE                  =   0x1026
   val CL_DEVICE_AVAILABLE                      =   0x1027
   val CL_DEVICE_COMPILER_AVAILABLE             =   0x1028
   val CL_DEVICE_EXECUTION_CAPABILITIES         =   0x1029
   val CL_DEVICE_QUEUE_PROPERTIES               =   0x102A
   val CL_DEVICE_NAME                           =   0x102B
   val CL_DEVICE_VENDOR                         =   0x102C
   val CL_DRIVER_VERSION                        =   0x102D
   val CL_DEVICE_PROFILE                        =   0x102E
   val CL_DEVICE_VERSION                        =   0x102F
   val CL_DEVICE_EXTENSIONS                     =   0x1030
   val CL_DEVICE_PLATFORM                       =   0x1031
   /* 0x1032 reserved for CL_DEVICE_DOUBLE_FP_CONFIG */
   /* 0x1033 reserved for CL_DEVICE_HALF_FP_CONFIG */

   val CL_FP_DENORM                             =   (1 << 0)
   val CL_FP_INF_NAN                            =   (1 << 1)
   val CL_FP_ROUND_TO_NEAREST                   =   (1 << 2)
   val CL_FP_ROUND_TO_ZERO                      =   (1 << 3)
   val CL_FP_ROUND_TO_INF                       =   (1 << 4)
   val CL_FP_FMA                                =   (1 << 5)

   val CL_NONE                                  =   0x0
   val CL_READ_ONLY_CACHE                       =   0x1
   val CL_READ_WRITE_CACHE                      =   0x2

   val CL_LOCAL                                 =   0x1
   val CL_GLOBAL                                =   0x2

   val CL_EXEC_KERNEL                           =   (1 << 0)
   val CL_EXEC_NATIVE_KERNEL                    =   (1 << 1)
}

