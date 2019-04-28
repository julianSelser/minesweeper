package minesweeper

import Grid.Row

case class RandomGridGenerator(width: Int, height: Int) {
  val nBombs = ((width*height)/3D).ceil.toInt

  def generate: Grid = {
    if(width == 1 && height == 1)
      Grid(Row(Bomb))
    else ???
  }
}
