package io.github.anynamus.alchemy.domain.sql.data

import io.github.anynamus.alchemy.core.Result
import io.github.anynamus.alchemy.data.RawRecord
import io.github.anynamus.alchemy.domain.sql.model.Table

class RawDataConverter:

  def convert(
      table: Table,
      data: RawTableData
  ): Result[TableData] =
    Right(
      TableData(
        table = table,
        records = data.data.records.map(record =>
          convertRecord(data.data.headers, record)
        )
      )
    )

  private def convertRecord(
      headers: Vector[String],
      record: RawRecord
  ): DataRecord =
    DataRecord(
      headers.zip(record.values).toMap
    )
