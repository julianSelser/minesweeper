package rest.dtos

import minesweeper.Grid

case class Game(id: Long, grid: Grid, message: String)
case class NewGame(width: Int, height: Int, mines: Int)