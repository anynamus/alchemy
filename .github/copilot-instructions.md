# Project Overview

This project is a Scala 3 library/application for generating SQL INSERT
statements from YAML table definitions and CSV data.

The project is intentionally designed using functional programming principles.

## Technology

- Scala 3.8.1
- ScalaTest
- SnakeYAML Engine 2.10
- sbt
- IntelliJ IDEA

## Functional Programming

Prefer functional programming over imperative or object-oriented approaches.

Prefer:
- immutable values
- pure functions
- expressions over statements
- pattern matching
- map / flatMap / fold / traverse
- Either for computations that may fail
- Option for optional values

Avoid:
- mutable state
- null
- exceptions for expected business errors
- unnecessary inheritance
- imperative loops when a functional collection operation is clearer

## Error Handling

The project uses:

    type Result[+A] = Either[String, A]

Use Result for operations that can fail because of invalid input or invalid
domain data.

Do not throw exceptions for expected validation or decoding failures.

Errors should be explicit and useful to the caller.

## Architecture

Keep the domain independent from infrastructure and third-party libraries.

In particular:
- domain classes must not depend on SnakeYAML
- YAML parsing belongs to the infrastructure layer
- decoding/conversion between external representations and domain objects
  belongs outside the domain
- business validation belongs in the domain validation layer

Do not introduce dependencies from domain code toward infrastructure code.

## Scala Style

Use idiomatic Scala 3.

Prefer:
- `case class`
- `enum`
- extension methods
- `given` / type classes when appropriate
- pattern matching
- significant indentation

Avoid Java-style patterns when an idiomatic Scala solution exists.

Do not introduce classes named `*Impl`.

Prefer meaningful names and companion objects.

## Tests

Use ScalaTest.

Every behavior change must have corresponding tests.

Tests must pass before considering an implementation complete.

Prefer focused unit tests over large integration tests when testing pure
functions or domain behavior.

Tests should document the expected behavior, especially for error cases.

## Development Process

Before implementing a non-trivial change:

1. Understand the existing architecture and conventions.
2. Reuse existing abstractions whenever possible.
3. Avoid introducing a new abstraction if an existing one can express the
   requirement clearly.
4. Implement the smallest coherent change.
5. Add or update tests.
6. Run the relevant tests.
7. Only then consider the implementation complete.

Do not refactor unrelated code unless it is necessary for the requested change.

## Code Quality

Prefer simple, composable functions.

Avoid premature abstraction.

When several implementations are possible, prefer the one that is:
1. idiomatic Scala
2. easy to test
3. composable
4. consistent with the existing architecture
5. simple to understand

When modifying existing code, preserve its established style unless there
is a strong reason to change it.