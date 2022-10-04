package Server;

import javafx.scene.control.Alert;
import org.json.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class pgHinzufügen {

    public static ArrayList<Integer> ProjektUser(int pgID) throws SQLException {
        ArrayList<Integer> userListe = new ArrayList<>();
        try {
            Connection con = ClientThread.conDB();
            Statement st = con.createStatement();
            PreparedStatement pSt = con.prepareStatement("SELECT * FROM PROJEKTUSER WHERE Projektid=?");
            pSt.setInt(1, pgID);
            ResultSet rs = pSt.executeQuery();
            while (rs.next()) {
                userListe.add(rs.getInt("Userid"));
            }
            con.close();
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
        return userListe;
    }
    public static void nutzerHinzufügen(int pgID,int userID){
        try {
            Connection con = ClientThread.conDB();
            Statement st = con.createStatement();
            st.executeUpdate("Insert INTO Projektuser(userid,projektid)" + " VALUES " + "(" + userID + ", " + pgID + ")");
            con.close();
        }
        catch (SQLException ex){
            System.out.println(ex);
        }
    }
    public static ArrayList<String> lehrveranstaltungsMitglieder(int lvID, String txt) throws SQLException {
        ArrayList<String> lvMitglieder = new ArrayList<>();

        try {
            Connection con = ClientThread.conDB();
            Statement st = con.createStatement();
            PreparedStatement pSt = con.prepareStatement("SELECT userid FROM USERINVERANSTALTUNGEN  WHERE VERANSTALTUNGSID=? AND userid like '" + txt + "%'");
            pSt.setInt(1, lvID);
            ResultSet rs = pSt.executeQuery();
            while (rs.next()) {
                lvMitglieder.add("" + rs.getInt("userid"));
            }
            System.out.println(lvID);
            System.out.println(lvMitglieder.size());
            con.close();
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
        return lvMitglieder;

    }
    public static void toDoHinzufügen(String toDo,int pid,int userid) throws SQLException {
        try {
            Connection con = ClientThread.conDB();
            Statement st = con.createStatement();
            st.executeUpdate("Insert INTO TODO(projektgruppe,todo, userid)" + " VALUES " + "(" + pid + ", '" + toDo + "', '" + userid + "')");
            con.close();
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
        }


    }
    public static void toDoLoeschen(String toDo,int pgID) throws SQLException {

        try {

            Connection con = ClientThread.conDB();
            Statement st = con.createStatement();
            PreparedStatement pSt = con.prepareStatement("DELETE FROM TODO WHERE Projektgruppe=? AND TODO=?");
            pSt.setInt(1, pgID);
            pSt.setString(2, toDo);
            pSt.executeUpdate();
            con.close();

        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }


    }
    public static ArrayList<String> todoListeAnzeigen(int pgID){
        ArrayList<String> toDOListe = new ArrayList<>();
        try {
            Connection con = ClientThread.conDB();
            PreparedStatement pSt = con.prepareStatement("SELECT * FROM TODO WHERE Projektgruppe=?");
            pSt.setInt(1,pgID);
            ResultSet rs = pSt.executeQuery();
            while (rs.next()){
                toDOListe.add(rs.getString("todo"));
            }
            con.close();
            return toDOListe;
        }catch (SQLException throwables){
            System.out.println(throwables.getMessage());
            return toDOListe;
        }
    }
    public static ArrayList<Integer> toDoUserListAnzeigen(int pgID){
        ArrayList<Integer> toDoUserList = new ArrayList();
        try {

            Connection con = ClientThread.conDB();
            PreparedStatement abfrageUser = con.prepareStatement("SELECT USERID FROM TODO WHERE Projektgruppe=?");
            abfrageUser.setInt(1,pgID);
            ResultSet abfrageUserErgebnis = abfrageUser.executeQuery();
            while (abfrageUserErgebnis.next()){
                toDoUserList.add(abfrageUserErgebnis.getInt("USERID"));
            }
            con.close();
            return toDoUserList;
        }catch (SQLException throwables){
            System.out.println(throwables.getMessage());
            return toDoUserList;
        }
    }
    public static void NachrichtSenden(String nachricht,int pgID, int userid){
        try {
            Connection con = ClientThread.conDB();
            Statement st = con.createStatement();

            st.executeUpdate("Insert INTO ChatPG (projektid,nachricht,userid)" + " VALUES " + "(" + pgID + ", '" + nachricht +"', "+userid + ")");
            con.close();
        }
        catch (SQLException ex){
            System.out.println(ex);
        }

    }
    public static ArrayList<String> NachrichtenAnzeigen(int pgID){
        ArrayList<String> nachrichten = new ArrayList<>();
        try {
            Connection con = ClientThread.conDB();
            Statement st = con.createStatement();
            PreparedStatement pSt = con.prepareStatement("SELECT * FROM ChatPG WHERE Projektid=?");
            pSt.setInt(1,pgID);
            ResultSet rs = pSt.executeQuery();
            while (rs.next()){
                nachrichten.add(rs.getString("nachricht"));
            }
            con.close();
            return nachrichten;
        }catch (SQLException throwables){
            System.out.println(throwables.getMessage());
            return nachrichten;
        }

    }
    public static ArrayList<Integer> UserAnzeigen(int pgID){
        ArrayList<Integer> userList = new ArrayList<>();
        try {
            Connection con = ClientThread.conDB();
            Statement st = con.createStatement();
            PreparedStatement pSt = con.prepareStatement("SELECT * FROM ChatPG WHERE projektid=?");
            pSt.setInt(1,pgID);
            ResultSet rs = pSt.executeQuery();
            while (rs.next()){
                userList.add(rs.getInt("userid"));
            }
            con.close();
            return userList;
        }catch (SQLException throwables){
            System.out.println(throwables.getMessage());
            return userList;
        }

    }
}
