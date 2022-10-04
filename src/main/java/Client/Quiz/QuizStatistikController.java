package Client.Quiz;

import Client.Lehrveranstaltung.LVUebersichtsseiteController;
import Server.ClientStart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class QuizStatistikController {

    @FXML
    private Pane pane;

    @FXML
    private ListView<String> listViewFragenRichtig;

    @FXML
    private PieChart pieChartTeilnehmerQ;

    @FXML
    private PieChart pieChartBestehensQ;

    @FXML
    private ListView<String> listViewVersuche;

    private int currentUserID;
    private int currentLVID;
    private int quizID;
    private Stage stage;

    @FXML
    void cmdBack(ActionEvent event) throws IOException, SQLException {
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
    public void init(int quizID, int currentUserID, int currentLVID) throws IOException {
        this.quizID = quizID;
        this.currentLVID = currentLVID;
        this.currentUserID = currentUserID;

        int countBestanden = 0;
        int countNichtBestanden = 0;
        JSONObject json = new JSONObject();
        json.put("Methode","countBestandenUndNichtBestanden");
        json.put("P1",quizID);
        ClientStart.verbinden.send(json);
        JSONArray jsonArray = ClientStart.verbinden.receiveary();
        countBestanden = jsonArray.getInt(0);
        countNichtBestanden = jsonArray.getInt(1);
        try{
            TimeUnit.MILLISECONDS.sleep(200);
        }
        catch(Exception e){
            System.out.println(e);
        }
        /*try{
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("select * from quizversuche where bestanden=? and quizid=?");
            ps.setBoolean(1, true);
            ps.setInt(2,quizID);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                countBestanden++;
            }
            PreparedStatement ps2 = con.prepareStatement("select * from quizversuche where bestanden=? and quizid=?");
            ps2.setBoolean(1, false);
            ps2.setInt(2,quizID);
            ResultSet rs2 = ps2.executeQuery();
            while(rs2.next()){
                countNichtBestanden++;
            }
            con.close();
        }
        catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }*/
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Bestanden", countBestanden),
                new PieChart.Data("Nicht Bestanden", countNichtBestanden));
        pieChartBestehensQ.setData(pieChartData);
        pieChartBestehensQ.setTitle("Bestehensquote");
        pieChartBestehensQ.setClockwise(true);
        pieChartBestehensQ.setLabelLineLength(50);
        pieChartBestehensQ.setLegendVisible(true);
        int countUserInLV = 0;
        int countQuizTeilnehmer = 0;

        JSONObject json2 = new JSONObject();
        json2.put("Methode","countQuizTeilnehmerUndUserInLV");
        json2.put("P1",currentLVID);
        json2.put("P2",quizID);
        ClientStart.verbinden.send(json2);
        JSONArray jsonArray2 = ClientStart.verbinden.receiveary();
        countUserInLV = jsonArray2.getInt(0);
        countQuizTeilnehmer = jsonArray2.getInt(1);
        try{
            TimeUnit.MILLISECONDS.sleep(200);
        }
        catch(Exception e){
            System.out.println(e);
        }
        /*List<Integer> userIDs = new ArrayList<>();
        try{
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("SELECT * FROM USERINVERANSTALTUNGEN where veranstaltungsid=?");
            ps.setInt(1, currentLVID);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                userIDs.add(rs.getInt("userid"));
            }
            for(int i=0; i<userIDs.size();i++){
                PreparedStatement ps2 = con.prepareStatement("SELECT * FROM User where id=? and lehrender=false");
                ps2.setInt(1, userIDs.get(i));
                ResultSet rs2 = ps2.executeQuery();
                while(rs2.next()){
                    countUserInLV++;
                }
            }
            PreparedStatement ps2 = con.prepareStatement("SELECT * FROM QUIZVERSUCHE where quizid=?");
            ps2.setInt(1, quizID);
            ResultSet rs2 = ps2.executeQuery();
            while(rs2.next()){
                countQuizTeilnehmer++;
            }
            PreparedStatement ps3 = con.prepareStatement("SELECT * FROM USERINVERANSTALTUNGEN where veranstaltungsid=?");
            ps3.setInt(1, currentLVID);
            ResultSet rs3 = ps3.executeQuery();
            while(rs3.next()){
                countUserInLV++;
            }
            con.close();
        }
        catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }*/
        int countNichtTeilgenommen = countUserInLV-countQuizTeilnehmer;
        ObservableList<PieChart.Data> pieChartData2 = FXCollections.observableArrayList(
                new PieChart.Data("Teilgenommen", countQuizTeilnehmer),
                new PieChart.Data("Nicht Teilgenommen", countNichtTeilgenommen));
        pieChartTeilnehmerQ.setData(pieChartData2);
        System.out.println(pieChartTeilnehmerQ.getData());
        pieChartTeilnehmerQ.setTitle("Teilnehmerquote");
        pieChartTeilnehmerQ.setClockwise(true);
        pieChartTeilnehmerQ.setLabelLineLength(10);
        pieChartTeilnehmerQ.setLabelsVisible(true);
        pieChartTeilnehmerQ.setLegendVisible(true);

        JSONObject json3 = new JSONObject();
        json3.put("Methode","getQuizVersuche");
        json3.put("P1",quizID);
        ClientStart.verbinden.send(json3);
        JSONArray jsonArray3 = ClientStart.verbinden.receiveary();
        //Liste mit Versuchen füllen
        List<String> versuche = new ArrayList<>();
        for(int i=0; i<jsonArray3.length();i++){
            versuche.add(jsonArray3.getString(i));
        }
        /*try{
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("select * from quizversuche where quizid=?");
            ps.setInt(1,quizID);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                versuche.add(rs.getInt("userid")+": "+rs.getInt("versuche")+" Versuche");
            }
            con.close();
        }
        catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }*/
        listViewVersuche.getItems().addAll(versuche);
        //Liste mit Richtig/Teilnehmer füllen

        JSONObject json4 = new JSONObject();
        json4.put("Methode","getFragenRichtig");
        json4.put("P1",quizID);
        ClientStart.verbinden.send(json4);
        JSONArray jsonArray4 = ClientStart.verbinden.receiveary();
        //Liste mit Versuchen füllen
        List<String> ergebnis = new ArrayList<>();
        for(int i=0; i<jsonArray4.length();i++){
            ergebnis.add(jsonArray4.getString(i));
        }
        /*List<Integer> quizIDs = new ArrayList<>();
        try{
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("select * from quiz where quizid=?");
            ps.setInt(1,quizID);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                quizIDs.add(rs.getInt("quizfrageid"));
            }
            for(int i=0; i<quizIDs.size(); i++){
                int qfCount =0;
                int qfRichtigCount=0;
                PreparedStatement ps2 = con.prepareStatement("select * from quizstatistik where quizfragenid=?");
                ps2.setInt(1,quizIDs.get(i));
                ResultSet rs2 = ps2.executeQuery();
                while(rs2.next()){
                    qfCount++;
                }
                PreparedStatement ps3 = con.prepareStatement("select * from quizstatistik where quizfragenid=? and richtig=true");
                ps3.setInt(1,quizIDs.get(i));
                ResultSet rs3 = ps3.executeQuery();
                while(rs3.next()){
                    qfRichtigCount++;
                }
                int fn = i+1;
                ergebnis.add("Frage "+fn+": "+qfRichtigCount+"/"+qfCount+" mal richtig");
            }
            con.close();
        }
        catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }*/
        listViewFragenRichtig.getItems().addAll(ergebnis);
    }
}