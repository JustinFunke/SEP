package Client.Quiz;

import Client.Lehrveranstaltung.LVUebersichtsseiteController;
import Server.ClientStart;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.concurrent.TimeUnit;

public class QuizXMLController {

    @FXML
    private Label labelDateiName;
    @FXML
    private Pane pane;
    File xmlFile;
    private int currentUserID;
    private int currentLVID;
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

    @FXML
    void cmdDateiAuswählen(ActionEvent event) {
        FileChooser holeCSV = new FileChooser();
        holeCSV.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML", "*.xml"));
        Stage neueStage = new Stage();
        xmlFile = holeCSV.showOpenDialog(neueStage);
        if(xmlFile != null) {
            labelDateiName.setText(xmlFile.getAbsolutePath());
        } else {

        }
    }

    @FXML
    void cmdErstellen(ActionEvent event) throws IOException, SQLException {
        //xml-Einlesen anhand der Vorlage auf dieser Internetseite
        //https://www.delftstack.com/de/howto/java/java-read-xml/
        if(xmlFile!=null){

            try {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document document = db.parse(xmlFile);
                document.getDocumentElement().normalize();
                System.out.println("Root Element :" + document.getDocumentElement().getNodeName());
                NodeList nList = document.getElementsByTagName("Fragenzeile");
                System.out.println("----------------------------");
                for (int temp = 0; temp < nList.getLength(); temp++) {
                    Node nNode = nList.item(temp);
                    System.out.println("\nCurrent Element :" + nNode.getNodeName());
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        JSONObject json = new JSONObject();
                        json.put("Methode","insertQuizfrageXML");
                        json.put("P1",eElement.getElementsByTagName("Frage").item(0).getTextContent());
                        json.put("P2",eElement.getElementsByTagName("AntwortA").item(0).getTextContent());
                        json.put("P3",eElement.getElementsByTagName("AntwortB").item(0).getTextContent());
                        json.put("P4",eElement.getElementsByTagName("AntwortC").item(0).getTextContent());
                        json.put("P5",eElement.getElementsByTagName("AntwortD").item(0).getTextContent());
                        json.put("P6",eElement.getElementsByTagName("KorrekteAntwort").item(0).getTextContent());
                        json.put("P7",Integer.parseInt(eElement.getElementsByTagName("Fragennummer").item(0).getTextContent()));
                        ClientStart.verbinden.send(json);
                        try{
                            TimeUnit.MILLISECONDS.sleep(100);
                        }
                        catch(Exception e){
                            System.out.println(e);
                        }
                        /*Connection con = DriverManager.getConnection(urlDB, user, passwort);
                        PreparedStatement ps = con.prepareStatement("INSERT INTO QUIZFRAGEN (quizfrage,antwort_a,antwort_b,antwort_c,antwort_d,richtige_antwort,markiert,fragennummer)VALUES (?, ?, ?, ?, ?, ?, true,?)");
                        ps.setString(1, eElement.getElementsByTagName("Frage").item(0).getTextContent());
                        ps.setString(2, eElement.getElementsByTagName("AntwortA").item(0).getTextContent());
                        ps.setString(3, eElement.getElementsByTagName("AntwortB").item(0).getTextContent());
                        ps.setString(4, eElement.getElementsByTagName("AntwortC").item(0).getTextContent());
                        ps.setString(5, eElement.getElementsByTagName("AntwortD").item(0).getTextContent());
                        ps.setString(6, eElement.getElementsByTagName("KorrekteAntwort").item(0).getTextContent());
                        ps.setInt(7, Integer.parseInt(eElement.getElementsByTagName("Fragennummer").item(0).getTextContent()));
                        ps.executeUpdate();
                        System.out.println("Quizfrage hinzugefügt! / Markierung auf TRUE");
                        con.close();*/
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            JSONObject json2 = new JSONObject();
            json2.put("Methode","updateQuiz");
            json2.put("P1",currentLVID);
            ClientStart.verbinden.send(json2);
            /*int quizid = 0;
            try{
                Connection con = DriverManager.getConnection(urlDB, user, passwort);
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("select max(QUIZID) as maxquizid from QUIZ");
                while(rs.next()){
                    quizid = rs.getInt("maxquizid")+1;
                }
                con.close();
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
            erfolgreich.setHeaderText("Keine xml-Datei ausgewählt!");
            erfolgreich.setContentText("Bitte wählen Sie zuerst eine xml-Datei aus.");
            erfolgreich.showAndWait();
        }

    }
    public void init(int currentUserID, int currentLVID){
        this.currentUserID = currentUserID;
        this.currentLVID = currentLVID;
        xmlFile = null;
        labelDateiName.setText("Noch keine Datei ausgewählt");
    }

}