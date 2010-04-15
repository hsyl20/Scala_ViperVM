/*
**
**      \    |  | _ \    \ __ __| |  |  \ |  __| 
**     _ \   |  |   /   _ \   |   |  | .  |  _|  
**   _/  _\ \__/ _|_\ _/  _\ _|  \__/ _|\_| ___| 
**
**       ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
**
**      OpenCL binding (and more) for Scala
**
**         http://www.hsyl20.fr/auratune
**
*/

package fr.hsyl20.opencl

import net.java.dev.sna.SNA
import com.sun.jna.ptr.{IntByReference, PointerByReference}
import com.sun.jna.{Pointer, Structure, PointerType, NativeSize, Memory}
import com.sun.jna.Pointer.NULL
import scala.collection.immutable._
import java.nio.ByteBuffer

object Wrapper extends SNA {
   snaLibrary = "OpenCL"
   /* Platform API */
   val clGetPlatformIDs  = SNAS[Int, Pointer, Pointer, Int]("clGetPlatformIDs")
   val clGetPlatformInfo = SNAS[Pointer,Int, NativeSize, Pointer, Pointer, Int]("clGetPlatformInfo")
   /* Device API */
   val clGetDeviceIDs    = SNAS[Pointer, Int, Int, Pointer, Pointer, Int]("clGetDeviceIDs")
   val clGetDeviceInfo   = SNAS[Pointer, Int, NativeSize, Pointer, Pointer, Int]("clGetDeviceInfo") 

   /* Context API */
   val clCreateContext = SNAS[Pointer, Int, Array[Pointer], Pointer, Pointer, Pointer, Pointer]("clCreateContext")
   val clCreateContextFromType = SNAS[Pointer, Int, Pointer, Pointer, Pointer, Pointer]("clCreateContextFromType")
   val clRetainContext = SNAS[Pointer, Int]("clRetainContext")
   val clReleaseContext = SNAS[Pointer, Int]("clReleaseContext")
   val clGetContextInfo = SNAS[Pointer, Int, NativeSize, Pointer, Pointer, Int]("clGetContextInfo") 

   /* Command Queue APIs */
   val clCreateCommandQueue = SNAS[Pointer, Pointer, Long, Pointer, Pointer]("clCreateCommandQueue")
   val clRetainCommandQueue = SNAS[Pointer, Int]("clRetainCommandQueue")
   val clReleaseCommandQueue = SNAS[Pointer, Int]("clReleaseCommandQueue")
   val clGetCommandQueueInfo = SNAS[Pointer, Int, NativeSize, Pointer, Pointer, Int]("clGetCommandQueueInfo") 
   val clSetCommandQueueProperty = SNAS[Pointer, Long, Int, Pointer, Int]("clSetCommandQueueProperty") 

   /* Memory Object APIs  */
   //val clCreateBuffer = SNAS[Pointer, Long, NativeSize, Pointer, Pointer, Pointer]("clCreateBuffer")
   val clCreateBuffer = SNAS[Pointer, Long, NativeSize, ByteBuffer, Pointer, Pointer]("clCreateBuffer")

   val clRetainMemObject = SNAS[Pointer, Int]("clRetainMemObject")
   val clReleaseMemObject = SNAS[Pointer, Int]("clReleaseMemObject")
   val clGetMemObjectInfo = SNAS[Pointer, Int, NativeSize, Pointer, Pointer, Int]("clGetMemObjectInfo")

   /* Program Object APIs  */
   val clCreateProgramWithSource = SNAS[Pointer, Int, Array[String], Array[NativeSize], Pointer, Pointer]("clCreateProgramWithSource")
   val clRetainProgram = SNAS[Pointer, Int]("clRetainProgram")
   val clReleaseProgram = SNAS[Pointer, Int]("clReleaseProgram")
   val clBuildProgram = SNAS[Pointer, Int, Array[Pointer], String, Pointer, Pointer, Int]("clBuildProgram")
   val clGetProgramInfo = SNAS[Pointer, Int, NativeSize, Pointer, Pointer, Int]("clGetProgramInfo")

   val clUnloadCompiler = SNAS[Int]("clUnloadCompiler")

   val clGetProgramBuildInfo = SNAS[Pointer, Pointer, Int, NativeSize, Pointer, Pointer, Int]("clGetProgramBuildInfo")

   /* Kernel Object APIs */
   val clCreateKernel = SNAS[Pointer, String, Pointer, Pointer]("clCreateKernel")
   val clRetainKernel = SNAS[Pointer, Int]("clRetainKernel")
   val clReleaseKernel = SNAS[Pointer, Int]("clReleaseKernel")
   val clGetKernelInfo = SNAS[Pointer, Int, NativeSize, Pointer, Pointer, Int]("clGetKernelInfo")
   val clGetKernelWorkGroupInfo = SNAS[Pointer, Pointer, Int, NativeSize, Pointer, Pointer, Int]("clGetKernelWorkGroupInfo")
   val clCreateKernelsInProgram = SNAS[Pointer, Int, Pointer, Pointer, Int]("clCreateKernelsInProgram")
   val clSetKernelArg = SNAS[Pointer, Int, NativeSize, Pointer, Int]("clSetKernelArg")

   /* Event Object APIs  */
   val clRetainEvent = SNAS[Pointer, Int]("clRetainEvent")
   val clReleaseEvent = SNAS[Pointer, Int]("clReleaseEvent")
   val clGetEventInfo = SNAS[Pointer, Int, NativeSize, Pointer, Pointer, Int]("clGetEventInfo")
   val clWaitForEvents = SNAS[Int, Array[Pointer], Int]("clWaitForEvents")
   
   /* Profiling APIs  */
   val clGetEventProfilingInfo = SNAS[Pointer, Int, NativeSize, Pointer, Pointer, Int]("clGetEventProfilingInfo") 

   /* Flush and Finish APIs */
   val clFlush = SNAS[Pointer, Int]("clFlush")
   val clFinish = SNAS[Pointer, Int]("clFinish")

   /* Enqueued Commands APIs */
   val clEnqueueReadBuffer = SNAS[Pointer, Pointer, Int, NativeSize, NativeSize, Pointer, Int, Array[Pointer], Pointer, Int]("clEnqueueReadBuffer")
   val clEnqueueWriteBuffer = SNAS[Pointer, Pointer, Int, NativeSize, NativeSize, Pointer, Int, Array[Pointer], Pointer, Int]("clEnqueueWriteBuffer")
   val clEnqueueCopyBuffer = SNAS[Pointer, Pointer, Pointer, NativeSize, NativeSize, NativeSize, Int, Array[Pointer], Pointer, Int]("clEnqueueCopyBuffer")

   val clEnqueueMapBuffer = SNAS[Pointer, Pointer, Int, Long, NativeSize, NativeSize, Int, Array[Pointer], Pointer, Pointer, Pointer]("clEnqueueMapBuffer")

   val clEnqueueUnmapMemObject = SNAS[Pointer, Pointer, Pointer, Int, Array[Pointer], Pointer, Int]("clEnqueueUnmapMemObject")

   val clEnqueueNDRangeKernel = SNAS[Pointer, Pointer, Int, Array[NativeSize], Array[NativeSize], Array[NativeSize], Int, Array[Pointer], Pointer, Int]("clEnqueueNDRangeKernel")

   val clEnqueueWaitForEvents = SNAS[Pointer, Int, Array[Pointer], Int]("clEnqueueWaitForEvents")
   
   val clEnqueueTask = SNAS[Pointer, Pointer, Int, Array[Pointer], Pointer, Int]("clEnqueueTask")
   val clEnqueueMarker = SNAS[Pointer, Pointer, Int]("clEnqueueMarker")
   val clEnqueueBarrier = SNAS[Pointer, Int]("clEnqueueBarrier")

   /* Image APIs */
   val clGetImageInfo = SNAS[Pointer, Int, NativeSize, Pointer, Pointer, Int]("clGetImageInfo")

   /* Sampler APIs */
   val clCreateSampler = SNAS[Pointer, Int, Int, Int, Pointer, Pointer]("clCreateSampler")
   val clGetSamplerInfo = SNAS[Pointer, Int, NativeSize, Pointer, Pointer, Int]("clGetSamplerInfo")
   val clRetainSampler = SNAS[Pointer, Int]("clRetainSampler")
   val clReleaseSampler = SNAS[Pointer, Int]("clReleaseSampler")


   implicit def int2nativesize(i:Int): NativeSize = new NativeSize(i)
   implicit def long2nativesize(i:Long): NativeSize = new NativeSize(i)
   implicit def nativesize2long(i:NativeSize): Long = i.longValue()
}

/*
clCreateProgramWithBinary(cl_context                     /* context */,
                       cl_uint                        /* num_devices */,
                       const cl_device_id *           /* device_list */,
                       const size_t *                 /* lengths */,
                       const unsigned char **         /* binaries */,
                       cl_int *                       /* binary_status */,
                       cl_int *                       /* errcode_ret */) CL_API_SUFFIX__VERSION_1_0;

/* Enqueued Commands APIs */
extern CL_API_ENTRY cl_int CL_API_CALL
clEnqueueReadImage(cl_command_queue     /* command_queue */,
                cl_mem               /* image */,
                cl_bool              /* blocking_read */, 
                const size_t *       /* origin[3] */,
                const size_t *       /* region[3] */,
                size_t               /* row_pitch */,
                size_t               /* slice_pitch */, 
                void *               /* ptr */,
                cl_uint              /* num_events_in_wait_list */,
                const cl_event *     /* event_wait_list */,
                cl_event *           /* event */) CL_API_SUFFIX__VERSION_1_0;

extern CL_API_ENTRY cl_int CL_API_CALL
clEnqueueWriteImage(cl_command_queue    /* command_queue */,
                 cl_mem              /* image */,
                 cl_bool             /* blocking_write */, 
                 const size_t *      /* origin[3] */,
                 const size_t *      /* region[3] */,
                 size_t              /* input_row_pitch */,
                 size_t              /* input_slice_pitch */, 
                 const void *        /* ptr */,
                 cl_uint             /* num_events_in_wait_list */,
                 const cl_event *    /* event_wait_list */,
                 cl_event *          /* event */) CL_API_SUFFIX__VERSION_1_0;

extern CL_API_ENTRY cl_int CL_API_CALL
clEnqueueCopyImage(cl_command_queue     /* command_queue */,
                cl_mem               /* src_image */,
                cl_mem               /* dst_image */, 
                const size_t *       /* src_origin[3] */,
                const size_t *       /* dst_origin[3] */,
                const size_t *       /* region[3] */, 
                cl_uint              /* num_events_in_wait_list */,
                const cl_event *     /* event_wait_list */,
                cl_event *           /* event */) CL_API_SUFFIX__VERSION_1_0;

extern CL_API_ENTRY cl_int CL_API_CALL
clEnqueueCopyImageToBuffer(cl_command_queue /* command_queue */,
                        cl_mem           /* src_image */,
                        cl_mem           /* dst_buffer */, 
                        const size_t *   /* src_origin[3] */,
                        const size_t *   /* region[3] */, 
                        size_t           /* dst_offset */,
                        cl_uint          /* num_events_in_wait_list */,
                        const cl_event * /* event_wait_list */,
                        cl_event *       /* event */) CL_API_SUFFIX__VERSION_1_0;

extern CL_API_ENTRY cl_int CL_API_CALL
clEnqueueCopyBufferToImage(cl_command_queue /* command_queue */,
                        cl_mem           /* src_buffer */,
                        cl_mem           /* dst_image */, 
                        size_t           /* src_offset */,
                        const size_t *   /* dst_origin[3] */,
                        const size_t *   /* region[3] */, 
                        cl_uint          /* num_events_in_wait_list */,
                        const cl_event * /* event_wait_list */,
                        cl_event *       /* event */) CL_API_SUFFIX__VERSION_1_0;


extern CL_API_ENTRY void * CL_API_CALL
clEnqueueMapImage(cl_command_queue  /* command_queue */,
               cl_mem            /* image */, 
               cl_bool           /* blocking_map */, 
               cl_map_flags      /* map_flags */, 
               const size_t *    /* origin[3] */,
               const size_t *    /* region[3] */,
               size_t *          /* image_row_pitch */,
               size_t *          /* image_slice_pitch */,
               cl_uint           /* num_events_in_wait_list */,
               const cl_event *  /* event_wait_list */,
               cl_event *        /* event */,
               cl_int *          /* errcode_ret */) CL_API_SUFFIX__VERSION_1_0;




extern CL_API_ENTRY cl_int CL_API_CALL
clEnqueueNativeKernel(cl_command_queue  /* command_queue */,
              void (*user_func)(void *), 
                   void *            /* args */,
                   size_t            /* cb_args */, 
                   cl_uint           /* num_mem_objects */,
                   const cl_mem *    /* mem_list */,
                   const void **     /* args_mem_loc */,
                   cl_uint           /* num_events_in_wait_list */,
                   const cl_event *  /* event_wait_list */,
                   cl_event *        /* event */) CL_API_SUFFIX__VERSION_1_0;

extern CL_API_ENTRY cl_mem CL_API_CALL
clCreateImage2D(cl_context              /* context */,
             cl_mem_flags            /* flags */,
             const cl_image_format * /* image_format */,
             size_t                  /* image_width */,
             size_t                  /* image_height */,
             size_t                  /* image_row_pitch */, 
             void *                  /* host_ptr */,
             cl_int *                /* errcode_ret */) CL_API_SUFFIX__VERSION_1_0;
                     
extern CL_API_ENTRY cl_mem CL_API_CALL
clCreateImage3D(cl_context              /* context */,
             cl_mem_flags            /* flags */,
             const cl_image_format * /* image_format */,
             size_t                  /* image_width */, 
             size_t                  /* image_height */,
             size_t                  /* image_depth */, 
             size_t                  /* image_row_pitch */, 
             size_t                  /* image_slice_pitch */, 
             void *                  /* host_ptr */,
             cl_int *                /* errcode_ret */) CL_API_SUFFIX__VERSION_1_0;
                     
extern CL_API_ENTRY cl_int CL_API_CALL
clGetSupportedImageFormats(cl_context           /* context */,
                        cl_mem_flags         /* flags */,
                        cl_mem_object_type   /* image_type */,
                        cl_uint              /* num_entries */,
                        cl_image_format *    /* image_formats */,
                        cl_uint *            /* num_image_formats */) CL_API_SUFFIX__VERSION_1_0;


/* Extension function access
*
* Returns the extension function address for the given function name,
* or NULL if a valid function can not be found.  The client must
* check to make sure the address is not NULL, before using or 
* calling the returned function address.
*/
extern CL_API_ENTRY void * CL_API_CALL clGetExtensionFunctionAddress(const char * /* func_name */) CL_API_SUFFIX__VERSION_1_0;

*/
     
