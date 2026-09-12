package io.github.anynamus.alchemy.yaml

import io.github.anynamus.alchemy.core.Result
import io.github.anynamus.alchemy.core.Traverse.traverse
import org.snakeyaml.engine.v2.api.LoadSettings
import org.snakeyaml.engine.v2.api.lowlevel.Compose
import org.snakeyaml.engine.v2.nodes.*

import scala.jdk.CollectionConverters.*
import scala.jdk.OptionConverters.*

class SnakeYamlReader extends YamlReader:

  private val compose =
    Compose(
      LoadSettings.builder()
        .setUseMarks(false)
        .build()
    )

  override def read(yaml: String): Result[YamlNode] =
    val node = compose.composeString(yaml).toScala

    node match
      case Some(root) =>
        convert(root)

      case None =>
        Left("invalid yaml")

  private def convert(node: Node): Result[YamlNode] =
    node match
      case mapping: MappingNode =>
        convertMapping(mapping)

      case sequence: SequenceNode =>
        convertSequence(sequence)

      case scalar: ScalarNode =>
        Right(YamlNode.Scalar(scalar.getValue))

  private def convertMapping(mapping: MappingNode): Result[YamlNode] =
    val entries = mapping.getValue.asScala.toVector

    traverse(entries)(convertMappingEntry).map(fields =>
      YamlNode.Mapping(fields.toMap)
    )

  private def convertSequence(sequence: SequenceNode): Result[YamlNode] =
    val values = sequence.getValue.asScala.toVector

    traverse(values)(convert).map(nodes =>
      YamlNode.Sequence(nodes)
    )

  private def convertMappingEntry(tuple: NodeTuple): Result[(String, YamlNode)] =
    for
      key <- tuple.getKeyNode match
        case scalar: ScalarNode => Right(scalar.getValue)
        case _ => Left("invalid YAML key: keys must be scalars")

      value <- convert(tuple.getValueNode)
    yield (key, value)
