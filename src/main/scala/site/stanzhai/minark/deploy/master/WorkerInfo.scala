package site.stanzhai.minark.deploy.master

import akka.actor.ActorRef

/**
  * Created by stan on 2017/4/7.
  */
class WorkerInfo(
    val id: String,
    val host: String,
    val port: Int,
    val cores: Int,
    val memory: Int,
    val actorRef: ActorRef)
  extends Serializable {

}
