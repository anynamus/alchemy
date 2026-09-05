package io.github.anynamus.alchemy.domain.sql.decoder

import io.github.anynamus.alchemy.core.Traverse.traverse
import io.github.anynamus.alchemy.core.{Decoder, Result}
import io.github.anynamus.alchemy.domain.sql.model.{Schema, Table}
import io.github.anynamus.alchemy.yaml.YamlNode
import io.github.anynamus.alchemy.yaml.YamlNodeOps.*

class SchemaDecoder(tableDecoder: Decoder[YamlNode, Table])
  extends Decoder[YamlNode, Schema]:

  override def decode(node: YamlNode): Result[Schema] =
    node
      .asSequence
      .flatMap(nodes => traverse(nodes)(tableDecoder.decode))
      .map(Schema.apply)
