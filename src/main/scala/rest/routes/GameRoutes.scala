package rest.routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.PathMatchers.IntNumber
import rest.Authentication

object GameRoutes {
  def routes = authenticateBasic(realm = "user games", Authentication.dbAuth) { userId =>
    pathPrefix("games") {
      pathEnd{
        get {
          complete("[game1, game2, game3]")
        } ~
        post {
          complete("creates new game")
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
