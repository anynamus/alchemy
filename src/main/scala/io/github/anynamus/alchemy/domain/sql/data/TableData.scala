package io.github.anynamus.alchemy.domain.sql.data

import io.github.anynamus.alchemy.domain.sql.model.Table

final case class TableData(
                            table: Table,
                            records: Vector[DataRecord]
                          )