package Client.Bewertungen;

import Client.Lehrveranstaltung.LVUebersichtsseiteController;
import Server.ClientStart;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

public class BewertungErstellenController {

    @FXML
    private Pane pane;
    @FXML
    private TextField txtBewertungsfrage;
    @FXML
    private Label idLabel;
    @FXML
    private Label currentLVIDLabel;

    private Stage stage;

    private int currentUserID;
    private int currentLVID;

    @FXML
    void cmdNeueFrage(ActionEvent event) throws IOException {
        if(!txtBewertungsfrage.getText().equals("")){
            String bewertungsfrage = txtBewertungsfrage.getText();
            //DB Methode insertBewertungsfrage
            JSONObject json = new JSONObject();
            json.put("Methode","insertBewertungsfrage");
            json.put("P1",bewertungsfrage);
            ClientStart.verbinden.send(json);
            try{
                TimeUnit.MILLISECONDS.sleep(200);
            }
            catch(Exception e){
                System.out.println(e);
            }
            stage = (Stage) pane.getScene().getWindow();
            stage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/BewertungErstellen.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            BewertungErstellenController controller = loader.getController();
            controller.init(currentUserID,currentLVID);
            stage.show();
        }
        else{
            Alert erfolgreich = new Alert(Alert.AlertType.INFORMATION);
            erfolgreich.setTitle("Alert");
            erfolgreich.setHeaderText("Feld leer!");
            erfolgreich.setContentText("Bitte füllen Sie erst das Fragefeld!");
            erfolgreich.showAndWait();
        }
    }

    @FXML
    void cmdSpeichern(ActionEvent event) throws IOException, SQLException {
        if(!txtBewertungsfrage.getText().equals("")){
            String bewertungsfrage = txtBewertungsfrage.getText();
            //DB Methode insertBewertungsfrage
            JSONObject json = new JSONObject();
            json.put("Methode","insertBewertungsfrage");
            json.put("P1",bewertungsfrage);
            ClientStart.verbinden.send(json);
            try{
                TimeUnit.MILLISECONDS.sleep(200);
            }
            catch(Exception e){
                System.out.println(e);
            }
            JSONObject json2 = new JSONObject();
            json2.put("Methode","insertBewertung");
            json2.put("P1",currentLVID);
            ClientStart.verbinden.send(json2);
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
        else{
            Alert erfolgreich = new Alert(Alert.AlertType.INFORMATION);
            erfolgreich.setTitle("Alert");
            erfolgreich.setHeaderText("Feld leer!");
            erfolgreich.setContentText("Bitte füllen Sie erst das Fragefeld!");
            erfolgreich.showAndWait();
        }
    }

    @FXML
    void cmdVerwerfen(ActionEvent event) throws IOException, SQLException {
        JSONObject json = new JSONObject();
        json.put("Methode","deleteMarkierteBewertungsfragen");
        json.put("P1",true);
        ClientStart.verbinden.send(json);

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
    public void init(int currentUserID, int currentLVID){
        this.currentUserID = currentUserID;
        this.currentLVID = currentLVID;
    }

}
