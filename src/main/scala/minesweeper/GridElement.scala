package minesweeper

sealed trait GridElement

case object MarkedMine extends GridElement
case object Hidden extends GridElement
case object Mine extends GridElement
case object Empty extends GridElement
case class Number(value: Int) extends GridElement {
  override def toString: String = value.toString
}
