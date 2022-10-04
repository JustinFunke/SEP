package Client.ProjektGruppen;

import Server.ClientStart;
import Server.Datenbank;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class NeueDateiPGController {

    @FXML
    private TextField NeuePGDateiPath;
    @FXML
    private TextField DateiTitle;
    @FXML
    private Label currentPGIDLabel;
    private int pgID;
    private File myPDFFile;
    private static List<String> dateienList = new ArrayList<>();



    //PGID übernehmen und umwandeln
    public void setPGIDLabel(int pUID){
        Integer integer = pUID;
        currentPGIDLabel.setText(integer.toString());
    }

    //Methode zum initialisieren und leeren der Eingaben
    public void init(){
        String pgIDstr = currentPGIDLabel.getText();
        pgID = Integer.parseInt(pgIDstr);
        NeuePGDateiPath.setText("");
        DateiTitle.setText("");
        myPDFFile = null;
    }

    //Methode um eine PDF-Datei zum Upload zu wählen
    @FXML
    public void uploadPDF(){
        FileChooser waehlePDF = new FileChooser();
        waehlePDF.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.PDF"));
        Stage neueStage = new Stage();
        myPDFFile = waehlePDF.showOpenDialog(neueStage);
        if(myPDFFile != null) {
            NeuePGDateiPath.setText(myPDFFile.getAbsolutePath());
        } else {

        }
    }

    //Methode um eine PDF-Datei in Base64 umzuwandeln(für die Datenbank)
    public String pdfUmwandeln(File pdf) throws IOException {


        byte[] pdfDaten = FileUtils.readFileToByteArray(pdf);
        String pdfUmgewandelt = Base64.getEncoder().encodeToString(pdfDaten);
        return  pdfUmgewandelt;
    }

    //Methode zum checken, ob bereits eine Datei im Kurs mit dem gewünschten Namen vorhanden ist
    public static boolean dateiVorhanden(String dateiName,int projektID) throws  IOException{
        JSONObject json = new JSONObject();
        json.put("Methode", "holeCheckVorhandenDatei");
        json.put("P1", dateiName);
        json.put("P2", projektID);
        ClientStart.verbinden.send(json);
        JSONObject json2 = ClientStart.verbinden.receiveobj();
            boolean vorhanden = (Boolean) json2.get("P1");
            if(vorhanden){
                return true;
            }
        return false;
    }

    //Datei bestätigen Methode
    @FXML
    public void dateiBestaetigen() throws IOException {
        //1. Checken ob Datei und Name gewählt wurde
        if(DateiTitle.getText().trim().isEmpty() || myPDFFile == null){
            Alert fehler = new Alert(Alert.AlertType.ERROR);
            fehler.setTitle("Fehler");
            fehler.setHeaderText("Bitte Eingaben überprüfen.");
            fehler.setContentText("Es muss eine PDF-Datei und ein Anzeigename gewählt werden.");
            fehler.showAndWait();
            //2. Checken ob Datei mit diesem Namen bereits vorhanden ist
        } else if(dateiVorhanden(DateiTitle.getText().trim(), pgID) == true) {
            Alert fehler = new Alert(Alert.AlertType.ERROR);
            fehler.setTitle("Fehler");
            fehler.setHeaderText("Datei bereits vorhanden.");
            fehler.setContentText("Es ist bereits eine andere Datei mit diesem Anzeigenamen <" + DateiTitle.getText().trim() + "> vorhanden.\nBitte Eingaben und vorhandene Dateien überprüfen.");
            fehler.showAndWait();
        } else {
            //3. Datei zur Datenbank hinzufügen
            JSONObject json = new JSONObject();
            json.put("Methode", "dateiPGHinzufuegen");
            json.put("P1", DateiTitle.getText().trim());
            json.put("P2", pdfUmwandeln(myPDFFile));
            json.put("P3", pgID);
            ClientStart.verbinden.send(json);
            Alert erfolgreich = new Alert(Alert.AlertType.INFORMATION);
            erfolgreich.setTitle("Erfolgreich");
            erfolgreich.setHeaderText("Erfolgreich");
            erfolgreich.setContentText("PDF-Datei <" + DateiTitle.getText().trim() + "> wurde erfolgreich hinzugefügt.");
            erfolgreich.showAndWait();
            init();
        }
    }

}
