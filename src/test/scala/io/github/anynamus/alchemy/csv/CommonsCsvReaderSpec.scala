package io.github.anynamus.alchemy.csv

import io.github.anynamus.alchemy.domain.sql.model.RawRecord
import org.scalatest.funsuite.AnyFunSuite

class CommonsCsvReaderSpec extends AnyFunSuite:

  private val reader = new CommonsCsvReader()

  test("read CSV with headers and records"):
    val input =
      """name,email
        |Alice,alice@example.com
        |Bob,bob@example.com""".stripMargin

    val result = reader.read(input)

    assert(
      result == Right(
        CsvData(
          headers = Vector("name", "email"),
          records = Vector(
            RawRecord(Vector("Alice", "alice@example.com")),
            RawRecord(Vector("Bob", "bob@example.com"))
          )
        )
      )
    )

  test("read CSV containing headers only"):
    val input =
      """name,email""".stripMargin

    val result = reader.read(input)

    assert(
      result == Right(
        CsvData(
          headers = Vector("name", "email"),
          records = Vector.empty
        )
      )
    )

  test("reject empty CSV"):
    val input = ""

    val result = reader.read(input)

    assert(result.isLeft)

  test("reject CSV with duplicated headers"):
    val input =
      """name,email,name
        |Alice,alice@example.com,Alice""".stripMargin

    val result = reader.read(input)

    assert(result.isLeft)

  test("reject CSV with fewer values than headers"):
    val input =
      """name,email,age
        |Alice,alice@example.com""".stripMargin

    val result = reader.read(input)

    assert(result.isLeft)

  test("reject CSV with more values than headers"):
    val input =
      """name,email
        |Alice,alice@example.com,42""".stripMargin

    val result = reader.read(input)

    assert(result.isLeft)

  test("read CSV containing empty values"):
    val input =
      """name,email
        |Alice,
        |,bob@example.com""".stripMargin

    val result = reader.read(input)

    assert(
      result == Right(
        CsvData(
          headers = Vector("name", "email"),
          records = Vector(
            RawRecord(Vector("Alice", "")),
            RawRecord(Vector("", "bob@example.com"))
          )
        )
      )
    )

  test("read CSV containing quoted values"):
    val input =
      "name,description\n" +
        "Alice,\"Hello, world\"\n" +
        "Bob,\"He said \"\"hello\"\"\""

    val result = reader.read(input)

    assert(
      result == Right(
        CsvData(
          headers = Vector("name", "description"),
          records = Vector(
            RawRecord(Vector("Alice", "Hello, world")),
            RawRecord(Vector("Bob", "He said \"hello\""))
          )
        )
      )
    )

  test("read CSV containing multiline quoted values"):
    val input =
      """name,description
        |Alice,"Line one
        |Line two"
        |Bob,"Another value"""".stripMargin

    val result = reader.read(input)

    assert(
      result == Right(
        CsvData(
          headers = Vector("name", "description"),
          records = Vector(
            RawRecord(Vector("Alice", "Line one\nLine two")),
            RawRecord(Vector("Bob", "Another value"))
          )
        )
      )
    )

  test("reject malformed CSV"):
    val input =
      """name,email
        |Alice,"alice@example.com
        |Bob,bob@example.com""".stripMargin

    val result = reader.read(input)

    assert(result.isLeft)
