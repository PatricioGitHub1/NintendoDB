import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class ControllerMobile0 implements Initializable{
    @FXML
    private Pane personatges;
    @FXML
    private Pane jocs;
    @FXML
    private Pane consoles;

    static String currentInfoType = "";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    // Usamos Panes como los botones para escoger categoria
        personatges.setOnMouseClicked((MouseEvent event) -> {
            System.out.println("Click en personages");
            //Variable para saber la info que se tiene que mostrar
            currentInfoType = "Personatges";
            genericActions();
            
        });

        jocs.setOnMouseClicked((MouseEvent event) -> {
            System.out.println("Click en juegos");
            currentInfoType = "Jocs";
            genericActions();
        });

        consoles.setOnMouseClicked((MouseEvent event) -> {
            System.out.println("Click en consolas");
            currentInfoType = "Consoles";
            genericActions();
        });
    }

    // Aqui se declaran las acciones que se crean en cada caso, ej: titulo de la categoria, configurar boton para volver atras...
    public void genericActions() {
        ControllerMobile1 itemControllerMobile1 = (ControllerMobile1) UtilsViews.getController("Mobile1");
        // Poner titulo de categoria
        itemControllerMobile1.setTypeTitle(currentInfoType);
        
        // Accion para volver atras
        itemControllerMobile1.backbutton.setOnAction(e -> {
            UtilsViews.setViewAnimating("Mobile0");
        });

        // Cambio a la vista 1
        UtilsViews.setViewAnimating("Mobile1");
    }
}
