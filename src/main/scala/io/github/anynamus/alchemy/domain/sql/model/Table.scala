package io.github.anynamus.alchemy.domain.sql.model

final case class Table(
    name: String,
    columns: Vector[Column]
)
