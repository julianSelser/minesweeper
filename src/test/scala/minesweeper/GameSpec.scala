
import minesweeper.grid.{Number, Bomb, Grid}
import minesweeper.grid.Grid.Row
import minesweeper.{Game, Lost, Won}
import org.scalatest._


class GameSpec extends FlatSpec with Matchers {

  "Game" should "be lost when sweeped a 1x1 grid" in {
    val game = Game(wide = 1, tall = 1)

    game.sweep(1, 1)

    game.state shouldBe Lost
  }
}