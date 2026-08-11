package io.github.anynamus.alchemy.domain.sql.validation

import io.github.anynamus.alchemy.core.{ValidationResult, Validator}
import io.github.anynamus.alchemy.domain.sql.model.Constraint
import io.github.anynamus.alchemy.domain.sql.model.Constraint.*
//import io.github.anynamus.alchemy.core.Collections.duplicatesBy

class ColumnConstraintValidator extends Validator[Vector[Constraint]]:
  override def validate(constraints: Vector[Constraint]): ValidationResult[Vector[Constraint]] =
    val references = constraints.collect {
      case reference: Reference => reference
    }.filter(_.table.isEmpty)

    if(references.isEmpty)
      Right(constraints)
    else
      Left(Vector("BR-004"))
