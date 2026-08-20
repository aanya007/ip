import java.util.Scanner;

/**
 * A friendly command-line chatbot that stores user-entered tasks in memory.
 */
public class Murphy {
    /** The maximum number of tasks Murphy can remember during one run. */
    private static final int MAX_TASKS = 100;

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

        Task[] tasks = new Task[MAX_TASKS];
        int taskCount = 0;

        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                String command = scanner.nextLine();

                System.out.println(separator);

                if (command.trim().equalsIgnoreCase("bye")) {
                    System.out.println("Bye. Hope to see you again soon! Even command lines need a punchline.");
                    System.out.println(separator);
                    break;
                }

                if (command.trim().equalsIgnoreCase("list")) {
                    System.out.println("     Here are the tasks in your list:");
                    for (int i = 0; i < taskCount; i++) {
                        System.out.println("     " + (i + 1) + ".[" + tasks[i].getType().getSymbol() + "]["
                                + tasks[i].getStatusIcon() + "] " + tasks[i].getDisplayDescription());
                    }
                } else if (command.trim().toLowerCase().startsWith("mark ")) {
                    String taskNumber = command.trim().substring("mark ".length()).trim();
                    try {
                        int taskIndex = Integer.parseInt(taskNumber) - 1;
                        if (taskIndex < 0 || taskIndex >= taskCount) {
                            System.out.println("     I couldn't find that task. Please choose a number from 1 to "
                                    + taskCount + ".");
                        } else {
                            tasks[taskIndex].markAsDone();
                            System.out.println("     Nice! I've marked this task as done:");
                            System.out.println("       [X] " + tasks[taskIndex].getDescription());
                        }
                    } catch (NumberFormatException exception) {
                        System.out.println("     Please tell me which task number to mark, like: mark 2");
                    }
                } else if (command.trim().toLowerCase().startsWith("unmark ")) {
                    String taskNumber = command.trim().substring("unmark ".length()).trim();
                    try {
                        int taskIndex = Integer.parseInt(taskNumber) - 1;
                        if (taskIndex < 0 || taskIndex >= taskCount) {
                            System.out.println("     I couldn't find that task. Please choose a number from 1 to "
                                    + taskCount + ".");
                        } else {
                            tasks[taskIndex].markAsNotDone();
                            System.out.println("     OK, I've marked this task as not done yet:");
                            System.out.println("       [ ] " + tasks[taskIndex].getDescription());
                        }
                    } catch (NumberFormatException exception) {
                        System.out.println("     Please tell me which task number to unmark, like: unmark 2");
                    }
                } else if (command.trim().toLowerCase().startsWith("todo ") && taskCount < MAX_TASKS) {
                    String description = command.trim().substring("todo ".length()).trim();
                    tasks[taskCount] = new Task(Task.Type.TODO, description, null, null);
                    taskCount++;
                    printAddedTask(tasks[taskCount - 1], taskCount);
                } else if (command.trim().toLowerCase().startsWith("deadline ") && taskCount < MAX_TASKS) {
                    String input = command.trim().substring("deadline ".length()).trim();
                    int marker = input.indexOf(" /by ");
                    if (marker < 0) {
                        System.out.println("     A deadline needs a date/time, like: deadline submit report /by Friday");
                    } else {
                        tasks[taskCount] = new Task(Task.Type.DEADLINE, input.substring(0, marker).trim(),
                                null, input.substring(marker + 5).trim());
                        taskCount++;
                        printAddedTask(tasks[taskCount - 1], taskCount);
                    }
                } else if (command.trim().toLowerCase().startsWith("event ") && taskCount < MAX_TASKS) {
                    String input = command.trim().substring("event ".length()).trim();
                    int fromMarker = input.indexOf(" /from ");
                    int toMarker = input.indexOf(" /to ", fromMarker + 7);
                    if (fromMarker < 0 || toMarker < 0) {
                        System.out.println("     An event needs a start and end time, like: event meeting /from 2pm /to 4pm");
                    } else {
                        tasks[taskCount] = new Task(Task.Type.EVENT, input.substring(0, fromMarker).trim(),
                                input.substring(fromMarker + 7, toMarker).trim(), input.substring(toMarker + 5).trim());
                        taskCount++;
                        printAddedTask(tasks[taskCount - 1], taskCount);
                    }
                } else if (taskCount >= MAX_TASKS) {
                    System.out.println("     I can't remember more than " + MAX_TASKS
                            + " tasks. My memory has reached its fixed-size finale.");
                } else {
                    System.out.println("     I understand todo, deadline, event, list, mark, and unmark commands.");
                }
                System.out.println(separator);
            }
        }
    }

    /** Prints the confirmation shown after a task is successfully added. */
    private static void printAddedTask(Task task, int taskCount) {
        System.out.println("     Got it. I've added this task:");
        System.out.println("       [" + task.getType().getSymbol() + "][ ] " + task.getDisplayDescription());
        System.out.println("     Now you have " + taskCount + " tasks in the list.");
    }
}
