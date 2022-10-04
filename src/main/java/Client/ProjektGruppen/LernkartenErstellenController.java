package Client.ProjektGruppen;

import Server.ClientStart;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.json.simple.JSONObject;

import java.io.IOException;

public class LernkartenErstellenController {
    public TextField txtName;
    public TextField txtFrage;
    public TextField txtAntwort;
    public boolean firstTime= true;
    public Label currentPGIDLabel;
    public Pane paneLernkartenErstellen;

    Stage stage;

    public void setPGIDLabel(int pUID){
        Integer integer = pUID;
        currentPGIDLabel.setText(integer.toString());
    }

    public void cmdFertig_Clicked(MouseEvent mouseEvent) throws IOException {
        ((Stage) paneLernkartenErstellen.getScene().getWindow()).close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/LernkartenUebersichtsSeite.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        LernkartenUebersichtsseiteController controller = loader.getController();
        controller.setPGIDLabel(Integer.parseInt(currentPGIDLabel.getText()));
        controller.init();
        stage.show();

    }

    public void cmdFrage_Clicked(MouseEvent mouseEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Fehler");
        alert.setHeaderText("Fehler");
        if(txtName.getText() !="" && txtFrage.getText()!="" && txtAntwort.getText()!="") {

            if (firstTime) {

                if(LernkartenHelper.nameVergeben(Integer.parseInt(currentPGIDLabel.getText()+""),txtName.getText())){
                    alert.setTitle("Fehler");
                    alert.setHeaderText("Fehler");
                    alert.setContentText("Name bereits vergeben");
                    alert.showAndWait();
                    return;
                }
                txtName.setDisable(true);
                firstTime =false;
            }

            JSONObject json = new JSONObject();
            json.put("Methode","LernkarteHinzufuegen");
            json.put("P1", Integer.parseInt(currentPGIDLabel.getText()));
            json.put("P2",txtName.getText());
            json.put("P3",txtFrage.getText());
            json.put("P4",txtAntwort.getText());
            ClientStart.verbinden.send(json);


            //Alert box
            alert.setTitle("Hinzugefügt");
            alert.setHeaderText("Hinzugefügt");
            alert.setContentText("Sie habe eine Lernkarte hinzugefügt");
            alert.showAndWait();

            //Felder leeren
            txtFrage.setText("");
            txtAntwort.setText("");
        }else{
            alert.setContentText("Sie müssen alle Felder ausfüllen");
            alert.showAndWait();
        }

    }

}
