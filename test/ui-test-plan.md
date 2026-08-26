# Murphy UI Test Plan

This file is the source of truth for console-level UI tests. Run the cases in order and stop at the first failure.

## Test environment

- **Program command:** `javac -d _temp/classes src/main/java/*.java` followed by `java -cp _temp/classes Murphy`
- **Working directory:** repository root
- **Java version:** Java 25 (`sdk use java 25.0.3.fx-zulu` on macOS, if needed)
- **Output comparison:** exact stdout/stderr, including prompts, whitespace, and line breaks
- **Test isolation:** Start a fresh Murphy process and apply the stated data setup before each case.

## Test Case: Save without an existing data directory

**Aim:** Verify that Murphy starts with an empty list when no save file exists, creates the missing directory, and saves UTF-8 text containing a pipe.

**Data setup:** The `data` directory does not exist.

**Inputs:**

```text
todo compare A | B 😀
bye
```

**Expected output:**

```text
____________________________________________________________
M   M  U   U  RRRR   PPPP   H   H  Y   Y
MM MM  U   U  R   R  P   P  H   H   Y Y
M M M  U   U  RRRR   PPPP   HHHHH    Y
M   M  U   U  R  R   P      H   H    Y
M   M   UUU   R   R  P      H   H    Y
Hi there! I'm Murphy, your command-line conversationalist.
What can I do for you? (I promise not to judge your typing.)
____________________________________________________________
____________________________________________________________
     Got it. I've added this task:
       [T][ ] compare A | B 😀
     Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon! Even command lines need a punchline.
____________________________________________________________
```

**Expected data file:**

```text
T | 0 | compare A \| B 😀
```

## Test Case: Recover valid tasks from corrupted data

**Aim:** Verify that Murphy ignores blank lines, reports malformed lines, and loads every valid task that remains.

**Data file before test:**

```text
T | 1 | read book

T | maybe | invalid status
D | 0 | submit report | 2019-10-15
E | 0 | missing end | 2pm |
Z | 0 | unknown task
E | 1 | café meeting | 2pm | 4pm
T | 0 | compare A \| B
```

**Inputs:**

```text
list
bye
```

**Expected output:**

```text
____________________________________________________________
M   M  U   U  RRRR   PPPP   H   H  Y   Y
MM MM  U   U  R   R  P   P  H   H   Y Y
M M M  U   U  RRRR   PPPP   HHHHH    Y
M   M  U   U  R  R   P      H   H    Y
M   M   UUU   R   R  P      H   H    Y
Hi there! I'm Murphy, your command-line conversationalist.
What can I do for you? (I promise not to judge your typing.)
____________________________________________________________
     OOPS! I skipped corrupted save-file line 3.
     OOPS! I skipped corrupted save-file line 5.
     OOPS! I skipped corrupted save-file line 6.
____________________________________________________________
     Here are the tasks in your list:
     1.[T][X] read book
     2.[D][ ] submit report (by: Oct 15 2019)
     3.[E][X] café meeting (from: 2pm to: 4pm)
     4.[T][ ] compare A | B
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon! Even command lines need a punchline.
____________________________________________________________

## Test Case: Parse and display an ISO deadline date

**Aim:** Verify that Murphy parses a deadline date into a typed date, displays it in a different format, persists it in ISO format, and rejects an incorrectly formatted date.

**Data setup:** The `data` directory does not exist.

**Inputs:**

```text
deadline submit report /by 2019-10-15
list
deadline invalid date /by 15-10-2019
bye
```

**Expected output:**

```text
____________________________________________________________
M   M  U   U  RRRR   PPPP   H   H  Y   Y
MM MM  U   U  R   R  P   P  H   H   Y Y
M M M  U   U  RRRR   PPPP   HHHHH    Y
M   M  U   U  R  R   P      H   H    Y
M   M   UUU   R   R  P      H   H    Y
Hi there! I'm Murphy, your command-line conversationalist.
What can I do for you? (I promise not to judge your typing.)
____________________________________________________________
____________________________________________________________
     Got it. I've added this task:
       [D][ ] submit report (by: Oct 15 2019)
     Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
     Here are the tasks in your list:
     1.[D][ ] submit report (by: Oct 15 2019)
____________________________________________________________
____________________________________________________________
     OOPS! Please enter the deadline date as yyyy-MM-dd, like: 2019-10-15
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon! Even command lines need a punchline.
____________________________________________________________
```

**Expected data file:**

```text
D | 0 | submit report | 2019-10-15
```

## Test Case: Find deadlines and events on a date

**Aim:** Verify that Murphy prints deadlines and ISO-date events occurring on a requested date, and rejects an invalid query date.

**Data file before test:**

```text
D | 0 | submit report | 2019-10-15
E | 0 | conference | 2019-10-15 | 2019-10-16
```

**Inputs:**

```text
on 2019-10-15
on bad-date
bye
```

**Expected output:**

```text
____________________________________________________________
M   M  U   U  RRRR   PPPP   H   H  Y   Y
MM MM  U   U  R   R  P   P  H   H   Y Y
M M M  U   U  RRRR   PPPP   HHHHH    Y
M   M  U   U  R  R   P      H   H    Y
M   M   UUU   R   R  P      H   H    Y
Hi there! I'm Murphy, your command-line conversationalist.
What can I do for you? (I promise not to judge your typing.)
____________________________________________________________
____________________________________________________________
     Tasks on 2019-10-15:
     1.[D][ ] submit report (by: Oct 15 2019)
     2.[E][ ] conference (from: 2019-10-15 to: 2019-10-16)
____________________________________________________________
____________________________________________________________
     OOPS! Please enter the date as yyyy-MM-dd, like: 2019-10-15
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon! Even command lines need a punchline.
____________________________________________________________
```
```
