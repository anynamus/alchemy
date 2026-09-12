package io.github.anynamus.alchemy.domain.sql.decoder

import io.github.anynamus.alchemy.domain.sql.model.Constraint.*
import io.github.anynamus.alchemy.domain.sql.model.{ColumnType, Constraint}
import io.github.anynamus.alchemy.yaml.YamlNode
import org.scalatest.funsuite.AnyFunSuite

class ColumnDecoderSpec extends AnyFunSuite:

  private val decoder = new ColumnDecoder(new ConstraintDecoder())

  test("A column node must be of type Mapping"):
    val node = YamlNode.Scalar("id")

    val result = decoder.decode(node)

    result match
      case Left(error) =>
        assert(error == "Expected a mapping for column definition")
      case Right(_) =>
        fail("error expected")

  test("reads a column without a name"):
    val node = YamlNode.Mapping(Map("dummy" -> YamlNode.Scalar("yup")))

    val result = decoder.decode(node)

    result match
      case Left(error) =>
        assert(error == "Missing required field 'name'")
      case Right(_) =>
        fail("error expected")

  test("reads a column name which is not a scalar"):
    val node = YamlNode.Mapping(Map("name" -> YamlNode.Sequence(Vector())))

    val result = decoder.decode(node)

    result match
      case Left(error) =>
        assert(error == "Cannot map a Sequence node to Scalar")
      case Right(_) =>
        fail("error expected")

  test("reads a column type which is not a scalar"):
    val node = YamlNode.Mapping(Map("name" -> YamlNode.Scalar("id"), "type" -> YamlNode.Sequence(Vector())))

    val result = decoder.decode(node)

    result match
      case Left(error) =>
        assert(error == "Cannot map a Sequence node to Scalar")
      case Right(_) =>
        fail("error expected")

  test("reads a column without a type"):
    val node = YamlNode.Mapping(Map("name" -> YamlNode.Scalar("id")))

    val result = decoder.decode(node)

    result match
      case Left(error) =>
        assert(error == "Missing required field 'type'")
      case Right(_) =>
        fail("error expected")

  test("reads a column with an invalid type"):
    val node = YamlNode.Mapping(Map("name" -> YamlNode.Scalar("id"), "type" -> YamlNode.Scalar("agaguk")))

    val result = decoder.decode(node)

    result match
      case Left(error) =>
        assert(error == "Invalid column type 'agaguk'")
      case Right(_) =>
        fail("error expected")

  test("reads a valid column definition without constraint"):
    val node = YamlNode.Mapping(Map("name" -> YamlNode.Scalar("id"), "type" -> YamlNode.Scalar("autonumber")))

    val result = decoder.decode(node)

    result match
      case Right(column) =>
        assert("id" == column.name)
        assert(ColumnType.AutoNumber == column.`type`)
        assert(column.constraints == Vector.empty)
      case Left(_) =>
        fail("expected column named 'id' with type 'AutoNumber'")

  test("reads a valid column definition with not null constraint"):
    val node = YamlNode.Mapping(Map(
      "name" -> YamlNode.Scalar("id"),
      "type" -> YamlNode.Scalar("autonumber"),
      "constraints" -> YamlNode.Sequence(Vector(YamlNode.Scalar("not null")))
    ))

    val result = decoder.decode(node)

    result match
      case Right(column) =>
        assert(column.constraints.size == 1)
        assert(column.constraints.head == NotNull)
      case Left(_) =>
        fail("expected column to have a 'not null' constraint")

  test("reads a valid column definition with not null and reference constraint"):
    val node = YamlNode.Mapping(Map(
      "name" -> YamlNode.Scalar("id"),
      "type" -> YamlNode.Scalar("autonumber"),
      "constraints" -> YamlNode.Sequence(
        Vector(
          YamlNode.Scalar("not null"),
          YamlNode.Mapping(Map("reference" -> YamlNode.Scalar("Order")))
        )
      )
    ))

    val result = decoder.decode(node)

    result match
      case Right(column) =>
        assert(column.constraints.size == 2)
        assert(column.constraints == Vector(NotNull, Reference("Order")))
      case Left(_) =>
        fail("expected column to have a 'reference' constraint")

