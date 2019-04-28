package minesweeper.grid

import minesweeper.grid.Grid.Row
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.immutable.List

class GridSpec extends FlatSpec with Matchers {

  "Grid" should "know its dimentions given its rows" in {
    val grid = Grid(Row(Bomb))

    (grid.width, grid.height) should be (1, 1)
  }

  it should "know where its bombs are" in {
    val grid1 = Grid(Row(Bomb))

    grid1.bombs should be (List((0, 0)))
  }
}
