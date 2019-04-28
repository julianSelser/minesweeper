package minesweeper.grid

import minesweeper.grid.Grid.Row

case class Grid(rows: Row*) {
  val width = rows(0).length
  val height = rows.length
  val bombs: Set[(Int, Int)] = {
    for {
      (row, x) <- rows.view.zipWithIndex
      (elem, y) <- row.view.zipWithIndex
      if(elem equals Bomb)
    } yield (x, y)
  }.toSet
}

object Grid {
  type Row = List[GridElement]

  def Row(gridElement: GridElement*): Row = gridElement.toList

  def apply(x: Int, y: Int): Grid = {
    new Grid(Row(Bomb))
  }
}