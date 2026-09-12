package io.github.anynamus.alchemy.domain.sql.decoder

import io.github.anynamus.alchemy.core.Traverse.traverse
import io.github.anynamus.alchemy.core.{Decoder, Result}
import io.github.anynamus.alchemy.domain.sql.model.{Column, Table}
import io.github.anynamus.alchemy.yaml.YamlMappingOps.*
import io.github.anynamus.alchemy.yaml.YamlNode
import io.github.anynamus.alchemy.yaml.YamlNodeOps.*

class TableDecoder(columnDecoder: Decoder[YamlNode, Column]) extends Decoder[YamlNode,Table] :

  override def decode(node: YamlNode): Result[Table] =
    node match
      case YamlNode.Mapping(fields) =>
        for
          tableName <-
            fields.required("table")
              .flatMap(_.asScalar)
          columns <-
            fields.required("columns")
              .flatMap(_.asSequence)
              .flatMap(nodes => traverse(nodes)(columnDecoder.decode))
        yield Table(tableName, columns)
      case _ => Left("Expected a mapping for table definition")
