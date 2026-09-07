package io.github.anynamus.alchemy.csv

import io.github.anynamus.alchemy.core.Result
import io.github.anynamus.alchemy.domain.sql.model.{RawDataSource, RawDataset}

trait RawDataReader:
  def read(sources: Vector[RawDataSource]): Result[RawDataset]