package Client.javaFx;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class NeueLiteraturController {

    @FXML
    private TextField litArt;
    @FXML
    private TextField litAuthor;
    @FXML
    private TextField litTitel;
    @FXML
    private TextField litJahr;
    @FXML
    private Label currentIDLabel;
    @FXML
    private Pane pane;
    private Stage stage;
    private int userID;
    private String litString = "Manuell erstellte Literaturliste: \n" + "\n";


    //ID übernehmen und umwandeln
    public void setIDLabel(int pUID){
        Integer integer = pUID;
        currentIDLabel.setText(integer.toString());
    }

    //Methode zum initialisieren
    public void init() {
        String userIDstr = currentIDLabel.getText();
        userID = Integer.parseInt(userIDstr);
        litArt.setText("");
        litAuthor.setText("");
        litTitel.setText("");
        litJahr.setText("");
    }

    public void speichern() {
        String art = litArt.getText().trim();
        String author = litAuthor.getText().trim();
        String titel = litTitel.getText().trim();
        String jahr = litJahr.getText().trim();
        System.out.println("Literatur speichern vorher: \n" + litString);
        litString = litString + "@" + art + "{" + author + "." + jahr + ",\n" +
                " author = {" + author + "},\n" +
                " title = {" + titel + "},\n" +
                " year = {" + jahr + "},\n" +
                "}\n" +
                "\n" +
                "\n";
        System.out.println("Literatur speichern nachher: \n" + litString);
    }
    /*
    @FXML
    public void weiterenEintrag() {
        if (litArt.getText().trim().isEmpty() || litAuthor.getText().trim().isEmpty() || litTitel.getText().trim().isEmpty()) {
            Alert fehler = new Alert(Alert.AlertType.ERROR);
            fehler.setTitle("Fehler");
            fehler.setHeaderText("Bitte Eingaben überprüfen.");
            fehler.setContentText("Es muss mindestens das Textfeld Art, Author und Titel ausgefüllt sein.");
            fehler.showAndWait();
        } else {
            speichern();
            init();
        }
    }

     */

    @FXML
    public void eintragSpeichern(){
        if (litArt.getText().trim().isEmpty() || litAuthor.getText().trim().isEmpty() || litTitel.getText().trim().isEmpty()) {
            Alert fehler = new Alert(Alert.AlertType.ERROR);
            fehler.setTitle("Fehler");
            fehler.setHeaderText("Bitte Eingaben überprüfen.");
            fehler.setContentText("Es muss mindestens das Textfeld Art, Author und Titel ausgefüllt sein.");
            fehler.showAndWait();
        } else {
            speichern();
            init();
        }
    }

    @FXML
    public void literaturFertig() {
        NeuesThemaManuellController.literaturlisteAnnehmen(litString);
        stage = (Stage) pane.getScene().getWindow();
        stage.close();
    }

}