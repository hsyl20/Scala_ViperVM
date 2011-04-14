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

package fr.hsyl20.vipervm.platform

object EventPollingThread {

  /** List of monitored events. Require synchronized access */
  private var events: List[Event] = Nil

  /** Indicate that the thread must stop */
  private var _stop:Boolean = false

  var sleepTime:Long = 500


  def monitorEvent(event:Event): Unit = thread.synchronized {
    events = event :: events
    thread.notify
  }

  def shutdown: Unit = {
    thread.synchronized {
      _stop = true
      thread.notify
    }
  }

  private lazy val thread = new Thread {
    override def run: Unit = {
      val ev = this.synchronized {
        val ev = events
        events = Nil
        ev
      }

      val (finished, running) = ev.span(_.test)

      finished.foreach(_.complete)

      val ev2 = this.synchronized {
        events = running ::: events
        if (events.isEmpty && !_stop)
          this.wait
        events
      }

      Thread.sleep(sleepTime)

      if (!ev2.isEmpty || !_stop)
        run
    }
  }
}
