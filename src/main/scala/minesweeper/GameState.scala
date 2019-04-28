package minesweeper

sealed trait GameState

case object Won extends GameState
case object Lost extends GameState
case object OnGoing extends GameState
