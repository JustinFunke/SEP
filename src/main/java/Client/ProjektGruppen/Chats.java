package Client.ProjektGruppen;

import Server.ClientStart;
import javafx.scene.control.Alert;
import org.json.JSONArray;
import org.json.simple.JSONObject;

import java.sql.*;
import java.util.ArrayList;

public class Chats {

    public static void NachrichtSenden(String nachricht,int pgID, int userid){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Fehler");
        alert.setHeaderText("Fehler bei der Erstellung");


        try {
            JSONObject json = new JSONObject();
            json.put("Methode", "NachrichtSenden");
            json.put("P1", nachricht);
            json.put("P2", pgID);
            json.put("P3", userid);
            ClientStart.verbinden.send(json);
        }
        catch (Exception ex){
            System.out.println(ex);
        }

    }
    public static ArrayList<String> NachrichtenAnzeigen(int pgID){
        ArrayList<String> nachrichten = new ArrayList<>();

        try {
            //senden
            JSONObject json = new JSONObject();
            json.put("Methode", "NachrichtenAnzeigen");
            json.put("P1", pgID);
            ClientStart.verbinden.send(json);

            //Bekommen
            JSONArray jsonArray = ClientStart.verbinden.receiveary();
            ///immer für String Arrays
            if(jsonArray != null){
                for(int i =0;i<jsonArray.length();i++){
                    nachrichten.add((String) jsonArray.get(i) +"");
                }
            }

            return nachrichten;
        }catch (Exception throwables){
            System.out.println(throwables.getMessage());
            return nachrichten;
        }

    }
    public static ArrayList<Integer> UserAnzeigen(int pgID){
        ArrayList<Integer> userList = new ArrayList<>();
        try {
            JSONObject json = new JSONObject();
            json.put("Methode", "UserAnzeigen");
            json.put("P1", pgID);
            ClientStart.verbinden.send(json);

            JSONArray jsonArray = ClientStart.verbinden.receiveary();
            //immer für Int Arrays
            if(jsonArray != null){
                for(int i =0;i<jsonArray.length();i++){
                    userList.add(Integer.parseInt(jsonArray.get(i)+""));
                }
            }


            return userList;
        }catch (Exception throwables){
            System.out.println(throwables.getMessage());
            return userList;
        }

    }
}
