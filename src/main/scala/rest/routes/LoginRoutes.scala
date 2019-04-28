package rest.routes

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import persistance.repositories.UserRepository
import rest.dtos.User
import spray.json.DefaultJsonProtocol

import scala.util.Try

object LoginRoutes extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val clientFormat = jsonFormat2(User)

  def routes =
    path("signup") {
      post {
        entity(as[User]) { user =>
          val tryCreation = Try(UserRepository.save(user.username, user.password))

          if (tryCreation.isSuccess)
            complete(StatusCodes.Created)
          else
            complete(HttpResponse(StatusCodes.BadRequest, entity = "Try another username and password"))
        }
      }
    } ~ path("login") {
      post {
        entity(as[User]) { user =>
          val maybeUser = UserRepository.exists(user.username, user.password)

          if (maybeUser.isDefined)
            complete(HttpResponse(StatusCodes.OK))
          else
            complete(HttpResponse(StatusCodes.Unauthorized, entity = "Bad username or password"))
        }
      }
    }
}
