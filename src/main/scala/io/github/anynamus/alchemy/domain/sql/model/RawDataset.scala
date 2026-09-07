package io.github.anynamus.alchemy.domain.sql.model

final case class RawDataset(
                             tables: Vector[RawData]
                           )