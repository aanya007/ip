# Murphy UI Test Plan

This file is the source of truth for console-level UI tests. Add each test case before running the test session. The runner must process cases in the order listed and stop at the first failure.

## Test environment

- **Program command:** `javac -d _temp/classes src/main/java/*.java` followed by `java -cp _temp/classes Murphy`
- **Working directory:** repository root
- **Java version:** Java 25 (`sdk use java 25.0.3.fx-zulu` on macOS, if needed)
- **Output comparison:** exact stdout/stderr, including prompts, whitespace, and line breaks
- **Environment assumption:** The `data` directory exists because this round tests only the writing happy path.

## Test Case: Save task-list changes

**Aim:** Verify that commands which add, mark, unmark, and delete tasks still produce the expected console output while Murphy saves each successful change.

**Inputs:**

```text
todo read book
mark 1
unmark 1
delete 1
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
       [T][ ] read book
     Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
     Nice! I've marked this task as done:
       [X] read book
____________________________________________________________
____________________________________________________________
     OK, I've marked this task as not done yet:
       [ ] read book
____________________________________________________________
____________________________________________________________
     Noted. I've removed this task:
       [T][ ] read book
     Now you have 0 tasks in the list.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon! Even command lines need a punchline.
____________________________________________________________
```
