package Client.ProjektGruppen;

import Server.ClientStart;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class LernkartenAnzeigenController {
    public Pane paneAnzeige;
    public Label lblText;
    public Label currentPGIDLabel;
    public Label nameLabel;
    public Button cmdAntwort;
    public Button cmdNaechsteFrage;
    ArrayList<String> Frage = new ArrayList<>();
    ArrayList<String> Antwort = new ArrayList<>();
    int intFrage=0;
    int intAntwort=0;


    public void setPGIDLabel(int pUID){
        Integer integer = pUID;
        currentPGIDLabel.setText(integer.toString());
    }
    public void setNameLabel(String name){
        nameLabel.setText(name);
    }


    public void cmdNaechsteFrage_Clicked(MouseEvent mouseEvent) {
        naechsteFrage();
    }

    public void cmdVerlassen_Clicked(MouseEvent mouseEvent) throws IOException {
        ((Stage) paneAnzeige.getScene().getWindow()).close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/LernkartenUebersichtsSeite.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        LernkartenUebersichtsseiteController controller = loader.getController();
        controller.setPGIDLabel(Integer.parseInt(currentPGIDLabel.getText()));
        controller.init();
        stage.show();
    }
    public void init() throws IOException {
        System.out.println("Bin in init");
        lblText.setWrapText(true);
        JSONObject json = new JSONObject();
        json.put("Methode", "LernkartenAnzeigen");
        json.put("P1", currentPGIDLabel.getText());
        json.put("P2", nameLabel.getText());
        ClientStart.verbinden.send(json);
        JSONArray jsonArrayFrage = ClientStart.verbinden.receiveary();
        JSONArray jsonArrayAntwort = ClientStart.verbinden.receiveary();

        if(jsonArrayFrage != null) {
            for (int i = 0; i < jsonArrayFrage.length(); i++) {
                Frage.add((String) jsonArrayFrage.get(i) + "");
            }
        }
        if(jsonArrayAntwort != null) {
            for (int i = 0; i < jsonArrayAntwort.length(); i++) {
                Antwort.add((String) jsonArrayAntwort.get(i) + "");
            }
        }
        naechsteFrage();



    }
    public void naechsteFrage(){

        cmdAntwort.setDisable(false);
        cmdAntwort.setVisible(true);
        if(intAntwort != intFrage)
            intAntwort = intAntwort+1;
        lblText.setText(Frage.get(intFrage));
        intFrage +=1;
        if(intFrage>Frage.size()-1){
            cmdNaechsteFrage.setDisable(true);
        }
    }

    public void cmdAntwort_Clicked(MouseEvent mouseEvent) {
        cmdAntwort.setDisable(true);
        cmdAntwort.setVisible(false);
        lblText.setText(Antwort.get(intAntwort));
        intAntwort+=1;

    }
}
