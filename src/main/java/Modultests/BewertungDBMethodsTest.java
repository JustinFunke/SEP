package Modultests;

import Server.ClientStart;
import Server.ServerNeu;
import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import Server.BewertungDBMethods;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class BewertungDBMethodsTest {
    public static final String urlDB = "jdbc:h2:./myDB";
    public static final String user = "sa";
    public static final String passwort = "";
    private int ersteFrage = 4;
    private int zweiteFrage = 7;
    private int[] ersteAntwortenzahlen= {0,1,0,0};
    private int[] zweiteAntwortenzahlen={0,1,0,0};
    private ArrayList<String> uID = new ArrayList<>();
    private JSONArray jsonID = new JSONArray();
    private BewertungDBMethods   bDB = new BewertungDBMethods();;
    @BeforeAll
    static void before(){
    }
    @Test
    void countAntwortenVonUserList() throws IOException {
        uID.add("1000254");
        ArrayList<Integer> ergebnisse = countAntwortenVonUserList(ersteFrage,uID);
        System.out.println(ergebnisse);
        assertEquals(ersteAntwortenzahlen[0],ergebnisse.get(0));
        assertEquals(ersteAntwortenzahlen[1],ergebnisse.get(1));
        assertEquals(ersteAntwortenzahlen[2],ergebnisse.get(2));
        assertEquals(ersteAntwortenzahlen[3],ergebnisse.get(3));
    }
    public static ArrayList<Integer> countAntwortenVonUser(int frageID, int currentUserID){
        ArrayList<Integer> countAntworten = new ArrayList<>();
        int countAntwort1 = 0;
        int countAntwort2 = 0;
        int countAntwort3 = 0;
        int countAntwort4 = 0;
        try{
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("SELECT * FROM BEWERTUNGSSTATISTIK WHERE B_FRAGEID=? AND USERID=?");
            ps.setInt(1, frageID);
            ps.setInt(2, currentUserID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                if (rs.getInt("ANTWORT") == 1){
                    countAntwort1++;
                }
                else if(rs.getInt("ANTWORT") == 2){
                    countAntwort2++;
                }
                else if(rs.getInt("ANTWORT") == 3){
                    countAntwort3++;
                }
                else if(rs.getInt("ANTWORT") == 4){
                    countAntwort4++;
                }
            }
            con.close();
        }
        catch (SQLException throwables){
            System.out.println(throwables.getMessage());
        }
        countAntworten.add(countAntwort1);
        countAntworten.add(countAntwort2);
        countAntworten.add(countAntwort3);
        countAntworten.add(countAntwort4);
        return countAntworten;
    }
    //countAntwortenVonUser für UserList
    public static ArrayList<Integer> countAntwortenVonUserList(int frageID ,ArrayList<String> userIDs){
        ArrayList<Integer> countAntworten = new ArrayList<>();
        ArrayList<Integer> methodeCount = new ArrayList<>();
        int userID;
        /*countAntworten.set(0, 0);
        countAntworten.set(1, 0);
        countAntworten.set(2, 0);
        countAntworten.set(3, 0);*/
        countAntworten.add(0);
        countAntworten.add(0);
        countAntworten.add(0);
        countAntworten.add(0);
        for(int j = 0;j<userIDs.size();j++){
            userID = Integer.parseInt(userIDs.get(j));
            methodeCount = countAntwortenVonUser(frageID , userID);
            try{
                TimeUnit.MILLISECONDS.sleep(200);
            }
            catch(Exception e){
                System.out.println(e);
            }
            countAntworten.set(0, countAntworten.get(0) + methodeCount.get(0));
            countAntworten.set(1, countAntworten.get(1) + methodeCount.get(1));
            countAntworten.set(2, countAntworten.get(2) + methodeCount.get(2));
            countAntworten.set(3, countAntworten.get(3) + methodeCount.get(3));
        }
        return countAntworten;
    }
}