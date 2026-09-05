package io.github.anynamus.alchemy.domain.sql.validation

import io.github.anynamus.alchemy.core.Collections.duplicates
import io.github.anynamus.alchemy.core.{RuleValidation, ValidationResult, Validator}
import io.github.anynamus.alchemy.domain.sql.model.Schema

private class SchemaMustContainAtLeastOneTable extends RuleValidation[Schema]:
  override def validate(schema: Schema): Option[String] =
    if schema.tables.isEmpty then
      Some("BR-007: schema must contain at least one table")
    else
      None

private class TableNamesMustBeUnique extends RuleValidation[Schema]:
  override def validate(schema: Schema): Option[String] =
    val duplicatedNames =
      duplicates(schema.tables.map(_.name)).keySet

    val names =
      schema.tables
        .map(_.name)
        .filter(duplicatedNames.contains)
        .distinct

    if duplicatedNames.nonEmpty then
      Some(s"BR-008: duplicated table names ${names.mkString(", ")}")
    else
      None

class SchemaValidator extends Validator[Schema]:

  private val rules = Vector(
    new SchemaMustContainAtLeastOneTable(),
    new TableNamesMustBeUnique()
  )

  override def validate(schema: Schema): ValidationResult[Schema] =
    val violations = rules.flatMap(_.validate(schema))

    if violations.isEmpty then
      Right(schema)
    else
      Left(violations)
