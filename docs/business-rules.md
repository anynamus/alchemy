# Business Rules

## BR-001 — A table must contain at least one column

### Description

A table definition without any columns is invalid.

### Motivation

A SQL table without columns does not make sense in the context of this library.

### Expected Behaviour

The validator must report an error when the `columns` collection is empty.

## BR-002 — Column names must be unique

### Description

Two columns within the same table cannot have the same name.

### Motivation

A SQL statement cannot define multiple columns with the same identifier.

### Expected Behaviour

Validation fails if two or more columns have the same name.

## BR-003 — A table may contain only one AutoNumber column

### Description

Only one `AutoNumber` column is allowed per table.

### Motivation

The current model considers this column to be the table's technical key.

### Expected Behaviour

Validation fails if more than one column is of type `AutoNumber`.

## BR-004 — A reference must target a non-empty table name

### Description

A reference constraint must specify the name of the referenced table.

### Motivation

A reference has no meaning without a target table.

### Expected Behavior

Validation fails if the referenced table name is empty.


## BR-005 — A column cannot contain duplicate constraints

### Description

Each constraint must occur at most once within a column.

### Motivation

Duplicating the same constraint on a column provides no additional meaning.

### Expected Behavior

Validation fails if the same constraint occurs more than once within a column.


## BR-006 — A column cannot contain more than one reference constraint.

### Description

A column can have at most one reference constraint.

### Motivation

A column can reference only one table.

### Expected Behaviour

Validation fails if a column contains more than one reference constraint.


## BR-007 — A schema must contain at least one table

### Description

A schema must contain at least one table.

### Motivation

A schema without tables has no useful SQL definition.

### Expected Behaviour

Validation fails if a schema contains no tables.


## BR-008 — Table names must be unique within a schema

### Description

Each table name must occur at most once within a schema.

### Motivation

A table name identifies a table and must therefore be unambiguous.

### Expected Behaviour

Validation fails if multiple tables have the same name.


## BR-009 — A candidate key must exist

### Description

If a candidate key is defined, it must reference an existing column within the table.

### Motivation

A candidate key is not useful if the column does not exist.

### Expected Behaviour

Validation fails if a candidate key references an unknown column.


## BR-010 — A reference must point to an existing table

### Description

A reference constraint must target a table defined in the schema.

### Motivation

A reference cannot be resolved if the target table does not exist.

### Expected Behaviour

Validation fails if a reference targets an unknown table.


## BR-011 — A referenced table must define a candidate key

### Description

A table targeted by a reference must define a candidate key.

### Motivation

A reference needs a candidate key to identify the target record.

### Expected Behaviour

Validation fails if a reference targets a table without a candidate key.


## BR-012 — A reference value must match a target record

### Description

A reference value must match the candidate key of exactly one record in the referenced table.

### Motivation

A reference cannot be resolved if no target record matches the provided value.

### Expected Behaviour

Resolution fails if no record in the referenced table matches the reference value.


## BR-013 — A candidate key must uniquely identify a record

### Description

A candidate key value must identify at most one record within a table.

### Motivation

A reference cannot be resolved unambiguously if multiple records have the same candidate key value.

### Expected Behaviour

Resolution fails if multiple records in a table have the same candidate key value.


# Future Rules

- References must point to an existing table.
- A reference column must target a candidate key.
- Table names must be unique within a schema.