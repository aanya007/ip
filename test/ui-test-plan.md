# Murphy UI Test Plan

This file is the source of truth for console-level UI tests. Add each test case before running the test session. The runner must process cases in the order listed and stop at the first failure.

## Test environment

- **Program command:** `<command to start Murphy>`
- **Working directory:** repository root
- **Java version:** Java 25 (`sdk use java 25.0.3.fx-zulu` on macOS, if needed)
- **Output comparison:** exact stdout/stderr, including prompts, whitespace, and line breaks

## Test case template

Copy this block for each test case and replace every placeholder:

## Test Case: <short name>

**Aim:** <what behavior this verifies>

**Inputs:**

```text
<one command/input per line>
```

**Expected output:**

```text
<the exact complete output expected from the program>
```
