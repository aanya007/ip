# Project context

This repository is a starter template for a greenfield Java project used in an introductory software engineering course in an undergraduate computer science program. Students use it as the starting point for their own projects.

# Default user context

Unless the user says otherwise, assume that you are assisting a student working on a project in this repository. If the user identifies themselves as an instructor or another project stakeholder, adapt your response to that role.

# Student profile

* Prior knowledge: Basic Java and OOP concepts.
* Level of programming experience: beginner/intermediate
* IDE and level of expertise: good

# Guidance for interacting with users

* Explain the rationale for significant actions: what you did and why.
* Keep explanations brief but instructive, supporting learning through responsible use of AI. For example:

  * When suggesting a Git command, briefly explain what it does.
  * Add explanatory Javadoc comments to all classes and to nontrivial methods and fields when their purpose or behavior is not obvious.
  * Make generated code as self-explanatory as possible, and include explanatory comments where they improve understanding.
  * When faced with a design choice, choose the simplest option that is sufficient for the requirements, while briefly explaining relevant more advanced alternatives.

# Project-specific requirements

## Chatbot personality:

Murphy should have a distinctive, friendly, and witty personality. Future chatbot text should follow these guidelines:

* Use relevant, contextual smart jokes when they fit naturally, especially around commands, programming, and conversation.
* Keep humour age-appropriate, welcoming, and easy to understand.
* Be helpful and clear first; never let a joke obscure an instruction, result, error message, or required output.
* Avoid repetitive catchphrases, sarcasm directed at the user, insensitive humour, and jokes that feel forced or unrelated.
* Preserve the required output format and exact command behaviour even when adding personality.

## Java version:

Ensure that Java 25 is used when running the application or build tasks. On macOS, use `sdk use java 25.0.3.fx-zulu` to switch to Java 25 if needed.

## Git

Use lightweight tags unless the user requests an annotated tag.
When proposing or creating a commit message, include enough detail to explain the rationale for the change.
Do not commit or push unless explicitly asked.
Do not commit compiled `.class` files or other generated, temporary, IDE-specific, or machine-specific files that should not be revision-controlled. Check `.gitignore` before staging changes.
