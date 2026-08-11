package io.github.anynamus.alchemy.domain.sql.validation

import org.scalatest.funsuite.AnyFunSuite
import io.github.anynamus.alchemy.domain.sql.model.Constraint.*

class ColumnConstraintValidatorSpec extends AnyFunSuite:

  private val validator = new ColumnConstraintValidator()

  test("BR-004 — A reference must target a non-empty table name"):

    val constraints = Vector(Reference(""))

    val result = validator.validate(constraints)

    assert(result == Left(Vector("BR-004")))
