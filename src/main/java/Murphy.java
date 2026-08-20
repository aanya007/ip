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

        String[] tasks = new String[MAX_TASKS];
        boolean[] completed = new boolean[MAX_TASKS];
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
                        String status = completed[i] ? "X" : " ";
                        System.out.println("     " + (i + 1) + ".[" + status + "] " + tasks[i]);
                    }
                } else if (command.trim().toLowerCase().startsWith("mark ")) {
                    String taskNumber = command.trim().substring("mark ".length()).trim();
                    try {
                        int taskIndex = Integer.parseInt(taskNumber) - 1;
                        if (taskIndex < 0 || taskIndex >= taskCount) {
                            System.out.println("     I couldn't find that task. Please choose a number from 1 to "
                                    + taskCount + ".");
                        } else {
                            completed[taskIndex] = true;
                            System.out.println("     Nice! I've marked this task as done:");
                            System.out.println("       [X] " + tasks[taskIndex]);
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
                            completed[taskIndex] = false;
                            System.out.println("     OK, I've marked this task as not done yet:");
                            System.out.println("       [ ] " + tasks[taskIndex]);
                        }
                    } catch (NumberFormatException exception) {
                        System.out.println("     Please tell me which task number to unmark, like: unmark 2");
                    }
                } else if (taskCount < MAX_TASKS) {
                    tasks[taskCount] = command;
                    taskCount++;
                    System.out.println("     added: " + command);
                } else {
                    System.out.println("     I can't remember more than " + MAX_TASKS
                            + " tasks. My memory has reached its fixed-size finale.");
                }
                System.out.println(separator);
            }
        }
    }
}
