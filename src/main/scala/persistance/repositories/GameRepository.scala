package persistance.repositories

import spray.json._
import DefaultJsonProtocol._
import minesweeper._
import persistance.misc.GridToDBJsonFormatter
import rest.dtos.{Game, NewGame}
import scalikejdbc._

import scala.util.Try

object GameRepository extends MinesweeperDB {
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

  def create(newGame: NewGame)(implicit userId: Long): Game = {
    val newGameGenerator = RandomGridGenerator(newGame.width, newGame.height, newGame.mines)

    val generationTry = Try(newGameGenerator.generate()).recover({
      case generationError: CantGenerateGridException => generationError.getSaneGrid
    })

    save(generationTry.get)
  }

  private def save(grid: Grid)(implicit userId: Long): Game = {
    val jsonString = GridToDBJsonFormatter.write(grid).toString()

    val gameId = sql"insert into games (json, user_id) values (${jsonString}, ${userId})".updateAndReturnGeneratedKey.apply()

    Game(gameId, grid)
  }
}
