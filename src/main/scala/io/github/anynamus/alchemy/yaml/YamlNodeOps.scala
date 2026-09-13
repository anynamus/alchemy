package io.github.anynamus.alchemy.yaml

import io.github.anynamus.alchemy.core.Result

object YamlNodeOps:

  extension (node: YamlNode)

    def asScalar: Result[String] =
      node match
        case YamlNode.Scalar(value) => Right(value)
        case YamlNode.Sequence(_)   => Left("Cannot map a Sequence node to Scalar")
        case YamlNode.Mapping(_)    => Left("Cannot map a Mapping node to Scalar")

    def asSequence: Result[Vector[YamlNode]] =
      node match
        case YamlNode.Sequence(values) => Right(values)
        case YamlNode.Scalar(_)        => Left("Cannot map a Scalar node to Sequence")
        case YamlNode.Mapping(_)       => Left("Cannot map a Mapping node to Sequence")
