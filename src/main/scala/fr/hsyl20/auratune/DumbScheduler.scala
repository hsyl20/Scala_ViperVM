package fr.hsyl20.auratune

class DumbScheduler extends Scheduler {

   def schedule(task:Task): Event = {
      //Find a device
      //Dumb: we take the first one
      val device = Device.list.head

      //Dumb: We transfer data to device memory if necessary without checking memory availability
      /* Transfer inputs */
      for ((sym,d) <- task.inputs) d.postTransferRequest(device)

      /* Detach ReadWrite buffers */
      //TODO

      /* Allocate outputs */
      for ((sym,d) <- task.outputs) d.allocate(device)

      // Launch task
      val event = device.execute(task)

      event
   }
}
