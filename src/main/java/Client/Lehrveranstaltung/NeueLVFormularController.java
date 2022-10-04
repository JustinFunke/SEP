package Client.Lehrveranstaltung;

import Client.Lehrveranstaltung.Lehrveranstaltung;
import Server.ClientStart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NeueLVFormularController {
    @FXML
    private TextField LVnameTextField;
    @FXML
    private  TextField semesterJahr;
    @FXML
    private ChoiceBox TypChoice;
    @FXML
    private ChoiceBox SemChoice;
    @FXML
    private Label currentIDLabel;
    private int userID;
    private String typ;
    private String semesterTyp;
    private String semester;
    private static List<String> kursList = new ArrayList<>();


    //ID übernehmen und umwandeln
    public void setIDLabel(int pUID){
        Integer integer = pUID;
        currentIDLabel.setText(integer.toString());
    }

    //Methode zum initialisieren
    public void init(){
        ObservableList<String> typList = FXCollections.observableArrayList("Vorlesung","Seminar");
        TypChoice.setValue("Bitte Typ wählen");
        TypChoice.setItems(typList);
        ObservableList<String> semList = FXCollections.observableArrayList("WiSe","SoSe");
        SemChoice.setValue("Bitte Semester wählen");
        SemChoice.setItems(semList);
        LVnameTextField.setText("");
        semesterJahr.setText("");
        String userIDstr = currentIDLabel.getText();
        userID = Integer.parseInt(userIDstr);
    }

    //Methode um zu checken, ob Client.Lehrveranstaltung mit Name, Typ und Semester schon vorhanden ist.
    public static boolean kursVorhanden(String name,String typ, String semester) throws IOException {
        JSONObject json = new JSONObject();
        json.put("Methode", "holeCheckVorhandenLV");
        json.put("P1", name);
        json.put("P2", typ);
        json.put("P3", semester);
        ClientStart.verbinden.send(json);
        JSONObject json2 = ClientStart.verbinden.receiveobj();
        boolean vorhanden = (Boolean) json2.get("P1");
        if(vorhanden) {
            return true;
        }
        return false;
    }

    //Methode um die Eingaben zu bestätigen und die neue LV zu erstellen
    @FXML
    public void erstellenButtonPressed() throws IOException{
        if(TypChoice.getValue() == "Vorlesung"){
            typ = "Vorlesung";
        } else if (TypChoice.getValue() == "Seminar"){
            typ = "Seminar";
        } else {
            typ = "Bitte Typ wählen";
        }
        if(SemChoice.getValue() == "WiSe"){
            semesterTyp = "WiSe";
        } else if (SemChoice.getValue() == "SoSe"){
            semesterTyp = "SoSe";
        } else {
            typ = "Bitte Semester wählen";
        }
        if(TypChoice.getValue() == "Bitte Typ wählen" || SemChoice.getValue() == "Bitte Semester wählen" || LVnameTextField.getText().trim().isEmpty() || semesterJahr.getText().trim().isEmpty()){
            Alert fehler = new Alert(Alert.AlertType.ERROR);
            fehler.setTitle("Fehler");
            fehler.setHeaderText("Bitte Eingaben überprüfen.");
            fehler.setContentText("Es muss jedes Feld ausgefüllt sein und ein Lehrveranstaltungstyp und Semester gewählt werden.");
            fehler.showAndWait();
        } else {
            /* Referenz: "String.matches("\\d*"))" um nur bei Ziffern anzuschlagen, habe ich in dieser Kombination von
            "https://openbook.rheinwerk-verlag.de/javainsel9/javainsel_04_007.htm" im Kapitel
            4.7.1 "Arbeiten mit der Fassade: String#matches()" gesehen
             */
            if (semesterJahr.getText().length() > 4 || semesterJahr.getText().length() < 4 || !semesterJahr.getText().matches("\\d*")) {
                Alert fehlgeschlagen = new Alert(Alert.AlertType.ERROR);
                fehlgeschlagen.setTitle("Fehlgeschlagen");
                fehlgeschlagen.setHeaderText("Fehlgeschlagen");
                fehlgeschlagen.setContentText("Bitte das Jahr mit 4 Ziffern angeben, z.B. <2022>.\nDas folgende Jahr vom Wintersemester wird automatisch hinzugefügt.");
                fehlgeschlagen.showAndWait();
            } else {
                int jahr = Integer.parseInt(semesterJahr.getText());
                if (semesterTyp == "WiSe") {
                    semester = "WiSe " + semesterJahr.getText() + "/" + (jahr + 1);
                } else {
                    semester = "SoSe " + semesterJahr.getText();
                }
                if (kursVorhanden(LVnameTextField.getText().trim(), typ, semester)) {
                    Alert fehlgeschlagen = new Alert(Alert.AlertType.ERROR);
                    fehlgeschlagen.setTitle("Fehlgeschlagen");
                    fehlgeschlagen.setHeaderText("Fehlgeschlagen");
                    fehlgeschlagen.setContentText("Es ist bereits eine Client.Lehrveranstaltung mit diesem Namen, Typ und Semester vorhanden.");
                    fehlgeschlagen.showAndWait();
                } else {
                    //neue LV speichern
                    JSONObject json = new JSONObject();
                    json.put("Methode", "lvHinzufuegen");
                    json.put("P1", LVnameTextField.getText().trim());
                    json.put("P2", typ);
                    json.put("P3", semester);
                    json.put("P4", userID);
                    ClientStart.verbinden.send(json);
                    Lehrveranstaltung lv = new Lehrveranstaltung(LVnameTextField.getText().trim(), typ, semester);
                        Alert erfolgreich = new Alert(Alert.AlertType.INFORMATION);
                        erfolgreich.setTitle("Erfolgreich");
                        erfolgreich.setHeaderText("Erfolgreich");
                        erfolgreich.setContentText(lv.getTyp() + " <" + lv.getName() + "> für das " + lv.getSemester() + " wurde erfolgreich erstellt.");
                        erfolgreich.showAndWait();

                    //USERID und LVID zu USERINLV-Table hinzufügen
                    JSONObject json2 = new JSONObject();
                    json2.put("Methode", "userIDundlvID");
                    json2.put("P1", lv.getName());
                    json2.put("P2", lv.getTyp());
                    json2.put("P3", lv.getSemester());
                    json2.put("P4", userID);
                    ClientStart.verbinden.send(json2);
                    init();
                }
            }
        }
    }

}
