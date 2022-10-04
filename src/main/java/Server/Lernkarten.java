package Server;

import java.sql.*;
import java.util.ArrayList;

public class Lernkarten {
    public static void LernkarteHinzufuegen(int pid, String name, String frage, String antwort){
        try{
            Connection con = ClientThread.conDB();
            Statement st = con.createStatement();
            st.executeUpdate("Insert INTO Lernkarten(pid,LernkartenName,LernkartenFrage,LernkartenAntwort)" + " VALUES " + "(" + pid + ", '"+name+"', '"+frage+"', '"+antwort+"')");
            con.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public static ArrayList<String>LernkarteNamenVergeben(int pid){
        ArrayList<String> namenListe = new ArrayList<>();
        try{
            Connection con = ClientThread.conDB();
            Statement st = con.createStatement();
            System.out.println("bin in LernkarteNamenVergeben");
            PreparedStatement pSt = con.prepareStatement("SELECT LERNKARTENNAME FROM LERNKARTEN  WHERE pid=?");
            pSt.setInt(1, pid);
            ResultSet rs = pSt.executeQuery();
            while (rs.next()) {
                namenListe.add("" + rs.getString("LERNKARTENNAME"));
            }
            con.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();

        }
        return namenListe;
    }
    public static ArrayList<String>LernkartenDropDown(int pid){
        ArrayList<String> namenListe = new ArrayList<>();
        try{
            Connection con = ClientThread.conDB();
            Statement st = con.createStatement();
            System.out.println("bin in LernkartenDropDown");
            PreparedStatement pSt = con.prepareStatement("SELECT DISTINCT LERNKARTENNAME FROM LERNKARTEN  WHERE pid=?");
            pSt.setInt(1, pid);
            ResultSet rs = pSt.executeQuery();
            while (rs.next()) {
                namenListe.add("" + rs.getString("LERNKARTENNAME"));
            }
            con.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();

        }
        return namenListe;
    }
    public static ArrayList<String>LernkartenFrage(int pid,String name){
        ArrayList<String> namenListe = new ArrayList<>();
        try{
            Connection con = ClientThread.conDB();
            Statement st = con.createStatement();
            System.out.println("bin in LernkartenDropDown");
            PreparedStatement pSt = con.prepareStatement("SELECT * FROM LERNKARTEN WHERE pid=? AND LERNKARTENNAME=?");
            pSt.setInt(1, pid);
            pSt.setString(2,name);
            ResultSet rs = pSt.executeQuery();
            while (rs.next()) {
                namenListe.add("" + rs.getString("LernkartenFrage"));
            }
            con.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();

        }
        return namenListe;
    }
    public static ArrayList<String>LernkartenAntwort(int pid,String name){
        ArrayList<String> namenListe = new ArrayList<>();
        try{
            Connection con = ClientThread.conDB();
            Statement st = con.createStatement();
            System.out.println("bin in LernkartenDropDown");
            PreparedStatement pSt = con.prepareStatement("SELECT * FROM LERNKARTEN WHERE pid=? AND LERNKARTENNAME=?");
            pSt.setInt(1, pid);
            pSt.setString(2,name);
            ResultSet rs = pSt.executeQuery();
            while (rs.next()) {
                namenListe.add("" + rs.getString("LernkartenAntwort"));
            }
            con.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();

        }
        return namenListe;
    }
}
