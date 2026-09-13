package io.github.anynamus.alchemy.domain.sql.model

sealed trait Constraint

object Constraint:
  final case class Reference(table: String) extends Constraint

  case object NotNull                       extends Constraint
