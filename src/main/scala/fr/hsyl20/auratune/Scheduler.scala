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
**                     GPLv3
*/

package fr.hsyl20.auratune

class Scheduler {

   def schedule(task:Task): Event = {
      //Find a device
      //Dumb: we take the first one
      val device = Device.list.head

      //Dumb: We transfer data to device memory if necessary without checking memory availability
      val syncs = for (Argument(data,access) <- task.args) yield {
         //Try to find existing Buffer for this data in device memory
         val buffer = data.buffers.get(device).getOrElse {
            // Create it if it doesn't exist
            val buf = data.createBuffer(device)
            data.buffers += (device -> buf)
            buf
         }
         // Synchronize buffer (copy data from host or from elsewhere if necessary)
         data.synchronize(buffer, access)
      }

      // Launch task
      val event = device.execute(task, after=syncs)

      event
   }
}
