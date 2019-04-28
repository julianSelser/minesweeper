package minesweeper

import org.scalatest.{FlatSpec, Matchers}
import Grid.Row

import scala.util.Try

class RandomGridGeneratorSpec extends FlatSpec with Matchers {
  "RandomGridGenerator" should "always generate a grid with a single bomb for 1x1" in {
    RandomGridGenerator(1, 1).generate() shouldBe Grid(Row(Bomb))
  }

  it should "throw when you want more bombs than the cells available (except 1x1 because I like it!)" in {
    a [CantGenerateGridException] should be thrownBy {
      RandomGridGenerator(3, 3, Int.MaxValue).generate()
    }
  }

  it should "throw when you want bombs equal the cells available (except 1x1 because I like it!)" in {
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
      Row(Bomb,     Bomb,       Bomb),
      Row(Bomb,     Number(8),  Bomb),
      Row(Bomb,     Bomb,       Bomb)
    )

    RandomGridGenerator(3, 3, 8).generate() shouldBe validGame
  }

  it should "fill grid with n° of bombs ceiling((width*height)/3) if n° bombs not specified" in {
    val g1x1 = RandomGridGenerator(1, 1)
    val g2x1 = RandomGridGenerator(2, 1)
    val g3x1 = RandomGridGenerator(3, 1)
    val g4x1 = RandomGridGenerator(4, 1)
    val g2x2 = RandomGridGenerator(2, 2)
    val g3x3 = RandomGridGenerator(3, 3)
    val g4x4 = RandomGridGenerator(4, 4)

    g1x1.nBombs shouldBe 1
    g2x1.nBombs shouldBe 1
    g3x1.nBombs shouldBe 1
    g4x1.nBombs shouldBe 2
    g2x2.nBombs shouldBe 2
    g3x3.nBombs shouldBe 3
    g4x4.nBombs shouldBe 6
  }
}
