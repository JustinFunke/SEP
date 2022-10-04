package Client.javaFx;

import Server.ClientStart;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListeAllerKurseController {
    @FXML
    private Button JoinCourseButton;
    @FXML
    private Label TitelLabel;
    @FXML
    private ChoiceBox <String> AlleLehrveranstaltungenChoice;
    @FXML
    private Button BackButton;

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



    public void init() throws IOException {
        String test3 = currentIDLabel.getText();
        int test2 = Integer.parseInt(test3);
        currentUserID = test2;
        AlleLehrveranstaltungenChoice.getItems().clear();
        List<String> LVListe = new ArrayList<>();
        JSONObject json = new JSONObject();
        json.put("Methode","ListeAllerLVs");
        ClientStart.verbinden.send(json);
        JSONArray jsonArray = ClientStart.verbinden.receiveary();
        ///immer für String Arrays
        if(jsonArray != null){
            for(int i =0;i<jsonArray.length();i++){
                LVListe.add((String) jsonArray.get(i) +"");
            }
        }
        AlleLehrveranstaltungenChoice.getItems().addAll(LVListe);
//        ArrayList<String> VeranstaltungList = new ArrayList<>();
//        try{
//            Connection con = DriverManager.getConnection(urlDB,user,passwort);
//            Statement st = con.createStatement();
//            ResultSet rs = st.executeQuery("SELECT * from LEHRVERANSTALTUNG");
//            VeranstaltungList.clear();
//            while (rs.next()){
//                VeranstaltungList.add(rs.getString("NAME"));
//            }
//          AlleLehrveranstaltungenChoice.getItems().addAll(VeranstaltungList);
//            con.close();
//        }
//
//        catch (SQLException throwables){
//            System.out.println(throwables.getMessage());
//
//        }

    }
    public void setIDLabel(int pUID){
        Integer integer = pUID;
        currentIDLabel.setText(integer.toString());
    }
    public  void JoinCourseButtonOnAction(){
        JSONObject json = new JSONObject();
        json.put("Methode","listeAllerKurseADD");
        json.put("Parameter1",AlleLehrveranstaltungenChoice.getValue());
        json.put("Parameter2",currentUserID);
        System.out.println("versenden");
        ClientStart.verbinden.send(json);
        System.out.println("versendet");
//        try {
//            Connection con = DriverManager.getConnection(urlDB, user, passwort);
//            Statement st = con.createStatement();
//            PreparedStatement ps = con.prepareStatement("SELECT * FROM LEHRVERANSTALTUNG WHERE NAME=?");
//            ps.setString(1, AlleLehrveranstaltungenChoice.getValue());
//            ResultSet rs = ps.executeQuery();
//            rs.next();
//            int SearchedLVID = rs.getInt("ID");
//            System.out.println("Dem Kurs " + AlleLehrveranstaltungenChoice.getValue() + " wird versucht beizutreten");
//            Datenbank.DbUserLvHinzufügen(currentUserID,SearchedLVID);
//            con.close();
//        }
//        catch (SQLException throwables){
//            System.out.println(throwables.getMessage());
//
//        }
    }
    public void BackButtonOnAction() throws IOException {
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
