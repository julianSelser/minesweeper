package persistance

import minesweeper.{OnGoing, Won}
import org.scalatest.{FlatSpec, Matchers}
import persistance.repositories.{GameRepository, MinesweeperDB, UserRepository}
import rest.dtos.{NewGame, Spot}

class RepositorySpec extends FlatSpec with Matchers {
  MinesweeperDB.init

  val fixture = new {
    implicit val userId = UserRepository.save("admin", "admin")
    val game3x3 = GameRepository.create(NewGame(width = 3, height = 3, mines = 3))
    val game1x1 = GameRepository.create(NewGame(width = 1, height = 1, mines = 1))
  }

  "UserRepository" should "be able to save a user" in {
    UserRepository.exists("admin", "admin") shouldBe defined
  }

  "GameRepository" should "create a couple games" in {
    val userGames = GameRepository.getUserGames(fixture.userId)

    userGames should have length(2)
  }

  "GameRepository" should "be able to update a game" in {
    implicit val userId = fixture.userId

    val gameBeforeMove = GameRepository.getGameById(2).get

    gameBeforeMove.state shouldBe OnGoing
    gameBeforeMove mark Spot(0, 0)

    GameRepository.update(gameBeforeMove)

    val gameAfterMove = GameRepository.getGameById(2).get

    gameAfterMove.state shouldBe Won
  }
}
