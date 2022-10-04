package Client.Lehrveranstaltung;

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

public class NeuesLMController {

    @FXML
    private TextField pdfPath;
    @FXML
    private TextField LMtitle;
    @FXML
    private  Label currentLVIDLabel;
    //private int userID;
    private int lvID;
    private File pdfFile;
    private static List<String> lmList = new ArrayList<>();



    //LVID übernehmen und umwandeln
    public void setLVIDLabel(int pUID){
        Integer integer = pUID;
        currentLVIDLabel.setText(integer.toString());
    }

    //Methode um eine PDF-Datei zum Upload zu wählen
    @FXML
    public void pdfHolen(){
        FileChooser holePDF = new FileChooser();
        holePDF.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.PDF"));
        Stage neueStage = new Stage();
        pdfFile = holePDF.showOpenDialog(neueStage);
        if(pdfFile != null) {
            pdfPath.setText(pdfFile.getAbsolutePath());
        } else {

        }
    }
    //Methode um eine PDF-Datei in Base64 umzuwandeln(für die Datenbank)
    public String pdfUmwandeln(File pdf) throws IOException {
        byte[] pdfDaten = FileUtils.readFileToByteArray(pdf);
        String pdfUmgewandelt = Base64.getEncoder().encodeToString(pdfDaten);
        return  pdfUmgewandelt;
    }
    //Methode zum initialisieren
    public void init(){
        String lvIDstr = currentLVIDLabel.getText();
        lvID = Integer.parseInt(lvIDstr);
        pdfPath.setText("");
        LMtitle.setText("");
        pdfFile = null;
    }
    //Methode zum checken, ob bereits Lehrmaterial im Kurs mit dem gewünschten Namen vorhanden ist
    public static boolean lmVorhanden(String nameLM,int lvID) throws IOException{
        JSONObject json = new JSONObject();
        json.put("Methode", "holeCheckVorhandenLM");
        json.put("P1", nameLM);
        json.put("P2", lvID);
        ClientStart.verbinden.send(json);
        JSONObject json2 = ClientStart.verbinden.receiveobj();
        boolean vorhanden = (Boolean) json2.get("P1");
        if(vorhanden){
            return true;
        }
        return false;
    }

    //Methode für den Button um die PDF-Wahl zu bestätigen
    @FXML
    public void pdfBestaetigen() throws IOException{

        if(LMtitle.getText().trim().isEmpty() || pdfFile == null){
            Alert fehler = new Alert(Alert.AlertType.ERROR);
            fehler.setTitle("Fehler");
            fehler.setHeaderText("Bitte Eingaben überprüfen.");
            fehler.setContentText("Es muss eine PDF-Datei und ein Titel gewählt werden.");
            fehler.showAndWait();
        } else if(lmVorhanden(LMtitle.getText().trim(), lvID) == true) {
            Alert fehler = new Alert(Alert.AlertType.ERROR);
            fehler.setTitle("Fehler");
            fehler.setHeaderText("Lehrmaterial bereits vorhanden.");
            fehler.setContentText("Es ist bereits eine andere Datei mit diesem Anzeigenamen <" + LMtitle.getText().trim() + "> hochgeladen worden.\nBitte Eingaben und vorhandenes Lehrmaterial überprüfen.");
            fehler.showAndWait();
        } else {
            JSONObject json = new JSONObject();
            json.put("Methode", "lmLVHinzufuegen");
            json.put("P1", lvID);
            json.put("P2", pdfUmwandeln(pdfFile));
            json.put("P3", LMtitle.getText().trim());
            ClientStart.verbinden.send(json);
            Alert erfolgreich = new Alert(Alert.AlertType.INFORMATION);
            erfolgreich.setTitle("Erfolgreich");
            erfolgreich.setHeaderText("Erfolgreich");
            erfolgreich.setContentText("PDF <" + LMtitle.getText().trim() + "> wurde erfolgreich hinzugefügt.");
            erfolgreich.showAndWait();
            init();
        }
    }

}
