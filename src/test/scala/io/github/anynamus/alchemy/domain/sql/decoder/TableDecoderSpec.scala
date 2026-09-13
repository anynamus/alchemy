package io.github.anynamus.alchemy.domain.sql.decoder

import io.github.anynamus.alchemy.domain.sql.model.{Column, ColumnType, Table}
import io.github.anynamus.alchemy.yaml.YamlNode
import org.scalatest.funsuite.AnyFunSuite

class TableDecoderSpec extends AnyFunSuite:

  private val decoder = new TableDecoder(new ColumnDecoder(new ConstraintDecoder()))

  test("A table node must be of type Mapping"):
    val node = YamlNode.Scalar("Customer")

    val result = decoder.decode(node)

    result match
      case Left(error) =>
        assert(error == "Expected a mapping for table definition")

      case Right(_) =>
        fail("expected failure")

  test("reads a table without table definition"):
    val node = YamlNode.Mapping(Map("dummy" -> YamlNode.Scalar("Customer")))

    val result = decoder.decode(node)

    result match
      case Left(error) =>
        assert(error == "Missing required field 'table'")

      case Right(_) =>
        fail("expected failure")

  test("A table name must be of type Scalar"):
    val node = YamlNode.Mapping(Map("table" -> YamlNode.Sequence(Vector())))

    val result = decoder.decode(node)

    result match
      case Left(error) =>
        assert(error == "Cannot map a Sequence node to Scalar")

      case Right(_) =>
        fail("expected failure")

  test("Reads a table with a single column"):
    val columns = YamlNode.Mapping(Map(
      "name" -> YamlNode.Scalar("id"),
      "type" -> YamlNode.Scalar("autonumber")
    ))
    val node    = YamlNode.Mapping(Map(
      "table"   -> YamlNode.Scalar("Customer"),
      "columns" -> YamlNode.Sequence(Vector(columns))
    ))

    val result = decoder.decode(node)

    result match
      case Right(table) =>
        assert(table == Table("Customer", Vector(Column("id", ColumnType.AutoNumber))))
      case _            =>
        fail("expected table named Customer")

  test("decode candidate key"):

    val node = YamlNode.Mapping(
      Map(
        "table"         -> YamlNode.Scalar("Customer"),
        "columns"       -> YamlNode.Sequence(Vector(
          YamlNode.Mapping(
            Map(
              "name" -> YamlNode.Scalar("email"),
              "type" -> YamlNode.Scalar("string")
            )
          )
        )),
        "candidate-key" -> YamlNode.Scalar("email")
      )
    )

    val result = decoder.decode(node)

    assert(
      result ==
        Right(
          Table(
            name = "Customer",
            columns = Vector(Column("email", ColumnType.String, Vector.empty)),
            candidateKey = Some("email")
          )
        )
    )

  test("decode table without candidate key"):

    val node = YamlNode.Mapping(
      Map(
        "table"   -> YamlNode.Scalar("Customer"),
        "columns" -> YamlNode.Sequence(Vector(
          YamlNode.Mapping(
            Map(
              "name" -> YamlNode.Scalar("email"),
              "type" -> YamlNode.Scalar("string")
            )
          )
        ))
      )
    )

    val result = decoder.decode(node)

    assert(
      result ==
        Right(
          Table(
            name = "Customer",
            columns = Vector(Column("email", ColumnType.String, Vector.empty)),
            candidateKey = None
          )
        )
    )

  test("decode table with invalid candidate key"):

    val node = YamlNode.Mapping(
      Map(
        "table"         -> YamlNode.Scalar("Customer"),
        "columns"       -> YamlNode.Sequence(Vector(
          YamlNode.Mapping(
            Map(
              "name" -> YamlNode.Scalar("email"),
              "type" -> YamlNode.Scalar("string")
            )
          )
        )),
        "candidate-key" -> YamlNode.Mapping(Map("name" -> YamlNode.Scalar("agaguk")))
      )
    )

    val result = decoder.decode(node)

    assert(
      result == Left("Field 'candidate-key' must be a scalar")
    )
