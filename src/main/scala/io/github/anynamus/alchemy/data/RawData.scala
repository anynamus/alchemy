package io.github.anynamus.alchemy.data

final case class RawData(
                          headers: Vector[String],
                          records: Vector[RawRecord]
                        )