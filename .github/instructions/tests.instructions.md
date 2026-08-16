---
applyTo: "src/test/scala/**/*.scala"
---

# Testing rules

Use ScalaTest.

Tests must be deterministic.

Prefer one clear behavior per test.

Test both successful and failing cases for functions returning Result.

When testing validation, verify the error message as well as the failure.

Prefer tests that describe business behavior rather than implementation details.

Do not weaken an existing test merely to make an implementation pass.