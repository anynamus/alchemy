package io.github.anynamus.alchemy.domain.sql.model

import io.github.anynamus.alchemy.data.{RawData, RawTableData}

final case class RawDataset(
                             tables: Vector[RawTableData]
                           )