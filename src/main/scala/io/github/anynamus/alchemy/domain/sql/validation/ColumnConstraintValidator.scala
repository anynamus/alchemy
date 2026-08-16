package io.github.anynamus.alchemy.domain.sql.validation

import io.github.anynamus.alchemy.core.{RuleValidation, ValidationError, ValidationResult, Validator}
import io.github.anynamus.alchemy.domain.sql.model.Constraint
import io.github.anynamus.alchemy.domain.sql.model.Constraint.*
import io.github.anynamus.alchemy.core.Collections.duplicates

private class ReferenceMustTargetNonEmptyTable extends RuleValidation[Vector[Constraint]]:
  override def validate(constraints: Vector[Constraint]): Option[ValidationError] =
    val references = constraints.collect {
      case reference: Reference => reference
    }.filter(_.table.isEmpty)

    if (references.nonEmpty)
      Some("BR-004")
    else
      None


private class ConstraintsCannotBeDuplicated extends RuleValidation[Vector[Constraint]]:
  override def validate(constraints: Vector[Constraint]): Option[ValidationError] =
    val duplicatedConstraints = duplicates(constraints)

    if(duplicatedConstraints.nonEmpty)
      Some(s"BR-005: duplicated constraints ${duplicatedConstraints.keys.mkString(", ")}")
    else
      None



class ColumnConstraintValidator extends Validator[Vector[Constraint]]:

  private val rules = Vector(
    new ReferenceMustTargetNonEmptyTable(),
    new ConstraintsCannotBeDuplicated()
  )

  override def validate(constraints: Vector[Constraint]): ValidationResult[Vector[Constraint]] =
    val violations = rules.flatMap(_.validate(constraints))

    if (violations.isEmpty)
      Right(constraints)
    else
      Left(violations)
