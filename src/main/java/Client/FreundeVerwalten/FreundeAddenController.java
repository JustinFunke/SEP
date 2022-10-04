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

public class FreundeAddenController {

    @FXML
    private Pane pane;
    @FXML
    private ChoiceBox<String> choiceBoxStudenten;
    @FXML
    private Label currentIDLabel;


    private Stage stage;
    private Parent root;
    private Scene scene;
    private int currentUserID;

    @FXML
    void cmdAdd(ActionEvent event) throws IOException {
        //neue Architektur
        String freund = choiceBoxStudenten.getValue();
        String vorname = "";
        String nachname = "";
        if (freund != null) {
            String[] parts = freund.split(" ");
            vorname = parts[0];
            nachname = parts[1];
        }
        int freundid = 0;
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
        if (freundid != 0) {
            json.put("Methode", "freundschaftsAnfrage");
            json.put("P1", currentUserID);
            json.put("P2", freundid);
            ClientStart.verbinden.send(json);
        }
        init();
        //alte Architektur
//        String freund = choiceBoxStudenten.getValue();
//        String vorname = "";
//        String nachname = "";
//        if (freund != null) {
//            String[] parts = freund.split(" ");
//            vorname = parts[0];
//            nachname = parts[1];
//        }
//        int freundid = 0;
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
//                ps = con.prepareStatement("INSERT INTO FANFRAGEN (USERID, FID) VALUES (?, ?)");
//                ps.setInt(1, currentUserID);
//                ps.setInt(2, freundid);
//                ps.executeUpdate();
//                System.out.println(freundid);
//            }
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
    //Personen denen der User FA geschickt hat
    public List<String> peopleSentFA() throws IOException{
        //neue Architektur
        List<String> freundeIDs = new ArrayList<>();
        JSONObject json = new JSONObject();
        json.put("Methode","getUserIDvonFanfragen");
        json.put("P1",currentUserID);
        ClientStart.verbinden.send(json);
        JSONArray jsonArray = ClientStart.verbinden.receiveary();
        if (jsonArray != null) {
            for (int i=0;i<jsonArray.length();i++){
                freundeIDs.add(jsonArray.getString(i));
            }
        }
        return freundeIDs;
        //alte Architektur
//        List<String> freundeIDs = new ArrayList<>();
//        try{
//            Connection con = DriverManager.getConnection(urlDB, user, passwort);
//            PreparedStatement ps = con.prepareStatement("SELECT * FROM FANFRAGEN WHERE USERID = ?" );
//            ps.setInt(1, currentUserID);
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()){
//                freundeIDs.add(rs.getString("FID"));
//            }
//            con.close();
//            return freundeIDs;
//        }
//        catch (SQLException throwables) {
//            System.out.println(throwables.getMessage());
//        }
//        return freundeIDs;

    }
    //Personen welche dem User FA geschickt haben
    public List<String> peopleGotFA() throws IOException{
        //neue Architektur
        List<String> freundeIDs = new ArrayList<>();
        JSONObject json = new JSONObject();
        json.put("Methode","getFreundeIDvonFanfragen");
        json.put("P1",currentUserID);
        ClientStart.verbinden.send(json);
        JSONArray jsonArray = ClientStart.verbinden.receiveary();
        if (jsonArray != null) {
            for (int i=0;i<jsonArray.length();i++){
                freundeIDs.add(jsonArray.getString(i));
            }
        }
        return freundeIDs;
        //alte Architektur
//        List<String> freundeIDs = new ArrayList<>();
//        try{
//            Connection con = DriverManager.getConnection(urlDB, user, passwort);
//            PreparedStatement ps = con.prepareStatement("SELECT * FROM FANFRAGEN WHERE FID = ?" );
//            ps.setInt(1, currentUserID);
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()){
//                freundeIDs.add(rs.getString("USERID"));
//            }
//            con.close();
//            return freundeIDs;
//        }
//        catch (SQLException throwables) {
//            System.out.println(throwables.getMessage());
//        }
//        return freundeIDs;
    }
    public void setIDLabel(int test){
        Integer integer = test;
        currentIDLabel.setText(integer.toString());
    }
    public void init() throws IOException{
        String test3 = currentIDLabel.getText();
        int test2 = Integer.parseInt(test3);
        currentUserID = test2;
        //choicebox füllen

        //neue Architektur
        ArrayList<String> leuteInVeranstaltung = new ArrayList<>();
        ArrayList<String> alleFreundeID = new ArrayList<>();
        ArrayList<String> endergebnis = new ArrayList<>();

        JSONObject json = new JSONObject();
        json.put("Methode","getGleicheVeranstaltungLeute");
        json.put("P1",currentUserID);
        ClientStart.verbinden.send(json);
        JSONArray jsonArray = ClientStart.verbinden.receiveary();
        for(int i=0; i<jsonArray.length(); i++){
            leuteInVeranstaltung.add(jsonArray.getString(i));
        }
        System.out.println(leuteInVeranstaltung);

        JSONObject json1 = new JSONObject();
        json1.put("Methode","getFreundeVonUser");
        json1.put("P1",currentUserID);
        ClientStart.verbinden.send(json1);
        JSONArray jsonArray1 = ClientStart.verbinden.receiveary();
        for(int i=0; i<jsonArray1.length(); i++){
            alleFreundeID.add(jsonArray1.getString(i));
        }
        System.out.println(alleFreundeID);

        List<String> freundeID = peopleSentFA();
        List<String> freundeID2 = peopleGotFA();
        leuteInVeranstaltung.removeAll(freundeID);
        leuteInVeranstaltung.removeAll(freundeID2);
        // Prepare a union
        List<String> union = new ArrayList<String>(leuteInVeranstaltung);
        union.addAll(alleFreundeID);
        // Prepare an intersection
        List<String> intersection = new ArrayList<String>(leuteInVeranstaltung);
        intersection.retainAll(alleFreundeID);
        // Subtract the intersection from the union
        union.removeAll(intersection);
        //Methode wo ich ein Array als Parameter übersende
        JSONArray jsonArraySenden = new JSONArray();

        System.out.println(union);
        for(int i=0; i<union.size();i++){
            jsonArraySenden.put(union.get(i));
        }
        String jsonString = jsonArraySenden.toString();
        JSONObject json2 = new JSONObject();
        json2.put("Methode","getNamenVonIDs");
        json2.put("P1",currentUserID);
        json2.put("P2",jsonString);
        ClientStart.verbinden.send(json2);
        JSONArray jsonArray2 = ClientStart.verbinden.receiveary();
        for(int i=0; i<jsonArray2.length(); i++){
            endergebnis.add(jsonArray2.getString(i));
        }
        System.out.println(endergebnis);

        Set<String> set = new HashSet<>(endergebnis);
        endergebnis.clear();
        endergebnis.addAll(set);
        choiceBoxStudenten.getItems().clear();
        java.util.Collections.sort(endergebnis); //sortieren
        choiceBoxStudenten.getItems().addAll(endergebnis);
        if (!choiceBoxStudenten.getItems().isEmpty()) {
            choiceBoxStudenten.setValue(choiceBoxStudenten.getItems().get(0)); //setze Default Value auf den ersten Eintrag
        }
        //alte Architektur
//        try {
//            List<String> EigeneVeranstaltungen = new ArrayList<>();
//            List<String> Zwischenergebnis = new ArrayList<>();
//            List<String> Ergebnis = new ArrayList<>();
//            List<String> alleFreundeIDs = new ArrayList<>();
//            Connection con = DriverManager.getConnection(urlDB, user, passwort);
//            PreparedStatement ps = con.prepareStatement("SELECT * FROM USERINVERANSTALTUNGEN WHERE USERID=?");
//            ps.setInt(1, currentUserID);
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()){
//                EigeneVeranstaltungen.add(rs.getString("VERANSTALTUNGSID"));
//            }
//            for (int i = 0;i<EigeneVeranstaltungen.size();i++){
//                ps = con.prepareStatement("SELECT * FROM USERINVERANSTALTUNGEN WHERE VERANSTALTUNGSID=?");
//                ps.setInt(1, Integer.parseInt(EigeneVeranstaltungen.get(i)));
//                rs = ps.executeQuery();
//                while (rs.next()){
//                    if (!rs.getString("USERID").equals(test3)) {
//                        Zwischenergebnis.add(rs.getString("USERID"));
//                    }
//                }
//            }
//            PreparedStatement ps2 = con.prepareStatement("SELECT * FROM FREUNDE WHERE USERID=?");
//            ps2.setInt(1, currentUserID);
//            ResultSet rs2 = ps2.executeQuery();
//            while (rs2.next()){
//                alleFreundeIDs.add(rs2.getString("FREUNDID"));
//            }
//
//            List<String> freundeIDs = peopleSentFA();
//            List<String> freundeIDs2 = peopleGotFA();
//            Zwischenergebnis.removeAll(freundeIDs);
//            Zwischenergebnis.removeAll(freundeIDs2);
//            //https://stackoverflow.com/questions/15575417/how-to-remove-common-values-from-two-array-lists
//            // Prepare a union
//            List<String> union2 = new ArrayList<String>(Zwischenergebnis);
//            union2.addAll(alleFreundeIDs);
//            // Prepare an intersection
//            List<String> intersection2 = new ArrayList<String>(Zwischenergebnis);
//            intersection2.retainAll(alleFreundeIDs);
//            // Subtract the intersection from the union
//            union2.removeAll(intersection2);
//            for (int i = 0;i<union2.size();i++){
//                ps = con.prepareStatement("SELECT * FROM USER WHERE ID=?");
//                ps.setInt(1, Integer.parseInt(union2.get(i)));
//                rs = ps.executeQuery();
//                while (rs.next()){
//                    if (!rs.getString("ID").equals(test3) && !rs.getBoolean("LEHRENDER")) {
//                        //Ergebnis.add(rs.getString("VORNAME" + " NACHNAME"));
//                        Ergebnis.add(rs.getString("VORNAME") +" "+ rs.getString("NACHNAME"));
//                    }
//                }
//            }
//
//            Set<String> set2 = new HashSet<>(Ergebnis);
//            Ergebnis.clear();
//            Ergebnis.addAll(set2);
//            choiceBoxStudenten.getItems().clear();
//            java.util.Collections.sort(Ergebnis); //sortieren
//            choiceBoxStudenten.getItems().addAll(Ergebnis);
//            if (!choiceBoxStudenten.getItems().isEmpty()) {
//                choiceBoxStudenten.setValue(choiceBoxStudenten.getItems().get(0)); //setze Default Value auf den ersten Eintrag
//            }
//            con.close();
//        }
//        catch (SQLException throwables) {
//            System.out.println(throwables.getMessage());
//        }
//
    }
}

