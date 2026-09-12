package io.github.anynamus.alchemy.csv

import io.github.anynamus.alchemy.core.Result
import io.github.anynamus.alchemy.data.RawRecord
import org.scalatest.funsuite.AnyFunSuite

class CsvAutoNumberProviderSpec extends AnyFunSuite:

  test("return next value for a known table"):
    val provider = providerWith(
      CsvData(
        headers = Vector("table", "nextValue"),
        records = Vector(
          RawRecord(Vector("Customer", "100")),
          RawRecord(Vector("Order", "500"))
        )
      )
    )

    assert(provider.nextValue("Customer") == Right(100))
    assert(provider.nextValue("Order") == Right(500))

  test("return the same value on successive calls"):
    val provider = providerWith(
      CsvData(
        headers = Vector("table", "nextValue"),
        records = Vector(
          RawRecord(Vector("Customer", "100"))
        )
      )
    )

    assert(provider.nextValue("Customer") == Right(100))
    assert(provider.nextValue("Customer") == Right(100))

  test("fail for unknown table"):
    val provider = providerWith(
      CsvData(
        headers = Vector("table", "nextValue"),
        records = Vector(
          RawRecord(Vector("Customer", "100"))
        )
      )
    )

    assert(provider.nextValue("Order") == Left("Unknown table 'Order'"))

  test("fail when next value is not an integer"):
    val result =
      providerResultWith(
        CsvData(
          headers = Vector("table", "nextValue"),
          records = Vector(
            RawRecord(Vector("Customer", "abc"))
          )
        )
      )

    assert(
      result == Left("Invalid next value 'abc' for table 'Customer'")
    )

  test("fail when table name is empty"):
    val result =
      providerResultWith(
        CsvData(
          headers = Vector("table", "nextValue"),
          records = Vector(
            RawRecord(Vector("", "100"))
          )
        )
      )

    assert(result == Left("Table name must not be empty"))

  test("fail when a table is duplicated"):
    val providerResult =
      providerResultWith(
        CsvData(
          headers = Vector("table", "nextValue"),
          records = Vector(
            RawRecord(Vector("Customer", "100")),
            RawRecord(Vector("Order", "500")),
            RawRecord(Vector("Customer", "200"))
          )
        )
      )

    assert(
      providerResult ==
        Left("Duplicated table 'Customer'")
    )

  private def providerWith(data: CsvData): AutoNumberProvider =
    providerResultWith(data) match
      case Right(provider) => provider
      case Left(error) => fail(error)

  private def providerResultWith(
                                  data: CsvData
                                ): Result[AutoNumberProvider] =
    val csvReader = new CsvReader:
      override def read(input: String): Result[CsvData] =
        Right(data)

    CsvAutoNumberProvider.from(csvReader, "ignored")
