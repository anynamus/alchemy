package io.github.anynamus.alchemy.domain.sql.enrichment

import io.github.anynamus.alchemy.core.Result
import io.github.anynamus.alchemy.core.Traverse.traverse
import io.github.anynamus.alchemy.csv.AutoNumberProvider
import io.github.anynamus.alchemy.domain.sql.data.{DataRecord, Dataset, TableData}
import io.github.anynamus.alchemy.domain.sql.model.ColumnType

class AutoNumberEnricher(
    provider: AutoNumberProvider
):

  def enrich(dataset: Dataset): Result[Dataset] =
    traverse(dataset.tables)(enrichTable)
      .map(Dataset.apply)

  private def enrichTable(
      tableData: TableData
  ): Result[TableData] =
    tableData.table.columns
      .find(_.`type` == ColumnType.AutoNumber) match
      case None =>
        Right(tableData)

      case Some(column) if tableData.records.isEmpty =>
        Right(tableData)

      case Some(column) =>
        provider
          .nextValue(tableData.table.name)
          .map { initialValue =>
            enrichRecords(
              tableData,
              column.name,
              initialValue
            )
          }

  private def enrichRecords(
      tableData: TableData,
      columnName: String,
      initialValue: Int
  ): TableData =
    val records =
      tableData.records.zipWithIndex.map {
        case (record, index) =>
          DataRecord(
            record.values.updated(
              columnName,
              (initialValue + index).toString
            )
          )
      }

    tableData.copy(records = records)
