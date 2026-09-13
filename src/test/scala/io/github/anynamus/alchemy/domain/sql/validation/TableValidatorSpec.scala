package io.github.anynamus.alchemy.domain.sql.validation

import io.github.anynamus.alchemy.domain.sql.model.{Column, ColumnType, Table}
import org.scalatest.funsuite.AnyFunSuite

class TableValidatorSpec extends AnyFunSuite:
  private val validator = new TableValidator()

  test("BR-001 — A table must contain at least one column"):
    val table = Table("Customer", Vector.empty)

    val result = validator.validate(table)

    assert(result == Left(Vector("BR-001")))

  test("BR-002 — Column names must be unique"):
    val table = Table(
      "Customer",
      Vector(Column("id", ColumnType.AutoNumber), Column("id", ColumnType.String))
    )

    val result = validator.validate(table)

    assert(result == Left(Vector("BR-002")))

  test("BR-003 — A table may contain only one AutoNumber column"):
    val table = Table(
      "Customer",
      Vector(Column("id", ColumnType.AutoNumber), Column("name", ColumnType.AutoNumber))
    )

    val result = validator.validate(table)

    assert(result == Left(Vector("BR-003")))

  test("BR-002 & BR-003 Violated"):
    val table = Table(
      "Customer",
      Vector(Column("id", ColumnType.AutoNumber), Column("id", ColumnType.AutoNumber))
    )

    val result = validator.validate(table)

    assert(result == Left(Vector("BR-002", "BR-003")))

  test("A valid table has no error"):
    val table = Table("Customer", Vector(Column("id", ColumnType.AutoNumber)))

    val result = validator.validate(table)

    assert(result == Right(table))
