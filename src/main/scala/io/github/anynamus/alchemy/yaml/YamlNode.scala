package io.github.anynamus.alchemy.yaml

enum YamlNode:

  case Mapping(
      fields: Map[String, YamlNode]
  )

  case Sequence(
      values: Vector[YamlNode]
  )

  case Scalar(
      value: String
  )
