package io.github.anynamus.alchemy.yaml

import org.scalatest.funsuite.AnyFunSuite

class YamlNodeSpec extends AnyFunSuite:

  test("object exposes its fields"):

    val node =
      YamlNode.Mapping(
        Map(
          "table" -> YamlNode.Scalar("Customer")
        )
      )

    node match

      case YamlNode.Mapping(fields) =>

        assert(fields.contains("table"))

      case _ =>

        fail("expected an object")

  test("sequence preserves insertion order"):

    val node =
      YamlNode.Sequence(
        Vector(
          YamlNode.Scalar("a"),
          YamlNode.Scalar("b")
        )
      )

    node match

      case YamlNode.Sequence(values) =>

        assert(values.size == 2)

        assert(values.head == YamlNode.Scalar("a"))

      case _ =>

        fail("expected an array")
