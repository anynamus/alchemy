package io.github.anynamus.alchemy.domain.sql.model

import org.scalatest.funsuite.AnyFunSuite

class RawDataSpec extends AnyFunSuite:

  test("Raw data contains headers and records"):

    val data = RawData(
      table = "Customer",
      headers = Vector("name", "email"),
      records = Vector(
        RawRecord(Vector("Alice", "alice@example.com")),
        RawRecord(Vector("Bob", "bob@example.com"))
      )
    )

    assert(data.table == "Customer")
    assert(data.headers == Vector("name", "email"))
    assert(
      data.records == Vector(
        RawRecord(Vector("Alice", "alice@example.com")),
        RawRecord(Vector("Bob", "bob@example.com"))
      )
    )

  test("Raw data can contain no records"):

    val data = RawData(
      table = "Customer",
      headers = Vector("name", "email"),
      records = Vector.empty
    )

    assert(data.records.isEmpty)