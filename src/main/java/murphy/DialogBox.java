package murphy;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import java.io.IOException;

/** A reusable chat bubble containing an avatar and a message. */
public class DialogBox extends HBox {
    @FXML
    private Label dialog;
    @FXML
    private ImageView displayPicture;

    private DialogBox(String text, Image image) {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/DialogBox.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load the dialog box layout", exception);
        }
        dialog.setText(text);
        displayPicture.setImage(image);
    }

    /** Creates a right-aligned bubble for user text. */
    public static DialogBox getUserDialog(String text, Image image) {
        return new DialogBox(text, image);
    }

    /** Creates a left-aligned bubble for Murphy's response. */
    public static DialogBox getMurphyDialog(String text, Image image) {
        DialogBox dialogBox = new DialogBox(text, image);
        dialogBox.getStyleClass().add("reply-box");
        dialogBox.getChildren().clear();
        dialogBox.getChildren().addAll(dialogBox.displayPicture, dialogBox.dialog);
        return dialogBox;
    }
}
