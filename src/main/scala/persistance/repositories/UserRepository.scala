package persistance.repositories

import scalikejdbc._

object UserRepository extends MinesweeperDB {
  def save(username: String, password: String): Long = {
    sql"insert into users (username, password) values (${username}, ${password})"
      .updateAndReturnGeneratedKey.apply()
  }

  def passwordFor(username: String): Option[(Long, String)] = {
    sql"select id, password from users where username=${username}"
      .map(rs => (rs.long("id"), rs.string("password")))
      .single()
      .apply()
  }

  def exists(username: String, password: String): Option[Long] = {
    sql"select id from users where username=${username} and password=${password}"
      .map(_.long("id"))
      .single()
      .apply()
  }
}
