package site.stanzhai.minark.util

import java.util.concurrent.{BlockingQueue, LinkedBlockingDeque}
import java.util.concurrent.atomic.AtomicBoolean

import scala.util.control.NonFatal

/**
 * Created by stan on 2017/5/7.
 */
abstract class EventLoop[E](name: String) {

  private val eventQueue: BlockingQueue[E] = new LinkedBlockingDeque[E]()
  private val stopped = new AtomicBoolean(false)

  private val eventThread = new Thread(name) {
    setDaemon(true)

    override def run(): Unit = {
      try {
        while (!stopped.get) {
          val event = eventQueue.take()
          try {
            onReceived(event)
          } catch {
            case NonFatal(e) =>
              onError(e)
          }
        }
      } catch {
        case ie: InterruptedException =>
        case NonFatal(e) =>
      }
    }
  }

  def start(): Unit = {
    if (stopped.get) {
      throw new IllegalStateException(s"$name has already been stopped!")
    }
    onStart()
    eventThread.start()
  }

  def post(event: E): Unit = {
    eventQueue.put(event)
  }

  def isActive: Boolean = eventThread.isAlive

  def stop(): Unit = {
    if (stopped.compareAndSet(false, true)) {
      eventThread.interrupt()
      var onStopCalled = false
      try {
        eventThread.join()
        onStopCalled = true
        onStop()
      } catch {
        case ie: InterruptedException =>
          if (!onStopCalled) {
            onStop()
          }
      }
    } else {
      // Keep quiet to allow calling `stop` multiple times.
    }
  }

  protected def onStart(): Unit = {}

  protected def onReceived(event: E): Unit

  protected def onStop(): Unit = {}

  protected def onError(e: Throwable): Unit
}
