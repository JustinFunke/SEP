package Client.Quiz;

import Client.Lehrveranstaltung.LVUebersichtsseiteController;
import Server.ClientStart;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.sql.*;
import java.util.concurrent.TimeUnit;

public class QuizErstellenController {

    @FXML
    private Pane pane;

    @FXML
    private TextField txtQuizfrage;

    @FXML
    private TextField txtAntwort1;

    @FXML
    private TextField txtAntwort2;

    @FXML
    private TextField txtAntwort3;

    @FXML
    private TextField txtAntwort4;

    @FXML
    private ChoiceBox<String> choiceRichtigeA;

    @FXML
    private Label idLabel;

    @FXML
    private Label currentLVIDLabel;

    private Stage stage;

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



    public void setIDLabel(int id){
        Integer integer = id;
        idLabel.setText(integer.toString());
    }
    public void setLVIDLabel(int id){
        Integer integer = id;
        currentLVIDLabel.setText(integer.toString());
    }

    @FXML
    void cmdNeueFrage(ActionEvent event) throws IOException {
        if(!txtQuizfrage.getText().equals("")&& !txtAntwort1.getText().equals("")&& !txtAntwort2.getText().equals("")&& !txtAntwort3.getText().equals("")
                && !txtAntwort4.getText().equals("") && !choiceRichtigeA.getValue().equals(null)){
            String textQuizfrage = txtQuizfrage.getText();
            String textAntwort1 = txtAntwort1.getText();
            String textAntwort2 = txtAntwort2.getText();
            String textAntwort3 = txtAntwort3.getText();
            String textAntwort4 = txtAntwort4.getText();
            String richtigeA = choiceRichtigeA.getValue();
            JSONObject json = new JSONObject();
            json.put("Methode","insertQuizfrage");
            json.put("P1",textQuizfrage);
            json.put("P2",textAntwort1);
            json.put("P3",textAntwort2);
            json.put("P4",textAntwort3);
            json.put("P5",textAntwort4);
            json.put("P6",richtigeA);
            ClientStart.verbinden.send(json);
            try{
                TimeUnit.MILLISECONDS.sleep(200);
            }
            catch(Exception e){
                System.out.println(e);
            }
            /*int maxFN = 0;
            try {
                Connection con = DriverManager.getConnection(urlDB, user, passwort);
                try{
                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery("select max(fragennummer) as maxFN from Quizfragen where markiert=true");
                    while(rs.next()){
                        maxFN = rs.getInt("maxFN")+1;
                        System.out.println(maxFN);
                    }
                }
                catch (SQLException throwables) {
                    System.out.println(throwables.getMessage());
                }
                PreparedStatement ps = con.prepareStatement("INSERT INTO QUIZFRAGEN (quizfrage,antwort_a,antwort_b,antwort_c,antwort_d,richtige_antwort,markiert,fragennummer)VALUES (?, ?, ?, ?, ?, ?, true,?)");
                ps.setString(1, txtQuizfrage.getText());
                ps.setString(2, txtAntwort1.getText());
                ps.setString(3, txtAntwort2.getText());
                ps.setString(4, txtAntwort3.getText());
                ps.setString(5, txtAntwort4.getText());
                ps.setString(6, choiceRichtigeA.getValue());
                ps.setInt(7, maxFN);
                ps.executeUpdate();
                System.out.println("Quizfrage hinzugefügt! / Markierung auf TRUE");
                con.close();
            }
            catch (SQLException throwables) {
                System.out.println(throwables.getMessage());
            }*/
            stage = (Stage) pane.getScene().getWindow();
            stage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/QuizErstellen.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            QuizErstellenController controller = loader.getController();
            controller.setIDLabel(currentUserID);
            controller.setLVIDLabel(currentLVID);
            controller.init();
            stage.show();
        }
        else{
            Alert erfolgreich = new Alert(Alert.AlertType.INFORMATION);
            erfolgreich.setTitle("Alert");
            erfolgreich.setHeaderText("Felder leer!");
            erfolgreich.setContentText("Bitte füllen Sie erst alle Felder aus!");
            erfolgreich.showAndWait();
        }

    }

    @FXML
    void cmdSpeichern(ActionEvent event) throws IOException, SQLException {
        if(!txtQuizfrage.getText().equals("")&& !txtAntwort1.getText().equals("")&& !txtAntwort2.getText().equals("")&& !txtAntwort3.getText().equals("")
                && !txtAntwort4.getText().equals("") && !choiceRichtigeA.getValue().equals(null)){
            String textQuizfrage = txtQuizfrage.getText();
            String textAntwort1 = txtAntwort1.getText();
            String textAntwort2 = txtAntwort2.getText();
            String textAntwort3 = txtAntwort3.getText();
            String textAntwort4 = txtAntwort4.getText();
            String richtigeA = choiceRichtigeA.getValue();
            JSONObject json = new JSONObject();
            json.put("Methode","insertQuizfrage");
            json.put("P1",textQuizfrage);
            json.put("P2",textAntwort1);
            json.put("P3",textAntwort2);
            json.put("P4",textAntwort3);
            json.put("P5",textAntwort4);
            json.put("P6",richtigeA);
            ClientStart.verbinden.send(json);
            try{
                TimeUnit.MILLISECONDS.sleep(200);
            }
            catch(Exception e){
                System.out.println(e);
            }
            /*int maxFN = 0;
            try {
                Connection con = DriverManager.getConnection(urlDB, user, passwort);
                try{
                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery("select max(fragennummer) as maxFN from Quizfragen where markiert=true");
                    while(rs.next()){
                        maxFN = rs.getInt("maxFN")+1;
                    }
                }
                catch (SQLException throwables) {
                    System.out.println(throwables.getMessage());
                }
                PreparedStatement ps = con.prepareStatement("INSERT INTO QUIZFRAGEN (quizfrage,antwort_a,antwort_b,antwort_c,antwort_d,richtige_antwort,markiert,fragennummer)VALUES (?, ?, ?, ?, ?, ?, true, ?)");
                ps.setString(1, txtQuizfrage.getText());
                ps.setString(2, txtAntwort1.getText());
                ps.setString(3, txtAntwort2.getText());
                ps.setString(4, txtAntwort3.getText());
                ps.setString(5, txtAntwort4.getText());
                ps.setString(6, choiceRichtigeA.getValue());
                ps.setInt(7, maxFN);
                ps.executeUpdate();
                System.out.println("Quizfrage hinzugefügt!");
                con.close();
            }
            catch (SQLException throwables) {
                System.out.println(throwables.getMessage());
            }*/
            JSONObject json2 = new JSONObject();
            json2.put("Methode","insertQuiz");
            json2.put("P1",currentLVID);
            ClientStart.verbinden.send(json2);
            try{
                TimeUnit.MILLISECONDS.sleep(200);
            }
            catch(Exception e){
                System.out.println(e);
            }
            /*int quizid = 0;
            try{
                Connection con = DriverManager.getConnection(urlDB, user, passwort);
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("select max(QUIZID) as maxquizid from QUIZ");
                while(rs.next()){
                    quizid = rs.getInt("maxquizid")+1;
                }
            }
            catch (SQLException throwables) {
                System.out.println(throwables.getMessage());
            }
            try {
                Connection con = DriverManager.getConnection(urlDB, user, passwort);
                PreparedStatement ps = con.prepareStatement("SELECT * FROM QUIZFRAGEN WHERE MARKIERT=?");
                ps.setBoolean(1, true);
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    PreparedStatement ps2 = con.prepareStatement("INSERT INTO QUIZ VALUES (?, ?, ?)");
                    ps2.setInt(1,rs.getInt("ID"));
                    ps2.setInt(2,currentLVID);
                    ps2.setInt(3, quizid);
                    ps2.executeUpdate();
                    PreparedStatement ps3 = con.prepareStatement("UPDATE QUIZFRAGEN SET MARKIERT=? WHERE ID=?");
                    ps3.setBoolean(1, false);
                    ps3.setInt(2,rs.getInt("ID"));
                    ps3.executeUpdate();
                    System.out.println("In der Schleife");
                }
                PreparedStatement ps3 = con.prepareStatement("UPDATE QUIZFRAGEN SET MARKIERT=? WHERE MARKIERT=?");
                ps3.setBoolean(1, false);
                ps3.setBoolean(2, true);
                ps3.executeUpdate();
                con.close();
                System.out.println("QUIZ hinzugefügt/ MARKIERT auf false");
            }
            catch (SQLException throwables) {
                System.out.println(throwables.getMessage());
            }*/
            stage = (Stage) pane.getScene().getWindow();
            stage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LVUebersichtsseite.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            LVUebersichtsseiteController controller = loader.getController();
            controller.setIDLabel(currentUserID);
            controller.setLVIDLabel(currentLVID);
            controller.init();
            stage.show();
        }
        else{
            Alert erfolgreich = new Alert(Alert.AlertType.INFORMATION);
            erfolgreich.setTitle("Alert");
            erfolgreich.setHeaderText("Felder leer!");
            erfolgreich.setContentText("Bitte füllen Sie erst alle Felder aus!");
            erfolgreich.showAndWait();
        }

    }

    @FXML
    void cmdVerwerfen(ActionEvent event) throws IOException, SQLException {
        JSONObject json = new JSONObject();
        json.put("Methode","deleteMarkierteQuizfragen");
        json.put("P1",true);
        ClientStart.verbinden.send(json);
        /*try{
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("delete from QUIZFRAGEN WHERE MARKIERT=?");
            ps.setBoolean(1, true);
            ps.executeUpdate();
            con.close();
        }
        catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }*/
        stage = (Stage) pane.getScene().getWindow();
        stage.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/LVUebersichtsseite.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        LVUebersichtsseiteController controller = loader.getController();
        controller.setIDLabel(currentUserID);
        controller.setLVIDLabel(currentLVID);
        controller.init();
        stage.show();
    }

    @FXML
    void cmdXmlQuiz(ActionEvent event) throws IOException {
        JSONObject json = new JSONObject();
        json.put("Methode","deleteMarkierteQuizfragen");
        json.put("P1",true);
        ClientStart.verbinden.send(json);
        /*try{
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("delete from QUIZFRAGEN WHERE MARKIERT=?");
            ps.setBoolean(1, true);
            ps.executeUpdate();
            con.close();
        }
        catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }*/
        stage = (Stage) pane.getScene().getWindow();
        stage.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/QuizXML.fxml"));
        stage.setScene(new Scene(loader.load()));
        QuizXMLController controller = loader.getController();
        controller.init(currentUserID, currentLVID);
        stage.show();
    }
    public void init(){
        String zs = idLabel.getText();
        currentUserID = Integer.parseInt(zs);
        String lvzs = currentLVIDLabel.getText();
        currentLVID = Integer.parseInt(lvzs);
        choiceRichtigeA.getItems().add("A");
        choiceRichtigeA.getItems().add("B");
        choiceRichtigeA.getItems().add("C");
        choiceRichtigeA.getItems().add("D");
    }

}