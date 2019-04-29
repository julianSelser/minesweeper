package rest.routes

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.PathMatchers.IntNumber
import minesweeper.RandomGridGenerator
import rest.auth.Authentication
import rest.dtos.{Game, NewGame}
import rest.misc.GridOnlyReaderFormatter
import spray.json.DefaultJsonProtocol


object GameRoutes extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val gridFormat = GridOnlyReaderFormatter
  implicit val gameFormat = jsonFormat3(Game)
  implicit val newGameFormat = jsonFormat3(NewGame)

  def routes = authenticateBasic(realm = "user games", Authentication.dbAuth) { userId =>
    pathPrefix("games") {
      pathEnd{
        get {
          complete("[game1, game2, game3]")
        } ~
        post {
          entity(as[NewGame]) { newGame =>
            complete(RandomGridGenerator(newGame.width, newGame.height, newGame.mines).generate())
          }
        }
      } ~
      pathPrefix(IntNumber) { id =>
        pathEnd {
          get {
            complete(s"gets game of id: ${id}")
          }
        } ~
        path("sweep") {
          put {
            complete(s"sweeps ${id}")
          }
        } ~
        path("mark") {
          put {
            complete(s"marks $id")
          }
        }
      }
    }
  }
}
