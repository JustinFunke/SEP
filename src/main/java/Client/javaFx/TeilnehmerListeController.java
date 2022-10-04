package Client.javaFx;

import Client.Lehrveranstaltung.LVUebersichtsseiteController;
import Server.ClientStart;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class TeilnehmerListeController{
    @FXML
    private ListView<String> TLLehrendeList;
    @FXML
    private ListView<String> TLStudentenList;
    @FXML
    private Label TLTitelLabel;
    @FXML
    private Button ExitButton;
    @FXML
    private Button BackButton;
    @FXML
    private AnchorPane pane;

    private Stage stage;
    private Parent root;
    private Scene scene;
    @FXML
    private Label currentIDLabel;

    @FXML
    private Label currentLVIDLabel;

    private int currentUserID; // =1000106;
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




    public void BackButtonOnAction(ActionEvent event) throws IOException, SQLException {
        Stage stage1 = (Stage) BackButton.getScene().getWindow();
        stage1.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/LVUebersichtsseite.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        LVUebersichtsseiteController controller = loader.getController();
        controller.setIDLabel(currentUserID);
        controller.setLVIDLabel(currentLVID);
        controller.init();
        stage.show();
    }




    public void init() throws IOException {
        String test3 = currentIDLabel.getText();
        int test2 = Integer.parseInt(test3);
        currentUserID = test2;
        String test4 = currentLVIDLabel.getText();
        int test5 = Integer.parseInt(test4);
        currentLVID = test5;
        List<Integer> TeilnehmerIDsList = new ArrayList<>();
        List<String> LehrendeTeilnehmerNamenList = new ArrayList<>();
        List<String> StudentTeilnehmerNamenList = new ArrayList<>();

        JSONObject jsonLVName = new JSONObject();
        jsonLVName.put("Methode","setlvName");
        jsonLVName.put("Parameter1",currentLVID);
        ClientStart.verbinden.send(jsonLVName);
        JSONArray jsonArray = ClientStart.verbinden.receiveary();
        ///immer für String Arrays
        if(jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                TLTitelLabel.setText((String) jsonArray.get(i) + "");
            }
        }
        JSONObject jsonStudenten= new JSONObject();
        jsonStudenten.put("Methode","getStudentenTeilnehmer");
        jsonStudenten.put("Parameter1",currentUserID);
        jsonStudenten.put("Parameter2",currentLVID);
        ClientStart.verbinden.send(jsonStudenten);
        JSONArray jsonArrayStudenten = ClientStart.verbinden.receiveary();
        if(jsonArrayStudenten!=null){
            for(int i = 0;i<jsonArrayStudenten.length();i++){
                StudentTeilnehmerNamenList.add((String) jsonArrayStudenten.get(i)+"");
            }
        }
        JSONObject jsonLehrende=new JSONObject();
        jsonLehrende.put("Methode","getLehrendeTeilnehmer");
        jsonLehrende.put("Parameter1",currentUserID);
        jsonLehrende.put("Parameter2",currentLVID);
        ClientStart.verbinden.send(jsonLehrende);
        JSONArray jsonArrayLehrende = ClientStart.verbinden.receiveary();
        if(jsonArrayLehrende!=null){
            for(int i = 0;i<jsonArrayLehrende.length();i++) {
                LehrendeTeilnehmerNamenList.add((String) jsonArrayLehrende.get(i) + "");
            }
        }

//            PreparedStatement psLVName = con.prepareStatement("SELECT * FROM LEHRVERANSTALTUNG WHERE ID=?");
//            psLVName.setInt(1,currentLVID);
//            ResultSet rsLVName = psLVName.executeQuery();
//            rsLVName.next();
//            TLTitelLabel.setText(rsLVName.getString("NAME"));
//            PreparedStatement ps = con.prepareStatement("SELECT * FROM USERINVERANSTALTUNGEN WHERE VERANSTALTUNGSID=?");
//            ps.setInt(1,currentLVID);
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()){
//                TeilnehmerIDsList.add(rs.getInt("USERID"));
//            }
//            PreparedStatement psnamen = con.prepareStatement("Select * FROM USER WHERE ID=?");
//            for(int i=0;i < TeilnehmerIDsList.size();i++){
//                psnamen.setInt(1,TeilnehmerIDsList.get(i));
//                ResultSet rsn = psnamen.executeQuery();
//                rsn.next();
//                String newName = rsn.getString("Vorname") + " " + rsn.getString("Nachname");
//                if(currentUserID==rsn.getInt("ID")&& rsn.getBoolean("LEHRENDER")){
//                    TLLehrendeList.getItems().add(newName);
//                }
//                else if(currentUserID==rsn.getInt("ID")&& !rsn.getBoolean("LEHRENDER")){
//                    TLStudentenList.getItems().add(newName);
//                }
//                else if(rsn.getBoolean("LEHRENDER")){
//                    LehrendeTeilnehmerNamenList.add(newName);
//                }
//                else{
//                    StudentTeilnehmerNamenList.add(newName);
//                }
//            }
        TLLehrendeList.getItems().addAll(LehrendeTeilnehmerNamenList);
        TLStudentenList.getItems().addAll(StudentTeilnehmerNamenList);
        TLLehrendeList.scrollTo(0);
        TLLehrendeList.getSelectionModel().select(0);
        TLStudentenList.scrollTo(0);
        TLStudentenList.getSelectionModel().select(0);
//            con.close();
//        }
//
//        catch (SQLException | IOException throwables){
//            System.out.println(throwables.getMessage());
//
    }


    public void setIDLabel(int pUID) {
        Integer integer = pUID;
        currentIDLabel.setText(integer.toString());
    }
    public void setLVIDLabel(int pUID) {
        Integer integer = pUID;
        currentLVIDLabel.setText(integer.toString());
    }
    public void doubleClick(MouseEvent click){
        try {
            ArrayList<String> suchergebnisse = new ArrayList<>();
            if (click.getClickCount() == 2) {
                String ausgewähltesItem = TLStudentenList.getSelectionModel().getSelectedItem();   // Code zum Teilen von Namen: https://stackoverflow.com/questions/36096484/splitting-full-name
                String fullName = ausgewähltesItem;
                int idx = fullName.lastIndexOf(' ');
                if (idx == -1) {
                    throw new IllegalArgumentException("Only a single name: " + fullName);
                }
                String firstName = fullName.substring(0, idx);
                String lastName = fullName.substring(idx + 1);
                JSONObject json = new JSONObject();
                json.put("Methode","tLDoubleClick");
                json.put("Parameter1",firstName);
                json.put("Parameter2",lastName);
                json.put("Parameter3",currentUserID);
                ClientStart.verbinden.send(json);
                System.out.println("gesendet");
                JSONArray jsonArrayLehrende = ClientStart.verbinden.receiveary();
                System.out.println("empfangen");
                if(jsonArrayLehrende!=null){
                    for(int i = 0;i<jsonArrayLehrende.length();i++) {
                        suchergebnisse.add((String) jsonArrayLehrende.get(i) + "");
                    }
                }
                int SearchedID = Integer.parseInt(suchergebnisse.get(0));
                Boolean currentLehrender= Boolean.parseBoolean(suchergebnisse.get(2));
                Boolean SearchedLehrer = Boolean.parseBoolean(suchergebnisse.get(1));
//                    Connection con = DriverManager.getConnection(urlDB, user, passwort);
//                    PreparedStatement ps = con.prepareStatement("SELECT * FROM USER WHERE VORNAME=? AND NACHNAME=?");
//                    ps.setString(1, firstName);
//                    ps.setString(2, lastName);
//                    ResultSet rs = ps.executeQuery();
//                    rs.next();
//                    int SearchedID = rs.getInt("ID");
//                    Boolean SearchedLehrer = rs.getBoolean("LEHRENDER");
//                    PreparedStatement psCurrent = con.prepareStatement("SELECT * FROM USER WHERE ID=?");
//                    psCurrent.setInt(1, currentUserID);
//                    ResultSet rsCurrent = psCurrent.executeQuery();
//                    rsCurrent.next();
//                    Boolean currentLehrender = rsCurrent.getBoolean("LEHRENDER");
                if (SearchedID == currentUserID && SearchedLehrer) {
                    Stage stage1 = (Stage) BackButton.getScene().getWindow();
                    stage1.close();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/MeinNutzerprofilLehrender.fxml"));
                    Stage stage = new Stage();
                    stage.setScene(new Scene(loader.load()));
                    MeinNPLehrenderController LehrenderNPcontroller = loader.getController();
                    LehrenderNPcontroller.setIDLabel(currentUserID);
                    LehrenderNPcontroller.init();
                    stage.show();
                } else if (SearchedID == currentUserID && !SearchedLehrer) {
                    Stage stage1 = (Stage) BackButton.getScene().getWindow();
                    stage1.close();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/MeinNutzerprofilStudent.fxml"));
                    Stage stage = new Stage();
                    stage.setScene(new Scene(loader.load()));
                    MeinNPStudentController StudentNPcontroller = loader.getController();
                    StudentNPcontroller.setIDLabel(currentUserID);
                    StudentNPcontroller.init();
                    stage.show();
                } else if (!currentLehrender && !SearchedLehrer) {
                    Stage stage1 = (Stage) BackButton.getScene().getWindow();
                    stage1.close();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/NutzerprofilStudent(fürStudent).fxml"));
                    Stage stage = new Stage();
                    stage.setScene(new Scene(loader.load()));
                    NPStudentFürSController StudentNPcontroller = loader.getController();
                    StudentNPcontroller.setIDLabel(currentUserID);
                    StudentNPcontroller.setLVIDLabel(currentLVID);
                    StudentNPcontroller.setStudentenIDLabel(SearchedID);
                    StudentNPcontroller.init();
                    stage.show();
                } else if (currentLehrender && !SearchedLehrer) {
                    Stage stage1 = (Stage) BackButton.getScene().getWindow();
                    stage1.close();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/NutzerprofilStudent(fürLehrender).fxml"));
                    Stage stage = new Stage();
                    stage.setScene(new Scene(loader.load()));
                    NPStudentFürLController StudentNPcontroller = loader.getController();
                    StudentNPcontroller.setIDLabel(currentUserID);
                    StudentNPcontroller.setLVIDLabel(currentLVID);
                    StudentNPcontroller.setStudentenIDLabel(SearchedID);
                    StudentNPcontroller.init();
                    stage.show();
                } else if (SearchedLehrer) {
                    Stage stage1 = (Stage) BackButton.getScene().getWindow();
                    stage1.close();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/NutzerprofilLehrender.fxml"));
                    Stage stage = new Stage();
                    stage.setScene(new Scene(loader.load()));
                    NPLehrenderController StudentNPcontroller = loader.getController();
                    StudentNPcontroller.setIDLabel(currentUserID);
                    StudentNPcontroller.setLVIDLabel(currentLVID);
                    StudentNPcontroller.setLehrerIDLabel(SearchedID);
                    StudentNPcontroller.init();
                    stage.show();
                }
            }
        }
        catch (Exception e ){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Warnung");
            alert.setContentText("Bitte klicken sie auf einen User");

            alert.showAndWait();
        }
    }
    public void doubleClickLehrender(MouseEvent click){
        try {
            ArrayList<String> suchergebnisse = new ArrayList<>();
            if (click.getClickCount() == 2) {
                String ausgewähltesItem = TLLehrendeList.getSelectionModel().getSelectedItem();   // Code zum Teilen von Namen: https://stackoverflow.com/questions/36096484/splitting-full-name
                String fullName = ausgewähltesItem;
                int idx = fullName.lastIndexOf(' ');
                if (idx == -1) {
                    throw new IllegalArgumentException("Only a single name: " + fullName);
                }
                String firstName = fullName.substring(0, idx);
                String lastName = fullName.substring(idx + 1);
                JSONObject json = new JSONObject();
                json.put("Methode","tLDoubleClick");
                json.put("Parameter1",firstName);
                json.put("Parameter2",lastName);
                json.put("Parameter3",currentUserID);
                ClientStart.verbinden.send(json);
                JSONArray jsonArrayLehrende = ClientStart.verbinden.receiveary();
                if(jsonArrayLehrende!=null){
                    for(int i = 0;i<jsonArrayLehrende.length();i++) {
                        suchergebnisse.add((String) jsonArrayLehrende.get(i) + "");
                    }
                }
                int SearchedID = Integer.parseInt(suchergebnisse.get(0));
                Boolean currentLehrender= Boolean.parseBoolean(suchergebnisse.get(2));
                Boolean SearchedLehrer = Boolean.parseBoolean(suchergebnisse.get(1));
                System.out.println(SearchedID);
//                    Connection con = DriverManager.getConnection(urlDB, user, passwort);
//                    PreparedStatement ps = con.prepareStatement("SELECT * FROM USER WHERE VORNAME=? AND NACHNAME=?");
//                    ps.setString(1, firstName);
//                    ps.setString(2, lastName);
//                    ResultSet rs = ps.executeQuery();
//                    rs.next();
//                    int SearchedID = rs.getInt("ID");
//                    Boolean SearchedLehrer = rs.getBoolean("LEHRENDER");
//                    PreparedStatement psCurrent = con.prepareStatement("SELECT * FROM USER WHERE ID=?");
//                    psCurrent.setInt(1, currentUserID);
//                    ResultSet rsCurrent = psCurrent.executeQuery();
//                    rsCurrent.next();
//                    Boolean currentLehrender = rsCurrent.getBoolean("LEHRENDER");
                if (SearchedID == currentUserID && SearchedLehrer) {
                    Stage stage1 = (Stage) BackButton.getScene().getWindow();
                    stage1.close();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/MeinNutzerprofilLehrender.fxml"));
                    Stage stage = new Stage();
                    stage.setScene(new Scene(loader.load()));
                    MeinNPLehrenderController LehrenderNPcontroller = loader.getController();
                    LehrenderNPcontroller.setIDLabel(currentUserID);
                    LehrenderNPcontroller.init();
                    stage.show();
                } else if (SearchedID == currentUserID && !SearchedLehrer) {
                    Stage stage1 = (Stage) BackButton.getScene().getWindow();
                    stage1.close();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/MeinNutzerprofilStudent.fxml"));
                    Stage stage = new Stage();
                    stage.setScene(new Scene(loader.load()));
                    MeinNPStudentController StudentNPcontroller = loader.getController();
                    StudentNPcontroller.setIDLabel(currentUserID);
                    StudentNPcontroller.init();
                    stage.show();
                } else if (!currentLehrender && !SearchedLehrer) {
                    Stage stage1 = (Stage) BackButton.getScene().getWindow();
                    stage1.close();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/NutzerprofilStudent(fürStudent).fxml"));
                    Stage stage = new Stage();
                    stage.setScene(new Scene(loader.load()));
                    NPStudentFürSController StudentNPcontroller = loader.getController();
                    StudentNPcontroller.setIDLabel(currentUserID);
                    StudentNPcontroller.setLVIDLabel(currentLVID);
                    StudentNPcontroller.setStudentenIDLabel(SearchedID);
                    StudentNPcontroller.init();
                    stage.show();
                } else if (currentLehrender && !SearchedLehrer) {
                    Stage stage1 = (Stage) BackButton.getScene().getWindow();
                    stage1.close();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/NutzerprofilStudent(fürLehrender).fxml"));
                    Stage stage = new Stage();
                    stage.setScene(new Scene(loader.load()));
                    NPStudentFürLController StudentNPcontroller = loader.getController();
                    StudentNPcontroller.setIDLabel(currentUserID);
                    StudentNPcontroller.setLVIDLabel(currentLVID);
                    StudentNPcontroller.setStudentenIDLabel(SearchedID);
                    StudentNPcontroller.init();
                    stage.show();
                } else if (SearchedLehrer) {
                    Stage stage1 = (Stage) BackButton.getScene().getWindow();
                    stage1.close();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/NutzerprofilLehrender.fxml"));
                    Stage stage = new Stage();
                    stage.setScene(new Scene(loader.load()));
                    NPLehrenderController StudentNPcontroller = loader.getController();
                    StudentNPcontroller.setIDLabel(currentUserID);
                    StudentNPcontroller.setLVIDLabel(currentLVID);
                    StudentNPcontroller.setLehrerIDLabel(SearchedID);
                    StudentNPcontroller.init();
                    stage.show();
                }
            }
        }
        catch (Exception e ){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Warnung");
            alert.setContentText("Bitte klicken sie auf einen User");

            alert.showAndWait();
        }
    }


}

