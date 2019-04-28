
import minesweeper._
import org.scalatest._
import minesweeper.Grid.Row


class GameSpec extends FlatSpec with Matchers {

  "Game" should "be lost when sweeped a 1x1 grid" in {
    val game = Grid(width = 1, height = 1)

    game.sweep(0, 0)

    game.state shouldBe Lost
  }

  it should "know its dimentions given its rows" in {
    val game = Grid(Row(Bomb))

    (game.width, game.height) should be (1, 1)
  }

  it should "be able to reveal empty space" in {
    val game = Grid(
      Row(Bomb,       Number(1),  Empty,  Number(1),  Bomb),
      Row(Number(1),  Number(1),  Empty,  Number(1),  Number(1)),
      Row(Empty,      Empty,      Empty,  Empty,      Empty))

    game.sweep(2, 1) //clicks the center

    game.getRevealed shouldBe Grid(
      Row(Hidden,     Number(1),  Empty,  Number(1),  Hidden),
      Row(Number(1),  Number(1),  Empty,  Number(1),  Number(1)),
      Row(Empty,      Empty,      Empty,  Empty,      Empty))
  }

  it should "be won when marked a bomb in (1, 1) in a 2x1 grid and the bomb was there" in {
    val game = Grid(Row(Bomb, Number(1)))

    game.markBombIn(0, 0)

    game.state shouldBe Won
  }

  it should "should ignore a sweep on a cell marked as a bomb" in {
    val game = Grid(
          Row(Number(2), Bomb),
          Row(Bomb, Number(2)))

    game.markBombIn(0, 0)
    game.sweep(0, 0)

    game.state shouldBe OnGoing
  }

  it should "should all cells as hidden if the game just started" in {
    val game = Grid(1, 1)

    game.getRevealed shouldBe Grid(Row(Hidden))
  }

  it should "should reveal a cell that was clicked (sweeped)" in {
    val game = Grid(Row(Number(1), Bomb))

    game.sweep(0, 0)

    game.getRevealed shouldBe Grid(Row(Number(1), Hidden))
  }

  it should "should show marked bombs in game grid" in {
    val game = Grid(Row(Bomb))

    game.markBombIn(0, 0)

    game.getRevealed shouldBe Grid(Row(MarkedBomb))
  }
}