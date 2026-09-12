package io.github.anynamus.alchemy.csv

import io.github.anynamus.alchemy.core.Result

trait CsvReader:
  def read(input: String): Result[CsvData]