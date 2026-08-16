---
applyTo: "src/main/scala/**/*.scala"
---

# Scala implementation rules

Prefer pure functions.

Prefer expressions over statements.

Use pattern matching when it makes the possible cases explicit.

Prefer `Either` and `Option` over exceptions and null.

Avoid unnecessary `fold` when `map`, `flatMap`, `getOrElse`,
or pattern matching makes the intent clearer.

Prefer small composable functions over large functions with
multiple responsibilities.