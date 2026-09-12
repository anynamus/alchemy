package io.github.anynamus.alchemy.domain.sql.data

import io.github.anynamus.alchemy.data.{RawData, RawRecord}
import io.github.anynamus.alchemy.domain.sql.model.{Column, ColumnType, Table}
import org.scalatest.funsuite.AnyFunSuite

class RawDataConverterSpec extends AnyFunSuite:

  private val converter = new RawDataConverter()

  test("convert raw table data to table data"):
    val table = Table(
      name = "Customer",
      columns = Vector(
        Column("id", ColumnType.AutoNumber),
        Column("name", ColumnType.String),
        Column("email", ColumnType.String)
      )
    )

    val rawData = RawTableData(
      table = "Customer",
      data = RawData(
        headers = Vector("id", "name", "email"),
        records = Vector(
          RawRecord(Vector("1", "Alice", "alice@example.com")),
          RawRecord(Vector("2", "Bob", "bob@example.com"))
        )
      )
    )

    val result = converter.convert(table, rawData)

    assert(
      result == Right(
        TableData(
          table,
          Vector(
            DataRecord(
              Map(
                "id"    -> "1",
                "name"  -> "Alice",
                "email" -> "alice@example.com"
              )
            ),
            DataRecord(
              Map(
                "id"    -> "2",
                "name"  -> "Bob",
                "email" -> "bob@example.com"
              )
            )
          )
        )
      )
    )
