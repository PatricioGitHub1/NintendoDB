import java.net.URL;
import java.util.ResourceBundle;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

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
        
        // Generar elementos
        listElements(itemControllerMobile1);

        // Accion para volver atras
        itemControllerMobile1.backbutton.setOnAction(e -> {
            UtilsViews.setViewAnimating("Mobile0");
        });

        // Cambio a la vista 1
        UtilsViews.setViewAnimating("Mobile1");
    }

    // Se cargan los diferentes elementos y se añade la interaccion
    public void listElements(ControllerMobile1 ctrlMobile1) {
        // Pantalla espera
        showLoading(ctrlMobile1);
        
        AppData appData = AppData.getInstance();
        appData.load(currentInfoType, (result) -> {
            if (result == null) {
                System.out.println("ControllerDesktop: Error loading.");
            } else {
                try {
                    showList(currentInfoType, ctrlMobile1);
                } catch (Exception e) {
                    System.out.println("ControllerDesktop: Error showing list.");
                }
            }   
        });
    }

    public void showList(String opcioCarregada, ControllerMobile1 ctrlMobile1) throws Exception{

        // Si s'ha carregat una altra opció, no cal fer res
        // (perquè el callback pot arribar després de que l'usuari hagi canviat d'opció)
        // MIRAR SI QUITAR
        String opcioSeleccionada = currentInfoType;
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
        ctrlMobile1.loadOptions();

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
                final int index = i;
                itemTemplate.setOnMouseClicked(event -> {
                    System.out.println("SE ESCOGIO "+nom);

                    //Conseguimos controlador de template 2
                    ControllerMobile2 itemControllerMobile2 = (ControllerMobile2) UtilsViews.getController("Mobile2");
                    //Usamos funcion para cargar info del elemento
                    showInfo(currentInfoType, index, itemControllerMobile2);
                    // Accion para volver atras
                    itemControllerMobile2.backButton.setOnAction(e -> {
                        UtilsViews.setViewAnimating("Mobile1");
                    });
                    // Accion para canviar vista
                    UtilsViews.setViewAnimating("Mobile2");
                });

                
                ctrlMobile1.addEntry(itemTemplate);;

            }
        }
    }

    void showInfo(String type, int index, ControllerMobile2 ctrlMobile2) {
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
        ctrlMobile2.clearInfo();

        // Carregar la llista amb les dades dependiendo de que tipo sea personaje, juego o consola
        try {
            FXMLLoader loader = new FXMLLoader(resource);
            Parent itemTemplate = loader.load();
            ControllerInfoItem itemController = loader.getController();
            //Conseguir que el titulo del banner sea el nombre del elemento
            ctrlMobile2.setCharacterTitle(dades.getString("nom"));

            // Rellenar la info en funcion de que tipo de elemento sea
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
        ctrlMobile2.addInfo(itemTemplate);

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

    public void showLoading(ControllerMobile1 cMobile1) {
        // Esborrar la llista actual
        cMobile1.loadOptions();

        // Afegeix un indicador de progrés com a primer element de la llista
        ProgressIndicator progressIndicator = new ProgressIndicator();
        cMobile1.addEntry(progressIndicator);
    }
}
