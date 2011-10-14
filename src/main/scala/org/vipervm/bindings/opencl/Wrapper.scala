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

import org.vipervm.bindings.NativeSize
import com.sun.jna._
import java.nio.ByteBuffer

object Wrapper {

  Native.register("OpenCL")

  /* Platform API */
  @native def clGetPlatformIDs(numEntries:Int, platforms:Pointer, numPlatforms:Pointer): Int
  @native def clGetPlatformInfo(platform:Pointer, paramName:Int, paramValueSize:NativeSize, paramValue:Pointer, paramValueSizeRet:Pointer): Int

  /* Device API */
  @native def clGetDeviceIDs(platform:Pointer, deviceType:Int, numEntries:Int, devices:Pointer, numDevices:Pointer): Int
  @native def clGetDeviceInfo(device:Pointer, paramName:Int, paramValueSize:NativeSize, paramValue:Pointer, paramValueSizeRet:Pointer): Int

  /* Context API */
  @native def clCreateContext(properties:Pointer, numDevices:Int, devices:Pointer, pfnNotify:Pointer, userData:Pointer, errcodeRet:Pointer): Pointer
  @native def clCreateContextFromType(propeties:Pointer, deviceType:Int, pfnNotify:Pointer, userData:Pointer, errcodeRet:Pointer): Pointer
  @native def clRetainContext(context:Pointer): Int
  @native def clReleaseContext(context:Pointer): Int
  @native def clGetContextInfo(context:Pointer, paramName:Int, paramValueSize:NativeSize, paramValue:Pointer, paramValueSizeRet:Pointer): Int

  /* Command Queue APIs */
  @native def clCreateCommandQueue(context:Pointer, device:Pointer, properties:Long, errcodeRet:Pointer): Pointer
  @native def clRetainCommandQueue(commandQueue:Pointer): Int
  @native def clReleaseCommandQueue(commandQueue:Pointer): Int
  @native def clGetCommandQueueInfo(commandQueue:Pointer, paramName:Int, paramValueSize:NativeSize, paramValue:Pointer, paramValueSizeRet:Pointer): Int
  @native def clSetCommandQueueProperty(commandQueue:Pointer, properties:Long, enable:Int, oldProperties:Pointer): Int

  /* Memory Object APIs  */
  //@native def clCreateBuffer(context:Pointer, flags:Long, size:NativeSize, hostPtr:Pointer, errcodeRet:Pointer): Pointer
  @native def clCreateBuffer(context:Pointer, flags:Long, size:NativeSize, hostPtr:ByteBuffer, errcodeRet:Pointer): Pointer

  @native def clRetainMemObject(memobj:Pointer): Int
  @native def clReleaseMemObject(memobj:Pointer): Int
  @native def clGetMemObjectInfo(memobj:Pointer, paramName:Int, paramValueSize:NativeSize, paramValue:Pointer, paramValueSizeRet:Pointer): Int

  /* Program Object APIs  */
  @native def clCreateProgramWithSource(context:Pointer, count:Int, strings:StringArray, lengths:Pointer, errcodeRet:Pointer): Pointer
  @native def clRetainProgram(program:Pointer): Int
  @native def clReleaseProgram(program:Pointer): Int
  @native def clBuildProgram(program:Pointer, numDevices:Int, deviceList:Pointer, options:String, pfnNotify:Pointer, userData:Pointer): Int
  @native def clGetProgramInfo(program:Pointer, paramName:Int, paramValueSize:NativeSize, paramValue:Pointer, paramValueSizeRet:Pointer): Int

  @native def clUnloadCompiler():Int

  @native def clGetProgramBuildInfo(program:Pointer, device:Pointer, paramName:Int, paramValueSize:NativeSize, paramValue:Pointer, paramValueSizeRet:Pointer): Int

  /* Kernel Object APIs */
  @native def clCreateKernel(program:Pointer, kernelName:String, errcodeRet:Pointer): Pointer
  @native def clRetainKernel(kernel:Pointer): Int
  @native def clReleaseKernel(kernel:Pointer): Int
  @native def clGetKernelInfo(kernel:Pointer, paramName:Int, paramValueSize:NativeSize, paramValue:Pointer, paramValueSizeRet:Pointer): Int
  @native def clGetKernelWorkGroupInfo(kernel:Pointer, device:Pointer, paramName:Int, paramValueSize:NativeSize, paramValue:Pointer, paramValueSizeRet:Pointer): Int
  @native def clCreateKernelsInProgram(program:Pointer, numKernels:Int, kernels:Pointer, numkernelsRet:Pointer): Int
  @native def clSetKernelArg(kernel:Pointer, argIndex:Int, argSize:NativeSize, argValue:Pointer): Int

  /* Event Object APIs  */
  @native def clRetainEvent(event:Pointer): Int
  @native def clReleaseEvent(event:Pointer): Int
  @native def clGetEventInfo(event:Pointer, paramName:Int, paramValueSize:NativeSize, paramValue:Pointer, paramValueSizeRet:Pointer): Int
  @native def clWaitForEvents(numEvents:Int, eventList:Pointer): Int

  /* Profiling APIs  */
  @native def clGetEventProfilingInfo(event:Pointer, paramName:Int, paramValueSize:NativeSize, paramValue:Pointer, paramValueSizeRet:Pointer): Int

  /* Flush and Finish APIs */
  @native def clFlush(commandQueue:Pointer): Int
  @native def clFinish(commandQueue:Pointer): Int

  /* Enqueued Commands APIs */
  @native def clEnqueueReadBuffer(commandQueue:Pointer, buffer:Pointer, blockingRead:Int, offset:NativeSize, cb:NativeSize, ptr:Pointer, numEventsInWaitList:Int, eventWaitList:Pointer, event:Pointer): Int
  @native def clEnqueueWriteBuffer(commandQueue:Pointer, buffer:Pointer, blockingWrite:Int, offset:NativeSize, cb:NativeSize, ptr:Pointer, numEventsInWaitList:Int, eventWaitList:Pointer, event:Pointer): Int
  @native def clEnqueueCopyBuffer(commandQueue:Pointer, srcBuffer:Pointer, dstBuffer:Pointer, srcOffset:NativeSize, dstOffset:NativeSize, cb:NativeSize, numEventsInWaitList:Int, eventWaitList:Pointer, event:Pointer): Int

  @native def clEnqueueMapBuffer(commandQueue:Pointer, buffer:Pointer, blockingMap:Int, mapFlags:Long, offset:NativeSize, cb:NativeSize, numEventsInWaitList:Int, eventWaitList:Pointer, event:Pointer, errcodeRet:Pointer): Pointer

  @native def clEnqueueUnmapMemObject(commandQueue:Pointer, memobj:Pointer, mappedPtr:Pointer, numEventsInWaitList:Int, eventWaitList:Pointer, event:Pointer): Int

  @native def clEnqueueNDRangeKernel(commandQueue:Pointer, kernel:Pointer, workDim:Int, globalWorkOffset:Pointer, globalWorkSize:Pointer, localWorkSize:Pointer, numEventsInWaitList:Int, eventWaitList:Pointer, event:Pointer): Int

  @native def clEnqueueWaitForEvents(commandQueue:Pointer, numEvents:Int, eventList:Pointer): Int

  @native def clEnqueueTask(commandQueue:Pointer, kernel:Pointer, numEventsInWaitList:Int, eventWaitList:Pointer, event:Pointer): Int
  @native def clEnqueueMarker(commandQueue:Pointer, event:Pointer): Int
  @native def clEnqueueBarrier(commandQueue:Pointer): Int

  /* Image APIs */
  @native def clGetImageInfo(image:Pointer, paramName:Int, paramValueSize:NativeSize, paramValue:Pointer, paramValueSizeRet:Pointer): Int

  /* Sampler APIs */
  @native def clCreateSampler(context:Pointer, normalizedCoords:Int, addressingMode:Int, filterMode:Int, errcodeRet:Pointer): Pointer
  @native def clGetSamplerInfo(sampler:Pointer, paramName:Int, paramValueSize:NativeSize, paramValue:Pointer, paramValueSizeRet:Pointer): Int
  @native def clRetainSampler(sampler:Pointer): Int
  @native def clReleaseSampler(sampler:Pointer): Int


  implicit def int2nativesize(i:Int): NativeSize = new NativeSize(i)
  implicit def long2nativesize(i:Long): NativeSize = new NativeSize(i)
  implicit def nativesize2long(i:NativeSize): Long = i.longValue()

  implicit def arraypointer2pointer(a:Seq[Pointer]): Pointer = {
    if (a == null) {
      null
    }
    else {
      val m = new Memory(a.length * Pointer.SIZE)
      for ((e,i) <- a.zipWithIndex)
        m.setPointer(Pointer.SIZE*i, e)
      m
    }
  }

  implicit def arraynativesize2pointer(a:Seq[NativeSize]): Pointer = {
    if (a == null) {
      null
    }
    else {
      val m = new Memory(a.length * NativeSize.SIZE)
      for ((e,i) <- a.zipWithIndex)
        m.setLong(NativeSize.SIZE*i, e.value)
      m
    }
  }

  implicit def stringarray2stringarray(a:Array[String]): StringArray =
    if (a == null) null else new StringArray(a)
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
     
