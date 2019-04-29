package rest.routes

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.PathMatchers.IntNumber
import persistance.repositories.GameRepository.{create, getGamesByUserId}
import rest.auth.Authentication
import rest.dtos.{Game, NewGame}
import rest.misc.GridOnlyReaderFormatter
import spray.json.DefaultJsonProtocol


object GameRoutes extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val gridFormat = GridOnlyReaderFormatter
  implicit val gameFormat = jsonFormat2(Game)
  implicit val newGameFormat = jsonFormat3(NewGame)

  def routes = authenticateBasic(realm = "user games", Authentication.dbAuth) { implicit userId =>
    pathPrefix("games") {
      pathEnd{
        get {
          complete(getGamesByUserId(userId))
        } ~
        post {
          entity(as[NewGame]) { newGame =>
            complete(create(newGame))
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
