package minesweeper

import minesweeper.grid.Grid

import scala.collection.mutable.MutableList

case class Game(grid: Grid) {
  val marked = new MutableList[(Int, Int)]
  val bombs = grid.bombs

  def state: GameState = Lost

  def mark(x: Int, y: Int) = {}

  def sweep(x: Int, y: Int) = {}

}

object Game {
  def apply(wide: Int, tall: Int): Game = Game(Grid(wide, tall))
  def apply(grid: Grid): Game = new Game(grid)
}
