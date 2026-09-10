# Murphy User Guide

Murphy rejects newly added tasks that duplicate an existing task. Duplicate
detection ignores leading and trailing whitespace, letter case, and completion
status. Existing duplicate records in a save file are preserved.

## Adding deadlines

// Describe the action and its outcome.

// Give examples of usage

Example: `keyword (optional arguments)`

// A description of the expected outcome goes here

```
expected output
```

## Handling duplicate tasks

Duplicate detection applies to `todo`, `deadline`, and `event` commands. A
duplicate must have the same task type and the same task details.

```text
todo buy milk
todo Buy milk
```

The second command is rejected:

```text
OOPS! This task is already in your list:
[T][ ] buy milk
I kept the existing task and did not add a duplicate.
```

Tasks with different types, deadline dates, or event start/end values are not
duplicates. Use `list` and `delete <task number>` to review and remove any
duplicate records that already existed before this feature.
