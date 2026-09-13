package io.github.anynamus.alchemy.core

object Traverse:

  def traverse[A, B](
      values: Vector[A]
  )(
      f: A => Result[B]
  ): Result[Vector[B]] =
    values
      .foldLeft[Result[List[B]]](Right(Nil)) { (acc, value) =>
        for
          xs <- acc
          y  <- f(value)
        yield y :: xs
      }
      .map(_.reverse.toVector)
