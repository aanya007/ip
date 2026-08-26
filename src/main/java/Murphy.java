import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * A friendly command-line chatbot that stores user-entered tasks in memory.
 */
public class Murphy {
    /** The maximum number of tasks Murphy can remember during one run. */
    private static final int MAX_TASKS = 100;

    /** The relative path of Murphy's task data file. */
    private static final Path DATA_FILE_PATH = Paths.get("data", "duke.txt");

    /**
     * Starts Murphy's conversation with the user.
     *
     * @param args command-line arguments, which Murphy does not need
     */
    public static void main(String[] args) {
        Ui ui = new Ui();
        ui.showWelcome();
        Storage storage = new Storage(DATA_FILE_PATH, MAX_TASKS, ui);
        Parser parser = new Parser();

        TaskList tasks = storage.load();

        try (ui) {
            while (ui.hasNextCommand()) {
                String command = ui.readCommand();
                Parser.ParsedCommand parsedCommand = parser.parse(command);

                ui.showSeparator();

                try {
                    if (parsedCommand.command() == Parser.Command.BYE) {
                        ui.showMessage("Bye. Hope to see you again soon! Even command lines need a punchline.");
                        ui.showSeparator();
                        break;
                    }

                    if (parsedCommand.command() == Parser.Command.ON) {
                    String dateText = parsedCommand.argument();
                    try {
                        LocalDate date = LocalDate.parse(dateText);
                        System.out.println("     Tasks on " + date + ":");
                        int matchingTasks = 0;
                        for (Task task : tasks) {
                            if ((task instanceof Deadline && ((Deadline) task).occursOn(date))
                                    || (task instanceof Event && ((Event) task).occursOn(date))) {
                                matchingTasks++;
                                System.out.println("     " + matchingTasks + "." + task);
                            }
                        }
                        if (matchingTasks == 0) {
                            System.out.println("     No deadlines or events found on that date.");
                        }
                    } catch (DateTimeParseException exception) {
                        throw new MurphyException("Please enter the date as yyyy-MM-dd, like: 2019-10-15");
                    }
                } else if (parsedCommand.command() == Parser.Command.LIST) {
                    System.out.println("     Here are the tasks in your list:");
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println("     " + (i + 1) + "." + tasks.get(i));
                    }
                } else if (parsedCommand.command() == Parser.Command.DELETE) {
                    String taskNumber = parsedCommand.argument();
                    try {
                        int taskIndex = Integer.parseInt(taskNumber) - 1;
                        if (taskIndex < 0 || taskIndex >= tasks.size()) {
                            System.out.println("     I couldn't find that task. Please choose a number from 1 to "
                                    + tasks.size() + ".");
                        } else {
                            Task deletedTask = tasks.remove(taskIndex);
                            storage.save(tasks);
                            System.out.println("     Noted. I've removed this task:");
                            System.out.println("       " + deletedTask);
                            System.out.println("     Now you have " + tasks.size() + " tasks in the list.");
                        }
                    } catch (NumberFormatException exception) {
                        System.out.println("     Please tell me which task number to delete, like: delete 2");
                    }
                } else if (parsedCommand.command() == Parser.Command.MARK) {
                    String taskNumber = parsedCommand.argument();
                    try {
                        int taskIndex = Integer.parseInt(taskNumber) - 1;
                        if (taskIndex < 0 || taskIndex >= tasks.size()) {
                            System.out.println("     I couldn't find that task. Please choose a number from 1 to "
                                    + tasks.size() + ".");
                        } else {
                            tasks.get(taskIndex).markAsDone();
                            storage.save(tasks);
                            System.out.println("     Nice! I've marked this task as done:");
                            System.out.println("       [X] " + tasks.get(taskIndex).getDescription());
                        }
                    } catch (NumberFormatException exception) {
                        System.out.println("     Please tell me which task number to mark, like: mark 2");
                    }
                } else if (parsedCommand.command() == Parser.Command.UNMARK) {
                    String taskNumber = parsedCommand.argument();
                    try {
                        int taskIndex = Integer.parseInt(taskNumber) - 1;
                        if (taskIndex < 0 || taskIndex >= tasks.size()) {
                            System.out.println("     I couldn't find that task. Please choose a number from 1 to "
                                    + tasks.size() + ".");
                        } else {
                            tasks.get(taskIndex).markAsNotDone();
                            storage.save(tasks);
                            System.out.println("     OK, I've marked this task as not done yet:");
                            System.out.println("       [ ] " + tasks.get(taskIndex).getDescription());
                        }
                    } catch (NumberFormatException exception) {
                        System.out.println("     Please tell me which task number to unmark, like: unmark 2");
                    }
                } else if (parsedCommand.command() == Parser.Command.TODO && tasks.size() < MAX_TASKS) {
                    String trimmedCommand = parsedCommand.originalText();
                    String description = trimmedCommand.length() > "todo".length()
                            ? trimmedCommand.substring("todo".length()).trim() : "";
                    if (description.isEmpty()) {
                        throw new MurphyException("A todo needs a description. Try: todo buy groceries");
                    }
                    tasks.add(new Todo(description));
                    storage.save(tasks);
                    printAddedTask(tasks.get(tasks.size() - 1), tasks.size());
                } else if (parsedCommand.command() == Parser.Command.DEADLINE && tasks.size() < MAX_TASKS) {
                    String input = parsedCommand.argument();
                    int marker = input.indexOf(" /by ");
                    if (marker < 0 || input.substring(0, marker).trim().isEmpty()
                            || input.substring(marker + 5).trim().isEmpty()) {
                        throw new MurphyException("A deadline needs a description and a date/time, like: "
                                + "deadline submit report /by 2019-10-15");
                    } else {
                        String dateText = input.substring(marker + 5).trim();
                        try {
                            tasks.add(new Deadline(input.substring(0, marker).trim(), LocalDate.parse(dateText)));
                        } catch (DateTimeParseException exception) {
                            throw new MurphyException("Please enter the deadline date as yyyy-MM-dd, like: 2019-10-15");
                        }
                        storage.save(tasks);
                        printAddedTask(tasks.get(tasks.size() - 1), tasks.size());
                    }
                } else if (parsedCommand.command() == Parser.Command.EVENT && tasks.size() < MAX_TASKS) {
                    String input = parsedCommand.argument();
                    int fromMarker = input.indexOf(" /from ");
                    int toMarker = input.indexOf(" /to ", fromMarker + 7);
                    if (fromMarker < 0 || toMarker < 0 || input.substring(0, fromMarker).trim().isEmpty()
                            || input.substring(fromMarker + 7, toMarker).trim().isEmpty()
                            || input.substring(toMarker + 5).trim().isEmpty()) {
                        throw new MurphyException("An event needs a description, start, and end time, like: "
                                + "event meeting /from 2pm /to 4pm");
                    } else {
                        tasks.add(new Event(input.substring(0, fromMarker).trim(),
                                input.substring(fromMarker + 7, toMarker).trim(), input.substring(toMarker + 5).trim()));
                        storage.save(tasks);
                        printAddedTask(tasks.get(tasks.size() - 1), tasks.size());
                    }
                } else if (tasks.size() >= MAX_TASKS) {
                    System.out.println("     I can't remember more than " + MAX_TASKS
                            + " tasks. My memory has reached its fixed-size finale.");
                } else {
                    throw new MurphyException("I don't recognise that command. Try todo, deadline, event, list, on, delete, mark, or unmark.");
                }
                } catch (MurphyException exception) {
                    System.out.println("     OOPS! " + exception.getMessage());
                }
                ui.showSeparator();
            }
        }
    }

    /** Prints the confirmation shown after a task is successfully added. */
    private static void printAddedTask(Task task, int taskCount) {
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + task);
        System.out.println("     Now you have " + taskCount + " tasks in the list.");
    }
}
