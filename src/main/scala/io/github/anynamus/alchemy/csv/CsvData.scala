package io.github.anynamus.alchemy.csv

import io.github.anynamus.alchemy.data.RawRecord

final case class CsvData(
                          headers: Vector[String],
                          records: Vector[RawRecord]
                        )