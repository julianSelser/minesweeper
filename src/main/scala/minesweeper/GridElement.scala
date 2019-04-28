package minesweeper

sealed trait GridElement

case object MarkedBomb extends GridElement
case object Hidden extends GridElement
case object Bomb extends GridElement
case object Empty extends GridElement
case class Number(value: Int) extends GridElement
