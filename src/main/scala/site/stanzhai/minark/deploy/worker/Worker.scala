package site.stanzhai.minark.deploy.worker

import scala.concurrent.duration._

import akka.actor.{Actor, ActorIdentity, ActorRef, ActorSelection, Identify, Props, ReceiveTimeout}

import site.stanzhai.minark.deploy.DeployMessages._
import site.stanzhai.minark.deploy.master.Master
import site.stanzhai.minark.util.AkkaUtils
import site.stanzhai.minark.util.Logging

/**
  * Created by stan on 2017/3/26.
  */
class Worker(host: String, port: Int, cores: Int, memory: Int, masterUrl: String) extends Actor with Logging {

  override def preStart(): Unit = {
    sendIdentifyRequest()
  }

  def sendIdentifyRequest(): Unit = {
    context.actorSelection(Master.akkaUrl(masterUrl)) ! Identify(masterUrl)
    import context.dispatcher
    context.system.scheduler.scheduleOnce(3.seconds, self, ReceiveTimeout)
  }

  override def receive: Receive = identifying

  def identifying: Receive = {
    case ActorIdentity(path, Some(master)) =>
      context.watch(master)
      context.become(active(master))
      context.setReceiveTimeout(Duration.Undefined)
      master ! RegisterWorker("test", host, port, cores, memory)
    case ActorIdentity(path, None) =>
      logInfo(s"Remote actor not available: $path")
    case ReceiveTimeout =>
      sendIdentifyRequest()
  }

  def active(master: ActorRef): Receive = {
    case _ =>
  }

}

object Worker {
  private val actorSystemName = "minarkWorker"
  private val actorName = "worker"

  def props(): Props = {
    Props(classOf[Worker], "0.0.0.0", 3333, 18, 50, "minark://0.0.0.0:3332")
  }

  def main(args: Array[String]): Unit = {
    val actorSystem = AkkaUtils.createActorSystem(actorSystemName, "0.0.0.0", 3333)
    actorSystem.actorOf(Worker.props(), actorName)
  }
}