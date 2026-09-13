package io.github.anynamus.alchemy.domain.sql.decoder

import io.github.anynamus.alchemy.domain.sql.model.Constraint.{NotNull, Reference}
import io.github.anynamus.alchemy.yaml.YamlNode
import org.scalatest.funsuite.AnyFunSuite

class ConstraintDecoderSpec extends AnyFunSuite:

  private val decoder = new ConstraintDecoder()

  test("reads a node which is a sequence"):
    val node = YamlNode.Sequence(Vector.empty)

    val result = decoder.decode(node)

    assert(result == Left("Expected a mapping or a scalar for constraint definition"))

  test("reads unknown scalar value"):
    val node = YamlNode.Scalar("unknown")

    val result = decoder.decode(node)

    assert(result == Left("Unknown constraint 'unknown'"))

  test("reads a 'not null' constraint"):
    val node = YamlNode.Scalar("not null")

    val result = decoder.decode(node)

    assert(result == Right(NotNull))

  test("reads reference constraint"):
    val node = YamlNode.Mapping(Map("reference" -> YamlNode.Scalar("Order")))

    val result = decoder.decode(node)

    assert(result == Right(Reference("Order")))

  test("reads mapping node without reference"):
    val node = YamlNode.Mapping(Map("nothing" -> YamlNode.Scalar("something")))

    val result = decoder.decode(node)

    assert(result == Left("Unknown constraint field(s): 'nothing'"))

  test("reads reference constraint with an invalid constaint"):
    val node = YamlNode.Mapping(
      Map(
        "reference" -> YamlNode.Scalar("Order"),
        "dummy"     -> YamlNode.Scalar("x")
      )
    )

    val result = decoder.decode(node)

    assert(result == Left("Unknown constraint field(s): 'dummy'"))
