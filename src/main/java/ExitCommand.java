/** A command that ends Murphy's conversation. */
public class ExitCommand extends Command {
    /** Displays the farewell message. */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showMessage("Bye. Hope to see you again soon! Even command lines need a punchline.");
    }

    /** Indicates that Murphy should stop after this command. */
    @Override
    public boolean isExit() {
        return true;
    }
}
