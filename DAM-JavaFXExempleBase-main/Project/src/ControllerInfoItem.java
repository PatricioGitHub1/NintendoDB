import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ControllerInfoItem {
  @FXML
  private ImageView img;

  @FXML
  private Label title = new Label();

  @FXML
  private Label year = new Label();

  @FXML
  private Label genre = new Label();

  @FXML
  private Label text = new Label();

  @FXML
  private Label processor = new Label();

  @FXML
  private Label sales = new Label();

  @FXML
  private Rectangle itemcolor = new Rectangle();

  public void setImage(String resourceName) {

    // Obté una referència al recurs dins del .jar
    ClassLoader classLoader = getClass().getClassLoader();
    Image image = new Image(classLoader.getResourceAsStream(resourceName));

    // Estableix la imatge a l'ImageView
    img.setImage(image);
  }
  public void setTitle(String text) {

    // Estableix el contingut del Label
    this.title.setText(text);
  }

  public void setText(String text) {

    // Estableix el contingut del Label
    this.text.setText(text);
  }

  // Contingut del quadrat de color
  public void setRectangleColor (Color color) {
    this.itemcolor.setFill(color);
  }

  // Contingut de l'any
  public void setYear(String year) {
    this.year.setText(year);
  }

  // Contingut del genere del videojoc
  public void setGenre(String genre) {
    this.genre.setText(genre);
  }

  // Contingut del processador
  public void setProcessor(String processor) {
    this.processor.setText(processor);
  }

  // Contingut de los unitats venudes
  public void setSales(int sales) {
    this.sales.setText(Integer.toString(sales)+" venudes");
  }
}
