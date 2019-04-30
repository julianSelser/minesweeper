package rest.routes

import akka.http.scaladsl.server.Directives._

object WebsiteRoutes {
  def routes =
    pathEndOrSingleSlash {
      pathEnd {
        getFromResource("index.html")
      }
    } ~
    pathPrefix("site") {
      getFromResourceDirectory("site")
    }
}
