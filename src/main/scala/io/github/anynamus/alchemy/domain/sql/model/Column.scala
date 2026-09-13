package io.github.anynamus.alchemy.domain.sql.model

final case class Column(
    name: String,
    `type`: ColumnType,
    constraints: Vector[Constraint] = Vector.empty
)
