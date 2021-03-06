package minesweeper

import minesweeper.Grid.Row

import scala.collection.mutable

case class Grid(
                 mines: Set[(Int, Int)],
                 rows: Seq[Row],
                 var state: GameState = OnGoing,
                 revealed: mutable.HashSet[(Int, Int)],
                 markedMines: mutable.HashSet[(Int, Int)]
               )(val started: Long /*in here to avoid being used for equals*/) {

  val width = rows(0).length
  val height = rows.length

  def markMineIn(point: (Int, Int)): Unit = {
    if(!state.equals(OnGoing)) return

    if(!markedMines.contains(point))
      markedMines.add(point)
    else
      markedMines.remove(point)

    if (mines.equals(markedMines)) state = Won
  }

  def sweep(x: Int, y: Int): Unit = {
    //if the point was marked as a mine
    //by the user we never sweep it
    if (markedMines.contains((x, y)) || !state.equals(OnGoing)) return

    rows(y)(x) match {
      case Number(_) => reveal(x, y)
      case Mine => reveal(x, y); state = Lost
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
            else if (markedMines.contains(x, y))
              MarkedMine
            else
              Hidden
        })
    })

    Grid(revealedItems: _*)
  }

  def isValid = {
    //actually incomplete check but good enough
    //the only thing that could possibly go wrong
    //is having mines with no numbers adjacent to them
    this.equals(Grid(Row(Mine))) || mines.forall(hasAnAdjacentNumber)
  }

  def isInsideGrid(point: (Int, Int)) = point match {
    case (x, y) => x < width && x >= 0 && y < height && y >= 0
  }

  private def hasAnAdjacentNumber(minePos: (Int, Int)) = minePos match {
    case (x, y) => {
      Directions
        .adjacentFrom(x, y)
        .filter(isInsideGrid)
        .exists({ case (x, y) => rows(y)(x).isInstanceOf[Number] })
    }
  }

  private def reveal(point: (Int, Int)): Unit = {
    if(!markedMines.contains(point)) revealed.add(point)
  }

  private def revealAreaFrom(x: Int, y: Int): Unit = {
    reveal(x, y) // function is always called on an Empty first

    Directions
      .adjacentFrom(x, y)
      .filter(isInsideGrid) // should be inside grid
      .filter(!revealed.contains(_)) // shouldnt have been revealed
      .foreach({ case (x, y) =>
        if (rows(y)(x) equals Empty)
          revealAreaFrom(x, y)
        else
          reveal(x, y)
    })
  }
}

object Grid {
  type Row = List[GridElement]

  def Row(gridElement: GridElement*): Row = gridElement.toList

  def apply(mines: Set[(Int, Int)], rows: Seq[Row]): Grid = {
    Grid(
      mines, rows, OnGoing, new mutable.HashSet[(Int, Int)], new mutable.HashSet[(Int, Int)]
    )(System.currentTimeMillis())
  }

  def apply(rows: Row*): Grid = {
    val mines: Set[(Int, Int)] = {
      for {
        (row, x) <- rows.view.zipWithIndex
        (elem, y) <- row.view.zipWithIndex
        if (elem equals Mine)
      } yield (x, y)
    }.toSet

    Grid(mines, rows)
  }

  def apply(
    mines: Set[(Int, Int)],
    rows: Seq[Row],
    started: Long,
    state: GameState,
    revealed: mutable.HashSet[(Int, Int)],
    markedMines: mutable.HashSet[(Int, Int)]
  ): Grid = {
    Grid(mines, rows, state, revealed, markedMines)(started)
  }

  def apply(width: Int, height: Int): Grid = RandomGridGenerator(width, height).generate()
}