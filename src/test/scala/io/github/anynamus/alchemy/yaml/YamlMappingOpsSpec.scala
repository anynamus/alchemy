package io.github.anynamus.alchemy.yaml

import io.github.anynamus.alchemy.yaml.YamlMappingOps.*
import org.scalatest.funsuite.AnyFunSuite

class YamlMappingOpsSpec extends AnyFunSuite:

  test("calling required without value"):
    Map.empty[String, YamlNode].required("table") match
      case Left(error) => assert(error == "Missing required field 'table'")
      case _ => fail("error expected")

  test("calling required with value"):
    Map("table" -> YamlNode.Scalar("Customer")).required("table") match
      case Right(value) => assert(value == YamlNode.Scalar("Customer"))
      case _ => fail("value expected")
