package io.github.anynamus.alchemy.csv

import io.github.anynamus.alchemy.core.Result
import io.github.anynamus.alchemy.data.{RawData, RawRecord}
import org.apache.commons.csv.{CSVFormat, CSVParser}

import java.io.UncheckedIOException
import scala.jdk.CollectionConverters.*

class CommonsCsvReader extends CsvReader:

  override def read(input: String): Result[CsvData] =
    if input.isEmpty then
      Left("CSV must not be empty")
    else
      try
        val parser = createParser(input)
        val headers = extractHeaders(parser)

        for
          _ <- validateHeaders(headers)
          records <- extractRecords(parser, headers)

        yield CsvData(headers, records)
      catch
        case exception: UncheckedIOException =>
          Left(exception.getCause.getMessage)

  private def createParser(input: String): CSVParser =
    CSVParser.parse(
      input,
      CSVFormat.DEFAULT.builder()
        .setHeader()
        .setSkipHeaderRecord(true)
        .get()
    )

  private def extractHeaders(parser: CSVParser): Vector[String] =
    parser.getHeaderNames.asScala.toVector

  private def validateHeaders(headers: Vector[String]): Result[Unit] =
    if headers.distinct.size != headers.size then
      Left("CSV headers must be unique")
    else
      Right(())

  private def extractRecords(
                              parser: CSVParser,
                              headers: Vector[String]
                            ): Result[Vector[RawRecord]] =
    val records =
      parser
        .iterator()
        .asScala
        .map(record => record.iterator().asScala.toVector)
        .toVector

    records.find(_.size != headers.size) match
      case Some(_) =>
        Left("CSV record has an invalid number of values")
      case None =>
        Right(records.map(RawRecord.apply))