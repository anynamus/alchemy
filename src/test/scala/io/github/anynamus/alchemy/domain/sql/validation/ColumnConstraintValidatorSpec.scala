package io.github.anynamus.alchemy.domain.sql.validation

import io.github.anynamus.alchemy.core.ValidationResult
import io.github.anynamus.alchemy.domain.sql.model.Constraint.*
import org.scalatest.funsuite.AnyFunSuite

class ColumnConstraintValidatorSpec extends AnyFunSuite:

  private val validator = new ColumnConstraintValidator()

  test("BR-004 — A reference must target a non-empty table name"):

    val constraints = Vector(Reference(""))

    val result = validator.validate(constraints)

    assert(result == Left(Vector("BR-004")))

  test("BR-005 - A column cannot contain duplicate NotNull Constraint"):

    val constraints = Vector(NotNull, NotNull)

    val result = validator.validate(constraints)

    assert(result == Left(Vector("BR-005: duplicated constraints NotNull")))


  test("BR-005 - A column cannot contain duplicate Reference Constraint"):

    val constraints = Vector(Reference("Customer"), Reference("Customer"))

    val result = validator.validate(constraints)

    assert(result == Left(Vector("BR-005: duplicated constraints Reference(Customer)")))

  test("BR-005 - Constraints without duplication is valid"):

    val constraints = Vector(NotNull, Reference("Customer"))

    val result = validator.validate(constraints)

    assert(result == Right(constraints))

  test("BR-006 — A column cannot contain more than one reference constraint."):

    val constraints = Vector(Reference("Customer"), Reference("Order"))

    val result = validator.validate(constraints)

    assert(result == Left(Vector("BR-006: more than one Reference : Customer, Order")))