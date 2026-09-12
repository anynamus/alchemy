package io.github.anynamus.alchemy.domain.sql.enrichment

import io.github.anynamus.alchemy.core.Result
import io.github.anynamus.alchemy.csv.AutoNumberProvider
import io.github.anynamus.alchemy.domain.sql.data.{DataRecord, Dataset, TableData}
import io.github.anynamus.alchemy.domain.sql.model.{Column, ColumnType, Table}
import org.scalatest.funsuite.AnyFunSuite

class AutoNumberEnricherSpec extends AnyFunSuite:

  private val provider = new AutoNumberProvider:
    override def nextValue(table: String): Result[Int] =
      Right(100)

  test("generate auto numbers"):
    val table = Table(
      name = "Customer",
      columns = Vector(
        Column("id", ColumnType.AutoNumber),
        Column("name", ColumnType.String)
      )
    )

    val dataset = Dataset(
      Vector(
        TableData(
          table,
          Vector(
            DataRecord(Map("name" -> "Alice")),
            DataRecord(Map("name" -> "Bob")),
            DataRecord(Map("name" -> "Carol"))
          )
        )
      )
    )

    val enricher = AutoNumberEnricher(provider)

    val result = enricher.enrich(dataset)

    assert(
      result == Right(
        Dataset(
          Vector(
            TableData(
              table,
              Vector(
                DataRecord(Map("name" -> "Alice", "id" -> "100")),
                DataRecord(Map("name" -> "Bob", "id" -> "101")),
                DataRecord(Map("name" -> "Carol", "id" -> "102"))
              )
            )
          )
        )
      )
    )

  test("preserve data without autonumber"):
    val table = Table(
      name = "Customer",
      columns = Vector(
        Column("id", ColumnType.String),
        Column("name", ColumnType.String)
      )
    )

    val dataset = Dataset(
      Vector(
        TableData(
          table,
          Vector(
            DataRecord(Map("id" -> "200", "name" -> "Alice")),
            DataRecord(Map("id" -> "201", "name" -> "Bob")),
            DataRecord(Map("id" -> "202", "name" -> "Carol"))
          )
        )
      )
    )

    val enricher = AutoNumberEnricher(provider)

    val result = enricher.enrich(dataset)

    assert(
      result == Right(
        Dataset(
          Vector(
            TableData(
              table,
              Vector(
                DataRecord(Map("id" -> "200", "name" -> "Alice")),
                DataRecord(Map("id" -> "201", "name" -> "Bob")),
                DataRecord(Map("id" -> "202", "name" -> "Carol"))
              )
            )
          )
        )
      )
    )

  test("preserve empty table"):
    val table = Table(
      name = "Customer",
      columns = Vector(
        Column("id", ColumnType.AutoNumber),
        Column("name", ColumnType.String)
      )
    )

    val dataset = Dataset(Vector(TableData(table, Vector.empty)))

    val enricher = AutoNumberEnricher(provider)

    val result = enricher.enrich(dataset)

    assert(
      result == Right(Dataset(Vector(TableData(table, Vector.empty))))
    )
