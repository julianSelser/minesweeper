package rest

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import persistance.repositories.MinesweeperDB
import rest.routes.{GameRoutes, LoginRoutes}

import scala.concurrent.ExecutionContext

object Server extends App {
  implicit val system: ActorSystem = ActorSystem("minesweeper")
  implicit val executor: ExecutionContext = system.dispatcher
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  MinesweeperDB.init

  def routes = LoginRoutes.routes ~ GameRoutes.routes

  Http().bindAndHandle(routes, "0.0.0.0", 9000)
  println("Started server en port 9000")
}
