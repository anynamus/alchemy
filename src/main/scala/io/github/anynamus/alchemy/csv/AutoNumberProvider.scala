package io.github.anynamus.alchemy.csv

import io.github.anynamus.alchemy.core.Result
import io.github.anynamus.alchemy.domain.sql.model.{RawData, RawRecord}

trait AutoNumberProvider:
  def nextValue(table: String): Result[Int]

object CsvAutoNumberProvider:

  def from(
            csvReader: CsvReader,
            input: String
          ): Result[AutoNumberProvider] =
    for
      data <- csvReader.read(input)
      values <- buildValues(data)
    yield CsvProvider(values)

  private def buildValues(
                           data: RawData
                         ): Result[Map[String, Int]] =
    data.records.foldLeft[Result[Map[String, Int]]](Right(Map.empty)) {
      case (result, record) =>
        for
          values <- result
          (table, nextValue) <- extractValues(record)
          _ <- ensureNotDuplicated(values, table)
        yield values.updated(table, nextValue)
    }

  private def extractTable(
                            record: RawRecord
                          ): Result[String] =
    record.values.headOption match
      case Some(table) if table.nonEmpty =>
        Right(table)
      case _ =>
        Left("Table name must not be empty")

  private def extractNextValue(
                                record: RawRecord,
                                table: String
                              ): Result[Int] =
    record.values.drop(1).headOption match
      case Some(value) =>
        value.toIntOption
          .toRight(s"Invalid next value '$value' for table '$table'")
      case None =>
        Left(s"Missing next value for table '$table'")

  private def ensureNotDuplicated(
                                   values: Map[String, Int],
                                   table: String
                                 ): Result[Unit] =
    if values.contains(table) then
      Left(s"Duplicated table '$table'")
    else
      Right(())

  private def extractValues(
                             record: RawRecord
                           ): Result[(String, Int)] =
    record.values match
      case Vector(table, value) =>
        for
          _ <-
            if table.nonEmpty then Right(())
            else Left("Table name must not be empty")
          nextValue <-
            value.toIntOption
              .toRight(
                s"Invalid next value '$value' for table '$table'"
              )
        yield (table, nextValue)

      case _ =>
        Left("AutoNumber CSV record must contain exactly two values")

  private final case class CsvProvider(
                                        values: Map[String, Int]
                                      ) extends AutoNumberProvider:

    override def nextValue(table: String): Result[Int] =
      values
        .get(table)
        .toRight(s"Unknown table '$table'")

