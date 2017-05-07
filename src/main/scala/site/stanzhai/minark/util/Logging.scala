package site.stanzhai.minark.util

import org.apache.log4j.PropertyConfigurator
import org.slf4j.{Logger, LoggerFactory}

/**
  * Created by stan on 2017/4/7.
  */
trait Logging {
  @transient private var _log: Logger = null

  protected def logName: String = {
    this.getClass.getName.stripSuffix("$")
  }

  protected def log: Logger = {
    if (_log == null) {
      initIfNecessary()
      _log = LoggerFactory.getLogger(logName)
    }
    _log
  }

  protected def logInfo(msg: => String) {
    if (log.isInfoEnabled) log.info(msg)
  }

  protected def logDebug(msg: => String) {
    if (log.isDebugEnabled) log.debug(msg)
  }

  protected def logTrace(msg: => String) {
    if (log.isTraceEnabled) log.trace(msg)
  }

  protected def logWarning(msg: => String) {
    if (log.isWarnEnabled) log.warn(msg)
  }

  protected def logError(msg: => String) {
    if (log.isErrorEnabled) log.error(msg)
  }

  protected def logError(msg: => String, e: Throwable) {
    if (log.isErrorEnabled) log.error(msg, e)
  }

  private def initIfNecessary(): Unit = {
    if (!Logging.initialized) {
      Logging.initLocker.synchronized {
        if (!Logging.initialized) {
          PropertyConfigurator.configure("conf/log4j.properties")
          Logging.initialized = true
        }
      }
    }
  }
}

private object Logging {
  @volatile var initialized: Boolean = false
  val initLocker = new Object()
}
