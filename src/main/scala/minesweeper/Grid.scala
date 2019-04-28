package minesweeper

import minesweeper.Grid.Row

import scala.collection.mutable

case class Grid(bombs: Set[(Int, Int)], rows: Seq[Row], revealed: mutable.HashSet[(Int, Int)] = new mutable.HashSet[(Int, Int)]) {
  val width = rows(0).length
  val height = rows.length
  val started = System.currentTimeMillis

  var state: GameState = OnGoing

  private val markedBombs = new mutable.HashSet[(Int, Int)]

  def markBombIn(x: Int, y: Int) = {
    markedBombs.add((x, y))

    if (bombs.equals(markedBombs))
      state = Won
  }

  def sweep(x: Int, y: Int): Unit = {
    //if the point was marked as a bomb
    //by the user we never sweep it
    if (markedBombs.contains((x, y))) return

    rows(y)(x) match {
      case Number(_) => reveal(x, y)
      case Bomb => reveal(x, y); state = Lost
      case Empty => revealAreaFrom(x, y)
      case _ => throw new RuntimeException("THIS SHOULD NEVER HAPPEN")
    }
  }

  def getRevealed: Grid = {
    val revealedItems = rows.view.zipWithIndex.map(_ match {
      case (row, y) =>
        row.view.zipWithIndex.toList.map(_ match {
          case (gridElement, x) =>
            if (revealed.contains(x, y))
              gridElement
            else if (markedBombs.contains(x, y))
              MarkedBomb
            else
              Hidden
        })
    })

    Grid(revealedItems: _*)
  }

  def isValid = {
    //actually incomplete check but good enough
    //the only thing that could possibly go wrong
    //is having bombs with no numbers adjacent to them
    this.equals(Grid(Row(Bomb))) || bombs.forall(hasAnAdjacentNumber)
  }

  private def hasAnAdjacentNumber(bombPos: (Int, Int)) = bombPos match {
    case (x, y) => {
      Directions
        .adjacentFrom(x, y)
        .filter(isInsideGrid)
        .exists({ case (x, y) => rows(y)(x).isInstanceOf[Number] })
    }
  }

  private def reveal(x: Int, y: Int): Unit = {
    revealed.add((x, y))
  }

  private def revealAreaFrom(x: Int, y: Int): Unit = {
    reveal(x, y) // function is always called on an Empty first

    Directions
      .upDownLeftRight(x, y)
      .filter(isInsideGrid) // should be inside grid
      .filter(!revealed.contains(_)) // shouldnt have been revealed
      .foreach({ case (x, y) =>
      if (rows(y)(x) equals Empty)
        revealAreaFrom(x, y)
      else
        reveal(x, y)
    })
  }

  private def isInsideGrid(point: (Int, Int)) = point match {
    case (x, y) => x < width && x >= 0 && y < height && y >= 0
  }
}

object Grid {
  type Row = List[GridElement]

  def Row(gridElement: GridElement*): Row = gridElement.toList

  def apply(rows: Row*): Grid = {
    val bombs: Set[(Int, Int)] = {
      for {
        (row, x) <- rows.view.zipWithIndex
        (elem, y) <- row.view.zipWithIndex
        if (elem equals Bomb)
      } yield (x, y)
    }.toSet

    Grid(bombs, rows)
  }

  def apply(width: Int, height: Int): Grid = RandomGridGenerator(width, height).generate()
}