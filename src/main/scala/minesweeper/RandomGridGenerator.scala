package minesweeper

import minesweeper.Grid.Row

import scala.annotation.tailrec
import scala.util.Random
import scala.math.min

case class RandomGridGenerator(width: Int, height: Int, nBombs: Int) {

  @tailrec final def generate(maxAttempts: Int = 500, count: Int = 0): Grid = {
    val bombs = generateBombs
    val rows = generateGrid(bombs)
    val grid = Grid(bombs, rows: _*)

    if (grid.isValid)
      grid
    else if(count >= maxAttempts)
      throw new CantGenerateGridException(RandomGridGenerator(width, height))
    else
      generate(maxAttempts, count + 1)
  }

  private def getAdjacent(p: (Int, Int), bombs: Set[(Int, Int)]) = p match {
    case (x, y) =>
      val directions = List(
        (x, y - 1),
        (x, y + 1),
        (x + 1, y),
        (x - 1, y),
        (x + 1, y - 1),
        (x + 1, y + 1),
        (x - 1, y - 1),
        (x - 1, y + 1))

      directions.filter(bombs.contains)
  }

  private def generateGrid(bombs: Set[(Int, Int)]): List[Row] = {
    Range(0, height).toList.map(y => {
      Range(0, width).toList.map(x => {
        if (bombs.contains(x, y))
          Bomb
        else {
          val adjacentBombs: List[(Int, Int)] = getAdjacent((x, y), bombs)

          if (adjacentBombs.length > 0)
            Number(adjacentBombs.length)
          else
            Empty
        }
      })
    })
  }

  private def generateBombs: Set[(Int, Int)] = {
    @tailrec def positionBomb(bombs: Set[(Int, Int)]): Set[(Int, Int)] = {
      val xBomb = Random.nextInt(width)
      val yBomb = Random.nextInt(height)

      if (!bombs.contains(xBomb, yBomb))
        bombs + ((xBomb, yBomb))
      else
        positionBomb(bombs)
    }

    val numBombs = min(nBombs, width*height) //in case of Int.MaxValue

    (1 to numBombs).foldLeft(Set.empty[(Int, Int)])(
      (bombs, _) => positionBomb(bombs))
  }
}

object RandomGridGenerator {
  def apply(width: Int, height: Int): RandomGridGenerator = {
    val nBombs = ((width * height) / 3D).ceil.toInt

    RandomGridGenerator(width, height, nBombs)
  }
}

case class CantGenerateGridException(saneGenerator: RandomGridGenerator) extends Throwable {
  def getSaneGrid = saneGenerator.generate()
}