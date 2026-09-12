package io.github.anynamus.alchemy.domain.sql.data

import io.github.anynamus.alchemy.domain.sql.data.RawTableData

final case class RawDataset(
                             tables: Vector[RawTableData]
                           )