package site.stanzhai.minark.util

import java.util.concurrent.ConcurrentLinkedQueue

import scala.collection.JavaConverters._
import scala.concurrent.duration._

import org.scalatest.FunSuite
import org.scalatest.concurrent.{Eventually, TimeLimits, Timeouts}

/**
 * Created by stan on 2017/5/7.
 */
class EventLoopSuite extends FunSuite with TimeLimits with Eventually {

  test("EventLoop") {
    val buffer = new ConcurrentLinkedQueue[Int]()

    val eventLoop = new EventLoop[Int]("int-event-loop") {
      override protected def onReceived(event: Int): Unit = {
        buffer.add(event)
      }

      override protected def onError(e: Throwable): Unit = {}
    }

    eventLoop.start()
    (1 to 100).foreach(eventLoop.post)
    eventually(timeout(5 seconds), interval(5 millis)) {
      assert((1 to 100) === buffer.asScala.toSeq)
    }
    eventLoop.stop()
  }
}
