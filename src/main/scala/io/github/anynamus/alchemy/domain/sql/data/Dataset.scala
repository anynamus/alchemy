package io.github.anynamus.alchemy.domain.sql.data

final case class Dataset(
                          tables: Vector[TableData]
                        )