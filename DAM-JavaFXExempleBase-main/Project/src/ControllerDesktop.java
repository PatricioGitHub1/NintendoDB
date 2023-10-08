import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.fxml.Initializable;


public class ControllerDesktop implements Initializable{
    String opcions[] = {"Personatges", "Jocs", "Consoles"};
    @FXML
    private ChoiceBox<String> choiceBox;

    @FXML
    private VBox yPane;

    @FXML
    private AnchorPane info;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Opciones del choiceBox
        choiceBox.getItems().addAll(opcions);
        // Pillar primera opcion
        choiceBox.setValue(opcions[0]);
        // Callback cuando user escoge una opcion
        choiceBox.setOnAction((event) -> { loadList(); });
        // Carga automatica de ‘Personatges’
        loadList();

    }
    public void loadList() {
        // Saber opcion seleccionada
        String opcio = choiceBox.getValue();

        // Referencia AppData para gestionar dades
        AppData appData = AppData.getInstance();

        // Para mostrar mensage de carga
        showLoading();

        // Pedir los datos
        appData.load(opcio, (result) -> {
            if (result == null) {
                System.out.println("ControllerDesktop: Error loading.");
            } else {
                try {
                    showList(opcio);
                } catch (Exception e) {
                    System.out.println("ControllerDesktop: Error showing list.");
                }
            }   
        });
        
    }
    
    public void showList(String opcioCarregada) throws Exception{

        // Si s'ha carregat una altra opció, no cal fer res
        // (perquè el callback pot arribar després de que l'usuari hagi canviat d'opció)
        String opcioSeleccionada = choiceBox.getValue();
        if (opcioCarregada.compareTo(opcioSeleccionada) != 0) {
            return;
        }

        // Obtenir una referència a l'ojecte AppData que gestiona les dades
        AppData appData = AppData.getInstance();

        // Obtenir les dades de l'opció seleccionada
        JSONArray dades = appData.getData(opcioCarregada);
        // Esborrar la llista actual
        // Carregar la plantilla
        URL resource = this.getClass().getResource("assets/listItem.fxml");

        // Esborrar la llista actual
        yPane.getChildren().clear();

        // Carregar la llista amb les dades
        for (int i = 0; i < dades.length(); i++) {
            JSONObject consoleObject = dades.getJSONObject(i);

            if (consoleObject.has("nom")) {
                String nom = consoleObject.getString("nom");
                String imatge = "assets/images/" + consoleObject.getString("imatge");
                FXMLLoader loader = new FXMLLoader(resource);
                Parent itemTemplate = loader.load();
                ControllerListItem itemController = loader.getController();
                itemController.setText(nom);
                itemController.setImage(imatge);

                // Defineix el callback que s'executarà quan l'usuari seleccioni un element
                // (cal passar final perquè es pugui accedir des del callback)
                final String type = opcioSeleccionada;
                final int index = i;
                itemTemplate.setOnMouseClicked(event -> {
                    showInfo(type, index);
                });

                
                yPane.getChildren().add(itemTemplate);

            }
        }
    }

    public void showLoading() {
        // Esborrar la llista actual
        yPane.getChildren().clear();

        // Afegeix un indicador de progrés com a primer element de la llista
        ProgressIndicator progressIndicator = new ProgressIndicator();
        yPane.getChildren().add(progressIndicator);
    }

    void showInfo(String type, int index) {
        // Obtenir una referència a l'ojecte AppData que gestiona les dades
        AppData appData = AppData.getInstance();

        // Obtenir les dades de l'opció seleccionada
        JSONObject dades = appData.getItemData(type, index);

        // Carregar la plantilla en funcion del tipo
        URL resource = this.getClass().getResource("assets/template_info_item.fxml");;
        switch (type) {
            case "Consoles":
                resource = this.getClass().getResource("assets/template_info_consola.fxml"); 
                break;
            case "Jocs":
                resource = this.getClass().getResource("assets/templato_info_joc.fxml");
                break;
            case "Personatges":
                resource = this.getClass().getResource("assets/template_info_personatge.fxml");
                
                break;
            default:
                break;
        }

        // Esborrar la informació actual
        info.getChildren().clear();

        // Carregar la llista amb les dades dependiendo de que tipo sea personaje, juego o consola
        try {
            FXMLLoader loader = new FXMLLoader(resource);
            Parent itemTemplate = loader.load();
            ControllerInfoItem itemController = loader.getController();
            itemController.setImage("assets/images/" + dades.getString("imatge"));
            itemController.setTitle(dades.getString("nom"));
            switch (type) {
            case "Consoles": 
                itemController.setText(dades.getString("procesador"));
                itemController.setRectangleColor(getColor(dades.getString("color")));
                itemController.setYear(dades.getString("data"));
                itemController.setProcessor(dades.getString("procesador"));
                itemController.setSales(dades.getInt("venudes"));
                break;
            case "Jocs": 
                itemController.setYear(Integer.toString(dades.getInt("any")));
                itemController.setGenre(dades.getString("tipus"));
                itemController.setText(dades.getString("descripcio")); 
                break;
            case "Personatges": 
                itemController.setText(dades.getString("nom_del_videojoc")); 
                itemController.setRectangleColor(getColor(dades.getString("color")));
                break;
        }

        // Afegeix la informació a la vista
        info.getChildren().add(itemTemplate);

        // Estableix que la mida de itemTemplaate s'ajusti a la mida de info
        AnchorPane.setTopAnchor(itemTemplate, 0.0);
        AnchorPane.setRightAnchor(itemTemplate, 0.0);
        AnchorPane.setBottomAnchor(itemTemplate, 0.0);
        AnchorPane.setLeftAnchor(itemTemplate, 0.0);

        } catch (Exception e) {
        System.out.println("ControllerDesktop: Error showing info.");
        System.out.println(e);
        }
    }

    // Funcion para sacar el Color
    Color getColor (String colorName) {
        switch (colorName) {
            case "red":
                return Color.RED;
            case "green":
                return Color.GREEN;
            case "pink":
                return Color.PINK;
            case "orange":
                return Color.ORANGE;
            case "brown":
                return Color.BROWN;
            case "yellow":
                return Color.YELLOW;
            case "grey":
                return Color.GREY;
            case "black":
                return Color.BLACK;
            case "white":
                return Color.rgb(253, 239, 239);
            case "purple":
                return Color.PURPLE;
            default:
                return Color.AQUAMARINE;
        }
    }
}

