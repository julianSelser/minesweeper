package minesweeper

import minesweeper.Grid.Row

import scala.collection.mutable

case class Grid(rows: Row*) {
  val width = rows(0).length
  val height = rows.length

  var state: GameState = OnGoing

  private val markedBombs = new mutable.HashSet[(Int, Int)]
  private val revealed = new mutable.HashSet[(Int, Int)]

  private val bombs: Set[(Int, Int)] = {
    for {
      (row, x) <- rows.view.zipWithIndex
      (elem, y) <- row.view.zipWithIndex
      if (elem equals Bomb)
    } yield (x, y)
  }.toSet

  def markBombIn(x: Int, y: Int) = {
    markedBombs.add((x, y))

    if (bombs.equals(markedBombs))
      state = Won
  }

  def sweep(x: Int, y: Int): Unit = {
    //if the point is marked for bomb we dont sweep it
    if (markedBombs.contains((x, y))) return

    rows(y)(x) match {
      case Bomb => reveal(x, y); state = Lost
      case Empty => revealAreaFrom(x, y)
      case Number(_) => reveal(x, y)
      case _ => throw new RuntimeException("???")
    }
  }

  def getRevealed: Grid = {
    val revealedItems = rows.view.zipWithIndex.map(rowAndIndex => {
      val row = rowAndIndex._1
      val y = rowAndIndex._2

      row.view.zipWithIndex.map(elemAndIndex => {
        val elm = elemAndIndex._1
        val x = elemAndIndex._2

        if (revealed.contains(x, y))
          elm
        else if(markedBombs.contains(x, y))
          MarkedBomb
        else
          Hidden
      }).toList
    }).toList

    Grid(revealedItems: _*)
  }

  private def reveal(x: Int, y: Int): Unit = {
    revealed.add((x, y))
  }

  private def revealAreaFrom(x: Int, y: Int): Unit = {
    reveal(x, y) // function is always called on an Empty first

    val directions = List(
      (x, y - 1), // up
      (x, y + 1), // down
      (x - 1, y), // left
      (x + 1, y)  // right
    )

    directions
      .filter(isInsideGrid)           // should be inside grid
      .filter(!revealed.contains(_))  // shouldnt have been revealed
      .foreach(_ match {
        case (x, y) => {
          if (rows(y)(x) equals Empty)
            revealAreaFrom(x, y)
          else
            reveal(x, y)
        }
    })
  }

  private def isInsideGrid(point: (Int, Int)) = point match {
    case (x, y) => x < width && x >= 0 && y < height && y >= 0
  }
}

object Grid {
  type Row = List[GridElement]

  def Row(gridElement: GridElement*): Row = gridElement.toList

  def apply(width: Int, height: Int): Grid = {
    new Grid(Row(Bomb)) //TODO: make it random
  }
}