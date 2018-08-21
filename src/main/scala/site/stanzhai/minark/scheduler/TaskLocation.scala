package site.stanzhai.minark.scheduler

sealed trait TaskLocation {
  def host: String
}
