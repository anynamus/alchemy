package io.github.anynamus.alchemy.csv

import io.github.anynamus.alchemy.data.{RawData, RawRecord}
import io.github.anynamus.alchemy.domain.sql.data.{RawDataSource, RawDataset, RawTableData}
import org.scalatest.funsuite.AnyFunSuite

class CsvRawDataReaderSpec extends AnyFunSuite:

  private val csvReader = new CommonsCsvReader()
  private val reader    = new CsvRawDataReader(csvReader)

  test("read CSV data for a table"):
    val sources = Vector(
      RawDataSource(
        table = "Customer",
        input =
          """name,email
            |Alice,alice@example.com
            |Bob,bob@example.com""".stripMargin
      )
    )

    assert(
      reader.read(sources) ==
        Right(
          RawDataset(
            Vector(
              RawTableData(
                "Customer",
                RawData(
                  Vector("name", "email"),
                  Vector(
                    RawRecord(Vector("Alice", "alice@example.com")),
                    RawRecord(Vector("Bob", "bob@example.com"))
                  )
                )
              )
            )
          )
        )
    )

  test("read CSV data for multiple tables"):
    val sources = Vector(
      RawDataSource(
        table = "Customer",
        input =
          """name
            |Alice
            |Bob""".stripMargin
      ),
      RawDataSource(
        table = "Product",
        input =
          """name
            |Product A
            |Product B""".stripMargin
      )
    )

    assert(
      reader.read(sources) ==
        Right(
          RawDataset(
            Vector(
              RawTableData(
                "Customer",
                RawData(
                  Vector("name"),
                  Vector(
                    RawRecord(Vector("Alice")),
                    RawRecord(Vector("Bob"))
                  )
                )
              ),
              RawTableData(
                "Product",
                RawData(
                  Vector("name"),
                  Vector(
                    RawRecord(Vector("Product A")),
                    RawRecord(Vector("Product B"))
                  )
                )
              )
            )
          )
        )
    )

  test("preserve source order"):
    val sources = Vector(
      RawDataSource("Product", "name\nProduct A"),
      RawDataSource("Customer", "name\nAlice")
    )

    val result = reader.read(sources)

    assert(
      result.map(_.tables.map(_.table)) ==
        Right(Vector("Product", "Customer"))
    )

  test("propagate CSV reading errors"):
    val sources = Vector(
      RawDataSource(
        table = "Customer",
        input =
          """name,email
            |Alice,"alice@example.com
            |Bob,bob@example.com""".stripMargin
      )
    )

    assert(reader.read(sources).isLeft)

  test("reject duplicated table names"):
    val sources = Vector(
      RawDataSource("Customer", "name\nAlice"),
      RawDataSource("Customer", "name\nBob")
    )

    assert(
      reader.read(sources) ==
        Left("Duplicated table 'Customer'")
    )
