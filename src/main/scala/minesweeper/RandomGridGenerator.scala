package minesweeper

import Grid.Row

case class RandomGridGenerator(width: Int, height: Int) {
  def generate: Grid = {
    if(width == 1 && height == 1)
      Grid(Row(Bomb))
    else ???
  }
}
