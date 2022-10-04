package Client.FreundeVerwalten;

import Server.ClientStart;
import Client.javaFx.HauptseiteStudentController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FreundeBestätigenController {

    @FXML
    private Pane pane;
    @FXML
    private ChoiceBox<String> choiceBoxAnfragen;
    @FXML
    private Label currentIDLabel;


    private Stage stage;
    private Parent root;
    private Scene scene;
    private int currentUserID;

    @FXML
    void cmdAblehnen(ActionEvent event) throws IOException {
        String freund = choiceBoxAnfragen.getValue();
        String vorname = "";
        String nachname = "";
        if (freund != null) {
            String[] parts = freund.split(" ");
            vorname = parts[0];
            nachname = parts[1];
        }
        int freundid = 0;


        //ÄNDERN
        JSONObject json = new JSONObject();
        json.put("Methode","getUserID");
        json.put("P1",vorname);
        json.put("P2",nachname);
        ClientStart.verbinden.send(json);
        JSONObject jsonReturn = ClientStart.verbinden.receiveobj();
        String freundesid = jsonReturn.get("P1")+"";
        if(freundesid != null && !freundesid.equals("")){
            freundid = Integer.parseInt(freundesid);
        }

        JSONObject json1 = new JSONObject();
        json1.put("Methode","deleteFanfragen");
        json1.put("P1",currentUserID);
        json1.put("P2",freundid);
        ClientStart.verbinden.send(json1);

        init();
        //alt
//        try {
//            Connection con = DriverManager.getConnection(urlDB, user, passwort);
//            PreparedStatement ps = con.prepareStatement("SELECT * FROM USER WHERE VORNAME=? AND NACHNAME=?");
//            ps.setString(1,vorname);
//            ps.setString(2,nachname);
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()){
//                freundid = rs.getInt("ID");
//            }
//            if (freundid != 0) {
//                ps = con.prepareStatement("DELETE FROM FANFRAGEN WHERE USERID=? AND FID=?");
//                ps.setInt(1, freundid);
//                ps.setInt(2, currentUserID);
//                ps.executeUpdate();
//                System.out.println(freundid);
//            }
//            System.out.println("Freund abgelehnt");
//            init();
//            con.close();
//        }
//        catch (SQLException throwables) {
//            System.out.println(throwables.getMessage());
//        }
    }

    @FXML
    void cmdAkzeptieren(ActionEvent event) throws IOException {
        String freund = choiceBoxAnfragen.getValue();
        String vorname = "";
        String nachname = "";
        if (freund != null) {
            String[] parts = freund.split(" ");
            vorname = parts[0];
            nachname = parts[1];
        }
        int freundid = 0;

        //ÄNDERN
        JSONObject json = new JSONObject();
        json.put("Methode","getUserID");
        json.put("P1",vorname);
        json.put("P2",nachname);
        ClientStart.verbinden.send(json);
        JSONObject jsonReturn = ClientStart.verbinden.receiveobj();
        String freundesid = jsonReturn.get("P1")+"";
        if(freundesid != null && !freundesid.equals("")){
            freundid = Integer.parseInt(freundesid);
        }

        JSONObject json1 = new JSONObject();
        json1.put("Methode","insertFreund");
        json1.put("P1",currentUserID);
        json1.put("P2",freundid);
        ClientStart.verbinden.send(json1);

        init();
        //alt
//        try {
//            Connection con = DriverManager.getConnection(urlDB, user, passwort);
//            PreparedStatement ps = con.prepareStatement("SELECT * FROM USER WHERE VORNAME=? AND NACHNAME=?");
//            ps.setString(1,vorname);
//            ps.setString(2,nachname);
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()){
//                freundid = rs.getInt("ID");
//            }
//            if (freundid != 0) {
//                ps = con.prepareStatement("INSERT INTO FREUNDE (USERID, FREUNDID) VALUES (?, ?)");
//                ps.setInt(1, currentUserID);
//                ps.setInt(2, freundid);
//                ps.executeUpdate();
//                ps = con.prepareStatement("INSERT INTO FREUNDE (USERID, FREUNDID) VALUES (?, ?)");
//                ps.setInt(1, freundid);
//                ps.setInt(2, currentUserID);
//                ps.executeUpdate();
//            }
//            if (freundid != 0) {
//                ps = con.prepareStatement("DELETE FROM FANFRAGEN WHERE USERID=? AND FID=?");
//                ps.setInt(1, freundid);
//                ps.setInt(2, currentUserID);
//                ps.executeUpdate();
//                System.out.println(freundid);
//            }
//            System.out.println("Freund angenommen");
//            init();
//            con.close();
//        }
//        catch (SQLException throwables) {
//            System.out.println(throwables.getMessage());
//        }
    }

    @FXML
    void cmdBack(ActionEvent event) throws IOException {
        //Zurück zur Hauptseite
        stage = (Stage) pane.getScene().getWindow();
        stage.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Hauptseite(fürStudent).fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        HauptseiteStudentController controller = loader.getController();
        controller.setIDLabel(currentUserID);
        controller.init();
        stage.show();
    }
    public void setIDLabel(int test){
        Integer integer = test;
        currentIDLabel.setText(integer.toString());
    }
    public void init() throws IOException {
        String test3 = currentIDLabel.getText();
        int test2 = Integer.parseInt(test3);
        currentUserID = test2;

        List<String> anfragen = new ArrayList<>();
        List<String> ergebnis = new ArrayList<>();

        JSONObject json = new JSONObject();
        json.put("Methode","getFreundeIDvonFanfragen");
        json.put("P1",currentUserID);
        ClientStart.verbinden.send(json);
        JSONArray jsonArray = ClientStart.verbinden.receiveary();
        if (jsonArray != null) {
            for (int i=0;i<jsonArray.length();i++){
                anfragen.add(jsonArray.getString(i));
            }
        }

        JSONArray jsonArraySenden = new JSONArray();
        for(int i=0; i<anfragen.size();i++){
            jsonArraySenden.put(anfragen.get(i));
        }
        String jsonString = jsonArraySenden.toString();
        JSONObject json2 = new JSONObject();
        json2.put("Methode","getNamenVonIDs");
        json2.put("P1",currentUserID);
        json2.put("P2",jsonString);
        ClientStart.verbinden.send(json2);
        JSONArray jsonArray2 = ClientStart.verbinden.receiveary();
        for(int i=0; i<jsonArray2.length(); i++){
            ergebnis.add(jsonArray2.getString(i));
        }

        Set<String> set = new HashSet<>(ergebnis);
        ergebnis.clear();
        ergebnis.addAll(set);
        choiceBoxAnfragen.getItems().clear();
        java.util.Collections.sort(ergebnis); //sortieren
        choiceBoxAnfragen.getItems().addAll(ergebnis);
        if (!choiceBoxAnfragen.getItems().isEmpty()) {
            choiceBoxAnfragen.setValue(choiceBoxAnfragen.getItems().get(0)); //setze Default Value auf den ersten Eintrag
        }

        //alt
//        try {
//            List<String> Anfragen = new ArrayList<>();
//            List<String> Ergebnis = new ArrayList<>();
//            Connection con = DriverManager.getConnection(urlDB, user, passwort);
//            PreparedStatement ps = con.prepareStatement("SELECT * FROM FANFRAGEN WHERE FID=?");
//            ps.setInt(1, currentUserID);
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()){
//                Anfragen.add(rs.getString("USERID"));
//            }
//            for (int i = 0;i<Anfragen.size();i++){
//                ps = con.prepareStatement("SELECT * FROM USER WHERE ID=?");
//                ps.setInt(1, Integer.parseInt(Anfragen.get(i)));
//                rs = ps.executeQuery();
//                while (rs.next()){
//                    Ergebnis.add(rs.getString("VORNAME")+" "+rs.getString("NACHNAME"));
//                }
//            }
//
//            Set<String> set = new HashSet<>(Ergebnis);
//            Ergebnis.clear();
//            Ergebnis.addAll(set);
//            choiceBoxAnfragen.getItems().clear();
//            java.util.Collections.sort(Ergebnis); //sortieren
//            choiceBoxAnfragen.getItems().addAll(Ergebnis);
//            if (!choiceBoxAnfragen.getItems().isEmpty()) {
//                choiceBoxAnfragen.setValue(choiceBoxAnfragen.getItems().get(0)); //setze Default Value auf den ersten Eintrag
//            }
//            //boxSemester.setValue("");
//            con.close();
//        }
//        catch (SQLException throwables) {
//            System.out.println(throwables.getMessage());
//        }
    }
}
