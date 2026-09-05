package io.github.anynamus.alchemy.domain.sql.validation

import io.github.anynamus.alchemy.domain.sql.model.{Schema, Table}
import org.scalatest.funsuite.AnyFunSuite

class SchemaValidatorSpec extends AnyFunSuite:

  private val validator = new SchemaValidator()

  test("BR-007 — A schema must contain at least one table"):

    val schema = Schema(Vector.empty)

    val result = validator.validate(schema)

    assert(result == Left(Vector("BR-007: schema must contain at least one table")))


  test("BR-008 — Table names must be unique within a schema"):

    val customer1 = Table("Customer", Vector.empty)
    val customer2 = Table("Customer", Vector.empty)

    val schema = Schema(Vector(customer1, customer2))

    val result = validator.validate(schema)

    assert(result == Left(Vector("BR-008: duplicated table names Customer")))

  test("BR-008 — all duplicated table names are reported"):

    val schema = Schema(
      Vector(
        Table("Customer", Vector.empty),
        Table("Order", Vector.empty),
        Table("Customer", Vector.empty),
        Table("Product", Vector.empty),
        Table("Order", Vector.empty)
      )
    )

    val result = validator.validate(schema)

    assert(
      result == Left(
        Vector("BR-008: duplicated table names Customer, Order")
      )
    )


  test("A valid schema passes validation"):

    val customer = Table("Customer", Vector.empty)
    val order = Table("Order", Vector.empty)

    val schema = Schema(Vector(customer, order))

    val result = validator.validate(schema)

    assert(result == Right(schema))
