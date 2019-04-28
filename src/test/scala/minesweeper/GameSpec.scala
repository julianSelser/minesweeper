
import minesweeper.grid.{Number, Bomb, Grid}
import minesweeper.grid.Grid.Row
import minesweeper.{Game, Lost, Won}
import org.scalatest._


class GameSpec extends FlatSpec with Matchers {

  it should "be won when marked a bomb in (1, 1) in a 2x1 grid and the bomb was there" in {
    val grid = Grid(Row(Bomb, Number(1)))
    val game = Game(grid)

    game.markBombIn(0, 0)

    game.state shouldBe Won
  }
}