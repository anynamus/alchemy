package io.github.anynamus.alchemy.domain.sql.validation

import io.github.anynamus.alchemy.core.Collections.duplicates
import io.github.anynamus.alchemy.core.{RuleValidation, ValidationResult, Validator}
import io.github.anynamus.alchemy.domain.sql.model.Constraint.Reference
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

private class ReferencedTablesMustExist extends RuleValidation[Schema]:
  override def validate(schema: Schema): Option[String] =
    val tables = schema.tables.map(_.name)

    val references = distinctReferences(schema)

    val notExisting = references.filterNot(tables.contains)

    if notExisting.isEmpty then
      None
    else
      Some(s"BR-010: Referenced tables does not exist: '${notExisting.mkString(", ")}'")

private class ReferencedTablesMustDefineCandidateKey extends RuleValidation[Schema]:
  override def validate(schema: Schema): Option[String] =

    val references = distinctReferences(schema)

    val referencedTablesWithoutCandidateKey = schema.tables
      .filterNot(_.candidateKey.isDefined)
      .map(_.name)
      .filter(references.contains)
      .toList

    if referencedTablesWithoutCandidateKey.nonEmpty then
      Some(s"BR-011: Referenced tables without candidate key: '${referencedTablesWithoutCandidateKey.mkString(", ")}'")
    else
      None


private def distinctReferences(schema: Schema): List[String] =
  schema.tables
    .flatMap(_.columns)
    .flatMap(_.constraints)
    .collect {
      case reference: Reference => reference
    }.map(_.table).toList.distinct


class SchemaValidator extends Validator[Schema]:

  private val rules = Vector(
    new SchemaMustContainAtLeastOneTable(),
    new TableNamesMustBeUnique(),
    new ReferencedTablesMustExist(),
    new ReferencedTablesMustDefineCandidateKey()
  )

  override def validate(schema: Schema): ValidationResult[Schema] =
    val violations = rules.flatMap(_.validate(schema))

    if violations.isEmpty then
      Right(schema)
    else
      Left(violations)
