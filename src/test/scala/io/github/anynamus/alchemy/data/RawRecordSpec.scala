package io.github.anynamus.alchemy.data

import io.github.anynamus.alchemy.data.RawRecord
import org.scalatest.funsuite.AnyFunSuite

class RawRecordSpec extends AnyFunSuite:

  test("A raw record contains values"):

    val record = RawRecord(
      Vector("Alice", "alice@example.com")
    )

    assert(
      record.values == Vector("Alice", "alice@example.com")
    )