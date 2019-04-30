package rest

import akka.http.scaladsl.server.HttpApp
import persistance.repositories.MinesweeperDB
import rest.misc.CORSHandler
import rest.routes.{GameRoutes, LoginRoutes, WebsiteRoutes}

object Server extends HttpApp with CORSHandler{
  MinesweeperDB.init

  def main(args: Array[String]) = startServer("0.0.0.0", 9000)

  protected override def routes =
    preflightRequestHandler ~ WebsiteRoutes.routes ~ LoginRoutes.routes ~ GameRoutes.routes
}


