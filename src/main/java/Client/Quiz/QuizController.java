package Client.Quiz;

import Client.Lehrveranstaltung.LVUebersichtsseiteController;
import Server.ClientStart;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class QuizController {

    @FXML
    private Pane pane;
    @FXML
    private Label labelFrage;
    @FXML
    private CheckBox checkBoxAntwortA;
    @FXML
    private CheckBox checkBoxAntwortB;
    @FXML
    private CheckBox checkBoxAntwortC;
    @FXML
    private CheckBox checkBoxAntwortD;
    @FXML
    private Label idLabel;
    @FXML
    private Label currentLVIDLabel;
    @FXML
    private Button btnAbgeben;

    private Stage stage;
    private int currentUserID;
    private int currentLVID;
    private int quizID;

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
    public int count =0;
    public int position =0;
    public int quizfragenID= 0;
    public String selectedA="";
    List<Integer> quizfragenIDs = new ArrayList<>();

    @FXML
    void cmdAntwortAbgeben(ActionEvent event) throws IOException {
        if(checkBoxAntwortA.isSelected()||checkBoxAntwortB.isSelected()||checkBoxAntwortC.isSelected()||checkBoxAntwortD.isSelected()){
            JSONObject json = new JSONObject();
            json.put("Methode","antwortAbgeben");
            json.put("P1",quizfragenID);
            json.put("P2",currentUserID);
            json.put("P3",selectedA);
            ClientStart.verbinden.send(json);
            //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            /*try {
                Connection con = DriverManager.getConnection(urlDB, user, passwort);
                PreparedStatement ps = con.prepareStatement("Select * from quizfragen where id=?");
                ps.setInt(1, quizfragenID);
                ResultSet rs = ps.executeQuery();
                String richtigeA ="";
                while(rs.next()){
                    richtigeA = rs.getString("richtige_antwort");
                }
                PreparedStatement ps2 = con.prepareStatement("Select * from quizstatistik where userid=? and quizfragenid=?");
                ps2.setInt(1,currentUserID);
                ps2.setInt(2,quizfragenID);
                ResultSet rs2 = ps2.executeQuery();
                int tester = 0;
                if(rs2.next()){
                    tester=1;
                }
                System.out.println(tester);
                if(tester==0){
                    if(richtigeA.equals(selectedA)){
                        PreparedStatement ps3 = con.prepareStatement("Insert into quizstatistik values (?,?,?)");
                        ps3.setInt(1,currentUserID);
                        ps3.setInt(2,quizfragenID);
                        ps3.setBoolean(3,true);
                        ps3.executeUpdate();
                    }
                    else{
                        PreparedStatement ps3 = con.prepareStatement("Insert into quizstatistik values (?,?,?)");
                        ps3.setInt(1,currentUserID);
                        ps3.setInt(2,quizfragenID);
                        ps3.setBoolean(3,false);
                        ps3.executeUpdate();
                    }
                }
                else{
                    if(richtigeA.equals(selectedA)){
                        PreparedStatement ps3 = con.prepareStatement("update quizstatistik set richtig=true where userid=? and quizfragenid=?");
                        ps3.setInt(1,currentUserID);
                        ps3.setInt(2,quizfragenID);
                        ps3.executeUpdate();
                    }
                    else{
                        PreparedStatement ps3 = con.prepareStatement("update quizstatistik set richtig=false where userid=? and quizfragenid=?");
                        ps3.setInt(1,currentUserID);
                        ps3.setInt(2,quizfragenID);
                        ps3.executeUpdate();
                    }
                }
                con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }*/

            //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            if(position!=count){
                stage = (Stage) pane.getScene().getWindow();
                stage.close();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Quiz.fxml"));
                stage.setScene(new Scene(loader.load()));
                QuizController controller = loader.getController();
                controller.setIDLabel(currentUserID);
                controller.setLVIDLabel(currentLVID);
                controller.init(quizID, position);
                stage.show();
            }
            else{
                JSONObject json2 = new JSONObject();
                json2.put("Methode","updateQuizversuche");
                json2.put("P1",quizID);
                json2.put("P2",currentUserID);
                ClientStart.verbinden.send(json2);
                //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                /*try {
                    Connection con = DriverManager.getConnection(urlDB, user, passwort);
                    PreparedStatement ps = con.prepareStatement("Select * from quizversuche where userid=? and quizid=?");
                    ps.setInt(1, currentUserID);
                    ps.setInt(2,quizID);
                    ResultSet rs = ps.executeQuery();
                    int countQV = 0;
                    while(rs.next()){
                        countQV = rs.getInt("versuche");
                    }
                    System.out.println(countQV);
                    if(countQV==0){
                        PreparedStatement ps2 = con.prepareStatement("Insert into quizversuche values (?,?,?,false)");
                        ps2.setInt(1, currentUserID);
                        ps2.setInt(2,quizID);
                        ps2.setInt(3,1);
                        ps2.executeUpdate();
                    }
                    else{
                        countQV++;
                        PreparedStatement ps3 =con.prepareStatement("Update quizversuche set versuche = ? where userid=? and quizid=?");
                        ps3.setInt(1,countQV);
                        ps3.setInt(2,currentUserID);
                        ps3.setInt(3,quizID);
                        ps3.executeUpdate();
                    }
                    con.close();
                }
                catch (SQLException throwables) {
                    System.out.println(throwables.getMessage());
                }*/
                //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                try{
                    TimeUnit.MILLISECONDS.sleep(200);
                }
                catch(Exception e){
                    System.out.println(e);
                }
                JSONArray jsonArray = new JSONArray();
                for(int i=0; i<quizfragenIDs.size();i++){
                    jsonArray.put(quizfragenIDs.get(i));
                }
                System.out.println("jsonArray in controller: "+jsonArray);
                String jsonString = jsonArray.toString();
                JSONObject json3 = new JSONObject();
                json3.put("Methode","updateQuizstatistik");
                json3.put("P1",currentUserID);
                json3.put("P2",jsonString);
                json3.put("P3",quizID);
                ClientStart.verbinden.send(json3);

                /*int countRichtig = 0;
                int countFalsch = 0;
                try {
                    Connection con = DriverManager.getConnection(urlDB, user, passwort);
                    for (int i=0; i<quizfragenIDs.size();i++){
                        PreparedStatement ps = con.prepareStatement("select * from quizstatistik where userid=? and quizfragenid=? and richtig=true");
                        ps.setInt(1, currentUserID);
                        ps.setInt(2,quizfragenIDs.get(i));
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()){
                            countRichtig++;
                        }
                    }
                    for (int i=0; i<quizfragenIDs.size();i++){
                        PreparedStatement ps = con.prepareStatement("select * from quizstatistik where userid=? and quizfragenid=? and richtig=false");
                        ps.setInt(1, currentUserID);
                        ps.setInt(2,quizfragenIDs.get(i));
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()){
                            countFalsch++;
                        }
                    }
                    if(countRichtig>=countFalsch){
                        PreparedStatement ps = con.prepareStatement("update quizversuche set bestanden=true where userid=? and quizid=?");
                        ps.setInt(1, currentUserID);
                        ps.setInt(2,quizID);
                        ps.executeUpdate();
                    }
                    else{
                        PreparedStatement ps = con.prepareStatement("update quizversuche set bestanden=false where userid=? and quizid=?");
                        ps.setInt(1, currentUserID);
                        ps.setInt(2,quizID);
                        ps.executeUpdate();
                    }
                    con.close();
                }
                catch (SQLException throwables) {
                    System.out.println(throwables.getMessage());
                }*/
                try{
                    TimeUnit.MILLISECONDS.sleep(400);
                }
                catch(Exception e){
                    System.out.println(e);
                }
                stage = (Stage) pane.getScene().getWindow();
                stage.close();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/QuizFeedback.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(loader.load()));
                QuizFeedbackController controller = loader.getController();
                controller.init(quizID,currentUserID,currentLVID);
                stage.show();
            }
        }
        else{
            Alert erfolgreich = new Alert(Alert.AlertType.INFORMATION);
            erfolgreich.setTitle("Alert");
            erfolgreich.setHeaderText("Keine Antwort ausgewählt!");
            erfolgreich.setContentText("Bitte wählen Sie zuerst eine Antwort aus!");
            erfolgreich.showAndWait();
        }

    }

    @FXML
    void cmdVerwerfen(ActionEvent event) throws IOException, SQLException {
        JSONObject json = new JSONObject();
        json.put("Methode","quizVersuchVerwerfen");
        json.put("P1",currentUserID);
        json.put("P2",quizID);
        ClientStart.verbinden.send(json);
        //alte Server-Client Architektur
        /*try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("Select * from quizversuche where userid=? and quizid=?");
            ps.setInt(1, currentUserID);
            ps.setInt(2,quizID);
            ResultSet rs = ps.executeQuery();
            int countQV = 0;
            while(rs.next()){
                countQV = rs.getInt("versuche");
            }
            System.out.println(countQV);
            if(countQV==0){
                PreparedStatement ps2 = con.prepareStatement("Insert into quizversuche values (?,?,?,false)");
                ps2.setInt(1, currentUserID);
                ps2.setInt(2,quizID);
                ps2.setInt(3,1);
                ps2.executeUpdate();
            }
            else{
                countQV++;
                PreparedStatement ps3 =con.prepareStatement("Update quizversuche set versuche = ?");
                ps3.setInt(1,countQV);
                ps3.executeUpdate();
            }
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
    void cmdCheckA(ActionEvent event) {
        checkBoxAntwortA.setSelected(true);
        checkBoxAntwortB.setSelected(false);
        checkBoxAntwortC.setSelected(false);
        checkBoxAntwortD.setSelected(false);
        selectedA="A";
    }

    @FXML
    void cmdCheckB(ActionEvent event) {
        checkBoxAntwortA.setSelected(false);
        checkBoxAntwortB.setSelected(true);
        checkBoxAntwortC.setSelected(false);
        checkBoxAntwortD.setSelected(false);
        selectedA="B";
    }

    @FXML
    void cmdCheckC(ActionEvent event) {
        checkBoxAntwortA.setSelected(false);
        checkBoxAntwortB.setSelected(false);
        checkBoxAntwortC.setSelected(true);
        checkBoxAntwortD.setSelected(false);
        selectedA="C";
    }

    @FXML
    void cmdCheckD(ActionEvent event) {
        checkBoxAntwortA.setSelected(false);
        checkBoxAntwortB.setSelected(false);
        checkBoxAntwortC.setSelected(false);
        checkBoxAntwortD.setSelected(true);
        selectedA="D";
    }
    public void init(int quizID, int pos) throws IOException{
        System.out.println("Quiz init");
        String zs = idLabel.getText();
        currentUserID = Integer.parseInt(zs);
        String lvzs = currentLVIDLabel.getText();
        currentLVID = Integer.parseInt(lvzs);
        int minFN=0;
        position = pos+1;
        this.quizID = quizID;
        checkBoxAntwortA.setSelected(false);
        checkBoxAntwortB.setSelected(false);
        checkBoxAntwortC.setSelected(false);
        checkBoxAntwortD.setSelected(false);
        //neue Server-Client Architektur
        System.out.println("Quiz init 2.");
        JSONObject json = new JSONObject();
        json.put("Methode","getQuizfrageIDs");
        json.put("P1",quizID);
        ClientStart.verbinden.send(json);
        JSONArray jsonArray = ClientStart.verbinden.receiveary();
        for(int i=0; i<jsonArray.length(); i++){
            quizfragenIDs.add(jsonArray.getInt(i));
        }
        System.out.println("Schritt 1: "+quizfragenIDs);
        //alte Server Client Architektur
        /*try {
            //quizfragen-Liste mit allen QUIZFRAGENID's der QUIZ Tabelle füllen (die die übergebene QUIZID besitzen)
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("SELECT * FROM QUIZ WHERE quizid=?");
            ps.setInt(1, quizID);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                quizfragenIDs.add(rs.getInt("quizfrageid"));
            }
            con.close();
        }
        catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }*/
        //neue ServerClient Architektur
        for (int i=0; i<quizfragenIDs.size();i++){
            JSONObject json2 = new JSONObject();
            json2.put("Methode","setQuizfragenTrue");
            json2.put("P1",quizfragenIDs.get(i));
            ClientStart.verbinden.send(json2);
            try{
                TimeUnit.MILLISECONDS.sleep(200);
            }
            catch(Exception e){
                System.out.println(e);
            }
        }
        System.out.println("Schritt 2");

        JSONObject json2 = new JSONObject();
        json2.put("Methode","countQuizfragenMarkiert");
        json2.put("P1", true);
        ClientStart.verbinden.send(json2);
        System.out.println("vor receiveobj");
        JSONObject jsonReceive = ClientStart.verbinden.receiveobj();
        String jsonString = jsonReceive.get("P1")+"";
        System.out.println(jsonString);
        count = count + Integer.parseInt(jsonString);
        System.out.println("Schritt 3");

        JSONObject json3 = new JSONObject();
        json3.put("Methode","getMinFN");
        json3.put("P1",pos);
        ClientStart.verbinden.send(json3);
        JSONObject jsonReceive2 = ClientStart.verbinden.receiveobj();
        String jsonString2 = jsonReceive2.get("P1")+"";
        System.out.println(jsonString2);
        minFN = Integer.parseInt(jsonString2);
        System.out.println("Schritt 4");

        JSONObject json4 = new JSONObject();
        json4.put("Methode","getQuiz");
        json4.put("P1",minFN);
        ClientStart.verbinden.send(json4);
        JSONArray jsonArray2 = ClientStart.verbinden.receiveary();
        quizfragenID = Integer.parseInt(jsonArray2.getString(0));
        System.out.println("quizfragenID: "+quizfragenID);
        labelFrage.setText(jsonArray2.getString(1));
        checkBoxAntwortA.setText(jsonArray2.getString(2));
        checkBoxAntwortB.setText(jsonArray2.getString(3));
        checkBoxAntwortC.setText(jsonArray2.getString(4));
        checkBoxAntwortD.setText(jsonArray2.getString(5));
        System.out.println("Schritt 5");

        JSONObject json5 = new JSONObject();
        json5.put("Methode","setQuizfragenFalse");
        ClientStart.verbinden.send(json5);
        System.out.println("Schritt 6");
        //alte ServerClient Architektur
        /*try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            for (int i=0; i<quizfragenIDs.size();i++){
                PreparedStatement ps2 = con.prepareStatement("Update quizfragen set markiert=true where id=?");
                ps2.setInt(1, quizfragenIDs.get(i));
                ps2.executeUpdate();
            }
            PreparedStatement ps = con.prepareStatement("SELECT * FROM quizfragen WHERE markiert=?");
            ps.setBoolean(1, true);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                count++; //zählt die Anzahl der Einträge, die in quizfragen gespeichert werden
            }
            Statement st = con.createStatement();
            ResultSet rs2 = st.executeQuery("select min(fragennummer) as minFN from Quizfragen where markiert=true");
            while(rs2.next()){
                minFN = rs2.getInt("minFN")+pos;
            }
            //bis hier gemacht minFN geholt
            PreparedStatement ps2 = con.prepareStatement("SELECT * FROM quizfragen WHERE markiert=true and fragennummer=?");
            ps2.setInt(1, minFN);
            ResultSet rs3 = ps2.executeQuery();
            while(rs3.next()){
                quizfragenID = rs3.getInt("id");
                System.out.println(quizfragenID);
                labelFrage.setText(rs3.getString("quizfrage"));
                checkBoxAntwortA.setText(rs3.getString("antwort_a"));
                checkBoxAntwortB.setText(rs3.getString("antwort_b"));
                checkBoxAntwortC.setText(rs3.getString("antwort_c"));
                checkBoxAntwortD.setText(rs3.getString("antwort_d"));
                if(count>pos+1){
                    btnAbgeben.setText("Nächste Frage");
                }
            }
            Statement st2 = con.createStatement();
            st2.executeUpdate("update quizfragen set markiert=false where markiert=true");
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }*/
    }

}