package io.github.anynamus.alchemy.domain.sql.validation

import io.github.anynamus.alchemy.core.{RuleValidation, ValidationResult, Validator}
import io.github.anynamus.alchemy.domain.sql.model.{ColumnType, Table}

private class AtLeastOneColumnValidator extends RuleValidation[Table]:
  override def validate(table: Table): Option[String] =
    if(table.columns.isEmpty)
      Some("BR-001")
    else
      None

private class UniqueColumnNameValidator extends RuleValidation[Table]:
  override def validate(table: Table): Option[String] =
    if(table.columns.map(c => c.name).toSet.size != table.columns.size)
      Some("BR-002")
    else
      None

private class UniqueAutoNumberValidator extends RuleValidation[Table]:
  override def validate(table: Table): Option[String] =
    if(table.columns.count(c => ColumnType.AutoNumber == c.`type`) > 1)
      Some("BR-003")
    else
      None

class TableValidator extends Validator[Table]:

  private val rules = Vector(
    new AtLeastOneColumnValidator(),
    new UniqueColumnNameValidator(),
    new UniqueAutoNumberValidator()
  )

  override def validate(table: Table): ValidationResult[Table] =
    val violations = rules.flatMap(_.validate(table))

    if(violations.isEmpty)
      Right(table)
    else
      Left(violations)
