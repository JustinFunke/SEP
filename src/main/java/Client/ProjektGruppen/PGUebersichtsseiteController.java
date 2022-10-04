package Client.ProjektGruppen;

import Server.ClientStart;
import Client.Lehrveranstaltung.LVUebersichtsseiteController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.json.JSONArray;
import org.json.simple.JSONObject;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Timer;
import java.util.TimerTask;

public class PGUebersichtsseiteController {


    public ListView ListTodoListe;
    public ListView ListToDoUser;
    public TextField txtTodoEingabe;
    public TextArea txtChat;
    public TextField txtEingabe;
    public ArrayList<String> nachrichtenalt;
    public ArrayList<Integer> useralt;

    @FXML
    private Label currentIDLabel;
    @FXML
    private Label currentLVIDLabel;
    @FXML
    private Label currentPGIDLabel;
    @FXML
    private Label PGname;
    @FXML
    private Pane PGuebersichtsseite;
    @FXML
    private ChoiceBox<String> PGDateienBox;
    @FXML
    private ChoiceBox<Integer> ToDoUserChoice;
    @FXML
    private Button addUserPG;
    private Stage stage;
    private int userID;
    private int lvID;
    private int pgID;
    private boolean checkUserTyp;
    TimerTask refreshTask = new TimerTask() {
        public void run() {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Timer Läuft");
                    chatAnzeigen();



                    try {
                        ToDoListeAufrufen();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ToDoUserListeAufrufen();
                    System.out.println("läuft");



                }
            });

        }
    };
    Timer refresh = new Timer();


    //ID übernehmen und umwandeln
    public void setIDLabel(int pUID) {
        Integer integer = pUID;
        currentIDLabel.setText(integer.toString());
    }

    //LVID übernehmen und umwandeln
    public void setLVIDLabel(int pUID) {
        Integer integer = pUID;
        currentLVIDLabel.setText(integer.toString());
    }

    //PGID übernehmen und umwandeln
    public void setPGIDLabel(int pUID) {
        Integer integer = pUID;
        currentPGIDLabel.setText(integer.toString());
    }

    //Methode zum initialisieren
    public void init() throws IOException, SQLException {
        System.out.println("PG Hier bin ich");
        String userIDstr = currentIDLabel.getText();
        userID = Integer.parseInt(userIDstr);
        String lvIDstr = currentLVIDLabel.getText();
        lvID = Integer.parseInt(lvIDstr);
        String pgIDstr = currentPGIDLabel.getText();
        pgID = Integer.parseInt(pgIDstr);
        String pgname;


        refresh.schedule(refreshTask, 1, 1000 * 4);
        System.out.println("PG Hier bin ich");
        //ToDoliste aufrufen
        ToDoListeAufrufen();

        System.out.println("PG Hier bin ich2");
        //ToDoUser aufrufen
        ToDoUserListeAufrufen();
        System.out.println("PG Hier bin ich3");

        chatAnzeigen();

        //Anzeigen User in ChoiceBox-ToDoListe
        anzeigenUser();
        System.out.println("PG Hier bin ich4");

        //Anezigen Dateien in ChoiceBox und User in ChoiceBox
        anzeigenDateien();
        System.out.println("PG Hier bin ich5");





            // System.out.println(pgID);
            //PG-Name holen und in Label eintragen
            //Sende
            JSONObject json = new JSONObject();
            json.put("Methode", "holePGInfos");
            json.put("P1", pgID);
            json.put("P2", userID);
            ClientStart.verbinden.send(json);
            //Empfange
            JSONObject json2 = ClientStart.verbinden.receiveobj();
            pgname = (String) json2.get("P1");
            //Setze Name
            PGname.setText(pgname);
            //schauen ob Lehrender oder Studierender
            checkUserTyp = (Boolean) json2.get("P2");
            //Button für "neuen Teilnehmer manuell hinzufügen" nur sichtbar für Lehrende
            if (checkUserTyp == true) {
                System.out.println("Ich bin ein Lehrender");
                addUserPG.setVisible(true);
                addUserPG.setDisable(false);
            } else {
                addUserPG.setVisible(false);
            }

            stage = (Stage) PGuebersichtsseite.getScene().getWindow();
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent windowEvent) {
                    System.out.println("X Button");
                    refreshTask.cancel();
                    JSONObject json = new JSONObject();
                    json.put("Methode","closeConnection");
                    ClientStart.verbinden.send(json);
                    try {
                        ClientStart.verbinden.socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.exit(0);
                }
            });

            //Timer


            txtChat.textProperty().addListener(new ChangeListener<Object>() {
                @Override
                public void changed(ObservableValue<?> observable, Object oldValue,
                                    Object newValue) {
                    txtChat.setScrollTop(Double.MIN_VALUE);
                }
            });
            System.out.println("PG hier bin ich 3");


            //Timer stopen wenn auf X

    }

        //Methode für den Button "Zurück zur Client.Lehrveranstaltung"
        @FXML
        public void zurLehrveranstaltung () throws IOException, SQLException {
            refreshTask.cancel();
            stage = (Stage) PGuebersichtsseite.getScene().getWindow();
            stage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LVUebersichtsseite.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            LVUebersichtsseiteController controller = loader.getController();
            controller.setIDLabel(userID);
            controller.setLVIDLabel(lvID);
            controller.init();
            stage.show();
        }

        public void ToDoListeAufrufen () throws IOException {
            //Fertig
            System.out.println("Bin in ToDoListeAufrufen");
            ArrayList<String> toDoListe = TodoListe.listAnzeigen(pgID);
            ListTodoListe.getItems().clear();
            ListTodoListe.getItems().addAll(toDoListe);

        }

        public void ToDoUserListeAufrufen () {
            //Fertig
            ArrayList<Integer> toDoUserListe = TodoListe.toDoUserListAnzeigen(pgID);
            ListToDoUser.getItems().clear();
            ListToDoUser.getItems().addAll(toDoUserListe);
        }

        public void cmdHinzufuegen_Clicked (MouseEvent mouseEvent) throws IOException {
            //Fertig
            TodoListe.toDoHinzufuegen(txtTodoEingabe.getText(), pgID, ToDoUserChoice.getValue());
            ToDoListeAufrufen();
            ToDoUserListeAufrufen();
        }


        public void cmdLoeschen_Clicked (MouseEvent mouseEvent) throws SQLException, IOException {
            //Fertig
            int selectedItem = -1;
            selectedItem = ListTodoListe.getSelectionModel().getSelectedIndex();
            if (selectedItem > -1) {
                String ToDO = (String) ListTodoListe.getSelectionModel().getSelectedItem();
                TodoListe.toDoLöschen(ToDO, pgID);
                ToDoListeAufrufen();
                ToDoUserListeAufrufen();
            }
        }

        //Anzeigen User in ChoiceBox
        public void anzeigenUser () throws IOException {
        System.out.println("meine USERID= " + userID);
            ToDoUserChoice.setValue(0);
            JSONObject json = new JSONObject();
            json.put("Methode", "holePGUser");
            json.put("P1", pgID);
            ClientStart.verbinden.send(json);

            JSONArray json2 = ClientStart.verbinden.receiveary();
            ObservableList<Integer> userList = FXCollections.observableArrayList();
            for (int i = 0; i < json2.length(); i++) {
                userList.add(Integer.parseInt(json2.get(i)+""));
            }
            ToDoUserChoice.setItems(userList);
        }

        //Dateien in ChoiceBox anzeigen
        public void anzeigenDateien () throws IOException {
            PGDateienBox.setValue("Datei wählen");
            JSONObject json = new JSONObject();
            json.put("Methode", "holePGDateien");
            json.put("P1", pgID);
            ClientStart.verbinden.send(json);

            JSONArray json2 = ClientStart.verbinden.receiveary();
            ObservableList<String> dateienNameList = FXCollections.observableArrayList();
            for (int i = 0; i < json2.length(); i++) {
                dateienNameList.add((String) json2.get(i));
            }
            PGDateienBox.setItems(dateienNameList);
        }

        //Methode um neue Dateien anzuzeigen
        @FXML
        public void aktualisiereDateien () throws IOException {
            anzeigenDateien();
            anzeigenUser();
            ToDoListeAufrufen();
            ToDoUserListeAufrufen();
        }

        //Methode für den Button "Teilnehmer hinzufügen"
        @FXML
        public void neuerTeilnehmer () throws IOException {
            stage = (Stage) PGuebersichtsseite.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProjektgruppeHinzufügen.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            ProjektgruppeHinzufügenController controller = loader.getController();
            controller.setPGIDLabel(pgID);
            controller.setLVIDLabel(lvID);
            stage.show();
        }

        //Methode um eine neue Datei hochzuladen(wechsel zur Ansicht NeueDateiPG)
        @FXML
        public void neueDatei (ActionEvent klick) throws IOException {
            stage = (Stage) PGuebersichtsseite.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/NeueDateiPG.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            NeueDateiPGController controller = loader.getController();
            controller.setPGIDLabel(pgID);
            controller.init();
            stage.show();
        }

        //Methode um die gewählte Datei von base64 zurück in PDF umzuwandeln und lokal zu speichern
        public void base64Umwandeln (String base) throws IOException {
            String dateiName = PGDateienBox.getValue();
            FileChooser speicherPDF = new FileChooser();
            speicherPDF.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.PDF"));
            Stage neueStage = new Stage();
            File newFile = speicherPDF.showSaveDialog(neueStage);
            if (newFile != null) {
                FileOutputStream speichern = new FileOutputStream(newFile);
                byte[] pdfDaten = Base64.getDecoder().decode(base);
                speichern.write(pdfDaten);
                speichern.close();
                Alert erfolgreich = new Alert(Alert.AlertType.INFORMATION);
                erfolgreich.setTitle("Erfolgreich");
                erfolgreich.setHeaderText("Erfolgreich");
                erfolgreich.setContentText("Datei <" + dateiName + "> wurde erfolgreich heruntergeladen.");
                erfolgreich.showAndWait();
                aktualisiereDateien();
            } else {

            }
        }

        //Methode um die gewählte Datei herunterzuladen
        @FXML
        public void downloadDatei () throws IOException {
            String gewaehlteDatei = PGDateienBox.getValue();
            if (PGDateienBox.getValue() == "Datei wählen") {
                Alert fehlgeschlagen = new Alert(Alert.AlertType.ERROR);
                fehlgeschlagen.setTitle("Fehlgeschlagen");
                fehlgeschlagen.setHeaderText("Fehlgeschlagen");
                fehlgeschlagen.setContentText("Es wurde noch keine Datei gewählt.");
                fehlgeschlagen.showAndWait();
            } else {

                JSONObject json = new JSONObject();
                json.put("Methode", "holePGDownloadDatei");
                json.put("P1", pgID);
                json.put("P2", gewaehlteDatei);
                ClientStart.verbinden.send(json);

                JSONObject json2 = ClientStart.verbinden.receiveobj();
                base64Umwandeln((String) json2.get("P1"));

            }
        }

        public void txtEingabe_KeyPressed (KeyEvent keyEvent){
            if (keyEvent.getCode().toString().equals("ENTER") && txtEingabe.getText() != "") {
                Chats.NachrichtSenden(txtEingabe.getText(), pgID, userID);
                chatAnzeigen();
                txtEingabe.setText("");

            }
        }

        public void chatAnzeigen () {
            //Fertig
            ArrayList<String> nachrichten = Chats.NachrichtenAnzeigen(pgID); //
            ArrayList<Integer> user = Chats.UserAnzeigen(pgID);
            System.out.println(nachrichten.size());
            System.out.println(user.size());
            txtChat.clear();
            System.out.println("Text leer");

                txtChat.setText("");
                for (int i = 0; i < nachrichten.size(); i++) {
                    txtChat.appendText("--------------------------------------------" + "\n");
                    txtChat.appendText(user.get(i) + " : " + nachrichten.get(i) + "\n");
                }

            txtChat.textProperty().addListener(new ChangeListener<Object>() {
                @Override
                public void changed(ObservableValue<?> observable, Object oldValue,
                                    Object newValue) {
                    txtChat.setScrollTop(Double.MIN_VALUE);
                }
            });


            txtChat.positionCaret(txtChat.getLength());


        }

    public void cmdLernkarten_Clicked(MouseEvent mouseEvent) throws IOException {
        stage = (Stage) PGuebersichtsseite.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/LernkartenUebersichtsSeite.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        LernkartenUebersichtsseiteController controller = loader.getController();
        controller.setPGIDLabel(pgID);
        controller.init();
        stage.show();
    }
}






