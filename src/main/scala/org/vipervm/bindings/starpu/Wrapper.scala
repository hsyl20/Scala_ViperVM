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

package org.vipervm.bindings.starpu

import org.vipervm.bindings.NativeSize
import com.sun.jna._
import java.nio.ByteBuffer

object Wrapper {

  Native.register("starpu")

  /* Init */
  @native def starpu_conf_init(conf:Pointer):Int
  @native def starpu_init(conf:Pointer):Int
  @native def starpu_shutdown():Unit

  @native def starpu_worker_get_count():Int
  @native def starpu_combined_worker_get_count():Int
  @native def starpu_cpu_worker_get_count():Int
  @native def starpu_cuda_worker_get_count():Int
  @native def starpu_spu_worker_get_count():Int
  @native def starpu_opencl_worker_get_count():Int

  /* Data */
  @native def starpu_data_unregister(handle:Pointer):Unit
  @native def starpu_data_unregister_no_coherency(handle:Pointer):Unit
  @native def starpu_data_invalidate(handle:Pointer):Unit

  /* Consistency flag */
  @native def starpu_data_set_sequential_consistency_flag(handle:Pointer, flag:Int):Unit
  @native def starpu_data_get_default_sequential_consistency_flag():Unit
  @native def starpu_data_set_default_sequential_consistency_flag(flag:Int):Unit

  /* Task */
  @native def starpu_task_create():Pointer
  @native def starpu_task_init(task:Pointer):Unit
  @native def starpu_task_deinit(task:Pointer):Unit
  @native def starpu_task_destroy(task:Pointer):Unit
  @native def starpu_task_submit(task:Pointer):Int
  @native def starpu_task_wait(task:Pointer):Int
  @native def starpu_task_wait_for_all():Int
  @native def starpu_task_wait_for_no_ready():Int
  @native def starpu_task_declare_deps_array(task:Pointer,ndeps:Int,deps:Array[Pointer]):Int

  /* Tag */
  //TODO



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
