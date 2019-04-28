package rest

import akka.http.scaladsl.server.directives.Credentials
import persistance.repositories.UserRepository

object Authentication {
  def dbAuth(credentials: Credentials): Option[Long] = {
    credentials match {
      case passwd @ Credentials.Provided(username) => {
        UserRepository.passwordFor(username).flatMap(_ match {
          case (id, pass) if passwd.verify(pass) => Some(id)
          case _ => None
        })
      }
      case _ => None
    }
  }
}
