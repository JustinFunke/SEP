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
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class RegistrierenWindowStudentController {

    public TextField txtVorname;
    public Pane panelRegistierenStudent;
    public TextField txtNachname;
    public TextField txtEmail;
    public TextField txtPasswort;
    public TextField txtStadt;
    public TextField txtStudienfach;
    public TextField txtStrasse;
    private File selectedFile;

//Registrieren
    public void cmdRegistrieren_Clicked(MouseEvent mouseEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Fehler");
        alert.setHeaderText("Fehler bei der Client.Registrierung");

        //Prüfen ob ein Feld leer ist
        if(!txtEmail.getText().equals("") && !txtNachname.getText().equals("") && !txtPasswort.getText().equals("") && !txtStadt.getText().equals("") && !txtStrasse.getText().equals("") && !txtVorname.getText().equals("")) {

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
            json.put("Methode","RegistrierungStudent");
            json.put("Parameter1",txtEmail.getText());
            json.put("Parameter2",txtNachname.getText());
            json.put("Parameter3",txtPasswort.getText());
            json.put("Parameter4",txtStadt.getText());
            json.put("Parameter5",txtStrasse.getText());
            json.put("Parameter6",txtVorname.getText());
            json.put("Parameter7",txtStudienfach.getText());
            json.put("Parameter8",selectedFile);
            ClientStart.verbinden.send(json);

            //
            try{
                int Matrikelnummer= CheckEingabeRegistrierung.Matrikel();
                alert.setTitle("Erfolgreich");
                alert.setHeaderText("Erfolgreiche Registrieung");
                alert.setContentText("Sie haben sich erfolgreich Registriert. \nIhr Matrikelnummer ist: " + Matrikelnummer);
                alert.showAndWait();

                //Fenster Schließen und neues Fenster erstellen
                Stage stage;
                Parent root;
                Scene scene;

                ((Stage) panelRegistierenStudent.getScene().getWindow()).close();
                stage = new Stage();
                root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Login.fxml")));
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }catch (Exception ex){
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

        ((Stage) panelRegistierenStudent.getScene().getWindow()).close();
        stage = new Stage();
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Login.fxml")));
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void init(){
        Stage stage= (Stage) panelRegistierenStudent.getScene().getWindow();
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
