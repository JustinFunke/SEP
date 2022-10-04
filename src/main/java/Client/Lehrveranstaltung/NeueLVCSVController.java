package Client.Lehrveranstaltung;


import Server.ClientStart;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.*;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;


public class NeueLVCSVController {

    @FXML
    private TextField csvPath;
    @FXML
    private Label currentIDLabel;
    private int userID;
    private File csvFile;


    //ID übernehmen und umwandeln
    public void setIDLabel(int pUID){
        Integer integer = pUID;
        currentIDLabel.setText(integer.toString());
    }

    //Methode zum auswählen der CSV-Datei
    @FXML
    public void csvHolen(){
        FileChooser holeCSV = new FileChooser();
        holeCSV.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
        Stage neueStage = new Stage();
        csvFile = holeCSV.showOpenDialog(neueStage);
        if(csvFile != null) {
            csvPath.setText(csvFile.getAbsolutePath());
        } else {

        }
    }
    //Methode zum initialisieren
    public void init(){
        String userIDstr = currentIDLabel.getText();
        userID = Integer.parseInt(userIDstr);
        csvPath.setText("");
        csvFile = null;
    }

    //Methode zum lesen des Inhalts der CSV-Datei und danach einheitlich die Kurse erstellen
    public void csvLesen(File file) throws IOException, InterruptedException {
        String auslesen = Files.readString(Paths.get(file.getAbsolutePath()));
        String[] inhalt = auslesen.split(";");
        String inhaltTemp = "";
        for(int x = 0; x < inhalt.length; x++){
            inhaltTemp = inhaltTemp + inhalt[x] + "\n";
        }
        inhalt = inhaltTemp.split("\n");
        int counter = 3; //Erste Zeile überspringen
        String[][] test = new String[inhalt.length/3][3];
        char checkTyp;
        String[] splitJahre;
        String[] splitJahr1;
        String[] splitJahr2;
        String jahre;

       // String semester;
        Lehrveranstaltung tempLV;
        Lehrveranstaltung tempLVvorhanden;
        String[][] vorhandenArray = new String[inhalt.length/3][3];
        int counterVorhanden = 0;
        for(int i = 0; i < (inhalt.length/3-1); i++) {
            test[i][0] = inhalt[counter]; //Name
            counter++;
            test[i][1] = inhalt[counter]; //Typ
            counter++;
            test[i][2] = inhalt[counter]; //Semester
            counter++;

            while(test[i][1].charAt(0) == ' '){
                String sub;
                sub = test[i][1].substring(1);
                test[i][1] = sub;
            }
            checkTyp = test[i][1].charAt(0);
            if (checkTyp == 'V' || checkTyp == 'v') {
                test[i][1] = "Vorlesung";
            } else if (checkTyp == 'S' || checkTyp == 's') {
                test[i][1] = "Seminar";
            }
            splitJahre = test[i][2].split(" ");
            jahre = splitJahre[1];
            splitJahr1 = jahre.split("/");
            splitJahr2 = jahre.split("-");
            if (splitJahr1.length == 2) {
                if(splitJahr1[0].length() == 2){
                    splitJahr1[0] = "20" + splitJahr1[0];
                    splitJahr1[1] = "20" + splitJahr1[1];
                    test[i][2] = "WiSe " + splitJahr1[0] + "/" + splitJahr1[1];
                } else {
                    test[i][2] = "WiSe " + splitJahr1[0] + "/" + splitJahr1[1];
                }
            } else if (splitJahr2.length == 2) {
                if(splitJahr2[0].length() == 2){
                    splitJahr2[0] = "20" + splitJahr2[0];
                    splitJahr2[1] = "20" + splitJahr2[1];
                    test[i][2] = "WiSe " + splitJahr2[0] + "/" + splitJahr2[1];
                } else {
                    test[i][2] = "WiSe " + splitJahr2[0] + "/" + splitJahr2[1];
                }
            } else {
                test[i][2] = "SoSe " + jahre;
            }
            while(test[i][0].charAt(0) == ' '){
                String sub;
                sub = test[i][0].substring(1);
                test[i][0] = sub;
            }
            if(NeueLVFormularController.kursVorhanden(test[i][0].trim(), test[i][1].trim(), test[i][2].trim()) == true){
                System.out.println("Kurs bereits vorhanden!");
                //tempLVvorhanden = new Client.Lehrveranstaltung(test[i][0].trim(), test[i][1].trim(), test[i][2].trim());
                vorhandenArray[counterVorhanden][0] = test[i][0].trim();
                vorhandenArray[counterVorhanden][1] = test[i][1].trim();
                vorhandenArray[counterVorhanden][2] = test[i][2].trim();
                counterVorhanden++;
            } else {
                tempLV = new Lehrveranstaltung(test[i][0].trim(), test[i][1].trim(), test[i][2].trim());
                String neueLVName = tempLV.getName();
                String neueLVTyp = tempLV.getTyp();
                String neueLVSemester = tempLV.getSemester();
                int neueLVLehrender = userID;
                //neue LV speichern
                JSONObject json = new JSONObject();
                json.put("Methode", "lvHinzufuegen");
                json.put("P1", neueLVName);
                json.put("P2", neueLVTyp);
                json.put("P3", neueLVSemester);
                json.put("P4", neueLVLehrender);
                ClientStart.verbinden.send(json);
                TimeUnit.SECONDS.sleep(1L);
                //USERID und LVID zu USERINLV-Table hinzufügen
                JSONObject json2 = new JSONObject();
                json2.put("Methode", "userIDundlvID");
                json2.put("P1", neueLVName);
                json2.put("P2", neueLVTyp);
                json2.put("P3", neueLVSemester);
                json2.put("P4", neueLVLehrender);
                ClientStart.verbinden.send(json2);

            }
            TimeUnit.SECONDS.sleep(1L);
        }
        System.out.println("Erledigt");
        Alert erfolgreich = new Alert(Alert.AlertType.INFORMATION);
        erfolgreich.setTitle("Erfolgreich");
        erfolgreich.setHeaderText("Erfolgreich");
        if(vorhandenArray[0][0] != null){
            erfolgreich.setContentText("CSV-Datei wurde erfolgreich ausgewertet und die gewünschten fehlenden Lehrveranstaltungen erstellt.\nEs wurden " +
                    counterVorhanden + " Lehrveranstaltungen nicht erstellt, da diese bereits vorhanden sind.\nBitte überprüfen Sie auf der Hauptseite Ihre Lehrveranstaltungen.");
            erfolgreich.showAndWait();
        } else {
            erfolgreich.setContentText("CSV-Datei wurde erfolgreich ausgewertet und die gewünschten Lehrveranstaltungen erstellt.");
            erfolgreich.showAndWait();
        }
        init();

    }

    @FXML
    //Methode zum Bestätigen der gewählten CSV-Datei
    public void csvBestaetigen() throws IOException, InterruptedException{
        if(csvFile != null) {
            csvLesen(csvFile);
        } else {
            Alert fehlgeschlagen = new Alert(Alert.AlertType.ERROR);
            fehlgeschlagen.setTitle("Fehlgeschlagen");
            fehlgeschlagen.setHeaderText("Fehlgeschlagen");
            fehlgeschlagen.setContentText("Es wurde noch keine CSV-Datei ausgewählt.");
            fehlgeschlagen.showAndWait();
        }
    }

}