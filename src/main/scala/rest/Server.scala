package rest

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import persistance.repositories.MinesweeperDB
import rest.routes.LoginRoutes

import scala.concurrent.ExecutionContext

object Server extends App {
  implicit val system: ActorSystem = ActorSystem("minesweeper")
  implicit val executor: ExecutionContext = system.dispatcher
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  MinesweeperDB.init

  def route = LoginRoutes.routes

  Http().bindAndHandle(route, "0.0.0.0", 9000)
  println("Started server en port 9000")
}
