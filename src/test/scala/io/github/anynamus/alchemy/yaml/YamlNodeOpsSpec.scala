package io.github.anynamus.alchemy.yaml

import io.github.anynamus.alchemy.yaml.YamlNodeOps.*
import org.scalatest.funsuite.AnyFunSuite

class YamlNodeOpsSpec extends AnyFunSuite:

  test("using asScalar on a Sequence node"):
    YamlNode.Sequence(Vector()).asScalar match
      case Left(error) => assert(error == "Cannot map a Sequence node to Scalar")
      case Right(_) => fail("error expected")

  test("using asScalar on a Mapping node"):
    YamlNode.Mapping(Map.empty[String, YamlNode]).asScalar match
      case Left(error) => assert(error == "Cannot map a Mapping node to Scalar")
      case Right(_) => fail("error expected")

  test("using asScalar on a Scalar node"):
    YamlNode.Scalar("aScalarValue").asScalar match
      case Right(value) => assert(value == "aScalarValue")
      case Left(_) => fail("unexpected error")

  test("using asSequence on a Scalar node"):
    YamlNode.Scalar("a scalar value").asSequence match
      case Left(error) => assert(error == "Cannot map a Scalar node to Sequence")
      case Right(_) => fail("error expected")

  test("using asSequence on a Mapping node"):
    YamlNode.Mapping(Map.empty[String, YamlNode]).asSequence match
      case Left(error) => assert(error == "Cannot map a Mapping node to Sequence")
      case Right(_) => fail("error expected")

  test("using asSequence on a Sequence node"):
    YamlNode.Sequence(Vector(YamlNode.Scalar("first value"))).asSequence match
      case Right(value) =>
        assert(value.size == 1)
        assert(value.head == YamlNode.Scalar("first value"))
      case Left(_) => fail("unexpected error")
