package minesweeper.grid

import minesweeper.grid.Grid.Row
import org.scalatest.{FlatSpec, Matchers}

class GridSpec extends FlatSpec with Matchers {

  "Grid" should "know its dimentions given its rows" in {
    val grid = Grid(Row(Bomb))

    (grid.width, grid.height) should be (1, 1)
  }

  it should "know where its bombs are" in {
    val grid1 = Grid(Row(Bomb))

    val grid2 = Grid(
      Row(Number(2), Bomb),
      Row(Bomb, Number(2))
    )

    grid1.bombs should be (Set((0, 0)))
    grid2.bombs should be (Set((0, 1), (1, 0)))
  }
}
