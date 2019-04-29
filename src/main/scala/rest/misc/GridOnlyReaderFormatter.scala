package rest.misc

import minesweeper.Grid
import spray.json.{JsArray, JsNumber, JsObject, JsString, JsValue, RootJsonFormat}

object GridOnlyReaderFormatter extends RootJsonFormat[Grid] {
  override def write(grid: Grid): JsValue = {
    val jsonGrid = grid.getRevealed.rows.map(row => {
      val stringifiedElms = row.map(el => JsString(el.toString))

      JsArray(stringifiedElms.toVector)
    })

    JsObject(
      "width" -> JsNumber(grid.width),
      "height" -> JsNumber(grid.height),
      "mines" -> JsNumber(grid.mines.size),
      "status" -> JsString(grid.state.toString),
      "started" -> JsNumber(grid.started),
      "grid" -> JsArray(jsonGrid.toVector)
    )
  }

  override def read(json: JsValue): Grid = throw new NotImplementedError() //on purpose, will never read
}
