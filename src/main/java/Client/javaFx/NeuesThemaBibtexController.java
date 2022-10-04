package Client.javaFx;

import Server.ClientStart;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.scene.control.*;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class NeuesThemaBibtexController {

    @FXML
    private TextField themaTitel;
    @FXML
    private TextArea themaBeschreibung;
    @FXML
    private TextField bibtexPath;
    @FXML
    private Label currentIDLabel;
    private int userID;
    private File bibtexFile;


    //ID übernehmen und umwandeln
    public void setIDLabel(int pUID){
        Integer integer = pUID;
        currentIDLabel.setText(integer.toString());
    }

    //Methode zum initialisieren
    public void init(){
        String userIDstr = currentIDLabel.getText();
        userID = Integer.parseInt(userIDstr);
        themaTitel.setText("");
        themaBeschreibung.setText("");
        bibtexPath.setText("");
        bibtexFile = null;
    }

    //Methode um zu checken, ob Thema mit diesem Namen bei diesem Lehrenden schon vorhanden ist.
    public boolean themaVorhanden(String titel, int nutzerid) throws IOException {
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


    //Methode zum auswählen der Bibtex-Datei
    @FXML
    public void bibtexHolen(){
        FileChooser holeBibtex = new FileChooser();
        holeBibtex.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT", "*.txt", "*.bib"));
        Stage neueStage = new Stage();
        bibtexFile = holeBibtex.showOpenDialog(neueStage);
        if(bibtexFile != null) {
            bibtexPath.setText(bibtexFile.getAbsolutePath());
        } else {

        }
    }

    @FXML
    public void bibtexLesen(String bibtexInhalt, int themenid){
        JSONObject json = new JSONObject();
        json.put("Methode", "literaturHinzufuegen");
        json.put("P1", themenid);
        json.put("P2", bibtexInhalt);
        //System.out.println("2. bibtexAulesen start: \n" + bibtexInhalt + " :bibtexAuslesen Ende .2");
        ClientStart.verbinden.send(json);
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

    @FXML
    //Methode zum Bestätigen aller Eingaben
    public void themaBestaetigen() throws IOException, InterruptedException{
        if(themaTitel.getText().trim().isEmpty() || themaBeschreibung.getText().trim().isEmpty() || bibtexFile == null){
            Alert fehler = new Alert(Alert.AlertType.ERROR);
            fehler.setTitle("Fehler");
            fehler.setHeaderText("Bitte Eingaben überprüfen.");
            fehler.setContentText("Es muss jedes Feld ausgefüllt sein und eine bibtex-Datei gewählt werden.");
            fehler.showAndWait();
        } else if(themaVorhanden(themaTitel.getText().trim(), userID)){
            Alert fehlgeschlagen = new Alert(Alert.AlertType.ERROR);
            fehlgeschlagen.setTitle("Fehlgeschlagen");
            fehlgeschlagen.setHeaderText("Fehlgeschlagen");
            fehlgeschlagen.setContentText("Es ist bereits ein Themenangebot mit diesem Titel von Ihnen vorhanden.");
            fehlgeschlagen.showAndWait();
        } else {
            //neues Thema speichern
            String bibtexAuslesen = Files.readString(Paths.get(bibtexFile.getAbsolutePath()));
            //System.out.println("1. bibtexAulesen start: \n" + bibtexAuslesen + " :bibtexAuslesen Ende .1");
            //System.out.println("Ab hier bibtex-Inhalt: <\n" + bibtexAuslesen + "\n> hier bibtex-Inhalt Ende");
            JSONObject json = new JSONObject();
            json.put("Methode", "themaHinzufuegen");
            json.put("P1", themaTitel.getText().trim());
            json.put("P2", themaBeschreibung.getText().trim());
            json.put("P3", userID);
            ClientStart.verbinden.send(json);

            Thema thema = new Thema(themaTitel.getText().trim(), themaBeschreibung.getText().trim(), userID);
            //ThemenID abrufen
            JSONObject json2 = new JSONObject();
            json2.put("Methode", "holeThemenID");
            json2.put("P1", thema.getTitel());
            json2.put("P2", thema.getUserID());
            ClientStart.verbinden.send(json2);
            JSONObject json3 = ClientStart.verbinden.receiveobj();
            int themenID = Integer.parseInt( json3.get("P1")+"");
            //String testString = "Teststring für bibtex auf Datenbank";
            bibtexLesen(bibtexAuslesen, themenID);

            Alert erfolgreich = new Alert(Alert.AlertType.INFORMATION);
            erfolgreich.setTitle("Erfolgreich");
            erfolgreich.setHeaderText("Erfolgreich");
            erfolgreich.setContentText("Das Themenangebot" + " <" + thema.getTitel() + "> wurde erfolgreich hinzugefügt.");
            erfolgreich.showAndWait();
            init();
        }
    }


}
