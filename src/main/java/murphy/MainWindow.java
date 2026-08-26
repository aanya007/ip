package murphy;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/** Controller for Murphy's main chat window. */
public class MainWindow extends AnchorPane {
    @FXML private ScrollPane scrollPane;
    @FXML private VBox dialogContainer;
    @FXML private TextField userInput;
    private final Duke duke = new Duke();
    private final Image userImage = new Image(Main.class.getResourceAsStream("/images/user.svg"));
    private final Image murphyImage = new Image(Main.class.getResourceAsStream("/images/murphy.svg"));

    /** Binds scrolling to the growing conversation. */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Adds the user's message and Murphy's response to the conversation. */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        dialogContainer.getChildren().addAll(DialogBox.getUserDialog(input, userImage),
                DialogBox.getMurphyDialog(duke.getResponse(input), murphyImage));
        userInput.clear();
    }
}
