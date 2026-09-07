package io.github.anynamus.alchemy.csv

import io.github.anynamus.alchemy.core.Result
import io.github.anynamus.alchemy.core.Collections.duplicates
import io.github.anynamus.alchemy.core.Traverse.traverse
import io.github.anynamus.alchemy.domain.sql.model.{RawData, RawDataSource, RawDataset}

class CsvRawDataReader(
                        csvReader: CsvReader
                      ) extends RawDataReader:

  override def read(
                     sources: Vector[RawDataSource]
                   ): Result[RawDataset] =
    for
      _ <- validateTableNames(sources)
      tables <- traverse(sources)(readTable)
    yield RawDataset(tables)

  private def readTable(source: RawDataSource): Result[RawData] =
    csvReader
      .read(source.input)
      .map(data =>
        RawData(
          table = source.table,
          headers = data.headers,
          records = data.records
        )
      )


  private def validateTableNames(
                                  sources: Vector[RawDataSource]
                                ): Result[Unit] =
    val duplicatedTables =
      duplicates(sources.map(_.table))

    if duplicatedTables.isEmpty then
      Right(())
    else
      Left(s"Duplicated table '${duplicatedTables.keys.head}'")
