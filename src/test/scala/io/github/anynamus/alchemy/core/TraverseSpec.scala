package io.github.anynamus.alchemy.core

import io.github.anynamus.alchemy.core.Traverse.traverse
import org.scalatest.funsuite.AnyFunSuite

class TraverseSpec extends AnyFunSuite:

  test("calls traverse with an empty vector"):
    val result = traverse(Vector.empty)((x: String) => Right(x))

    result match
      case Right(vector) => assert(Vector.empty == vector)
      case Left(_) => fail("unexpected error")

  test("calls traverse without error"):
    val result = traverse(Vector(1, 2, 3))((x: Int) => Right(x + 1))

    result match
      case Right(vector) => assert(Vector(2, 3, 4) == vector)
      case Left(_) => fail("unexpected error")

  test("calls traverse with failure on first element"):
    val result = traverse(Vector(1, 2, 3))(failOn(1))

    result match
      case Left(error) => assert(error == "1")
      case Right(_) => fail("unexpected error")

  test("calls traverse with failure on second element"):
    val result = traverse(Vector(1, 2, 3))(failOn(2))

    result match
      case Left(error) => assert(error == "2")
      case Right(_) => fail("unexpected error")

  test("calls traverse with failure on last element"):
    val result = traverse(Vector(1, 2, 3))(failOn(3))

    result match
      case Left(error) => assert(error == "3")
      case Right(_) => fail("unexpected error")

  private def failOn(value: Int)(x: Int): Result[Int] =
    if x == value then Left(x.toString)
    else Right(x)
