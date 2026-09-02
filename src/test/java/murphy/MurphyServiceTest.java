package murphy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Tests Murphy's UI-independent command processing. */
public class MurphyServiceTest {
    @Test
    public void respond_addsAndListsTodo() throws Exception {
        MurphyService service = new MurphyService(Files.createTempDirectory("murphy-test").resolve("tasks.txt"));

        String response = service.respond("todo submit assignment");

        assertTrue(response.contains("I've added this task"));
        assertEquals(1, service.getTasks().size());
        assertTrue(service.respond("list").contains("submit assignment"));
    }

    @Test
    public void respond_marksAndFindsTask() throws Exception {
        MurphyService service = new MurphyService(Files.createTempDirectory("murphy-test").resolve("tasks.txt"));
        service.respond("todo Read JavaFX guide");

        assertTrue(service.respond("find javafx").contains("Read JavaFX guide"));
        service.respond("mark 1");
        assertTrue(service.getTasks().get(0).isDone());
    }

    @Test
    public void respond_rejectsInvalidCommandArgument() throws Exception {
        MurphyService service = new MurphyService(Files.createTempDirectory("murphy-test").resolve("tasks.txt"));

        assertTrue(service.respond("deadline report /by not-a-date").startsWith("OOPS!"));
        assertEquals(0, service.getTasks().size());
    }
}
