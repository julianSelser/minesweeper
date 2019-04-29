package minesweeper

import minesweeper.Grid.Row

import scala.annotation.tailrec
import scala.util.Random
import scala.math.min

case class RandomGridGenerator(width: Int, height: Int, nMines: Int) {

  @tailrec final def generate(maxAttempts: Int = 500, count: Int = 0): Grid = {
    val mines = generateMines
    val rows = generateRows(mines)
    val grid = Grid(mines, rows)

    if (grid.isValid)
      grid
    else if(count >= maxAttempts)
      throw new CantGenerateGridException(RandomGridGenerator(width, height))
    else
      generate(maxAttempts, count + 1)
  }

  private def generateRows(mines: Set[(Int, Int)]): List[Row] = {
    Range(0, height).toList.map(y => {
      Range(0, width).toList.map(x => {
        if (mines.contains(x, y))
          Mine
        else {
          val adjacentMines = Directions.adjacentFrom((x, y), mines)

          if (adjacentMines.length > 0)
            Number(adjacentMines.length)
          else
            Empty
        }
      })
    })
  }

  private def generateMines: Set[(Int, Int)] = {
    @tailrec def positionMine(mines: Set[(Int, Int)]): Set[(Int, Int)] = {
      val xMine = Random.nextInt(width)
      val yMine = Random.nextInt(height)

      if (!mines.contains(xMine, yMine))
        mines + ((xMine, yMine))
      else
        positionMine(mines)
    }

    val numMines = min(nMines, width*height) //in case of Int.MaxValue

    (1 to numMines).foldLeft(Set.empty[(Int, Int)])(
      (mines, _) => positionMine(mines))
  }
}

object RandomGridGenerator {
  def apply(width: Int, height: Int): RandomGridGenerator = {
    val nMines = ((width * height) / 3D).ceil.toInt

    RandomGridGenerator(width, height, nMines)
  }
}

case class CantGenerateGridException(saneGenerator: RandomGridGenerator) extends Throwable {
  def getSaneGrid = saneGenerator.generate()
}