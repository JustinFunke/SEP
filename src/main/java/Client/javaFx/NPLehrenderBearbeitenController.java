package Client.javaFx;

import Server.ClientStart;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.simple.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

public class NPLehrenderBearbeitenController {
    @FXML
    private ImageView imgProfilbild;
    @FXML
    private Pane pane;
    @FXML
    private TextField txtLehrstuhl;
    @FXML
    private TextField txtForschungsgebiet;
    @FXML
    private TextField txtStrasse;
    @FXML
    private TextField txtWohnort;
    @FXML
    private PasswordField txtPasswort;
    @FXML
    private PasswordField txtPasswortBestätigen;
    @FXML
    private Label idLabel;

    private Stage stage;
    private Parent root;
    private Scene scene;
    private int currentUserID;
    private File selectedFile;



    public int getCurrentUserID() {
        return currentUserID;
    }

    public void setCurrentUserID(int currentUserID) {
        this.currentUserID = currentUserID;
    }

    @FXML
    void cmdSpeichern(ActionEvent event) throws IOException, InterruptedException {
        if (!txtWohnort.getText().equals("")) {
            JSONObject json = new JSONObject();
            json.put("Methode","updateWohnort");
            json.put("P1",txtWohnort.getText());
            json.put("P2",currentUserID);
            ClientStart.verbinden.send(json);
            TimeUnit.SECONDS.sleep(1L);
        }
        if (!txtStrasse.getText().equals("")) {
            JSONObject json = new JSONObject();
            json.put("Methode","updateStrasse");
            json.put("P1",txtStrasse.getText());
            json.put("P2",currentUserID);
            ClientStart.verbinden.send(json);
            TimeUnit.SECONDS.sleep(1L);
        }
        if (!txtLehrstuhl.getText().equals("")) {
            JSONObject json = new JSONObject();
            json.put("Methode","updateLehrstuhl");
            json.put("P1",txtLehrstuhl.getText());
            json.put("P2",currentUserID);
            ClientStart.verbinden.send(json);
            TimeUnit.SECONDS.sleep(1L);
        }
        if (!txtForschungsgebiet.getText().equals("")) {
            JSONObject json = new JSONObject();
            json.put("Methode","updateForschungsgebiet");
            json.put("P1",txtForschungsgebiet.getText());
            json.put("P2",currentUserID);
            ClientStart.verbinden.send(json);
            TimeUnit.SECONDS.sleep(1L);
        }
        if (txtPasswort.getText().equals(txtPasswortBestätigen.getText()) && !txtPasswort.getText().equals("")){
            JSONObject json = new JSONObject();
            json.put("Methode","updatePasswort");
            json.put("P1",txtPasswort.getText());
            json.put("P2",currentUserID);
            ClientStart.verbinden.send(json);
            TimeUnit.SECONDS.sleep(1L);
        }
        else {
            System.out.println("Das Passwort stimmt nicht überein");
        }
        if (!setImage().equals("")){
            JSONObject json = new JSONObject();
            json.put("Methode","updateProfilbild");
            json.put("P1",setImage());
            json.put("P2",currentUserID);
            ClientStart.verbinden.send(json);
            TimeUnit.SECONDS.sleep(1L);
        }
        else{
            System.out.println("Kein Bild ausgewählt!");
        }
        System.out.println("Änderungen gespeichert!");
        //Fenster schließen wieder zurück zum Nutzerprofil
        stage = (Stage) pane.getScene().getWindow();
        stage.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MeinNutzerprofilLehrender.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        MeinNPLehrenderController controller = loader.getController();
        controller.setIDLabel(currentUserID);
        controller.init();
        stage.show();
    }

    @FXML
    void cmdVerwerfen(ActionEvent event) throws IOException {
        //Zurück zur MeinNutzerprofilLehrender
        stage = (Stage) pane.getScene().getWindow();
        stage.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MeinNutzerprofilLehrender.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        MeinNPLehrenderController controller = loader.getController();
        controller.setIDLabel(currentUserID);
        controller.init();
        stage.show();
        System.out.println("Änderungen verworfen!");
    }
    @FXML
    void cmdBildÄndern(ActionEvent event) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Bild auswahl");
        FileChooser fchooser = new FileChooser();
        fchooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Bilder", "*.png*","*.jpg"));

        Stage secondStage = new Stage();
        selectedFile = fchooser.showOpenDialog(secondStage);
    }
    public void setIDLabel(int id){
        Integer integer = id;
        idLabel.setText(integer.toString());
    }
    private String setImage() throws IOException {
        if(selectedFile != null) {
            byte[] fileContent = FileUtils.readFileToByteArray(selectedFile);
            String encodedString = Base64.getEncoder().encodeToString(fileContent);
//            System.out.println(encodedString);
            return encodedString;
        }
        else{
            return "";
        }

    }

    public void init() throws IOException {
        //Auf den Textfeldern werden die jeweiligen Informationen des Users stehen
        String zs = idLabel.getText();
        currentUserID = Integer.parseInt(zs);
        JSONObject json = new JSONObject();
        json.put("Methode","getUserDatenLehrender");
        json.put("P1",currentUserID);
        ClientStart.verbinden.send(json);
        JSONArray jsonArray = ClientStart.verbinden.receiveary();
        txtLehrstuhl.setText(jsonArray.getString(2));
        txtForschungsgebiet.setText(jsonArray.getString(3));
        String adresse = jsonArray.getString(4);
        String[] parts = adresse.split(", ");
        String wohnort = parts[0];
        String strasse = parts[1];
        txtWohnort.setText(wohnort);
        txtStrasse.setText(strasse);
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(jsonArray.getString(5)));
            Image image = new Image(inputStream);
            imgProfilbild.setImage(image);
        }
        catch (Exception exception){
            System.out.println("Kein Profilbild vorhanden");
        }
    }
}
