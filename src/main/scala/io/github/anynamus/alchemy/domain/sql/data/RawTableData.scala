package io.github.anynamus.alchemy.domain.sql.data

import io.github.anynamus.alchemy.data.RawData

final case class RawTableData(
                               table: String,
                               data: RawData
                             )