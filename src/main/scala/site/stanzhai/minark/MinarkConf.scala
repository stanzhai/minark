package site.stanzhai.minark

import java.io.File

import com.typesafe.config.{Config, ConfigFactory}

class MinarkConf {

  private lazy val config: Config = {
    val filePath = "./conf/minark.conf"
    val file = new File(filePath)
    if (!file.exists()) {
      throw new Exception(s"minark config file does not exists: $filePath")
    }
    ConfigFactory.parseFile(file)
  }

  val MasterPort = config.getInt("minark.master.port")
}
