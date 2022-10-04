package Client.ProjektGruppen;

import Server.ClientStart;
import javafx.scene.control.Alert;
import org.json.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjektGruppeStudentHinzufügen {
    public static ArrayList<String> lvMitglieder = new ArrayList<>();


    public static ArrayList<Integer> ProjektUser(int pgID) throws SQLException, IOException {
        ArrayList<Integer> userListe = new ArrayList<>();
        JSONObject json = new JSONObject();
        json.put("Methode","ProjektUser");
        json.put("Parameter1",pgID);
        ClientStart.verbinden.send(json);

        System.out.println("Sende sendeantrag");
        JSONArray jsonArray = ClientStart.verbinden.receiveary();
        System.out.println("Hab bekommen");
        ///immer für String Arrays
        if(jsonArray != null){
            for(int i =0;i<jsonArray.length();i++){
                userListe.add(Integer.parseInt(jsonArray.get(i)+""));
            }
        }
        return userListe;



    }
    public static void nutzerHinzufügen(int pgID,int userID) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Fehler");
        alert.setHeaderText("Fehler bei der Erstellung");

            if (userID < 0) {
                alert.setContentText("Nutzer gibt es nicht");
                alert.showAndWait();
                return;
            }
            JSONObject json = new JSONObject();
            json.put("Methode","pgNutzerHinzufuegen");
            json.put("Parameter1",pgID);
            json.put("Parameter2",userID);
            ClientStart.verbinden.send(json);
            alert.setTitle("Hinzugefügt");
            alert.setHeaderText("Erfolgreich Hinzugefügt");
            alert.setContentText("Sie haben einen Nutzer hinzugefügt");
            alert.showAndWait();


    }

    public static ArrayList<String> lehrveranstaltungsMitglieder(int lvID, String txt) throws SQLException, IOException {
        ArrayList<String> lvMitglieder = new ArrayList<>();
        JSONObject json = new JSONObject();
        json.put("Methode","lehrveranstaltungsMitglieder");
        json.put("Parameter1",lvID);
        json.put("Parameter2",txt);
        ClientStart.verbinden.send(json);

        JSONArray jsonArray = ClientStart.verbinden.receiveary();
        ///immer für String Arrays
        if(jsonArray != null){
            for(int i =0;i<jsonArray.length();i++){
                lvMitglieder.add((String) jsonArray.get(i) +"");
            }
        }


        return lvMitglieder;

    }

}
