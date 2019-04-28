package minesweeper.grid

sealed trait GridElement

case object Bomb extends GridElement
case object Nothing extends GridElement
case class Number(value: Int) extends GridElement
