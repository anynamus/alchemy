package io.github.anynamus.alchemy.core

object Collections:
  def duplicatesBy[A, K](values: Iterable[A])(key: A => K): Map[K, Vector[A]] =
    values
      .groupBy(key)
      .view
      .filter { (_, occurrences) => occurrences.size > 1 }
      .mapValues(_.toVector)
      .toMap

  def duplicates[A](values: Vector[A]): Map[A, Vector[A]] =
    values
      .groupBy(identity)
      .collect {
        case (value, occurrences) if occurrences.size > 1 =>
          value -> occurrences
      }
