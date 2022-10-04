package Client.ProjektGruppen;

import Server.ClientStart;
import javafx.scene.control.Alert;
import org.json.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class TodoListe {

    public static void toDoHinzufuegen(String toDo,int pid, int userid){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Fehler");
        alert.setHeaderText("Fehler bei der TODO-Erstellung");

            if(toDo.length() <3){
                alert.setContentText("ToDo muss länger als 3 Zeichen sein");
                alert.showAndWait();
                return;
            }

            JSONObject json = new JSONObject();
            json.put("Methode","toDoHinzufuegen");
            json.put("Parameter1",toDo);
            json.put("Parameter2",pid);
            json.put("Parameter3",userid);
            ClientStart.verbinden.send(json);

            alert.setTitle("Hinzugefügt");
            alert.setHeaderText("ToDo Hinzugefügt");
            alert.setContentText("Sie haben ein Todo erfolgreich hinzugefügt");
            alert.showAndWait();

    }
    public static void toDoLöschen(String toDo,int pgID) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Fehler");
        alert.setHeaderText("Fehler bei der TODO-Erstellung");

        try {
            JSONObject json = new JSONObject();
            json.put("Methode","toDoLoeschen");
            json.put("Parameter1",toDo);
            json.put("Parameter2", pgID);
            ClientStart.verbinden.send(json);


            alert.setTitle("Gelöscht");
            alert.setHeaderText("ToDo Gelöscht");
            alert.setContentText("Sie haben ein Todo erfolgreich gelöscht");
            alert.showAndWait();
        }catch (Exception ex){
            alert.setTitle("Gelöscht");
            alert.setHeaderText("Fehler");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }


    }
    public static ArrayList<String> listAnzeigen(int pgID) throws IOException {
        System.out.println("Bin in listAnzeigen");
        ArrayList<String> toDOListe = new ArrayList<>();

            JSONObject json = new JSONObject();
            json.put("Methode","todoListeAnzeigen");
            json.put("Parameter1",pgID);
            System.out.println("Bin in listAnzeigen2");
            ClientStart.verbinden.send(json);
            System.out.println("Bin in listAnzeigen3");

            JSONArray jsonArray = ClientStart.verbinden.receiveary();
            ///immer für String Arrays
            if(jsonArray != null){
                System.out.println("Bin in listAnzeigen3");
                for(int i =0;i<jsonArray.length();i++){
                    toDOListe.add((String) jsonArray.get(i) +"");
                }
            }

            return toDOListe;

    }

    public static ArrayList<Integer> toDoUserListAnzeigen(int pgID){
        ArrayList<Integer> toDoUserList = new ArrayList();
        try {
            System.out.println("bin in Userliste ");
            JSONObject json = new JSONObject();
            json.put("Methode","todoListeUserAnzeigen");
            json.put("Parameter1",pgID);
            ClientStart.verbinden.send(json);

            System.out.println("bin in Userliste bekommen");
            JSONArray jsonArray = ClientStart.verbinden.receiveary();
            //immer für Int Arrays
            if(jsonArray != null){
                for(int i =0;i<jsonArray.length();i++){
                    toDoUserList.add(Integer.parseInt(jsonArray.get(i)+""));
                }
            }


            return toDoUserList;
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return toDoUserList;
        }

    }
}
