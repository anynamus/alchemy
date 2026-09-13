package io.github.anynamus.alchemy.domain.sql.model

enum ColumnType(val yamlName: String):
  case AutoNumber extends ColumnType("autonumber")
  case String     extends ColumnType("string")

object ColumnType:
  def fromString(name: String): Option[ColumnType] =
    ColumnType.values.find(_.yamlName == name)
