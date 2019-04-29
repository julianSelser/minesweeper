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
      "bombs" -> JsArray(grid.bombs.toVector.map(tupleFormat.write(_))),
      "revealed" -> JsArray(grid.revealed.toVector.map(tupleFormat.write(_))),
      "grid" -> JsArray(jsonGrid.toVector),
      "started" -> JsNumber(grid.started)
    )
  }

  override def read(jv: JsValue): Grid = {
    val bombs = fromField[Set[(Int, Int)]](jv, "bombs")
    val started = fromField[Long](jv, "started")
    val savedRevealed = fromField[Set[(Int, Int)]](jv, "revealed")
    val revealed = savedRevealed.foldLeft(new mutable.HashSet[(Int, Int)])((set, point) => {
      set.add(point); set
    })

    val grid = fromField[List[List[String]]](jv, "grid")
    val rows = grid.map(rows =>
      rows.map(_.toString match {
        case "Empty" => Empty
        case "Hidden" => Hidden
        case "Bomb" => Bomb
        case "MarkedBomb" => MarkedBomb
        case itsANumber => Number(itsANumber.toInt)
      }
      ))

    Grid(bombs, rows, revealed, started)
  }
}
