package io.github.anynamus.alchemy.yaml

import org.scalatest.funsuite.AnyFunSuite

class SnakeYamlReaderSpec extends AnyFunSuite:

  private val reader = new SnakeYamlReader()

  test("reads nested YAML structures"):

    val yaml =
      """
        |table: Customer
        |
        |columns:
        |  - name: id
        |    type: autonumber
        |    constraints:
        |      - not null
        |      - reference: Order
        |""".stripMargin

    val node = reader.read(yaml)

    node match
      case Right(YamlNode.Mapping(fields)) =>
        assert(fields.size == 2)
        assert(fields.head._1 == "table")
        assert(fields.tail.head._1 == "columns")

      case Right(_) =>
        fail("expected a mapping")

      case Left(error) =>
        fail(error)

  test("reads empty string"):
    val reader = new SnakeYamlReader()

    val node = reader.read("")

    node match
      case Left(error) =>
        assert(error.nonEmpty)

      case Right(_) =>
        fail("expected an error")

  test("reads a scalar node"):
    val reader = new SnakeYamlReader()

    val node = reader.read("a, b, c")

    assert(node.isRight)

    node.getOrElse(null) match
      case YamlNode.Scalar(value) => assert(value == "a, b, c")
      case _                      => fail("unexpected node")

  test("mapping with non-scalar key returns error"):
    val yaml =
      """
        |[a, b]: value
        |""".stripMargin

    val node = reader.read(yaml)

    node match
      case Left(error) =>
        assert(error == "invalid YAML key: keys must be scalars")
      case Right(_)    =>
        fail("expected failure")
