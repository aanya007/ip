import java.util.Scanner;

/**
 * A friendly command-line chatbot that echoes user commands until it hears
 * the command {@code bye}.
 */
public class Murphy {
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

        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                String command = scanner.nextLine();

                System.out.println(separator);

                if (command.trim().equalsIgnoreCase("bye")) {
                    System.out.println("Bye. Hope to see you again soon! Even command lines need a punchline.");
                    System.out.println(separator);
                    break;
                }

                System.out.println("     " + command);
                System.out.println(separator);
            }
        }
    }
}
