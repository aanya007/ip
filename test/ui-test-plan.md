# Murphy UI Test Plan

This file is the source of truth for console-level UI tests. Add each test case before running the test session. The runner must process cases in the order listed and stop at the first failure.

## Test environment

- **Program command:** `javac -d _temp/classes src/main/java/*.java` followed by `java -cp _temp/classes Murphy`
- **Working directory:** repository root
- **Java version:** Java 25 (`sdk use java 25.0.3.fx-zulu` on macOS, if needed)
- **Output comparison:** exact stdout/stderr, including prompts, whitespace, and line breaks
- **Environment assumption:** `data/duke.txt` exists and contains the valid task data shown below because this round tests only the reading happy path.

## Test Case: Load saved tasks

**Aim:** Verify that Murphy loads saved todos, deadlines, and events, including their completion states, when it starts.

**Data file before test:**

```text
T | 1 | read book
D | 0 | submit report | Friday
E | 1 | project meeting | 2pm | 4pm
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
____________________________________________________________
     Here are the tasks in your list:
     1.[T][X] read book
     2.[D][ ] submit report (by: Friday)
     3.[E][X] project meeting (from: 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon! Even command lines need a punchline.
____________________________________________________________
```
