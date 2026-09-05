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

    if references.nonEmpty then
      Some("BR-004")
    else
      None


private class ConstraintsCannotBeDuplicated extends RuleValidation[Vector[Constraint]]:
  override def validate(constraints: Vector[Constraint]): Option[ValidationError] =
    val duplicatedConstraints = duplicates(constraints)

    if duplicatedConstraints.nonEmpty then
      Some(s"BR-005: duplicated constraints ${duplicatedConstraints.keys.mkString(", ")}")
    else
      None

private class AtMostOneReferenceConstraint extends RuleValidation[Vector[Constraint]]:
  override def validate(constraints: Vector[Constraint]): Option[ValidationError] =
    val references = constraints.collect {
      case reference: Reference => reference
    }.distinct

    if references.size > 1 then
      Some(s"BR-006: more than one Reference : ${references.map(_.table).mkString(", ")}")
    else
      None


class ColumnConstraintValidator extends Validator[Vector[Constraint]]:

  private val rules = Vector(
    new ReferenceMustTargetNonEmptyTable(),
    new ConstraintsCannotBeDuplicated(),
    new AtMostOneReferenceConstraint()
  )

  override def validate(constraints: Vector[Constraint]): ValidationResult[Vector[Constraint]] =
    val violations = rules.flatMap(_.validate(constraints))

    if (violations.isEmpty)
      Right(constraints)
    else
      Left(violations)
