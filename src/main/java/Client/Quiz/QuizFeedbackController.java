package Client.Quiz;

import Client.Lehrveranstaltung.LVUebersichtsseiteController;
import Server.ClientStart;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuizFeedbackController {

    @FXML
    private Pane pane;

    @FXML
    private ListView<String> listViewFeedback;


    private Stage stage;
    private int currentUserID;
    private int currentLVID;
    private int quizID;

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
    public void init(int quizID,int currentUserID,int currentLVID) throws IOException {
        this.quizID = quizID;
        this.currentUserID = currentUserID;
        this.currentLVID = currentLVID;

        JSONObject json = new JSONObject();
        json.put("Methode","getFeedback");
        json.put("P1",quizID);
        json.put("P2",currentUserID);
        ClientStart.verbinden.send(json);
        JSONArray jsonArray = ClientStart.verbinden.receiveary();
        List<String> ergebnis = new ArrayList<>();
        for(int i=0;i<jsonArray.length();i++){
            ergebnis.add(jsonArray.getString(i));
        }
        /*List<Integer> quizfragenIDs = new ArrayList<>();
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("SELECT * FROM QUIZ WHERE quizid=?");
            ps.setInt(1, quizID);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                quizfragenIDs.add(rs.getInt("quizfrageid"));
            }
            for(int i=0;i<quizfragenIDs.size();i++){
                PreparedStatement ps3 = con.prepareStatement("select * from quizfragen where id=?");
                ps3.setInt(1,quizfragenIDs.get(i));
                ResultSet rs3 = ps3.executeQuery();
                int fragennummer= 0;
                while(rs3.next()){
                    fragennummer = rs3.getInt("fragennummer");
                }
                PreparedStatement ps2 = con.prepareStatement("SELECT * FROM quizstatistik WHERE userid=? and quizfragenid=?");
                ps2.setInt(1, currentUserID);
                ps2.setInt(2, quizfragenIDs.get(i));
                ResultSet rs2 = ps2.executeQuery();
                while(rs2.next()){
                    if(rs2.getBoolean("richtig")){
                        ergebnis.add("Frage"+fragennummer+": richtig");
                    }
                    else{
                        ergebnis.add("Frage"+fragennummer+": falsch");
                    }
                }
            }
            PreparedStatement ps2 = con.prepareStatement("SELECT * FROM quizversuche WHERE userid=? and quizid=?");
            ps2.setInt(1, currentUserID);
            ps2.setInt(2, quizID);
            ResultSet rs2 = ps2.executeQuery();
            boolean bestanden=false;
            while (rs2.next()){
                bestanden = rs2.getBoolean("bestanden");
                if(bestanden==true){
                    ergebnis.add("Sie haben das Client.Quiz bestanden");
                }
                else{
                    ergebnis.add("Sie haben das Client.Quiz nicht bestanden");
                }
            }
            System.out.println("ergebnis: "+ ergebnis);
            con.close();
        }
        catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }*/
        listViewFeedback.getItems().addAll(ergebnis);
    }
}