package io.github.anynamus.alchemy.domain.sql.model

import org.scalatest.funsuite.AnyFunSuite

class SchemaSpec extends AnyFunSuite:

  test("A schema can contain multiple tables"):

    val customer = Table(
      name = "Customer",
      columns = Vector.empty
    )

    val order = Table(
      name = "Order",
      columns = Vector.empty
    )

    val schema = Schema(Vector(customer, order))

    assert(schema.tables == Vector(customer, order))

  test("A schema can contain no tables"):

    val schema = Schema(Vector.empty)

    assert(schema.tables.isEmpty)
