package persistance.repositories

import minesweeper._
import persistance.misc.GridToDBJsonFormatter
import rest.dtos.{Game, NewGame}
import scalikejdbc._
import spray.json._

import scala.util.Try

object GameRepository extends MinesweeperDB {
  def create(grid: Grid)(implicit userId: Long): Game = {
    val jsonString = GridToDBJsonFormatter.write(grid).toString()

    val gameId = sql"insert into games (json, user_id) values (${jsonString}, ${userId})"
      .updateAndReturnGeneratedKey.apply()

    Game(gameId, grid)
  }

  def update(game: Game)(implicit userId: Long): Unit = {
    val query = sql"""
      update games
      set json=${GridToDBJsonFormatter.write(game.grid).toString}
      where id=${game.id} and user_id=$userId
    """

    query.update.apply()
  }

  def getGamesByUserId(userId: Long): List[Game] = {
    sql"select id, json, user_id from games where user_id=${userId}"
      .map(rs =>
        Game(
          rs.long("id"),
          GridToDBJsonFormatter.read(rs.string("json").parseJson)
        ))
      .list()
      .apply()
  }

  def getGameById(id: Long)(implicit userId: Long): Option[Game] = {
    sql"select * from games where user_id=$userId and id=$id limit 1"
      .map(rs =>
        Game(
          rs.long("id"),
          GridToDBJsonFormatter.read(rs.string("json").parseJson)
        ))
      .single()
      .apply()
  }
}
