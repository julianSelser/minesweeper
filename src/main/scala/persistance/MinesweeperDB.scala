package persistance

import scalikejdbc._

trait MinesweeperDB {

  Class.forName("org.h2.Driver")
  ConnectionPool.singleton("jdbc:h2:mem:minesweeper;DB_CLOSE_DELAY=-1", "user", "pass")

  implicit val session = AutoSession
}

object MinesweeperDB extends MinesweeperDB {
  def init = {
    sql"""
      create table if not exists users (
        id serial not null primary key,
        username varchar not null,
        password varchar not null
      )
      """.execute.apply()
  }
}
