package Modultests;

import Server.ClientStart;
import Server.LeonardDBMethods;
import Server.ServerNeu;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UnterscheidungBestandenTest {
    public static final String urlDB = "jdbc:h2:./myDB";
    public static final String user = "sa";
    public static final String passwort = "";

    private LeonardDBMethods lDB = new LeonardDBMethods();
    private int userIDbestanden = 1000252;
    private int userIDdurchgefallen = 1000254;
    private int lehrveranstaltungsID = 71;
    private boolean testTrue;
    private boolean testFalse;

    @BeforeAll
    static void before(){
        ServerNeu S = new ServerNeu();
        S.serverStart();
        ClientStart c = new ClientStart();
//        c.start(args);
    }
    @Test
    void testBestanden() throws IOException {
        //Leonard Lehrender 1000259 (gmail)
        //Leonard Student 1000252 bestanden (googlemail)
        //Lukas Student 1000254 nicht bestanden (gmail)
        //LV 71 names ModulTest

        testTrue = bestanden(userIDbestanden, lehrveranstaltungsID);
        testFalse = bestanden(userIDdurchgefallen, lehrveranstaltungsID);
        assertTrue(testTrue);
        assertFalse(testFalse);
    }

    //boolean ob jemand den Kurs bestanden hat
    public static boolean bestanden(int currentUserID, int lehrveranstaltungsID){
        ArrayList<Integer> QuizIDS = new ArrayList<>();
        ArrayList<Integer> QuizIDS2 = new ArrayList<>();
        int anzahlAnFragen = 0;
        int anzahlAnBestanden = 0;
        try{
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("SELECT * FROM QUIZ WHERE LEHRVERANSTALTUNGID = ?");
            ps.setInt(1, lehrveranstaltungsID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                QuizIDS.add(rs.getInt("QUIZID"));
                //andere Möglichkeit
                //anzahlAnFragen++;
            }
            //remove duplicates
            Set<Integer> set = new HashSet<>(QuizIDS);
            QuizIDS.clear();
            QuizIDS.addAll(set);
            anzahlAnFragen = QuizIDS.size();
            for(int i=0; i<QuizIDS.size(); i++){
                ps = con.prepareStatement("SELECT * FROM QUIZVERSUCHE WHERE USERID=? AND QUIZID=?");
                ps.setInt(1, currentUserID);
                ps.setInt(2, QuizIDS.get(i));
                rs = ps.executeQuery();
                while (rs.next()) {
                    if (rs.getBoolean("BESTANDEN")) {
                        QuizIDS2.add(rs.getInt("QUIZID"));
                        //andere Möglichkeit
                        //anzahlAnBearbeitet++;
                    }
                }
            }
            anzahlAnBestanden = QuizIDS2.size();
            con.close();
        }
        catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        if (anzahlAnFragen != 0){
            double anzahlBestanden = anzahlAnBestanden;
            double anzahlFragen = anzahlAnFragen;
            double half = anzahlFragen / 2;
            if (half <= anzahlBestanden){
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return true;
        }
    }
}
