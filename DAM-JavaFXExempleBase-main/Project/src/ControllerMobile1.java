import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ControllerMobile1 {
    @FXML
    private Label category;

    @FXML
    private VBox options;

    @FXML
    Button backbutton;

    public void setTypeTitle(String type) {
        category.setText(type);
    }

    public void loadOptions() {
        options.getChildren().clear();
    }

    public void addEntry(Parent item) {
        options.getChildren().add(item);
    }
}


