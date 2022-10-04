package Client.ProjektGruppen;

import Client.Lehrveranstaltung.Projektgruppe;
import Server.ClientStart;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.json.simple.JSONObject;

import java.io.IOException;

public class NeuePGController {

    @FXML
    private TextField NeuePGNameTextfield;
    @FXML
    private Label currentIDLabel;
    @FXML
    private Label currentLVIDLabel;
    private int userID;
    private int lvID;



    //ID übernehmen und umwandeln
    public void setIDLabel(int pUID) {
        Integer integer = pUID;
        currentIDLabel.setText(integer.toString());
    }

    //LVID übernehmen und umwandeln
    public void setLVIDLabel(int pUID) {
        Integer integer = pUID;
        currentLVIDLabel.setText(integer.toString());
    }

    //Methode zum initialisieren
    public void init() {
        NeuePGNameTextfield.setText("");
        String userIDstr = currentIDLabel.getText();
        userID = Integer.parseInt(userIDstr);
        String lvIDstr = currentLVIDLabel.getText();
        lvID = Integer.parseInt(lvIDstr);
    }

    //Methode zum checken, ob bereits eine Projektgruppe mit diesem Namen in dieser Client.Lehrveranstaltung vorhanden ist.
    public static boolean projektVorhanden(String projektName, int lvID) throws IOException {
        JSONObject json = new JSONObject();
        json.put("Methode", "holeCheckVorhandenProjekt");
        json.put("P1", projektName);
        json.put("P2", lvID);
        ClientStart.verbinden.send(json);
        JSONObject json2 = ClientStart.verbinden.receiveobj();
            boolean vorhanden = (Boolean) json2.get("P1");
            if(vorhanden) {
                return true;
            }
        return false;
    }

    //Methode zum erstellen der Projektgruppe
    @FXML
    public void erstellePG() throws IOException{
        if(NeuePGNameTextfield.getText().trim().isEmpty()){
            Alert fehler = new Alert(Alert.AlertType.ERROR);
            fehler.setTitle("Fehler");
            fehler.setHeaderText("Bitte Name überprüfen.");
            fehler.setContentText("Es muss ein Name für die Projektgruppe gewählt werden.");
            fehler.showAndWait();
        } else {
            if (projektVorhanden(NeuePGNameTextfield.getText().trim(), lvID)) {
                Alert fehlgeschlagen = new Alert(Alert.AlertType.ERROR);
                fehlgeschlagen.setTitle("Fehlgeschlagen");
                fehlgeschlagen.setHeaderText("Fehlgeschlagen");
                fehlgeschlagen.setContentText("Es ist bereits eine Projektgruppe mit diesem Namen vorhanden.");
                fehlgeschlagen.showAndWait();
            } else {
                    //Projektgruppe zur Datenbank hinzufügen
                    Projektgruppe neuePG = new Projektgruppe(NeuePGNameTextfield.getText().trim(), lvID);
                    JSONObject json = new JSONObject();
                    json.put("Methode", "projektHinzufuegen");
                    json.put("P1", NeuePGNameTextfield.getText().trim());
                    json.put("P2", lvID);
                    json.put("P3", userID);
                    ClientStart.verbinden.send(json);
                    Alert erfolgreich = new Alert(Alert.AlertType.INFORMATION);
                    erfolgreich.setTitle("Erfolgreich");
                    erfolgreich.setHeaderText("Erfolgreich");
                    erfolgreich.setContentText("Die Projektgruppe <" + neuePG.getName() + "> wurde erfolgreich erstellt.");
                    erfolgreich.showAndWait();

                    //USERID und PROJEKTID zu PROJEKTUSER-Table hinzufügen
                    JSONObject json2 = new JSONObject();
                    json2.put("Methode", "userIDundprojektID");
                    json2.put("P1", neuePG.getName());
                    json2.put("P2", lvID);
                    json2.put("P3", userID);
                    ClientStart.verbinden.send(json2);
                    init();
            }
        }
    }


}