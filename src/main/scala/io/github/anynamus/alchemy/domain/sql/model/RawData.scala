package io.github.anynamus.alchemy.domain.sql.model

final case class RawData(
                          headers: Vector[String],
                          records: Vector[RawRecord]
                        )