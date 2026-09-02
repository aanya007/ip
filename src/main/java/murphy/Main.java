package murphy;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** Hosts Murphy's JavaFX user interface. */
public class Main extends Application {
    /** Loads the FXML view and displays the primary window. */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.setTitle("Murphy");
        stage.setMinWidth(417);
        stage.setMinHeight(220);
        stage.show();
    }
}
