package minesweeper

import minesweeper.grid.Grid

import scala.collection.mutable.HashSet

case class Game(grid: Grid) {
  var state: GameState = OnGoing

  val bombs = grid.bombs
  val markedBombs = new HashSet[(Int, Int)]

  def markBombIn(x: Int, y: Int) = {
    markedBombs.add((x, y))

    if(bombs.equals(markedBombs)) {
      state = Won
    }
  }

  def sweep(x: Int, y: Int) = {}

}

object Game {
  def apply(wide: Int, tall: Int): Game = Game(Grid(wide, tall))
  def apply(grid: Grid): Game = new Game(grid)
}
