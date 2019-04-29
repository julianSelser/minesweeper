package rest.routes

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.PathMatchers.IntNumber
import persistance.repositories.GameRepository.{create, getGameById, getUserGames, update}
import rest.auth.Authentication.dbAuth
import rest.dtos.{Game, NewGame, Spot}
import rest.misc.GridOnlyReaderFormatter
import spray.json.DefaultJsonProtocol


object GameRoutes extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val gridFormat = GridOnlyReaderFormatter
  implicit val gameFormat = jsonFormat2(Game)
  implicit val newGameFormat = jsonFormat3(NewGame)
  implicit val moveFormat = jsonFormat2(Spot)

  def routes = authenticateBasic(realm = "user games", dbAuth) { implicit userId =>
    pathPrefix("games") {
      pathEnd{
        get {
          complete(getUserGames)
        } ~
        post {
          entity(as[NewGame]) { newGame =>
            complete(create(newGame))
          }
        }
      } ~
      pathPrefix(IntNumber) { gameId =>
        val maybeGame = getGameById(gameId)

        validate(maybeGame.isDefined, s"Specified game doesn't exist") {
          val game = maybeGame.get

          pathEnd {
            get {
              complete(game)
            }
          } ~
          entity(as[Spot]) { spot =>
            validate(spot isInside game, s"x and y should be inside grid") {

              path("sweep") {
                put {
                  game sweep spot

                  update(game)

                  complete(game)
                }
              } ~
              path("mark") {
                put {
                  game mark spot

                  update(game)

                  complete(game)
                }
              }
            }
          }
        }
      }
    }
  }
}
