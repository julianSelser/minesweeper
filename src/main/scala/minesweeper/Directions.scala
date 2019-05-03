package minesweeper

object Directions {
  def adjacentFrom(x: Int, y: Int) = List(
    (x, y - 1),
    (x, y + 1),
    (x + 1, y),
    (x - 1, y),
    (x + 1, y - 1),
    (x + 1, y + 1),
    (x - 1, y - 1),
    (x - 1, y + 1),
  )

  def adjacentFrom(point: (Int, Int), adjancent: Set[(Int, Int)]): List[(Int, Int)] = point match {
    case (x, y) => Directions.adjacentFrom(x, y).filter(adjancent.contains)
  }
}
