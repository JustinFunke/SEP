package Client.Registrierung;

import Server.ClientStart;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Objects;

public class RegistrierenWindowLehrenderController {

    public TextField txtForschungsgebiet;
    public TextField txtLehrstuhl;
    public TextField txtStrasse;
    public TextField txtStadt;
    public TextField txtPasswort;
    public TextField txtEmail;
    public TextField txtVorname;
    public TextField txtNachname;
    public Pane panelRegistierenLehrender;
    private File selectedFile;

    public void cmdRegistrieren_Clicked(MouseEvent mouseEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Fehler");
        alert.setHeaderText("Fehler bei der Client.Registrierung");

        //Prüfen ob ein Feld leer ist
        if(!txtEmail.getText().equals("") && !txtNachname.getText().equals("") && !txtPasswort.getText().equals("") && !txtStadt.getText().equals("") && !txtStrasse.getText().equals("") && !txtVorname.getText().equals("") && !txtForschungsgebiet.getText().equals("") && !txtLehrstuhl.getText().equals("")) {

            //Prüfen ob Namen eine Zahl enthalten
            if(!CheckEingabeRegistrierung.KeineZahl(txtNachname.getText()) || !CheckEingabeRegistrierung.KeineZahl(txtVorname.getText())){
                alert.setContentText("Namen haben keine Zahl!");
                alert.showAndWait();
                return;
            }

            //Prüfen ob Gültige Email
            if(!CheckEingabeRegistrierung.GueltigeEmail(txtEmail.getText())){
                alert.setContentText("Ungültige Email");
                alert.showAndWait();
                return;
            }

            //Prüfen ob Email vergeben ist
            if(CheckEingabeRegistrierung.EmailVergeben(txtEmail.getText())){
                alert.setContentText("Email bereits Registriert!");
                alert.showAndWait();
                return;
            }

            JSONObject json = new JSONObject();
            json.put("Methode","RegistrierungLehrender");
            json.put("Parameter1",txtEmail.getText());
            json.put("Parameter2",txtPasswort.getText());
            json.put("Parameter3",txtVorname.getText());
            json.put("Parameter4",txtNachname.getText());
            json.put("Parameter5",txtStadt.getText());
            json.put("Parameter6",txtStrasse.getText());
            json.put("Parameter7",selectedFile);
            json.put("Parameter8",txtLehrstuhl.getText());
            json.put("Parameter9",txtForschungsgebiet.getText());
            ClientStart.verbinden.send(json);


            try {
                //Lehrender Registrieren
                alert.setTitle("Erfolgreich");
                alert.setHeaderText("Erfolgreiche Registrieung");
                alert.setContentText("Sie haben sich erfolgreich Registriert");
                alert.showAndWait();

                //Fenster Schließen und neues Fenster erstellen
                ((Stage) panelRegistierenLehrender.getScene().getWindow()).close();
                Stage stage = new Stage();
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Login.fxml")));
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
            catch (Exception ex){
                alert.setContentText("Es ist ein Fehler aufgetreten, bitte versuchen Sie es Später erneut");
                alert.showAndWait();
            }

        }
        //Falls ein Feld leer ist
        else{
            alert.setContentText("Sie müssen alle Felder ausfüllen!");
            alert.showAndWait();
        }
    }

    //Ausgewähltes Bild oder Standart Bild in Base64 umwandeln
    private String setImage() throws IOException {
        if(selectedFile != null) {
            byte[] fileContent = FileUtils.readFileToByteArray(selectedFile);
            String encodedString = Base64.getEncoder().encodeToString(fileContent);
            return  encodedString;
        }else{
            File defaultFile = new File("MavenTest/src/main/resources/DefaultProfilbild.png");
            byte[] fileContent = FileUtils.readFileToByteArray(defaultFile);
            String encodedString = Base64.getEncoder().encodeToString(fileContent);
            return  encodedString;
        }

    }

    //Bild auswählen
    public void cmdBild_Clicked(MouseEvent mouseEvent) {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Bilder", "*.png*","*.jpg"));

        Stage secondStage = new Stage();
        selectedFile = chooser.showOpenDialog(secondStage);
    }

    public void cmdZurueck_Clicked(MouseEvent mouseEvent) throws IOException {
        Stage stage;
        Parent root;
        Scene scene;

        ((Stage) panelRegistierenLehrender.getScene().getWindow()).close();
        stage = new Stage();
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Login.fxml")));
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void init(){
        Stage stage= (Stage) panelRegistierenLehrender.getScene().getWindow();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                System.out.println("Ausloggen");
                JSONObject json = new JSONObject();
                json.put("Methode", "closeConnection");
                ClientStart.verbinden.send(json);
                System.exit(0);
            }
        });
    }
}
