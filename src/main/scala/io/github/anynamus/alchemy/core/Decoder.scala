package io.github.anynamus.alchemy.core

trait Decoder[-I, +A]:
  def decode(input: I): Result[A]
