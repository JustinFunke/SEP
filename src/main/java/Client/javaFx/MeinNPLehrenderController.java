package Client.javaFx;

import Server.ClientStart;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.simple.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class MeinNPLehrenderController {

    @FXML
    private Pane pane;
    @FXML
    private ImageView imageProfilbild;
    @FXML
    private Text txtName;
    @FXML
    private Text txtEmail;
    @FXML
    private Text txtAdresse;
    @FXML
    private Text txtLehrstuhl;
    @FXML
    private Text txtForschungsgebiet;
    @FXML
    private ListView<String> listViewLV;
    @FXML
    private Label idLabel;
    @FXML
    private ListView ThemenListView;
    private Stage stage;
    private Parent root;
    private Scene scene;


    private int currentUserID;
    private int currentLVID;

    public int getCurrentUserID() {
        return currentUserID;
    }

    public void setCurrentUserID(int currentUserID) {
        this.currentUserID = currentUserID;
    }

    public int getCurrentLVID() {
        return currentLVID;
    }

    public void setCurrentLVID(int currentLVID) {
        this.currentLVID = currentLVID;
    }

    @FXML
    public void cmdBackButton(ActionEvent event) throws IOException {
        //Zurück zur Hauptseite
        stage = (Stage) pane.getScene().getWindow();
        stage.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Hauptseite(fürLehrender).fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        HauptseiteLehrenderController controller = loader.getController();
        controller.setIDLabel(currentUserID);
        controller.init();
        stage.show();
    }

    @FXML
    public void cmdProfilBearbeiten(ActionEvent event) throws IOException {
        //Öffnen des Bearbeitungsfensters
        stage = (Stage) pane.getScene().getWindow();
        stage.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/NutzerprofilLehrenderBearbeiten.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        NPLehrenderBearbeitenController controller = loader.getController();
        controller.setIDLabel(currentUserID);
        controller.init();
        stage.show();
    }
    public void setIDLabel(int id){
        Integer integer = id;
        idLabel.setText(integer.toString());
    }

    public void init() throws IOException {
        String zs = idLabel.getText();
        currentUserID = Integer.parseInt(zs);
        JSONObject json = new JSONObject();
        json.put("Methode","getUserDatenLehrender");
        json.put("P1",currentUserID);
        ClientStart.verbinden.send(json);
        JSONArray jsonArray = ClientStart.verbinden.receiveary();
        txtName.setText(jsonArray.getString(0));
        txtEmail.setText(jsonArray.getString(1));
        txtLehrstuhl.setText(jsonArray.getString(2));
        txtForschungsgebiet.setText(jsonArray.getString(3));
        txtAdresse.setText(jsonArray.getString(4));
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(jsonArray.getString(5)));
            Image image = new Image(inputStream);
            imageProfilbild.setImage(image);
        }
        catch (Exception exception){
            System.out.println("Kein Profilbild vorhanden");
        }
        List<String> ergebnis = new ArrayList<>();
        JSONObject json2 = new JSONObject();
        json2.put("Methode","lvDesUsers");
        json2.put("P1",currentUserID);
        ClientStart.verbinden.send(json2);
        JSONArray jsonArray2 = ClientStart.verbinden.receiveary();
        for(int i=0; i<jsonArray2.length(); i++){
            ergebnis.add(jsonArray2.getString(i));
        }
        listViewLV.getItems().clear();
        listViewLV.getItems().addAll(ergebnis);

        JSONObject jsonThemen = new JSONObject();
        jsonThemen.put("Methode","getThemen");
        jsonThemen.put("Parameter1",currentUserID);
        ClientStart.verbinden.send(jsonThemen);
        JSONArray jsonthemenTitel = ClientStart.verbinden.receiveary();
        ArrayList<String> suchergebnisse = new ArrayList<>();
        if(jsonthemenTitel!=null){
            for(int i = 0;i<jsonthemenTitel.length();i++) {
                suchergebnisse.add((String) jsonthemenTitel.get(i) + "");
            }
            ThemenListView.getItems().addAll(suchergebnisse);
        }
    }

}
