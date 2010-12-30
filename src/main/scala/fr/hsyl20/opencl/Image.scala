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

package fr.hsyl20.opencl

import net.java.dev.sna.SNA
import com.sun.jna.ptr.{IntByReference, PointerByReference}
import com.sun.jna.{Pointer, Structure, PointerType, NativeLong, Memory}
import com.sun.jna.Pointer.NULL
import scala.collection.immutable._

/*
class ImageFormat extends Structure {
   val ImageChannelOrder: ChannelOrder = 0
   val ImageChannelDataType: ChannelType = 0
}

*/
object Image {
   /* cl_channel_order */
   val CL_R                                     =   0x10B0
   val CL_A                                     =   0x10B1
   val CL_RG                                    =   0x10B2
   val CL_RA                                    =   0x10B3
   val CL_RGB                                   =   0x10B4
   val CL_RGBA                                  =   0x10B5
   val CL_BGRA                                  =   0x10B6
   val CL_ARGB                                  =   0x10B7
   val CL_INTENSITY                             =   0x10B8
   val CL_LUMINANCE                             =   0x10B9

   /* cl_channel_type */
   val CL_SNORM_INT8                            =   0x10D0
   val CL_SNORM_INT16                           =   0x10D1
   val CL_UNORM_INT8                            =   0x10D2
   val CL_UNORM_INT16                           =   0x10D3
   val CL_UNORM_SHORT_565                       =   0x10D4
   val CL_UNORM_SHORT_555                       =   0x10D5
   val CL_UNORM_INT_101010                      =   0x10D6
   val CL_SIGNED_INT8                           =   0x10D7
   val CL_SIGNED_INT16                          =   0x10D8
   val CL_SIGNED_INT32                          =   0x10D9
   val CL_UNSIGNED_INT8                         =   0x10DA
   val CL_UNSIGNED_INT16                        =   0x10DB
   val CL_UNSIGNED_INT32                        =   0x10DC
   val CL_HALF_FLOAT                            =   0x10DD
   val CL_FLOAT                                 =   0x10DE

   /* cl_image_info */
   val CL_IMAGE_FORMAT                          =   0x1110
   val CL_IMAGE_ELEMENT_SIZE                    =   0x1111
   val CL_IMAGE_ROW_PITCH                       =   0x1112
   val CL_IMAGE_SLICE_PITCH                     =   0x1113
   val CL_IMAGE_WIDTH                           =   0x1114
   val CL_IMAGE_HEIGHT                          =   0x1115
   val CL_IMAGE_DEPTH                           =   0x1116
}
