package site.stanzhai.minark.deploy.Worker

import akka.actor.{Actor, ActorSelection, Props}
import akka.actor.Actor.Receive

import site.stanzhai.minark.deploy.DeployMessages._
import site.stanzhai.minark.deploy.master.Master
import site.stanzhai.minark.util.AkkaUtils

/**
  * Created by stan on 2017/3/26.
  */
class Worker(host: String, port: Int, cores: Int, memory: Int, masterUrl: String) extends Actor {
  private var master: ActorSelection = null

  override def preStart(): Unit = {
    connectToMaster()
  }

  private def connectToMaster(): Unit = {
    master = context.actorSelection(Master.akkaUrl(masterUrl))
    master ! RegisterWorker("test", host, port, cores, memory)
  }

  override def receive: Receive = {
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