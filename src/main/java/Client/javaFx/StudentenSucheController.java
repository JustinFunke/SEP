package Client.javaFx;

import Server.ClientStart;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Fürs bessere Verständnis von PreparedStatements in SQL wurde folgende Seite benutzt: https://javabeginners.de/Datenbanken/Prepared_Statement.php

public class StudentenSucheController {

    @FXML
    private AnchorPane scenePane;
    private Stage stage;
    private Parent root;
    private Scene scene;
    @FXML
    private TextField VornameText;
    @FXML
    private TextField NachnameText;
    @FXML
    private TextField MatrikelNrText;
    @FXML
    private Button SearchButton;
    @FXML
    private Button BackButton;
    @FXML
    private ChoiceBox<String> StudentenChoice;
    @FXML
    private ChoiceBox<String> KurseChoice;
    @FXML
    private Button AddButton;
    @FXML
    private Label currentIDLabel;

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
    public void setIDLabel(int pUID) {
        Integer integer = pUID;
        currentIDLabel.setText(integer.toString());
        String test3 = currentIDLabel.getText();
        int test2 = Integer.parseInt(test3);
        currentUserID = test2;
    }

    public void SearchButtonOnAction(ActionEvent event) throws IOException {
        StudentenChoice.getItems().clear();
        KurseChoice.getItems().clear();
        if (!MatrikelNrText.getText().isEmpty()) {                                     //Zeile 53 - 70 Überprüfung Ob die Matrikelnummer wirklich aus Zahlen besteht
            try {
                int Matrikelnummer = Integer.parseInt(MatrikelNrText.getText());
                System.out.println(Matrikelnummer);
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Mitteilung");
                alert.setHeaderText(null);
                alert.setContentText("Bitte nur Zahlen eingeben");
                alert.showAndWait();
                return;
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("null");
                alert.setContentText("Fehlermeldung");
                alert.showAndWait();
                return;
            }
        }
        if (!MatrikelNrText.getText().isEmpty() || !VornameText.getText().isEmpty() || !NachnameText.getText().isEmpty()) {
            List<String> LVListe = new ArrayList<>();
            JSONObject json = new JSONObject();
            json.put("Methode","listeAllerLvsLehrender");
            json.put("Parameter1",currentUserID);
            ClientStart.verbinden.send(json);
            System.out.println();
            JSONArray jsonArray = ClientStart.verbinden.receiveary();
            ///immer für String Arrays
            if(jsonArray != null){
                for(int i =0;i<jsonArray.length();i++){
                    LVListe.add((String) jsonArray.get(i) +"");
                }
            }
            System.out.println("angekommen");
            KurseChoice.getItems().addAll(LVListe);
//            List<String> VeranstaltungList = new ArrayList<>();
//            try {
//                Connection con = DriverManager.getConnection(urlDB, user, passwort);
//                PreparedStatement psL = con.prepareStatement("SELECT * FROM LEHRVERANSTALTUNG WHERE LEHRENDERID=?");
//                psL.setInt(1,currentUserID);
//                ResultSet rsl = psL.executeQuery();
//                while (rsl.next()){
//                    VeranstaltungList.add(rsl.getString("NAME"));
//                }
//                KurseChoice.getItems().addAll(VeranstaltungList);
//                con.close();
//            } catch (SQLException throwables) {
//                System.out.println(throwables.getMessage());
//
//            } // Hinzufügen aller Lehrveranstaltungen beendet

            if (!MatrikelNrText.getText().isEmpty()) {     // Beginn der Suche nach Studenten
                List<String> StudentenListe = new ArrayList<>();
                JSONObject json1 = new JSONObject();
                json1.put("Methode","studentenSucheMatrikel");
                json1.put("Parameter1",MatrikelNrText.getText());
                ClientStart.verbinden.send(json1);
                System.out.println("Sent");
                JSONArray jsonArray1 = ClientStart.verbinden.receiveary();
                ///immer für String Arrays
                if(jsonArray1 != null){
                    for(int i =0;i<jsonArray1.length();i++){
                        StudentenListe.add((String) jsonArray1.get(i) +"");
                    }
                }
                System.out.println("angekommen");
                StudentenChoice.getItems().addAll(StudentenListe);
//                try {                                       // Suche Über Matrikelnummer falls vorhanden
//                    List<String> StudentenList = new ArrayList<>();
//                    Connection con = DriverManager.getConnection(urlDB, user, passwort);
//                    PreparedStatement ps = con.prepareStatement("SELECT * FROM USER WHERE ID=?");
//                    ps.setInt(1,Integer.parseInt(MatrikelNrText.getText()));
//                    ResultSet rs = ps.executeQuery();
//                    StudentenList.clear();
//                    while (rs.next()) {
//                        if(!rs.getBoolean("LEHRENDER")) {
//                            String NameZusammen = rs.getString("VORNAME") + " " + rs.getString("NACHNAME");
//                            StudentenList.add(NameZusammen);
//                        }
//                    }
//                    StudentenChoice.getItems().addAll(StudentenList);
//                    con.close();
//                } catch (SQLException throwables) {
//                    System.out.println(throwables.getMessage());
//
//                }
            }
            else if(!VornameText.getText().isEmpty()&& !NachnameText.getText().isEmpty()){
                List<String> vorNachSucheErgebnisse = new ArrayList<>();
                JSONObject jsonvornach = new JSONObject();
                jsonvornach.put("Methode","studentenSucheVorNach");
                jsonvornach.put("Parameter1",VornameText.getText());
                jsonvornach.put("Parameter2",NachnameText.getText());
                ClientStart.verbinden.send(jsonvornach);
                System.out.println("SentVorNach");
                JSONArray jsonvorNachergebnisse = ClientStart.verbinden.receiveary();
                if(jsonvorNachergebnisse != null){
                    for(int i =0;i<jsonvorNachergebnisse.length();i++){
                        vorNachSucheErgebnisse.add((String) jsonvorNachergebnisse.get(i) +"");
                    }
                }
                System.out.println("angekommen");
                StudentenChoice.getItems().addAll(vorNachSucheErgebnisse);
//                try {
//                    List<String> StudentenList = new ArrayList<>();
//                    Connection con = DriverManager.getConnection(urlDB, user, passwort);
//                    PreparedStatement ps = con.prepareStatement("SELECT * FROM USER WHERE VORNAME=? AND NACHNAME= ?");
//                    ps.setString(1,VornameText.getText());
//                    ps.setString(2,NachnameText.getText());
//                    ResultSet rs = ps.executeQuery();
//                    StudentenList.clear();
//                    while (rs.next()) {
//                        if(!rs.getBoolean("LEHRENDER")) {
//                            String NameZusammen = rs.getString("VORNAME") + " " + rs.getString("NACHNAME");
//                            StudentenList.add(NameZusammen);
//                        }
//                    }
//                    StudentenChoice.getItems().addAll(StudentenList);
//                    con.close();
//                } catch (SQLException throwables) {
//                    System.out.println(throwables.getMessage());
//
//                }
            }
            else if(!VornameText.getText().isEmpty()){
                List<String> vorSucheErgebnisse = new ArrayList<>();
                JSONObject jsonvor = new JSONObject();
                jsonvor.put("Methode","studentenSucheVor");
                jsonvor.put("Parameter1",VornameText.getText());
                ClientStart.verbinden.send(jsonvor);
                System.out.println("SentVor");
                JSONArray jsonvorergebnisse = ClientStart.verbinden.receiveary();
                if(jsonvorergebnisse != null){
                    for(int i =0;i<jsonvorergebnisse.length();i++){
                        vorSucheErgebnisse.add((String) jsonvorergebnisse.get(i) +"");
                    }
                }
                System.out.println("angekommen");
                StudentenChoice.getItems().addAll(vorSucheErgebnisse);
//                try {                                       // Suche Über Vornamen wenn keine Matrikelnummer vorhanden ist
//                    List<String> StudentenList = new ArrayList<>();
//                    Connection con = DriverManager.getConnection(urlDB, user, passwort);
//                    PreparedStatement ps = con.prepareStatement("SELECT * FROM USER WHERE VORNAME=?");
//                    ps.setString(1,VornameText.getText());
//                    ResultSet rs = ps.executeQuery();
//                    StudentenList.clear();
//                    while (rs.next()) {
//                        if(!rs.getBoolean("LEHRENDER")) {
//                            String NameZusammen = rs.getString("VORNAME") + " " + rs.getString("NACHNAME");
//                            StudentenList.add(NameZusammen);
//                        }
//                    }
//                    StudentenChoice.getItems().addAll(StudentenList);
//                    con.close();
//                } catch (SQLException throwables) {
//                    System.out.println(throwables.getMessage());
//
//                }
            }
            else if(!NachnameText.getText().isEmpty()){
                List<String> nachSucheErgebnisse = new ArrayList<>();
                JSONObject jsonnach = new JSONObject();
                jsonnach.put("Methode","studentenSucheNach");
                jsonnach.put("Parameter1",NachnameText.getText());
                ClientStart.verbinden.send(jsonnach);
                System.out.println("Sentnach");
                JSONArray jsonNachergebnisse = ClientStart.verbinden.receiveary();
                if(jsonNachergebnisse != null){
                    for(int i =0;i<jsonNachergebnisse.length();i++){
                        nachSucheErgebnisse.add((String) jsonNachergebnisse.get(i) +"");
                    }
                }
                System.out.println("angekommen");
                StudentenChoice.getItems().addAll(nachSucheErgebnisse);
//                try {                                       // Suche Über Nachnamen wenn keine Matrikelnummer vorhanden ist
//                    List<String> StudentenList = new ArrayList<>();
//                    Connection con = DriverManager.getConnection(urlDB, user, passwort);
//                    PreparedStatement ps = con.prepareStatement("SELECT * FROM USER WHERE NACHNAME=?");
//                    ps.setString(1,NachnameText.getText());
//                    ResultSet rs = ps.executeQuery();
//                    StudentenList.clear();
//                    while (rs.next()) {
//                        if(!rs.getBoolean("LEHRENDER")) {
//                            String NameZusammen = rs.getString("VORNAME") + " " + rs.getString("NACHNAME");
//                            StudentenList.add(NameZusammen);
//                        }
//                    }
//                    StudentenChoice.getItems().addAll(StudentenList);
//                    con.close();
//                } catch (SQLException throwables) {
//                    System.out.println(throwables.getMessage());
//
//                }
            }


        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Kein Feld ausgefüllt");
            alert.setContentText("Bitte mind. ein Feld ausfüllen");
            alert.showAndWait();
            return;
        }


    }
    public void AddButtonOnAction(ActionEvent event){
        if(!(StudentenChoice.getValue()==null) || !(KurseChoice.getValue()==null)) {
            String ausgewähltesItem = StudentenChoice.getValue();
            String fullName = ausgewähltesItem;
            int idx = fullName.lastIndexOf(' ');
            if (idx == -1) {
                throw new IllegalArgumentException("Only a single name: " + fullName);
            }
            String firstName = fullName.substring(0, idx);
            String lastName = fullName.substring(idx + 1);
            JSONObject json = new JSONObject();
            json.put("Methode", "studentenSucheAdd");
            json.put("Parameter1", firstName);
            json.put("Parameter2", lastName);
            json.put("Parameter3", KurseChoice.getValue());
            ClientStart.verbinden.send(json);
            System.out.println("AddSent");
        }
//        try {
//            Connection con = DriverManager.getConnection(urlDB, user, passwort);
//            Statement st = con.createStatement();
//            PreparedStatement ps = con.prepareStatement("SELECT * FROM USER WHERE VORNAME=? AND NACHNAME=?");
//            ps.setString(1, firstName);
//            ps.setString(2, lastName);
//            ResultSet rs = ps.executeQuery();
//            rs.next();
//            int SearchedID = rs.getInt("ID");
//            PreparedStatement psLV = con.prepareStatement("SELECT * FROM LEHRVERANSTALTUNG WHERE NAME=?");
//            psLV.setString(1,KurseChoice.getValue());
//            ResultSet rsLV = psLV.executeQuery();
//            rsLV.next();
//            int SearchedLVID = rsLV.getInt("ID");
//            System.out.println("Student " + StudentenChoice.getValue() + " soll dem Kurs " + KurseChoice.getValue() + " hinzugefügt werden");
//            Datenbank.DbUserLvHinzufügen(SearchedID,SearchedLVID);
//            con.close();
//        }
//        catch (SQLException throwables){
//            System.out.println(throwables.getMessage());
//
//        }

    }


    public void BackButtonOnAction(ActionEvent event) throws IOException {
        Boolean LehrenderAusgabe = false;
        ArrayList<String> tmp = new ArrayList<>();
        JSONObject json = new JSONObject();
        json.put("Methode","checkLehrender");
        json.put("Parameter1",currentUserID);
        ClientStart.verbinden.send(json);
        JSONArray jsonArray1 = ClientStart.verbinden.receiveary();
        ///immer für String Arrays
        if(jsonArray1 != null) {
            for (int i = 0; i < jsonArray1.length(); i++) {
                tmp.add((String) jsonArray1.get(i) + "");
            }
        }
//        try{
//            Connection con = DriverManager.getConnection(urlDB,user,passwort);
//            Statement st = con.createStatement();
//            PreparedStatement ps = con.prepareStatement("SELECT LEHRENDER FROM USER WHERE ID=?");
//            ps.setInt(1,currentUserID);
//            ResultSet rs = ps.executeQuery();
//            rs.next();
//            LehrenderAusgabe = rs.getBoolean("LEHRENDER");
//            System.out.println(LehrenderAusgabe);
//            con.close();
//        }
//
//        catch (SQLException throwables){
//            System.out.println(throwables.getMessage());
//
//        }
        LehrenderAusgabe= Boolean.parseBoolean(tmp.get(0));
        if(LehrenderAusgabe){
            Stage stage1 = (Stage) BackButton.getScene().getWindow();
            stage1.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Hauptseite(fürLehrender).fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            HauptseiteLehrenderController LehrenderMaincontroller = loader.getController();
            LehrenderMaincontroller.setIDLabel(currentUserID);
            LehrenderMaincontroller.init();
            stage.show();
        }
        else {
            Stage stage1 = (Stage) BackButton.getScene().getWindow();
            stage1.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Hauptseite(fürStudent).fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            HauptseiteStudentController StudentMaincontroller = loader.getController();
            StudentMaincontroller.setIDLabel(currentUserID);
            StudentMaincontroller.init();
            stage.show();

        }
    }


}
