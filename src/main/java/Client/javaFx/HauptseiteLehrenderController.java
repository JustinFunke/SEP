package Client.javaFx;

import Client.Lehrveranstaltung.LVUebersichtsseiteController;
import Client.Lehrveranstaltung.NeueLVCSVController;
import Client.Lehrveranstaltung.NeueLVFormularController;
import Client.Kalender.CalendarController;
import Server.ClientStart;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.sql.*;
import java.util.*;

public class HauptseiteLehrenderController {

    @FXML
    private Pane pane;
    @FXML
    private Button btnName;
    @FXML
    private ListView<String> listViewLV;
    @FXML
    private Label currentIDLabel;
    @FXML
    private Label currentLVIDLabel;
    @FXML
    private ChoiceBox<String> boxSemester;



    private Stage stage;
    private Parent root;
    private Scene scene;
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

    @FXML
    void cmdCSVButton(ActionEvent event) throws IOException {
        //Zu NeueLVCSV
//        Parent newRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/NeueLVCSV.fxml")));
//        Stage newStage = new Stage();
//        Scene newScene = new Scene(newRoot);
//        newStage.setScene(newScene);
//        newStage.show();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/NeueLVCSV.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        NeueLVCSVController controller = loader.getController();
        controller.setIDLabel(currentUserID);
        controller.init();
        stage.show();
    }
    @FXML
    void cmdFormularButton(ActionEvent event) throws IOException {
        //Zu NeueLVFormular
//        Parent newRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/NeueLVFormular.fxml")));
//        Stage newStage = new Stage();
//        Scene newScene = new Scene(newRoot);
//        newStage.setScene(newScene);
//        newStage.show();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/NeueLVFormular.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        NeueLVFormularController controller = loader.getController();
        controller.setIDLabel(currentUserID);
        controller.init();
        stage.show();
    }

    @FXML
    void cmdKurslisteButton(ActionEvent event) throws IOException {
        //Zu ListeAllerKurse
        stage = (Stage) pane.getScene().getWindow();
        stage.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeAllerKurse.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        ListeAllerKurseController controller = loader.getController();
        controller.setIDLabel(currentUserID);
        controller.init();
        stage.show();
    }

    @FXML
    void cmdLogoutButton(ActionEvent event) throws IOException {
        //Zu Erste Seite
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Login.fxml")));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void cmdNameButton(ActionEvent event) throws IOException {
        //Zu MeinNutzerprofilLehrender
        stage = (Stage) pane.getScene().getWindow();
        stage.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MeinNutzerprofilLehrender.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        MeinNPLehrenderController controller = loader.getController();
        controller.setIDLabel(currentUserID);
        controller.init();
        stage.show();
    }

    @FXML
    void cmdRefreshButton(ActionEvent event) throws IOException {
        //Refreshe die Seite
        init();
    }

    @FXML
    void cmdSucheStudentButton(ActionEvent event) throws IOException {
        //Zu StudentenSuche
        stage = (Stage) pane.getScene().getWindow();
        stage.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/StudentenSuche.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        StudentenSucheController controller = loader.getController();
        controller.setIDLabel(currentUserID);
        stage.show();
    }
    @FXML
    void cmdSemesterApply(ActionEvent event) throws IOException {
        //String clickedSemester = boxSemester.getSelectionModel().getSelectedItem();
        String clickedSemester = boxSemester.getValue();
        ArrayList<String> veranstaltungen = new ArrayList<>();


        JSONObject json = new JSONObject();
        json.put("Methode","lvNamenVonUser");
        json.put("P1",currentUserID);
        json.put("P2",clickedSemester);
        ClientStart.verbinden.send(json);
        JSONArray jsonArray = ClientStart.verbinden.receiveary();
        for(int i=0; i<jsonArray.length(); i++){
            veranstaltungen.add(jsonArray.getString(i));
        }
        System.out.println(veranstaltungen);
        listViewLV.getItems().clear();
        java.util.Collections.sort(veranstaltungen); //sortieren
        listViewLV.getItems().addAll(veranstaltungen);


        //alte
//        List<String> VeranstaltungList = new ArrayList<>();
//        List<String> GemeinsameList = new ArrayList<>();
//        try{
//            Connection con = DriverManager.getConnection(urlDB,user,passwort);
//            PreparedStatement ps = con.prepareStatement("SELECT * FROM USERINVERANSTALTUNGEN WHERE USERID=?");
//            ps.setInt(1, currentUserID);
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()){
//                GemeinsameList.add(rs.getString("VERANSTALTUNGSID"));
//            }
//            VeranstaltungList.clear();
//            for (int i = 0;i<GemeinsameList.size();i++){
//                ps = con.prepareStatement("SELECT * FROM LEHRVERANSTALTUNG WHERE ID=? AND SEMESTER=?");
//                ps.setInt(1, Integer.parseInt(GemeinsameList.get(i)));
//                ps.setString(2, clickedSemester);
//                rs = ps.executeQuery();
//                while (rs.next()){
//                    VeranstaltungList.add(rs.getString("NAME"));
//                }
//            }
//            con.close();
//            listViewLV.getItems().clear();
//            java.util.Collections.sort(VeranstaltungList); //sortieren
//            listViewLV.getItems().addAll(VeranstaltungList);
//        }
//
//        catch (SQLException throwables){
//            System.out.println(throwables.getMessage());
//
//        }
    }
    //click auf Name von LV um zu LVUebersichtsseite zu kommen mit der LV-ID
    @FXML
    void cmdLVclick(MouseEvent click) throws IOException, SQLException {
        if (click.getClickCount() == 2) {
            String ausgewähltesItem = listViewLV.getSelectionModel().getSelectedItem();
            String clickedSemester = boxSemester.getValue();
            currentLVID = 0;

            JSONObject json = new JSONObject();
            json.put("Methode","getIDfromLV");
            json.put("P1",ausgewähltesItem);
            json.put("P2",clickedSemester);
            ClientStart.verbinden.send(json);
            JSONObject jsonReturn = ClientStart.verbinden.receiveobj();
            String lvID = jsonReturn.get("P1")+"";
            if(lvID != null && !lvID.equals("")){
                currentLVID = Integer.parseInt(lvID);
            }
            if (currentLVID != 0) {
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

//                try{
//                    Connection con = DriverManager.getConnection(urlDB, user, passwort);
//                    PreparedStatement ps = con.prepareStatement("SELECT * FROM LEHRVERANSTALTUNG WHERE NAME=? AND SEMESTER=?");
//                    ps.setString(1, ausgewähltesItem);
//                    ps.setString(2, clickedSemester);
//                    ResultSet rs = ps.executeQuery();
//                    while (rs.next()){
//                        currentLVID = rs.getInt("ID");
//                    }
//                    con.close();
//                    System.out.println(currentLVID);
//                    if (currentLVID != 0) {
//                        stage = (Stage) pane.getScene().getWindow();
//                        stage.close();
//                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/LVUebersichtsseite.fxml"));
//                        Stage stage = new Stage();
//                        stage.setScene(new Scene(loader.load()));
//                        LVUebersichtsseiteController controller = loader.getController();
//                        controller.setIDLabel(currentUserID);
//                        controller.setLVIDLabel(currentLVID);
//                        controller.init();
//                        stage.show();
//                    }
//                    con.close();
//                }
//                catch (SQLException throwables) {
//                    System.out.println(throwables.getMessage());
//                }
        }
    }
    @FXML
    void cmdKalender(ActionEvent event) throws IOException {
        //Zu Calender
        stage = (Stage) pane.getScene().getWindow();
        stage.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Calendar.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        CalendarController controller = loader.getController();
        controller.setIDLabel(currentUserID);
        controller.init();
        stage.show();
    }
    public void setIDLabel(int test){
        Integer integer = test;
        currentIDLabel.setText(integer.toString());
    }
    public void setLVIDLabel(int pUID) {
        Integer integer = pUID;
        currentLVIDLabel.setText(integer.toString());
    }

    public void init() throws IOException {
        String test3 = currentIDLabel.getText();
        int test2 = Integer.parseInt(test3);
        currentUserID = test2;
        //currentUserID = 1000099;

        //Choicebox füllen
        ArrayList<String> eigeneVeranstaltungen = new ArrayList<>();
        ArrayList<String> ergebnis = new ArrayList<>();
        JSONObject json = new JSONObject();
        json.put("Methode","lvIDsVonUser");
        json.put("P1",currentUserID);
        ClientStart.verbinden.send(json);
        JSONArray jsonArray = ClientStart.verbinden.receiveary();
        if (jsonArray != null) {
            for (int i=0;i<jsonArray.length();i++){
                eigeneVeranstaltungen.add(jsonArray.getString(i));
            }
        }
        for (int i = 0;i<eigeneVeranstaltungen.size();i++){
            JSONObject json1 = new JSONObject();
            json1.put("Methode","getSemesterfromLV");
            json1.put("P1",eigeneVeranstaltungen.get(i));
            ClientStart.verbinden.send(json1);
            JSONObject jsonReturn = ClientStart.verbinden.receiveobj();
            String lvSemester = jsonReturn.get("P1")+"";
            //ändern
            if(lvSemester != null && !lvSemester.equals("")){
                ergebnis.add(lvSemester);
            }
            Set<String> set = new HashSet<>(ergebnis);
            //
            ergebnis.clear();
            ergebnis.addAll(set);
            boxSemester.getItems().clear();
            java.util.Collections.sort(ergebnis); //sortieren
            boxSemester.getItems().addAll(ergebnis);
            if (!boxSemester.getItems().isEmpty()) {
                boxSemester.setValue(boxSemester.getItems().get(0)); //setze Default Value auf den ersten Eintrag
            }
        }

        //Liste mit Veranstaltungen anzeigen (von Standartsemester)
        String clickedSemester = boxSemester.getValue();
        ArrayList<String> veranstaltungen = new ArrayList<>();


        JSONObject json2 = new JSONObject();
        json2.put("Methode","lvNamenVonUser");
        json2.put("P1",currentUserID);
        json2.put("P2",clickedSemester);
        ClientStart.verbinden.send(json2);
        JSONArray jsonArray2 = ClientStart.verbinden.receiveary();
        for(int i=0; i<jsonArray2.length(); i++){
            veranstaltungen.add(jsonArray2.getString(i));
        }
        System.out.println(veranstaltungen);
        listViewLV.getItems().clear();
        java.util.Collections.sort(veranstaltungen); //sortieren
        listViewLV.getItems().addAll(veranstaltungen);

        //Auf dem Button für den Namen wird der Name des Users angezeigt
        JSONObject json3 = new JSONObject();
        json3.put("Methode","userName");
        json3.put("P1",currentUserID);
        ClientStart.verbinden.send(json3);
        JSONObject jsonReturn = ClientStart.verbinden.receiveobj();
        String name = jsonReturn.get("P1")+"";
        btnName.setText(name);


        //ChoiceBox soll alle Semester von Student zeigen
//        try {
//
//            List<String> EigeneVeranstaltungen = new ArrayList<>();
//            List<String> Ergebnis = new ArrayList<>();
//            //
//            Connection con = DriverManager.getConnection(urlDB, user, passwort);
//            PreparedStatement ps = con.prepareStatement("SELECT * FROM USERINVERANSTALTUNGEN WHERE USERID=?");
//            ps.setInt(1, currentUserID);
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()){
//                EigeneVeranstaltungen.add(rs.getString("VERANSTALTUNGSID"));
//            }
//            //
//            Ergebnis.clear();
//            for (int i = 0;i<EigeneVeranstaltungen.size();i++){
//                ps = con.prepareStatement("SELECT * FROM LEHRVERANSTALTUNG WHERE ID=?");
//                ps.setInt(1, Integer.parseInt(EigeneVeranstaltungen.get(i)));
//                rs = ps.executeQuery();
//                while (rs.next()){
//                    Ergebnis.add(rs.getString("SEMESTER"));
//                }
//            }
//            con.close();
//            //Alle doppelten Einträge werden von der Liste gelöscht siehe https://wiki.byte-welt.net/wiki/Doppelte_Datensätze_aus_ArrayList_entfernen
////            int size = Ergebnis.size();
////            for(int i = 0; i < size; i++) {
////                Object o1 = Ergebnis.get(i);
////                for(int j = i + 1; j < size; j++) {
////                    Object o2 = Ergebnis.get(j);
////                    if(o1.equals(o2)) {
////                        Ergebnis.remove(j);
////                        size--;
////                    }
////                }
////            }
//            //Ergebnis = removeDuplicates(Ergebnis);
//            //https://stackoverflow.com/questions/203984/how-do-i-remove-repeated-elements-from-arraylist#:~:text=The%20easiest%20way%20to%20remove,clear()%3B%20yourList.
//            Set<String> set = new HashSet<>(Ergebnis);
//            //
//            Ergebnis.clear();
//            Ergebnis.addAll(set);
//            boxSemester.getItems().clear();
//            java.util.Collections.sort(Ergebnis); //sortieren
//            boxSemester.getItems().addAll(Ergebnis);
//            if (!boxSemester.getItems().isEmpty()) {
//                boxSemester.setValue(boxSemester.getItems().get(0)); //setze Default Value auf den ersten Eintrag
//            }
//            //
//            //boxSemester.setValue("");
//
//        }
//        catch (SQLException throwables) {
//            System.out.println(throwables.getMessage());
//        }
//        String clickedSemester = boxSemester.getValue();
//        List<String> VeranstaltungList = new ArrayList<>();
//        List<String> GemeinsameList = new ArrayList<>();
//        try{
//            Connection con = DriverManager.getConnection(urlDB,user,passwort);
//            PreparedStatement ps = con.prepareStatement("SELECT * FROM USERINVERANSTALTUNGEN WHERE USERID=?");
//            ps.setInt(1, currentUserID);
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()){
//                GemeinsameList.add(rs.getString("VERANSTALTUNGSID"));
//            }
//            VeranstaltungList.clear();
//            for (int i = 0;i<GemeinsameList.size();i++){
//                ps = con.prepareStatement("SELECT * FROM LEHRVERANSTALTUNG WHERE ID=? AND SEMESTER=?");
//                ps.setInt(1, Integer.parseInt(GemeinsameList.get(i)));
//                ps.setString(2, clickedSemester);
//                rs = ps.executeQuery();
//                while (rs.next()){
//                    VeranstaltungList.add(rs.getString("NAME"));
//                }
//            }
//            con.close();
//            listViewLV.getItems().clear();
//            java.util.Collections.sort(VeranstaltungList); //sortieren
//            java.util.Collections.reverse(VeranstaltungList); //Reihenfolge reversen (jetzt chronologisch abwärts)
//            listViewLV.getItems().addAll(VeranstaltungList);
//            con.close();
//        }
//
//        catch (SQLException throwables){
//            System.out.println(throwables.getMessage());
//
//        }
//        //Auf dem Button für den Namen wird der Name des Users angezeigt
//        try {
//            Connection con = DriverManager.getConnection(urlDB, user, passwort);
//            Statement st = con.createStatement();
//            PreparedStatement ps = con.prepareStatement("SELECT * FROM USER WHERE ID=?");
//            ps.setInt(1, currentUserID);
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()) {
//                btnName.setText(rs.getString("VORNAME") + " " + rs.getString("NACHNAME"));
//            }
//            con.close();
//        }
//
//        catch (SQLException throwables) {
//            System.out.println(throwables.getMessage());
//        }
    }

    //von Sebastian für Zyklus 3 - Themenangebote
    @FXML
    public void neuesThema(ActionEvent klick) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/NeuesThemaBibtex.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        NeuesThemaBibtexController controller = loader.getController();
        controller.setIDLabel(currentUserID);
        controller.init();
        stage.show();
    }

    @FXML
    public void neuesThemaManuell(ActionEvent klick) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/NeuesThemaManuell.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        NeuesThemaManuellController controller = loader.getController();
        controller.setIDLabel(currentUserID);
        controller.init();
        stage.show();
    }

}

