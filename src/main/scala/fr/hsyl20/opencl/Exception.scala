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


case class OpenCLException(val code: Int) extends RuntimeException(OpenCLException.errorMsg(code))

object OpenCLException {
   val CL_SUCCESS                               =   0
   val CL_DEVICE_NOT_FOUND                      =   -1
   val CL_DEVICE_NOT_AVAILABLE                  =   -2
   val CL_COMPILER_NOT_AVAILABLE                =   -3
   val CL_MEM_OBJECT_ALLOCATION_FAILURE         =   -4
   val CL_OUT_OF_RESOURCES                      =   -5
   val CL_OUT_OF_HOST_MEMORY                    =   -6
   val CL_PROFILING_INFO_NOT_AVAILABLE          =   -7
   val CL_MEM_COPY_OVERLAP                      =   -8
   val CL_IMAGE_FORMAT_MISMATCH                 =   -9
   val CL_IMAGE_FORMAT_NOT_SUPPORTED            =   -10
   val CL_BUILD_PROGRAM_FAILURE                 =   -11
   val CL_MAP_FAILURE                           =   -12

   val CL_INVALID_VALUE                         =   -30
   val CL_INVALID_DEVICE_TYPE                   =   -31
   val CL_INVALID_PLATFORM                      =   -32
   val CL_INVALID_DEVICE                        =   -33
   val CL_INVALID_CONTEXT                       =   -34
   val CL_INVALID_QUEUE_PROPERTIES              =   -35
   val CL_INVALID_COMMAND_QUEUE                 =   -36
   val CL_INVALID_HOST_PTR                      =   -37
   val CL_INVALID_MEM_OBJECT                    =   -38
   val CL_INVALID_IMAGE_FORMAT_DESCRIPTOR       =   -39
   val CL_INVALID_IMAGE_SIZE                    =   -40
   val CL_INVALID_SAMPLER                       =   -41
   val CL_INVALID_BINARY                        =   -42
   val CL_INVALID_BUILD_OPTIONS                 =   -43
   val CL_INVALID_PROGRAM                       =   -44
   val CL_INVALID_PROGRAM_EXECUTABLE            =   -45
   val CL_INVALID_KERNEL_NAME                   =   -46
   val CL_INVALID_KERNEL_DEFINITION             =   -47
   val CL_INVALID_KERNEL                        =   -48
   val CL_INVALID_ARG_INDEX                     =   -49
   val CL_INVALID_ARG_VALUE                     =   -50
   val CL_INVALID_ARG_SIZE                      =   -51
   val CL_INVALID_KERNEL_ARGS                   =   -52
   val CL_INVALID_WORK_DIMENSION                =   -53
   val CL_INVALID_WORK_GROUP_SIZE               =   -54
   val CL_INVALID_WORK_ITEM_SIZE                =   -55
   val CL_INVALID_GLOBAL_OFFSET                 =   -56
   val CL_INVALID_EVENT_WAIT_LIST               =   -57
   val CL_INVALID_EVENT                         =   -58
   val CL_INVALID_OPERATION                     =   -59
   val CL_INVALID_GL_OBJECT                     =   -60
   val CL_INVALID_BUFFER_SIZE                   =   -61
   val CL_INVALID_MIP_LEVEL                     =   -62
   val CL_INVALID_GLOBAL_WORK_SIZE              =   -63

   def errorMsg(err:Int): String = err match {
      case CL_SUCCESS => "Success"
      case CL_DEVICE_NOT_FOUND => "Device not found"
      case CL_DEVICE_NOT_AVAILABLE => "Device not available"
      case CL_COMPILER_NOT_AVAILABLE => "Compiler not available"
      case CL_MEM_OBJECT_ALLOCATION_FAILURE => "Memory object allocation failure"
      case CL_OUT_OF_RESOURCES => "Out of resources"
      case CL_OUT_OF_HOST_MEMORY => "Out of host memory"
      case CL_PROFILING_INFO_NOT_AVAILABLE => "Profiling information not available"
      case CL_MEM_COPY_OVERLAP => "Memory copy overlap"
      case CL_IMAGE_FORMAT_MISMATCH => "Image format mismatch"
      case CL_IMAGE_FORMAT_NOT_SUPPORTED => "Image format not supported"
      case CL_BUILD_PROGRAM_FAILURE => "Build program failure"
      case CL_MAP_FAILURE => "Map failure"

      case CL_INVALID_VALUE => "Invalid value"
      case CL_INVALID_DEVICE_TYPE => "Invalid device type"
      case CL_INVALID_PLATFORM => "Invalid platform"
      case CL_INVALID_DEVICE => "Invalid device"
      case CL_INVALID_CONTEXT => "Invalid context"
      case CL_INVALID_QUEUE_PROPERTIES => "Invalid queue properties"
      case CL_INVALID_COMMAND_QUEUE => "Invalid command queue"
      case CL_INVALID_HOST_PTR => "Invalid host pointer"
      case CL_INVALID_MEM_OBJECT => "Invalid memory object"
      case CL_INVALID_IMAGE_FORMAT_DESCRIPTOR => "Invalid image format descriptor"
      case CL_INVALID_IMAGE_SIZE => "Invalid image size"
      case CL_INVALID_SAMPLER => "Invalid sampler"
      case CL_INVALID_BINARY => "Invalid binary"
      case CL_INVALID_BUILD_OPTIONS => "Invalid build options"
      case CL_INVALID_PROGRAM => "Invalid program"
      case CL_INVALID_PROGRAM_EXECUTABLE => "Invalid program executable"
      case CL_INVALID_KERNEL_NAME => "Invalid kernel name"
      case CL_INVALID_KERNEL_DEFINITION => "Invalid kernel definition"
      case CL_INVALID_KERNEL  => "Invalid kernel"
      case CL_INVALID_ARG_INDEX => "Invalid argument index"
      case CL_INVALID_ARG_VALUE => "Invalid arguent value"
      case CL_INVALID_ARG_SIZE => "Invalid argument size"
      case CL_INVALID_KERNEL_ARGS => "Invalid kernel arguments"
      case CL_INVALID_WORK_DIMENSION => "Invalid work dimension"
      case CL_INVALID_WORK_GROUP_SIZE => "Invalid work group size"
      case CL_INVALID_WORK_ITEM_SIZE => "Invalid work item size"
      case CL_INVALID_GLOBAL_OFFSET => "Invalid global offset"
      case CL_INVALID_EVENT_WAIT_LIST => "Invalid event wait list"
      case CL_INVALID_EVENT => "Invalid event"
      case CL_INVALID_OPERATION => "Invalid operation"
      case CL_INVALID_GL_OBJECT => "Invalid GL object"
      case CL_INVALID_BUFFER_SIZE => "Invalid buffer size"
      case CL_INVALID_MIP_LEVEL => "Invalid mip level"
      case CL_INVALID_GLOBAL_WORK_SIZE => "Invalid global work size"
      case _ => "Unknown error"
   }
}
