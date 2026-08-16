package io.github.anynamus.alchemy.core

trait RuleValidation[A]:
  def validate(subject: A): Option[ValidationError]
