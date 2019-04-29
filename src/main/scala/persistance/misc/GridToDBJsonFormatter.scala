package persistance.misc

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import minesweeper._
import spray.json.{DefaultJsonProtocol, JsArray, JsNumber, JsObject, JsString, JsValue, RootJsonFormat}

import scala.collection.mutable


object GridToDBJsonFormatter extends RootJsonFormat[Grid] with DefaultJsonProtocol with SprayJsonSupport {

  implicit val tupleFormat = tuple2Format[Int, Int]

  override def write(grid: Grid): JsValue = {
    val jsonGrid = grid.rows.map(row => {
      val stringifiedElms = row.map(el => JsString(el.toString))

      JsArray(stringifiedElms.toVector)
    })

    JsObject(
      "mines" -> JsArray(grid.mines.toVector.map(tupleFormat.write(_))),
      "revealed" -> JsArray(grid.revealed.toVector.map(tupleFormat.write(_))),
      "marked" -> JsArray(grid.markedMines.toVector.map(tupleFormat.write(_))),
      "grid" -> JsArray(jsonGrid.toVector),
      "started" -> JsNumber(grid.started),
      "state" -> JsString(grid.state.toString),
    )
  }

  override def read(jv: JsValue): Grid = {
    //boring and ugly deserializing that could probably be better
    val mines = fromField[Set[(Int, Int)]](jv, "mines")
    val started = fromField[Long](jv, "started")

    val state = fromField[String](jv, "state") match {
      case "OnGoing" => OnGoing
      case "Won" => Won
      case "Lost" => Lost
    }

    val savedRevealed = fromField[Set[(Int, Int)]](jv, "revealed")
    val revealed = savedRevealed.foldLeft(new mutable.HashSet[(Int, Int)])((set, point) => {
      set.add(point); set
    })
    val savedMaked = fromField[Set[(Int, Int)]](jv, "marked")
    val marked = savedMaked.foldLeft(new mutable.HashSet[(Int, Int)])((set, point) => {
      set.add(point); set
    })

    val grid = fromField[List[List[String]]](jv, "grid")
    val rows = grid.map(rows =>
      rows.map(_.toString match {
        case "Empty" => Empty
        case "Hidden" => Hidden
        case "Mine" => Mine
        case "MarkedMine" => MarkedMine
        case itsANumber => Number(itsANumber.toInt)
      }))

    Grid(mines, rows, started, state, revealed, marked)
  }
}
