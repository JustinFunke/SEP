package Client.Bewertungen;

import Client.Lehrveranstaltung.LVUebersichtsseiteController;
import Server.ClientStart;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BewertungController {

    @FXML
    private Pane pane;
    @FXML
    private Label labelFrage;
    @FXML
    private CheckBox checkBoxAntwortA;
    @FXML
    private CheckBox checkBoxAntwortB;
    @FXML
    private CheckBox checkBoxAntwortC;
    @FXML
    private CheckBox checkBoxAntwortD;
    @FXML
    private Button btnAbgeben;
    @FXML
    private Label idLabel;
    @FXML
    private Label currentLVIDLabel;

    private Stage stage;
    private int currentUserID;
    private int currentLVID;
    private int bewertungsid;
    private int position;
    public int count=0;
    public int bewertungsfragenID=0;
    List<Integer> bewertungsfragenIDs = new ArrayList<>();
    public int selectedA = 0;
    List<Integer> antworten = new ArrayList<>();

    @FXML
    void cmdAntwortAbgeben(ActionEvent event) throws IOException, SQLException {
        if(checkBoxAntwortA.isSelected()||checkBoxAntwortB.isSelected()||checkBoxAntwortC.isSelected()||checkBoxAntwortD.isSelected()){
            antworten.add(selectedA);
            if(position!=count){
                init(currentUserID,currentLVID,bewertungsid,position);
            }
            else{
                //die bewertungsstatistik updaten
                //die bewertungsversuche updaten
                JSONArray jsonArray = new JSONArray();
                for(int i=0; i<bewertungsfragenIDs.size();i++){
                    jsonArray.put(bewertungsfragenIDs.get(i));
                }
                System.out.println("jsonArray in controller: "+jsonArray);
                String jsonString = jsonArray.toString();

                JSONArray jsonArray2 = new JSONArray();
                for(int i=0; i<antworten.size();i++){
                    jsonArray2.put(antworten.get(i));
                }
                System.out.println("jsonArray2 in controller: "+jsonArray2);
                String jsonString2 = jsonArray2.toString();

                JSONObject json = new JSONObject();
                json.put("Methode","bewertungAbgeben");
                json.put("P1",jsonString);
                json.put("P2",currentUserID);
                json.put("P3",jsonString2);
                json.put("P4",bewertungsid);
                ClientStart.verbinden.send(json);
                try{
                    TimeUnit.MILLISECONDS.sleep(200);
                }
                catch(Exception e){
                    System.out.println(e);
                }
                stage = (Stage) pane.getScene().getWindow();
                stage.close();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/LVUebersichtsseite.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(loader.load()));
                LVUebersichtsseiteController controller = loader.getController();
                controller.setIDLabel(currentUserID);
                controller.setLVIDLabel(currentLVID);
                controller.init();
                stage.show();
            }
        }
        else{
            Alert erfolgreich = new Alert(Alert.AlertType.INFORMATION);
            erfolgreich.setTitle("Alert");
            erfolgreich.setHeaderText("Keine Antwort ausgewählt!");
            erfolgreich.setContentText("Bitte wählen Sie zuerst eine Antwort aus!");
            erfolgreich.showAndWait();
        }

    }

    @FXML
    void cmdVerwerfen(ActionEvent event) throws IOException, SQLException {

        //!!!!!!!!!!!
        //bewertungVersuchVerwerfen löscht den bewertungsversuch aus der tabelle aber die bewertungsstatistik bleibt
        //!!!!!!!!!!!
        /*JSONObject json = new JSONObject();
        json.put("Methode","bewertungVersuchVerwerfen");
        json.put("P1",currentUserID);
        json.put("P2",bewertungsid);
        ClientStart.verbinden.send(json);*/

        stage = (Stage) pane.getScene().getWindow();
        stage.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/LVUebersichtsseite.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        LVUebersichtsseiteController controller = loader.getController();
        controller.setIDLabel(currentUserID);
        controller.setLVIDLabel(currentLVID);
        controller.init();
        stage.show();
    }

    @FXML
    void cmdCheckA(ActionEvent event) {
        checkBoxAntwortA.setSelected(true);
        checkBoxAntwortB.setSelected(false);
        checkBoxAntwortC.setSelected(false);
        checkBoxAntwortD.setSelected(false);
        selectedA=1;
    }

    @FXML
    void cmdCheckB(ActionEvent event) {
        checkBoxAntwortA.setSelected(false);
        checkBoxAntwortB.setSelected(true);
        checkBoxAntwortC.setSelected(false);
        checkBoxAntwortD.setSelected(false);
        selectedA=2;
    }

    @FXML
    void cmdCheckC(ActionEvent event) {
        checkBoxAntwortA.setSelected(false);
        checkBoxAntwortB.setSelected(false);
        checkBoxAntwortC.setSelected(true);
        checkBoxAntwortD.setSelected(false);
        selectedA=3;
    }

    @FXML
    void cmdCheckD(ActionEvent event) {
        checkBoxAntwortA.setSelected(false);
        checkBoxAntwortB.setSelected(false);
        checkBoxAntwortC.setSelected(false);
        checkBoxAntwortD.setSelected(true);
        selectedA=4;
    }

    public void init(int currentUserID,int currentLVID,int bewertungsid, int pos) throws IOException {
        System.out.println("Bewertung init");
        position = pos+1;
        this.bewertungsid = bewertungsid;
        this.currentUserID = currentUserID;
        this.currentLVID = currentLVID;
        checkBoxAntwortA.setSelected(false);
        checkBoxAntwortB.setSelected(false);
        checkBoxAntwortC.setSelected(false);
        checkBoxAntwortD.setSelected(false);
        int minFN=0;
        System.out.println("Bewertung init 2.");
        JSONObject json = new JSONObject();
        json.put("Methode","getBewertungsfragenIDs");
        json.put("P1",bewertungsid);
        ClientStart.verbinden.send(json);
        JSONArray jsonArray = ClientStart.verbinden.receiveary();
        bewertungsfragenIDs.clear();
        for(int i=0; i<jsonArray.length(); i++){
            bewertungsfragenIDs.add(jsonArray.getInt(i));
        }
        System.out.println("Schritt 1: "+bewertungsfragenIDs);

        for (int i=0; i<bewertungsfragenIDs.size();i++){
            JSONObject json2 = new JSONObject();
            json2.put("Methode","setBewertungsfragenTrue");
            json2.put("P1",bewertungsfragenIDs.get(i));
            ClientStart.verbinden.send(json2);
            try{
                TimeUnit.MILLISECONDS.sleep(200);
            }
            catch(Exception e){
                System.out.println(e);
            }
        }
        System.out.println("Schritt 2");

        JSONObject json2 = new JSONObject();
        json2.put("Methode","countBewertungsfragenMarkiert");
        json2.put("P1", true);
        ClientStart.verbinden.send(json2);
        System.out.println("vor receiveobj");
        JSONObject jsonReceive = ClientStart.verbinden.receiveobj();
        String jsonString = jsonReceive.get("P1")+"";
        System.out.println(jsonString);
        count=0;
        count = count + Integer.parseInt(jsonString);
        System.out.println("Schritt 3");

        JSONObject json3 = new JSONObject();
        json3.put("Methode","getMinFNBewertung");
        json3.put("P1",pos);
        ClientStart.verbinden.send(json3);
        JSONObject jsonReceive2 = ClientStart.verbinden.receiveobj();
        String jsonString2 = jsonReceive2.get("P1")+"";
        System.out.println(jsonString2);
        minFN = Integer.parseInt(jsonString2);
        System.out.println("Schritt 4");

        JSONObject json4 = new JSONObject();
        json4.put("Methode","getBewertung");
        json4.put("P1",minFN);
        ClientStart.verbinden.send(json4);
        JSONArray jsonArray2 = ClientStart.verbinden.receiveary();
        bewertungsfragenID = Integer.parseInt(jsonArray2.getString(0));
        System.out.println("bewertungsfragenID: "+bewertungsfragenID);
        labelFrage.setText(jsonArray2.getString(1));
        System.out.println("Schritt 5");

        JSONObject json5 = new JSONObject();
        json5.put("Methode","setBewertungsfragenFalse");
        ClientStart.verbinden.send(json5);
        System.out.println("Schritt 6");
        try{
            TimeUnit.MILLISECONDS.sleep(200);
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
}
