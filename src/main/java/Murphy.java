import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
        String separator = "____________________________________________________________";
        String banner = "M   M  U   U  RRRR   PPPP   H   H  Y   Y\n"
                + "MM MM  U   U  R   R  P   P  H   H   Y Y\n"
                + "M M M  U   U  RRRR   PPPP   HHHHH    Y\n"
                + "M   M  U   U  R  R   P      H   H    Y\n"
                + "M   M   UUU   R   R  P      H   H    Y\n";

        System.out.println(separator);
        System.out.println(banner + "Hi there! I'm Murphy, your command-line conversationalist.\n"
                + "What can I do for you? (I promise not to judge your typing.)");
        System.out.println(separator);

        List<Task> tasks = loadTasks();

        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                String command = scanner.nextLine();

                System.out.println(separator);

                try {
                    if (command.trim().equalsIgnoreCase("bye")) {
                        System.out.println("Bye. Hope to see you again soon! Even command lines need a punchline.");
                        System.out.println(separator);
                        break;
                    }

                    if (command.trim().toLowerCase().startsWith("on ")) {
                    String dateText = command.trim().substring("on ".length()).trim();
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
                } else if (command.trim().equalsIgnoreCase("list")) {
                    System.out.println("     Here are the tasks in your list:");
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println("     " + (i + 1) + "." + tasks.get(i));
                    }
                } else if (command.trim().toLowerCase().startsWith("delete ")) {
                    String taskNumber = command.trim().substring("delete ".length()).trim();
                    try {
                        int taskIndex = Integer.parseInt(taskNumber) - 1;
                        if (taskIndex < 0 || taskIndex >= tasks.size()) {
                            System.out.println("     I couldn't find that task. Please choose a number from 1 to "
                                    + tasks.size() + ".");
                        } else {
                            Task deletedTask = tasks.remove(taskIndex);
                            saveTasks(tasks);
                            System.out.println("     Noted. I've removed this task:");
                            System.out.println("       " + deletedTask);
                            System.out.println("     Now you have " + tasks.size() + " tasks in the list.");
                        }
                    } catch (NumberFormatException exception) {
                        System.out.println("     Please tell me which task number to delete, like: delete 2");
                    }
                } else if (command.trim().toLowerCase().startsWith("mark ")) {
                    String taskNumber = command.trim().substring("mark ".length()).trim();
                    try {
                        int taskIndex = Integer.parseInt(taskNumber) - 1;
                        if (taskIndex < 0 || taskIndex >= tasks.size()) {
                            System.out.println("     I couldn't find that task. Please choose a number from 1 to "
                                    + tasks.size() + ".");
                        } else {
                            tasks.get(taskIndex).markAsDone();
                            saveTasks(tasks);
                            System.out.println("     Nice! I've marked this task as done:");
                            System.out.println("       [X] " + tasks.get(taskIndex).getDescription());
                        }
                    } catch (NumberFormatException exception) {
                        System.out.println("     Please tell me which task number to mark, like: mark 2");
                    }
                } else if (command.trim().toLowerCase().startsWith("unmark ")) {
                    String taskNumber = command.trim().substring("unmark ".length()).trim();
                    try {
                        int taskIndex = Integer.parseInt(taskNumber) - 1;
                        if (taskIndex < 0 || taskIndex >= tasks.size()) {
                            System.out.println("     I couldn't find that task. Please choose a number from 1 to "
                                    + tasks.size() + ".");
                        } else {
                            tasks.get(taskIndex).markAsNotDone();
                            saveTasks(tasks);
                            System.out.println("     OK, I've marked this task as not done yet:");
                            System.out.println("       [ ] " + tasks.get(taskIndex).getDescription());
                        }
                    } catch (NumberFormatException exception) {
                        System.out.println("     Please tell me which task number to unmark, like: unmark 2");
                    }
                } else if (command.trim().toLowerCase().startsWith("todo") && tasks.size() < MAX_TASKS) {
                    String trimmedCommand = command.trim();
                    String description = trimmedCommand.length() > "todo".length()
                            ? trimmedCommand.substring("todo".length()).trim() : "";
                    if (description.isEmpty()) {
                        throw new MurphyException("A todo needs a description. Try: todo buy groceries");
                    }
                    tasks.add(new Todo(description));
                    saveTasks(tasks);
                    printAddedTask(tasks.get(tasks.size() - 1), tasks.size());
                } else if (command.trim().toLowerCase().startsWith("deadline ") && tasks.size() < MAX_TASKS) {
                    String input = command.trim().substring("deadline ".length()).trim();
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
                        saveTasks(tasks);
                        printAddedTask(tasks.get(tasks.size() - 1), tasks.size());
                    }
                } else if (command.trim().toLowerCase().startsWith("event ") && tasks.size() < MAX_TASKS) {
                    String input = command.trim().substring("event ".length()).trim();
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
                        saveTasks(tasks);
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
                System.out.println(separator);
            }
        }
    }

    /** Prints the confirmation shown after a task is successfully added. */
    private static void printAddedTask(Task task, int taskCount) {
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + task);
        System.out.println("     Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Writes the current task list to Murphy's data file.
     *
     * @param tasks tasks to save
     * @throws MurphyException if the task data cannot be written
     */
    private static void saveTasks(List<Task> tasks) throws MurphyException {
        List<String> taskData = new ArrayList<>();
        for (Task task : tasks) {
            taskData.add(task.toDataString());
        }
        try {
            Files.createDirectories(DATA_FILE_PATH.getParent());
            Files.write(DATA_FILE_PATH, taskData, StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new MurphyException("I changed the task list in memory, but couldn't save it to disk.");
        }
    }

    /**
     * Reads Murphy's task list from the data file.
     *
     * @return tasks reconstructed from the saved data
     */
    private static List<Task> loadTasks() {
        List<Task> tasks = new ArrayList<>();
        if (Files.notExists(DATA_FILE_PATH)) {
            return tasks;
        }

        try {
            List<String> lines = Files.readAllLines(DATA_FILE_PATH, StandardCharsets.UTF_8);
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.isBlank()) {
                    continue;
                }
                if (tasks.size() >= MAX_TASKS) {
                    System.out.println("     OOPS! The save file has more than " + MAX_TASKS
                            + " tasks, so I loaded only the first " + MAX_TASKS + ".");
                    break;
                }

                try {
                    tasks.add(parseTask(line));
                } catch (IllegalArgumentException exception) {
                    System.out.println("     OOPS! I skipped corrupted save-file line " + (i + 1) + ".");
                }
            }
        } catch (IOException exception) {
            System.out.println("     OOPS! I couldn't read the save file, so I'm starting with an empty list.");
            tasks.clear();
        }
        return tasks;
    }

    /** Converts one validated save-file line into a task. */
    private static Task parseTask(String line) {
        List<String> taskData = splitDataFields(line);
        if (taskData.size() < 2 || (!taskData.get(1).equals("0") && !taskData.get(1).equals("1"))) {
            throw new IllegalArgumentException("Invalid task status");
        }

        String taskType = taskData.get(0);
        int expectedFieldCount;
        if (taskType.equals("T")) {
            expectedFieldCount = 3;
        } else if (taskType.equals("D")) {
            expectedFieldCount = 4;
        } else if (taskType.equals("E")) {
            expectedFieldCount = 5;
        } else {
            throw new IllegalArgumentException("Unknown task type");
        }

        if (taskData.size() != expectedFieldCount) {
            throw new IllegalArgumentException("Incorrect field count");
        }
        for (int i = 2; i < taskData.size(); i++) {
            if (taskData.get(i).isBlank()) {
                throw new IllegalArgumentException("Missing task detail");
            }
        }

        Task task;
        if (taskType.equals("T")) {
            task = new Todo(taskData.get(2));
        } else if (taskType.equals("D")) {
            try {
                task = new Deadline(taskData.get(2), LocalDate.parse(taskData.get(3)));
            } catch (DateTimeParseException exception) {
                throw new IllegalArgumentException("Invalid deadline date", exception);
            }
        } else {
            task = new Event(taskData.get(2), taskData.get(3), taskData.get(4));
        }
        if (taskData.get(1).equals("1")) {
            task.markAsDone();
        }
        return task;
    }

    /** Splits a save-file line while preserving escaped pipes and backslashes. */
    private static List<String> splitDataFields(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char character = line.charAt(i);
            if (character == '\\' && i + 1 < line.length()
                    && (line.charAt(i + 1) == '|' || line.charAt(i + 1) == '\\')) {
                field.append(line.charAt(i + 1));
                i++;
            } else if (character == '|') {
                fields.add(field.toString().trim());
                field.setLength(0);
            } else {
                field.append(character);
            }
        }
        fields.add(field.toString().trim());
        return fields;
    }
}
