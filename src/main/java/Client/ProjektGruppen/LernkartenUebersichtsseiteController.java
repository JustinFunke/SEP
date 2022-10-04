package Client.ProjektGruppen;

import Server.ClientStart;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class LernkartenUebersichtsseiteController {
    public Pane paneLernkarten;
    public ChoiceBox CHOLernen;
    private Stage stage;
    public Label currentPGIDLabel;

    public void setPGIDLabel(int pUID){
        currentPGIDLabel.setText(pUID +"");
    }


    public void cmdErstellen_Clicked(MouseEvent mouseEvent) throws IOException {
        stage = (Stage) paneLernkarten.getScene().getWindow();
        stage.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/LernkartenErstellen.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        LernkartenErstellenController controller = loader.getController();
        controller.setPGIDLabel(Integer.parseInt(currentPGIDLabel.getText()));
        stage.show();
    }

    public void cmdZureuck_Clicked(MouseEvent mouseEvent) {
        stage = (Stage) paneLernkarten.getScene().getWindow();
        stage.close();
    }

    public void init() throws IOException {
        //DropDownMenü füllen
        List<String> LernkartenNamen = new ArrayList<>();
        CHOLernen.getItems().clear();

        JSONObject json = new JSONObject();
        json.put("Methode","LernkartenDropDown");
        json.put("P1",currentPGIDLabel.getText());
        ClientStart.verbinden.send(json);
        JSONArray jsonArray = ClientStart.verbinden.receiveary();
        if(jsonArray != null){
            for(int i =0;i<jsonArray.length();i++){
                LernkartenNamen.add(jsonArray.get(i)+"");
            }
        }
        CHOLernen.getItems().addAll(LernkartenNamen);
    }

    public void cmdLernen_Clicked(MouseEvent mouseEvent) throws IOException {
        if(CHOLernen.getValue() ==null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Keine Auswahl");
            alert.setHeaderText("Keine Lernkarte ausgewählt");
            alert.setContentText("Sie müssen ein Lernkartenset auswählen");
            alert.showAndWait();
            return;
        }

        System.out.println(CHOLernen.getValue()+"");

        //Seite Öffnen
        ((Stage) paneLernkarten.getScene().getWindow()).close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/LernkartenAnzeigen.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        LernkartenAnzeigenController controller = loader.getController();
        controller.setNameLabel(CHOLernen.getValue()+"");
        controller.setPGIDLabel(Integer.parseInt(currentPGIDLabel.getText()));
        controller.init();
        stage.show();

    }
}
