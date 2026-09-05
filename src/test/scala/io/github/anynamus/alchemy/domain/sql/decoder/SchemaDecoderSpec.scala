package io.github.anynamus.alchemy.domain.sql.decoder

import io.github.anynamus.alchemy.core.Decoder
import io.github.anynamus.alchemy.domain.sql.model.{Schema, Table}
import io.github.anynamus.alchemy.yaml.YamlNode
import org.scalatest.funsuite.AnyFunSuite

class SchemaDecoderSpec extends AnyFunSuite:

  test("decode a schema containing multiple tables"):

    val node = YamlNode.Sequence(
      Vector(
        YamlNode.Mapping(
          Map(
            "table" -> YamlNode.Scalar("Customer"),
            "columns" -> YamlNode.Sequence(Vector.empty)
          )
        ),
        YamlNode.Mapping(
          Map(
            "table" -> YamlNode.Scalar("Order"),
            "columns" -> YamlNode.Sequence(Vector.empty)
          )
        )
      )
    )

    val decoder = buildSchemaDecoder()

    val result = decoder.decode(node)

    assert(
      result == Right(
        Schema(
          Vector(
            Table("Customer", Vector.empty),
            Table("Order", Vector.empty)
          )
        )
      )
    )

  test("fail when schema definition is not a sequence"):

    val decoder = buildSchemaDecoder()

    val result =
      decoder.decode(YamlNode.Scalar("invalid"))

    assert(result == Left("Cannot map a Scalar node to Sequence"))

  test("fail when a table cannot be decoded"):

    val validNode = YamlNode.Scalar("valid")
    val invalidNode = YamlNode.Scalar("invalid")

    val decoder = buildSchemaDecoder()

    val result =
      decoder.decode(
        YamlNode.Sequence(Vector(validNode, invalidNode))
      )

    assert(result == Left("Expected a mapping for table definition"))


  private def buildSchemaDecoder(): Decoder[YamlNode, Schema] =
    val constraintDecoder = new ConstraintDecoder()
    val columnDecoder = new ColumnDecoder(constraintDecoder)
    val tableDecoder = new TableDecoder(columnDecoder)
    new SchemaDecoder(tableDecoder)
