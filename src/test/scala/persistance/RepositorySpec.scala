package persistance

import org.scalatest.{FlatSpec, Matchers}

class RepositorySpec extends FlatSpec with Matchers {
  "UserRepository" should "be able to save a user" in {
    MinesweeperDB.init

    UserRepository.save("admin", "admin")

    UserRepository.exists("admin", "admin").get shouldBe 1
  }
}
