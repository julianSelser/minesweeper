package minesweeper

import Grid.Row

import scala.collection.mutable
import scala.util.Random

case class RandomGridGenerator(width: Int, height: Int) {
  val nBombs = ((width*height)/3D).ceil.toInt
  private val bombs = new mutable.HashSet[(Int, Int)]()

  def generate: Grid = {
    if(width == 1 && height == 1)
      Grid(Row(Bomb))
    else doGenerate
  }

  private def doGenerate: Grid = {
    calculateBombPositions
//    placeBombs
    ???
  }

  private def calculateBombPositions = (1 to nBombs).foreach(_ => positionBomb)

  private def positionBomb: Unit = {
    val xBomb = Random.nextInt(width)
    val yBomb = Random.nextInt(height)

    if(!bombs.contains(xBomb, yBomb))
      bombs.add((xBomb, yBomb))
    else
      positionBomb
  }

}
