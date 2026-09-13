package io.github.anynamus.alchemy.yaml

import io.github.anynamus.alchemy.core.Result

trait YamlReader:

  def read(
      text: String
  ): Result[YamlNode]
