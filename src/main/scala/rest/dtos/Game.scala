package rest.dtos

import minesweeper.{CantGenerateGridException, Grid, RandomGridGenerator}

import scala.util.Try

case class Spot(x: Int, y: Int) {
  def isInside(game: Game) = game.grid isInsideGrid (x, y)
}

case class NewGame(width: Int, height: Int, mines: Int) {
  def grid = {
    val newGameGenerator = RandomGridGenerator(width, height, mines)

    Try(newGameGenerator.generate()).recover({
      case generationError: CantGenerateGridException => generationError.getSaneGrid
    }).get
  }
}

case class Game(id: Long, grid: Grid) {
  def state = grid.state
  def sweep(move: Spot) = grid.sweep(move.x, move.y)
  def mark(move: Spot) = grid.markMineIn(move.x, move.y)
}
