package Client.Lehrveranstaltung;

import Client.Bewertungen.BewertungController;
import Client.Bewertungen.BewertungErstellenController;
import Client.Bewertungen.BewertungsStatistikController;
import Client.ProjektGruppen.NeuePGController;
import Client.Quiz.QuizController;
import Client.Quiz.QuizErstellenController;
import Client.Quiz.QuizStatistikController;
import Server.ClientStart;
import Client.javaFx.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class LVUebersichtsseiteController {


    public Button btnQuizTeilnehmen;
    public Button btnQuizErstellen;
    //public ListView listQuizliste;
    public Button buttonTeilnehmerliste;
    public Button downloadLM;
    public Button aktuellButton;
    public Button PGHinzufuegenButton;
    public Button PGBeitretenButton;
    public Button PGAnzeigenButton;
    public Button buttonHauptseite;
    public Label LVlehrmaterialien;

    private Parent root;
    private Stage stage;
    private Scene scene;

    @FXML
    private Button btnBewertungErstellen;
    @FXML
    private Button btnBewertungTeilnehmen;
    @FXML
    private ListView<String> listBewertungsListe;
    @FXML
    private ListView<String> listQuizliste;
    @FXML
    private Label LVtyp;
    @FXML
    private Label LVname;
    @FXML
    private Label LVsemester;
    @FXML
    private ChoiceBox<String> boxLehrmaterial;
    @FXML
    private ChoiceBox<String> PGChoiceBox;
    @FXML
    private Label currentIDLabel;
    @FXML
    private  Label currentLVIDLabel;
    @FXML
    private Pane LVuebersichtsseite;
    @FXML
    private Button addLM;
    private int userID;
    private int lvID;
    private int pgID;
    private boolean checkUserTyp;
    List<Integer> quizListeIDs = new ArrayList<>();
    List<Integer> bewertungListeIDs = new ArrayList<>();

    //ID übernehmen und umwandeln
    public void setIDLabel(int pUID){
        Integer integer = pUID;
        currentIDLabel.setText(integer.toString());
    }
    //LVID übernehmen und umwandeln
    public void setLVIDLabel(int pUID){
        Integer integer = pUID;
        currentLVIDLabel.setText(integer.toString());
    }

    //Methode zum initialisieren
    public void init() throws IOException, SQLException {
        String userIDstr = currentIDLabel.getText();
        userID = Integer.parseInt(userIDstr);
        String lvIDstr = currentLVIDLabel.getText();
        lvID = Integer.parseInt(lvIDstr);
        String lvname, lvtyp, lvsemester;
        //Boolean usertyp;
            JSONObject json = new JSONObject();
            json.put("Methode", "holeLVInfos");
            json.put("P1", lvID);
            json.put("P2", userID);
            ClientStart.verbinden.send(json);

        System.out.println("Gesendet lv");
            JSONObject json2 = ClientStart.verbinden.receiveobj();
        System.out.println("Bekommen lv");

            //LV-Name holen und in Label eintragen
            lvname = (String) json2.get("P1");
            LVname.setText(lvname);
            //LV-Typ holen und in Label eintragen
            lvtyp = (String) json2.get("P2");
            LVtyp.setText(lvtyp);
            //LV-Semester holen und in Label eintragen
            lvsemester = (String) json2.get("P3");
            LVsemester.setText(lvsemester);
            //schauen ob Lehrender oder Studierender
            checkUserTyp = (Boolean) json2.get("P4");
            //Button für neues Lehrmaterial nur sichtbar für Lehrende


        if(checkUserTyp == true){
            addLM.setVisible(true);
            btnQuizTeilnehmen.setText("Quiz Statistik");
            btnBewertungTeilnehmen.setText("Bewertung Statistik");
        } else {
            addLM.setVisible(false);
            btnQuizErstellen.setVisible(false);
            btnBewertungErstellen.setVisible(false);
        }

        System.out.println("2.Teil");

            JSONObject jsonLM = new JSONObject();
            jsonLM.put("Methode", "holeLVDateien");
            jsonLM.put("P1", lvID);
            ClientStart.verbinden.send(jsonLM); //
        System.out.println("LM gesendet");
            JSONArray jsonLM2 = ClientStart.verbinden.receiveary();
        System.out.println("LM bekommen");
            //Lehrmaterial anzeigen
            boxLehrmaterial.setValue("PDF wählen");
            ObservableList<String> lmNameList = FXCollections.observableArrayList();
            for (int i = 0; i < jsonLM2.length(); i++) {
            lmNameList.add((String) jsonLM2.get(i));
        }
            boxLehrmaterial.setItems(lmNameList);


            pgFuellen();

        /*List<String> quizListe = new ArrayList<>();
        quizListe.clear();
        try {
            Connection con = DriverManager.getConnection(url, user, passwort);
            int count = 0;
            String zaehler;
            PreparedStatement ps = con.prepareStatement("SELECT * FROM QUIZ where lehrveranstaltungid=?");
            ps.setInt(1, lvID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                quizListeIDs.add(rs.getInt("quizid"));
            }
            con.close();
            Set<Integer> set = new HashSet<>(quizListeIDs);
            quizListeIDs.clear();
            quizListeIDs.addAll(set);
            for (int i = 0; i < quizListeIDs.size(); i++) {
                count++;
                zaehler = Integer.toString(count);
                quizListe.add("Client.Quiz " + zaehler);
            }
            listQuizliste.getItems().clear();
            listQuizliste.getItems().addAll(quizListe);
            con.close();
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
        }*/
        JSONObject jsonQuiz = new JSONObject();
        jsonQuiz.put("Methode","getQuizliste");
        jsonQuiz.put("P1",lvID);
        ClientStart.verbinden.send(jsonQuiz);
        JSONArray jsonArray2 = ClientStart.verbinden.receiveary();
        Set<Integer> set2 = new HashSet<>(quizListeIDs);
        quizListeIDs.clear();
        quizListeIDs.addAll(set2);
        List<String> quizListe = new ArrayList<>();
        int count2 =0;
        for(int i=0; i<jsonArray2.length(); i++){
            quizListeIDs.add(jsonArray2.getInt(i));
            count2++;
            quizListe.add("Quiz " +count2);
        }
        listQuizliste.getItems().clear();
        listQuizliste.getItems().addAll(quizListe);

        JSONObject jsonBewertung = new JSONObject();
        jsonBewertung.put("Methode","getBewertungsliste");
        jsonBewertung.put("P1",lvID);
        ClientStart.verbinden.send(jsonBewertung);
        JSONArray jsonArray = ClientStart.verbinden.receiveary();
        Set<Integer> set = new HashSet<>(bewertungListeIDs);
        bewertungListeIDs.clear();
        bewertungListeIDs.addAll(set);
        List<String> bewertungsListe = new ArrayList<>();
        int count =0;
        for(int i=0; i<jsonArray.length(); i++){
            bewertungListeIDs.add(jsonArray.getInt(i));
            count++;
            bewertungsListe.add("Bewertung "+count);
        }
        listBewertungsListe.getItems().clear();
        listBewertungsListe.getItems().addAll(bewertungsListe);
    }





    //Methode für den Button zum wechseln zur Hauptseite
    @FXML
    public void zurHauptseite(ActionEvent klick) throws IOException {

        if(checkUserTyp == true) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Hauptseite(fürLehrender).fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            HauptseiteLehrenderController controllerL = loader.getController();
            controllerL.setIDLabel(userID);
            controllerL.init();
            stage.show();
        } else if(checkUserTyp == false) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Hauptseite(fürStudent).fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            HauptseiteStudentController controllerS = loader.getController();
            controllerS.setIDLabel(userID);
            controllerS.init();
            stage.show();
        }
        stage = (Stage) LVuebersichtsseite.getScene().getWindow();
        stage.close();

    }

    //Methode für den Button um neues Lehrmaterial hochzuladen(wechsel zur Ansicht NeuesLM)
    @FXML
    public void neuesLM(ActionEvent klick) throws IOException {
        stage = (Stage) LVuebersichtsseite.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/NeuesLM.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        NeuesLMController controller = loader.getController();
        controller.setLVIDLabel(lvID);
        controller.init();
        stage.show();
    }

    //Methode um Lehrmaterial herunterzuladen
    public void base64Umwandeln(String base) throws IOException, SQLException {
        String lmName = boxLehrmaterial.getValue();
        FileChooser speicherPDF = new FileChooser();
        speicherPDF.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.PDF"));
        Stage neueStage = new Stage();
        File newFile = speicherPDF.showSaveDialog(neueStage);
        if(newFile != null) {
            FileOutputStream speichern = new FileOutputStream(newFile);
            byte[] pdfDaten = Base64.getDecoder().decode(base);
            speichern.write(pdfDaten);
            speichern.close();
            Alert erfolgreich = new Alert(Alert.AlertType.INFORMATION);
            erfolgreich.setTitle("Erfolgreich");
            erfolgreich.setHeaderText("Erfolgreich");
            erfolgreich.setContentText("PDF <" + lmName + "> erfolgreich heruntergeladen.");
            erfolgreich.showAndWait();
            init();
        } else {

        }

    }
    @FXML
    void cmdBewertungErstellen(ActionEvent event) throws IOException {
        stage = (Stage) LVuebersichtsseite.getScene().getWindow();
        stage.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/BewertungErstellen.fxml"));
        stage.setScene(new Scene(loader.load()));
        BewertungErstellenController controller = loader.getController();
        controller.init(userID,lvID);
        stage.show();
    }
    @FXML
    void cmdQuizErstellen(ActionEvent event) throws IOException {
        stage = (Stage) LVuebersichtsseite.getScene().getWindow();
        stage.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/QuizErstellen.fxml"));
        stage.setScene(new Scene(loader.load()));
        QuizErstellenController controller = loader.getController();
        controller.setIDLabel(userID);
        controller.setLVIDLabel(lvID);
        controller.init();
        stage.show();
    }
    @FXML
    void cmdBewertungTeilnehmen(ActionEvent event) {
        try{
            String selected = listBewertungsListe.getSelectionModel().getSelectedItem();
            String[] parts = selected.split(" ");
            String selectedBewertungIdString = parts[1];
            int selectedBewertungID= Integer.parseInt(selectedBewertungIdString)-1;
            selectedBewertungID = bewertungListeIDs.get(selectedBewertungID);

            JSONObject json2 = new JSONObject();
            json2.put("Methode","hatHaelfteBearbeitet");
            json2.put("P1",userID);
            json2.put("P2",lvID);
            ClientStart.verbinden.send(json2);
            JSONObject jsonReceive2 = ClientStart.verbinden.receiveobj();
            String jsonString2 = jsonReceive2.get("P1")+"";

            if(btnBewertungTeilnehmen.getText().equals("Bewertung Statistik")){
                stage = (Stage) LVuebersichtsseite.getScene().getWindow();
                stage.close();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Bewertungsstatistik.fxml"));
                stage.setScene(new Scene(loader.load()));
                BewertungsStatistikController controller = loader.getController();
                controller.init(selectedBewertungID,userID,lvID);
                stage.show();
            }
            else if(jsonString2.equals("true")){
                JSONObject json = new JSONObject();
                json.put("Methode","filterBewertungTeilnahme");
                json.put("P1",userID);
                json.put("P2",selectedBewertungID);
                ClientStart.verbinden.send(json);
                JSONObject jsonReceive = ClientStart.verbinden.receiveobj();
                String jsonString = jsonReceive.get("P1")+"";
                if(jsonString.equals("nicht teilgenommen")){
                    stage = (Stage) LVuebersichtsseite.getScene().getWindow();
                    stage.close();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Bewertung.fxml"));
                    stage.setScene(new Scene(loader.load()));
                    BewertungController controller = loader.getController();
                    controller.init(userID,lvID,selectedBewertungID,0);
                    stage.show();
                }
                else if(jsonString.equals("teilgenommen")){
                    Alert erfolgreich = new Alert(Alert.AlertType.INFORMATION);
                    erfolgreich.setTitle("Alert");
                    erfolgreich.setHeaderText("Bereits teilgenommen!");
                    erfolgreich.setContentText("Sie können die selbe Bewertung nicht mehrfach ausführen!");
                    erfolgreich.showAndWait();
                }
                else {
                    System.out.println("Weder teilgenommen noch nicht teilgenommen?!");
                }

            }
            else if(jsonString2.equals("false")){
                Alert erfolgreich = new Alert(Alert.AlertType.INFORMATION);
                erfolgreich.setTitle("Alert");
                erfolgreich.setHeaderText("Bearbeiten Sie erst Quizze!");
                erfolgreich.setContentText("Sie können erst teilnehmen, wenn Sie die Hälfte der Quizze bearbeitet haben!");
                erfolgreich.showAndWait();
            }
            else{
                System.out.println("Fehler in cmdBewertungTeilnehmen");
            }
        }
        catch(Exception exception){
            System.out.println("Bitte wählen Sie etwas aus!");
        }
    }
    @FXML
    void cmdQuizTeilnehmen(ActionEvent event) throws IOException {
        try{
            String selected = listQuizliste.getSelectionModel().getSelectedItem();
            String[] parts = selected.split(" ");
            String selectedQuizIdString = parts[1];
            int selectedQuizID= Integer.parseInt(selectedQuizIdString)-1;
            selectedQuizID = quizListeIDs.get(selectedQuizID);
            if(btnQuizTeilnehmen.getText().equals("Quiz Statistik")){
                stage = (Stage) LVuebersichtsseite.getScene().getWindow();
                stage.close();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/QuizStatistik.fxml"));
                stage.setScene(new Scene(loader.load()));
                QuizStatistikController controller = loader.getController();
                controller.init(selectedQuizID,userID,lvID);
                stage.show();
            }
            else{
                stage = (Stage) LVuebersichtsseite.getScene().getWindow();
                stage.close();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Quiz.fxml"));
                stage.setScene(new Scene(loader.load()));
                QuizController controller = loader.getController();
                controller.setIDLabel(userID);
                controller.setLVIDLabel(lvID);
                controller.init(selectedQuizID,0);
                stage.show();
            }
        }
        catch(Exception exception){
            System.out.println("Bitte wählen Sie etwas aus!");
        }

    }

    //Methode für den Button um die Teilnehmerliste zu öffnen(wechsel zur Ansicht Teilnehmerliste)
    @FXML
    public void openTeilnehmerliste(ActionEvent klick) throws IOException {
        stage = (Stage) LVuebersichtsseite.getScene().getWindow();
        stage.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/TeilnehmerListeView.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        TeilnehmerListeController controller = loader.getController();
        controller.setIDLabel(userID);
        controller.setLVIDLabel(lvID);
        controller.init();
        stage.show();
    }

    //Methode um das gewählte Lehrmaterial herunterzuladen
    public void holeLM() throws IOException, SQLException {
        String lmName = boxLehrmaterial.getValue();
        if(boxLehrmaterial.getValue() == "PDF wählen"){
            Alert fehlgeschlagen = new Alert(Alert.AlertType.ERROR);
            fehlgeschlagen.setTitle("Fehlgeschlagen");
            fehlgeschlagen.setHeaderText("Fehlgeschlagen");
            fehlgeschlagen.setContentText("Es wurde noch keine PDF(Lehrmaterial) gewählt.");
            fehlgeschlagen.showAndWait();
        } else {
            JSONObject json = new JSONObject();
            json.put("Methode", "holeLM");
            json.put("P1", lvID);
            json.put("P2", lmName);
            ClientStart.verbinden.send(json);
            JSONObject json2 = ClientStart.verbinden.receiveobj();
            System.out.println(json2.get("P1")+"");
            base64Umwandeln((String) json2.get("P1"));

        }
    }
    //Methode für den Buttom um einemal die init()-Methode neu auszuführen(damit neues Lehrmaterial angezeigt wird)
    public void aktuellisiereLMListe() throws IOException, SQLException {
        init();
    }

    //Ab hier Änderungen für Zyklus 2 + alte init()-Methode

    //Methode um neue Projektgruppe zu erstellen
    @FXML
    public void neuePG(ActionEvent klick) throws IOException {
        stage = (Stage) LVuebersichtsseite.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/NeuePG.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        NeuePGController controller = loader.getController();
        controller.setIDLabel(userID);
        controller.setLVIDLabel(lvID);
        controller.init();
        stage.show();
    }

    //Methode um einer Projektgruppe beizutreten
    @FXML
    public void projektBeitreten() throws IOException {
        String pgName = PGChoiceBox.getValue();
        int nutzerID = userID;
        //Checken ob Projektgruppe gewählt wurde
        if (PGChoiceBox.getValue() == "Projektgruppe wählen") {
            Alert fehlgeschlagen = new Alert(Alert.AlertType.ERROR);
            fehlgeschlagen.setTitle("Fehlgeschlagen");
            fehlgeschlagen.setHeaderText("Fehlgeschlagen");
            fehlgeschlagen.setContentText("Es wurde noch keine Projektgruppe gewählt.");
            fehlgeschlagen.showAndWait();
        } else {
            JSONObject json = new JSONObject();
            json.put("Methode", "holeProjektID");
            json.put("P1", pgName);
            json.put("P2", lvID);
            ClientStart.verbinden.send(json);
            JSONObject json2 = ClientStart.verbinden.receiveobj();
            int projektID = Integer.parseInt(json2.get("P1")+"");

            JSONObject checkVorhandenPG = new JSONObject();
            checkVorhandenPG.put("Methode", "holeCheckVorhandenPG");
            checkVorhandenPG.put("P1", nutzerID);
            checkVorhandenPG.put("P2", projektID);
            ClientStart.verbinden.send(checkVorhandenPG);
            JSONObject checkVorhandenPG2 = ClientStart.verbinden.receiveobj();
            boolean vorhanden = (Boolean) checkVorhandenPG2.get("P1");
            if (vorhanden) {
                Alert fehlgeschlagen = new Alert(Alert.AlertType.ERROR);
                fehlgeschlagen.setTitle("Fehlgeschlagen");
                fehlgeschlagen.setHeaderText("Fehlgeschlagen");
                fehlgeschlagen.setContentText("Sie sind bereits Teilnehmer dieser Projektgruppe.");
                fehlgeschlagen.showAndWait();
            } else {
                JSONObject userinPG = new JSONObject();
                userinPG.put("Methode", "userPGHinzufuegen");
                userinPG.put("P1", userID);
                userinPG.put("P2", projektID);
                ClientStart.verbinden.send(userinPG);
                Alert erfolgreich = new Alert(Alert.AlertType.INFORMATION);
                erfolgreich.setTitle("Erfolgreich");
                erfolgreich.setHeaderText("Erfolgreich");
                erfolgreich.setContentText("Sie wurden erfolgreich zur Projektgruppe <" + pgName + "> hinzugefügt.");
                erfolgreich.showAndWait();

            }
        }
    }

    //Methode zum Anzeigen der gewählten Projektgruppe
    @FXML
    public void projektAnzeigen() throws IOException, SQLException {
        String pgName = PGChoiceBox.getValue();
        int nutzerID = userID;
        //Checken ob Projektgruppe gewählt wurde
        if(PGChoiceBox.getValue() == "Projektgruppe wählen"){
            Alert fehlgeschlagen = new Alert(Alert.AlertType.ERROR);
            fehlgeschlagen.setTitle("Fehlgeschlagen");
            fehlgeschlagen.setHeaderText("Fehlgeschlagen");
            fehlgeschlagen.setContentText("Es wurde noch keine Projektgruppe gewählt.");
            fehlgeschlagen.showAndWait();
        } else {
            JSONObject json = new JSONObject();
            json.put("Methode", "holeProjektID");
            json.put("P1", pgName);
            json.put("P2", lvID);
            ClientStart.verbinden.send(json);
            JSONObject json2 = ClientStart.verbinden.receiveobj();
            pgID = Integer.parseInt(json2.get("P1")+"") ;

            JSONObject checkVorhandenPG = new JSONObject();
            checkVorhandenPG.put("Methode", "holeCheckVorhandenPG");
            checkVorhandenPG.put("P1", nutzerID);
            checkVorhandenPG.put("P2", pgID);
            ClientStart.verbinden.send(checkVorhandenPG);
            JSONObject checkVorhandenPG2 = ClientStart.verbinden.receiveobj();
            boolean vorhanden = (Boolean) checkVorhandenPG2.get("P1");
                //Checken ob User bereits Teilnehmer der Projekgruppe ist
                if (!vorhanden) {
                    Alert fehlgeschlagen = new Alert(Alert.AlertType.ERROR);
                    fehlgeschlagen.setTitle("Fehlgeschlagen");
                    fehlgeschlagen.setHeaderText("Fehlgeschlagen");
                    fehlgeschlagen.setContentText("Sie sind kein Teilnehmer dieser Projektgruppe.\nBitte zuerst der Projektgruppe beitreten.");
                    fehlgeschlagen.showAndWait();
                } else {
                    //Übersichtsseite der Projektgruppe aufrufen.
                    stage = (Stage) LVuebersichtsseite.getScene().getWindow();
                    stage.close();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/PGUebersichtsseite.fxml"));
                    Stage stage = new Stage();
                    stage.setScene(new Scene(loader.load()));
                    Client.ProjektGruppen.PGUebersichtsseiteController controller = loader.getController();
                    controller.setIDLabel(userID);
                    controller.setLVIDLabel(lvID);
                    controller.setPGIDLabel(pgID);
                    controller.init();
                    stage.show();
                }
        }

    }

    //hinzugefügt
    public void pgFuellen() throws IOException {
        JSONObject jsonPG = new JSONObject();
        jsonPG.put("Methode", "holePGNamen");
        jsonPG.put("P1", lvID);
        ClientStart.verbinden.send(jsonPG);
        JSONArray jsonPG2 = ClientStart.verbinden.receiveary();
        //Projektgruppen anzeigen (Zyklus 2)
        PGChoiceBox.setValue("Projektgruppe wählen");
        ObservableList<String> pgNameList = FXCollections.observableArrayList();
        for (int i = 0; i < jsonPG2.length(); i++) {
            pgNameList.add((String) jsonPG2.get(i));
        }
        PGChoiceBox.setItems(pgNameList);

    }

}