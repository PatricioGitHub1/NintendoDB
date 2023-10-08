import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class ControllerMobile2 {
    @FXML
    private Label characterName;
    @FXML
    private AnchorPane info;
    @FXML
    Button backButton;

    public void setCharacterTitle(String name) {
        characterName.setText(name);
    }

    public void clearInfo() {
        info.getChildren().clear();
    }

    public void addInfo(Parent data) {
        info.getChildren().addAll(data);
    }
}

