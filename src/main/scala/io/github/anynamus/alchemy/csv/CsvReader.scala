package io.github.anynamus.alchemy.csv

import io.github.anynamus.alchemy.core.Result
import io.github.anynamus.alchemy.domain.sql.model.RawData

trait CsvReader:
  def read(input: String): Result[RawData]