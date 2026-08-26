import java.util.ArrayList;
import java.util.List;

/** Owns Murphy's ordered collection of tasks and its basic operations. */
public class TaskList implements Iterable<Task> {
    /** The tasks currently remembered by Murphy. */
    private final List<Task> tasks;

    /** Creates an empty task list. */
    public TaskList() {
        this(new ArrayList<>());
    }

    /** Creates a task list containing the supplied tasks. */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /** Adds a task to the end of the list. */
    public void add(Task task) {
        tasks.add(task);
    }

    /** Removes and returns the task at the supplied zero-based index. */
    public Task remove(int index) {
        return tasks.remove(index);
    }

    /** Returns the task at the supplied zero-based index. */
    public Task get(int index) {
        return tasks.get(index);
    }

    /** Returns the number of remembered tasks. */
    public int size() {
        return tasks.size();
    }

    /** Removes every task from the list. */
    public void clear() {
        tasks.clear();
    }

    /** Provides the tasks for persistence without exposing the mutable list. */
    public List<Task> asList() {
        return new ArrayList<>(tasks);
    }

    /** Allows Murphy to display or search every task in order. */
    @Override
    public java.util.Iterator<Task> iterator() {
        return tasks.iterator();
    }
}
