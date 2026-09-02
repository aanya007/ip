package murphy;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/** JavaFX presentation for Murphy's task chatbot. */
public class MurphyApplication extends Application {
    private final MurphyService service = new MurphyService();
    private final VBox messages = new VBox(12);
    private final Label taskCount = new Label();

    /** Builds and displays Murphy's main window. */
    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("root-pane");
        root.setTop(createHeader());
        messages.getStyleClass().add("messages");
        addMessage("MURPHY", "hey. give me something to remember.", false);
        ScrollPane conversation = new ScrollPane(messages);
        conversation.setFitToWidth(true);
        conversation.getStyleClass().add("conversation");
        root.setCenter(conversation);
        root.setRight(createSidebar());
        root.setBottom(createInputArea());
        Scene scene = new Scene(root, 1000, 700);
        scene.getStylesheets().add(getClass().getResource("/murphy.css").toExternalForm());
        stage.setTitle("Murphy // Personal Task Archive");
        stage.setMinWidth(760);
        stage.setMinHeight(520);
        stage.setScene(scene);
        stage.show();
    }

    private VBox createHeader() { Label title = new Label("MURPHY"); title.getStyleClass().add("title"); Label subtitle = new Label("PERSONAL TASK ARCHIVE"); subtitle.getStyleClass().add("subtitle"); Label status = new Label("● ONLINE"); status.getStyleClass().add("status"); HBox row = new HBox(18, title, subtitle); row.setAlignment(Pos.BASELINE_LEFT); VBox header = new VBox(4, row, status); header.getStyleClass().add("header"); return header; }
    private VBox createSidebar() { taskCount.setText(taskSummary()); taskCount.getStyleClass().add("task-count"); Label heading = new Label("TASKS"); heading.getStyleClass().add("side-heading"); VBox side = new VBox(16, heading, taskCount); side.getStyleClass().add("sidebar"); return side; }
    private HBox createInputArea() { TextField input = new TextField(); input.setPromptText("type a command..."); Button send = new Button("SEND"); Runnable submit = () -> { String command = input.getText().trim(); if (!command.isEmpty()) { addMessage("YOU", command, true); String response = service.respond(command); addMessage("MURPHY", response, false); taskCount.setText(taskSummary()); input.clear(); } }; send.setOnAction(event -> submit.run()); input.setOnAction(event -> submit.run()); HBox area = new HBox(10, input, send); HBox.setHgrow(input, javafx.scene.layout.Priority.ALWAYS); area.getStyleClass().add("input-area"); return area; }
    private void addMessage(String sender, String text, boolean user) { Label name = new Label(sender); name.getStyleClass().add("message-sender"); Label body = new Label(text); body.setWrapText(true); body.getStyleClass().add(user ? "user-bubble" : "murphy-bubble"); VBox bubble = new VBox(4, name, body); bubble.setMaxWidth(620); bubble.setAlignment(user ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT); messages.getChildren().add(bubble); }
    private String taskSummary() { long done = service.getTasks().asList().stream().filter(task -> task.isDone()).count(); return "TOTAL  " + service.getTasks().size() + "\nDONE   " + done + "\nOPEN   " + (service.getTasks().size() - done); }
}
