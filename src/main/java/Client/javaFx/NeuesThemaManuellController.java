package Client.javaFx;

import Server.ClientStart;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.json.simple.JSONObject;

import java.io.IOException;


public class NeuesThemaManuellController {

    @FXML
    private TextField manuellTitelThema;
    @FXML
    private TextArea manuellBeschreibungThema;
    @FXML
    private Label currentIDLabel;
    @FXML
    private Pane pane;
    private Stage stage;
    private int userID;
    private static String literaturListe;

    //ID übernehmen und umwandeln
    public void setIDLabel(int pUID){
        Integer integer = pUID;
        currentIDLabel.setText(integer.toString());
    }

    //Methode zum initialisieren
    public void init(){
        String userIDstr = currentIDLabel.getText();
        userID = Integer.parseInt(userIDstr);
    }

    public void leeren(){
        manuellTitelThema.setText("");
        manuellBeschreibungThema.setText("");
    }

    //Methode um neues Fenster zu öffnen für: Literaturliste manuell erstellen
    @FXML
    public void literaturlisteErstellen (ActionEvent klick) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/NeueLiteratur.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        NeueLiteraturController controller = loader.getController();
        controller.setIDLabel(userID);
        controller.init();
        stage.show();
        /*
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/NeueLiteratur.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        NeueLiteraturController controller = loader.getController();
        //controller.setIDLabel(userID);
        controller.init();
        stage.show();

         */
    }

    public static void literaturlisteAnnehmen(String litListe){
        literaturListe = litListe;
    }

    //Methode um zu checken, ob Thema mit diesem Namen bei diesem Lehrenden schon vorhanden ist.
    public boolean themaManuellVorhanden(String titel, int nutzerid) throws IOException {
        JSONObject json = new JSONObject();
        json.put("Methode", "holeCheckVorhandenThema");
        json.put("P1", titel);
        json.put("P2", nutzerid);
        ClientStart.verbinden.send(json);

        JSONObject json2 = ClientStart.verbinden.receiveobj();
        boolean vorhanden = (Boolean) json2.get("P1");
        if(vorhanden) {
            return true;
        }
        return false;
    }

    @FXML
    public void literaturManuellhinzufuegen(String literatur, int themenid){
        if(literatur.isEmpty()){
            System.out.println("Keine Literatur vorhanden.");
        } else {
            JSONObject json = new JSONObject();
            json.put("Methode", "literaturHinzufuegen");
            json.put("P1", themenid);
            json.put("P2", literatur);
            //System.out.println("2. bibtexAulesen start: \n" + bibtexInhalt + " :bibtexAuslesen Ende .2");
            ClientStart.verbinden.send(json);
            System.out.println("Literatur vorhanden und abgeschickt.");
        /*
        String[] inhalt = bibtexInhalt.split("@");
        for(int i = 0; i < inhalt.length; i++){
            //Einzelne Einträge zur Datenbanktabelle "Literaturliste" hinzufügen
            JSONObject json = new JSONObject();
            json.put("Methode", "literaturHinzufuegen");
            json.put("P1", themenid);
            json.put("P2", inhalt[i].substring(0, inhalt[i].length()-3));
            ClientStart.verbinden.send(json);

        }

         */
        }
    }

    @FXML
    public void themaManuellBestaetigen() throws IOException{
        String lit = literaturListe;
        if(manuellTitelThema.getText().trim().isEmpty() || manuellBeschreibungThema.getText().trim().isEmpty()){
            Alert fehler = new Alert(Alert.AlertType.ERROR);
            fehler.setTitle("Fehler");
            fehler.setHeaderText("Bitte Eingaben überprüfen.");
            fehler.setContentText("Es muss ein Titel und eine kurze Beschreibung vergeben werden.");
            fehler.showAndWait();
        } else if(themaManuellVorhanden(manuellTitelThema.getText().trim(), userID)){
            Alert fehlgeschlagen = new Alert(Alert.AlertType.ERROR);
            fehlgeschlagen.setTitle("Fehlgeschlagen");
            fehlgeschlagen.setHeaderText("Fehlgeschlagen");
            fehlgeschlagen.setContentText("Es ist bereits ein Themenangebot mit diesem Titel von Ihnen vorhanden.");
            fehlgeschlagen.showAndWait();
        } else {
            JSONObject json = new JSONObject();
            json.put("Methode", "themaHinzufuegen");
            json.put("P1", manuellTitelThema.getText().trim());
            json.put("P2", manuellBeschreibungThema.getText().trim());
            json.put("P3", userID);
            ClientStart.verbinden.send(json);

            Thema thema = new Thema(manuellTitelThema.getText().trim(), manuellBeschreibungThema.getText().trim(), userID);
            //ThemenID abrufen
            JSONObject json2 = new JSONObject();
            json2.put("Methode", "holeThemenID");
            json2.put("P1", thema.getTitel());
            json2.put("P2", thema.getUserID());
            ClientStart.verbinden.send(json2);
            JSONObject json3 = ClientStart.verbinden.receiveobj();
            int themenID = Integer.parseInt( json3.get("P1")+"");
            //String testString = "Teststring für bibtex auf Datenbank";
            if (lit != null && !lit.isEmpty()) {
                literaturManuellhinzufuegen(literaturListe, themenID);
            }
            Alert erfolgreich = new Alert(Alert.AlertType.INFORMATION);
            erfolgreich.setTitle("Erfolgreich");
            erfolgreich.setHeaderText("Erfolgreich");
            erfolgreich.setContentText("Das Themenangebot" + " <" + thema.getTitel() + "> wurde erfolgreich hinzugefügt.");
            erfolgreich.showAndWait();
            leeren();
        }
    }

}
