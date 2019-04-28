package persistance.repositories

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
        username varchar not null unique,
        password varchar not null
      )
      """.execute.apply()

    sql"""
      create table if not exists games (
        id serial not null primary key,
        width int not null,
        height int not null,
        mines int not null,
        status varchar not null,
        started datetime not null,
        user_id int not null,
        foreign key (user_id) references users(id)
      )
      """.execute.apply()
  }
}
