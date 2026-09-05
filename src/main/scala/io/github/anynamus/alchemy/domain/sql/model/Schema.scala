package io.github.anynamus.alchemy.domain.sql.model

final case class Schema(
                         tables: Vector[Table]
                       )