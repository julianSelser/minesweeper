package minesweeper

import org.scalatest.{FlatSpec, Matchers}
import Grid.Row

import scala.util.Try

class RandomGridGeneratorSpec extends FlatSpec with Matchers {
  "RandomGridGenerator" should "always generate a grid with a single mine for 1x1" in {
    RandomGridGenerator(1, 1).generate() shouldBe Grid(Row(Mine))
  }

  it should "throw when you want more mines than the cells available (except 1x1 because I like it!)" in {
    a [CantGenerateGridException] should be thrownBy {
      RandomGridGenerator(3, 3, Int.MaxValue).generate()
    }
  }

  it should "throw when you want mines equal the cells available (except 1x1 because I like it!)" in {
    a [CantGenerateGridException] should be thrownBy {
      RandomGridGenerator(3, 3, 9).generate()
    }
  }

  it should "be possible to catch a the fact that we couldnt generate a grid and provide a sane one" in {
    val obviouslyFailedAttempt = Try(RandomGridGenerator(3, 3, Int.MaxValue).generate())
    val ex = obviouslyFailedAttempt.toEither.left get

    obviouslyFailedAttempt.isFailure shouldBe true

    ex match {
      case ex: CantGenerateGridException => ex.getSaneGrid.isValid shouldBe true
      case _ => fail("Grid generation should have failed with a CantGenerateGridException...")
    }
  }

  it should "generate a 3x3 even when n° specified is 8 (out of nine)" in {
    val validGame = Grid(
      Row(Mine,     Mine,       Mine),
      Row(Mine,     Number(8),  Mine),
      Row(Mine,     Mine,       Mine)
    )

    RandomGridGenerator(3, 3, 8).generate() shouldBe validGame
  }

  it should "fill grid with n° of mines ceiling((width*height)/3) if n° mines not specified" in {
    val g1x1 = RandomGridGenerator(1, 1)
    val g2x1 = RandomGridGenerator(2, 1)
    val g3x1 = RandomGridGenerator(3, 1)
    val g4x1 = RandomGridGenerator(4, 1)
    val g2x2 = RandomGridGenerator(2, 2)
    val g3x3 = RandomGridGenerator(3, 3)
    val g4x4 = RandomGridGenerator(4, 4)

    g1x1.nMines shouldBe 1
    g2x1.nMines shouldBe 1
    g3x1.nMines shouldBe 1
    g4x1.nMines shouldBe 2
    g2x2.nMines shouldBe 2
    g3x3.nMines shouldBe 3
    g4x4.nMines shouldBe 6
  }
}
