package duke.controller;

import duke.tasks.TaskList;
import duke.ui.DataSaver;
import duke.utils.Evaluator;
import duke.utils.Storage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Encapsulates the MainWindow class, representing the main window for the program.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Evaluator evaluator;
    private Storage storage;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/DaUser.png"));
    private Image dukeImage = new Image(this.getClass().getResourceAsStream("/images/DaDuke.png"));

    /**
     * Initialize the MainWindow class properties.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        TaskList list = new TaskList();
        storage = new Storage(list);
        evaluator = new Evaluator(storage);
        loadFile();
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Duke's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = evaluator.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getDukeDialog(response, dukeImage)
        );
        userInput.clear();
    }

    /**
     * Saves the data to a load file.
     */
    @FXML
    private void handleSaveFile() {
        DataSaver.save(storage);
        if (DataSaver.isQuitting()) {
            Platform.exit();
        }
    }

    /**
     * Loads data from a load file.
     */
    private void loadFile() {
        String readFileMessage = storage.readSavedFile();
        dialogContainer.getChildren().add(DialogBox.getDukeDialog(readFileMessage, dukeImage));
        dialogContainer.setAlignment(Pos.CENTER);
    }
}