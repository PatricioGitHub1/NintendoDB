import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class ControllerListItem {
  @FXML
  private ImageView img;

  @FXML
  private Label text = new Label();

  public void setText(String text) {
    // Estableix el contingut del Label
    this.text.setText(text);
  }

  public void setImage(String resourceName) {
    // Obté una referència al recurs dins del .jar
    ClassLoader classLoader = getClass().getClassLoader();
    Image image = new Image(classLoader.getResourceAsStream(resourceName));

    // Estableix la imatge a l'ImageView
    img.setImage(image);
  }
}

