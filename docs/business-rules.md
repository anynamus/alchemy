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


# Future Rules

- References must point to an existing table.
- A reference column must target a candidate key.
- Table names must be unique within a schema.