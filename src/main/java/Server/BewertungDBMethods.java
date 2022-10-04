package Server;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class BewertungDBMethods {
    public static final String urlDB = "jdbc:h2:./MavenTest/src/main/java/Server/Datenbank/myDB";
    public static final String user = "sa";
    public static final String passwort = "";

    //boolean ob machen darf oder nicht (in Bezug auf Bewertung der LV)
    public static String hatHaelfteBearbeitet(int currentUserID, int lehrveranstaltungsID){
        ArrayList<Integer> QuizIDS = new ArrayList<>();
        ArrayList<Integer> QuizIDS2 = new ArrayList<>();
        int anzahlAnFragen = 0;
        int anzahlAnBearbeitet = 0;
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
                    QuizIDS2.add(rs.getInt("QUIZID"));
                    //andere Möglichkeit
                    //anzahlAnBearbeitet++;
                }
            }
            anzahlAnBearbeitet = QuizIDS2.size();
            con.close();
        }
        catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        if (anzahlAnFragen != 0){
            double anzahlBearbeitet = anzahlAnBearbeitet;
            double anzahlFragen = anzahlAnFragen;
            double half = anzahlFragen / 2;
            if (half <= anzahlBearbeitet){
                return "true";
            }
            else {
                return "false";
            }
        }
        else {
            return "true";
        }
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
    public static void insertBewertungsfrage(String bewertungsfrage){
        int maxFN =0;
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            try {
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("select max(fragennummer) as maxFN from Bewertungsfragen where markiert=true");
                while (rs.next()) {
                    maxFN = rs.getInt("maxFN") + 1;
                    System.out.println(maxFN);
                }
            } catch (SQLException throwables) {
                System.out.println(throwables.getMessage());
            }
            PreparedStatement ps = con.prepareStatement("INSERT INTO bewertungsfragen (b_frage,markiert,fragennummer)VALUES (?, true,?)");
            ps.setString(1, bewertungsfrage);
            ps.setInt(2, maxFN);
            ps.executeUpdate();
            System.out.println("Bewertungsfrage hinzugefügt! / Markierung auf TRUE");
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
    }
    public static void insertBewertung(int currentLVID) {
        int bewertungsid = 0;
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select max(bewertungid) as maxbewertungid from bewertung");
            while (rs.next()) {
                bewertungsid = rs.getInt("maxbewertungid") + 1;
            }
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("SELECT * FROM bewertungsfragen WHERE MARKIERT=?");
            ps.setBoolean(1, true);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PreparedStatement ps2 = con.prepareStatement("INSERT INTO bewertung VALUES (?, ?, ?)");
                ps2.setInt(1, rs.getInt("ID"));
                ps2.setInt(2, currentLVID);
                ps2.setInt(3, bewertungsid);
                ps2.executeUpdate();
                PreparedStatement ps3 = con.prepareStatement("UPDATE bewertungsfragen SET MARKIERT=? WHERE ID=?");
                ps3.setBoolean(1, false);
                ps3.setInt(2, rs.getInt("ID"));
                ps3.executeUpdate();
                System.out.println("In der Schleife");
            }
            PreparedStatement ps3 = con.prepareStatement("UPDATE bewertungsfragen SET MARKIERT=? WHERE MARKIERT=?");
            ps3.setBoolean(1, false);
            ps3.setBoolean(2, true);
            ps3.executeUpdate();
            con.close();
            System.out.println("BEWERTUNG hinzugefügt/ MARKIERT auf false");
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        System.out.println("bewertungsid in insertBewertung: " + bewertungsid);
    }
    public static void deleteMarkierteBewertungsfragen(){
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("delete from bewertungsfragen WHERE MARKIERT=?");
            ps.setBoolean(1, true);
            ps.executeUpdate();
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
    }
    public static ArrayList<Integer> getBewertungsfragenIDs(int bewertungsid) {
        ArrayList<Integer> bewertungsfragenIDs = new ArrayList<>();
        try {
            //quizfragen-Liste mit allen QUIZFRAGENID's der QUIZ Tabelle füllen (die die übergebene QUIZID besitzen)
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("SELECT * FROM bewertung WHERE bewertungid=?");
            ps.setInt(1, bewertungsid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                bewertungsfragenIDs.add(rs.getInt("b_frageid"));
            }
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        return bewertungsfragenIDs;
    }
    public static void setBewertungsfragenTrue(int bewertungsfragenID) {
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps2 = con.prepareStatement("Update bewertungsfragen set markiert=true where id=?");
            ps2.setInt(1, bewertungsfragenID);
            ps2.executeUpdate();
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
    }
    public static int countBewertungsfragenMarkiert() {
        int count = 0;
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM bewertungsfragen WHERE markiert=true");
            while (rs.next()) {
                count++;
            }
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        return count;
    }
    public static int getMinFNBewertung(int pos) {
        int minFN = 0;
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            Statement st = con.createStatement();
            ResultSet rs2 = st.executeQuery("select min(fragennummer) as minFN from bewertungsfragen where markiert=true");
            while (rs2.next()) {
                minFN = rs2.getInt("minFN") + pos;
            }
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        return minFN;
    }
    public static ArrayList<String> getBewertung(int minFN) {
        ArrayList<String> bewertung = new ArrayList<>();
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps2 = con.prepareStatement("SELECT * FROM bewertungsfragen WHERE markiert=true and fragennummer=?");
            ps2.setInt(1, minFN);
            ResultSet rs3 = ps2.executeQuery();
            while (rs3.next()) {
                bewertung.add(Integer.toString(rs3.getInt("id")));
                bewertung.add(rs3.getString("b_frage"));
            }
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        return bewertung;
    }
    public static void setBewertungsfragenFalse() {
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            Statement st = con.createStatement();
            st.executeUpdate("update bewertungsfragen set markiert=false where markiert=true");
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
    }
    public static void bewertungVersuchVerwerfen(int currentUserID, int bewertungsid){
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps2 = con.prepareStatement("delete * FROM bewertungsversuche WHERE userid=? and bewertungid=?");
            ps2.setInt(1, currentUserID);
            ps2.setInt(2, bewertungsid);
            ps2.executeUpdate();
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
    }
    public static void bewertungAbgeben(List<Integer> bewertungsfragenIDs, int currentUserID, List<Integer> antworten, int bewertungsid) {
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            for(int i=0;i<bewertungsfragenIDs.size();i++){
                PreparedStatement ps2 = con.prepareStatement("Select * from bewertungsstatistik where userid=? and b_frageid=?");
                ps2.setInt(1, currentUserID);
                ps2.setInt(2, bewertungsfragenIDs.get(i));
                ResultSet rs2 = ps2.executeQuery();
                int tester = 0;
                if (rs2.next()) {
                    tester = 1;
                }
                System.out.println("tester: "+tester);
                if (tester == 0) {
                    PreparedStatement ps3 = con.prepareStatement("Insert into bewertungsstatistik values (?,?,?)");
                    ps3.setInt(1, currentUserID);
                    ps3.setInt(2, bewertungsfragenIDs.get(i));
                    ps3.setInt(3, antworten.get(i));
                    ps3.executeUpdate();
                }
                else{
                    System.out.println("Der User hat die Bewertungsfrage schon beantwortet! Änderungen trotzdem gespeichert!");
                    PreparedStatement ps3 = con.prepareStatement("update bewertungsstatistik set antwort=? where userid=? and b_frageid=?");
                    ps3.setInt(1, antworten.get(i));
                    ps3.setInt(2, currentUserID);
                    ps3.setInt(3, bewertungsfragenIDs.get(i));
                    ps3.executeUpdate();
                }
            }
            PreparedStatement ps3 = con.prepareStatement("Insert into bewertungsversuche values (?,?)");
            ps3.setInt(1, currentUserID);
            ps3.setInt(2, bewertungsid);
            ps3.executeUpdate();
            con.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public static ArrayList<Integer> getBewertungsliste(int lvID){
        ArrayList<Integer> bewertungsliste = new ArrayList<>();
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("select * from bewertung where lv_id=?");
            ps.setInt(1, lvID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                bewertungsliste.add(rs.getInt("bewertungid"));
            }
            con.close();
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        Set<Integer> set = new HashSet<>(bewertungsliste);
        bewertungsliste.clear();
        bewertungsliste.addAll(set);
        return bewertungsliste;
    }
    public static String filterBewertungTeilnahme(int currentUserID, int bewertungsid){
        int tester=0;
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("select * from bewertungsversuche where userid=? and bewertungid=?");
            ps.setInt(1, currentUserID);
            ps.setInt(2,bewertungsid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tester++;
            }
            con.close();
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if(tester==0){
            return "nicht teilgenommen";
        }
        else{
            return "teilgenommen";
        }
    }

    //LEOS PART
    //gibt Liste aller Studenten der LV
    public static ArrayList<String> nehmenAnLVteil(int lehrveranstaltungsID){
        ArrayList<String> studentenIDstring = new ArrayList<>();
        ArrayList<Integer> userIDs = new ArrayList<>();
        ArrayList<Integer> studentenIDs = new ArrayList<>();
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("SELECT * FROM USERINVERANSTALTUNGEN  WHERE VERANSTALTUNGSID = ?");
            ps.setInt(1, lehrveranstaltungsID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                userIDs.add(rs.getInt("USERID"));
            }
            for (int i = 0; i<userIDs.size(); i++) {
                ps = con.prepareStatement("SELECT * FROM USER WHERE ID = ?");
                ps.setInt(1, userIDs.get(i));
                rs = ps.executeQuery();
                while (rs.next()) {
                    if (!rs.getBoolean("LEHRENDER")) {
                        studentenIDs.add(rs.getInt("ID"));
                    }
                }
            }
            con.close();
        }
        catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        for (int i = 0; i<studentenIDs.size(); i++) {
            studentenIDstring.add(studentenIDs.get(i).toString());
        }
        return studentenIDstring;
    }
    //gibt Liste aller Leute die eine LV bestanden haben
    public static ArrayList<String> habenLVbestandenIDs(int lehrveranstaltungsID){
        ArrayList<String> bestandenIDs = new ArrayList<>();
        ArrayList<Integer> userIDs = new ArrayList<>();
        ArrayList<Integer> studentenIDs = new ArrayList<>();
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("SELECT * FROM USERINVERANSTALTUNGEN  WHERE VERANSTALTUNGSID = ?");
            ps.setInt(1, lehrveranstaltungsID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                userIDs.add(rs.getInt("USERID"));
            }
            for (int i = 0; i<userIDs.size(); i++) {
                ps = con.prepareStatement("SELECT * FROM USER WHERE ID = ?");
                ps.setInt(1, userIDs.get(i));
                rs = ps.executeQuery();
                while (rs.next()) {
                    if (!rs.getBoolean("LEHRENDER")) {
                        studentenIDs.add(rs.getInt("ID"));
                    }
                }
            }
            con.close();
        }
        catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        for (int i = 0; i<studentenIDs.size(); i++) {
            try{
                TimeUnit.MILLISECONDS.sleep(200);
            }
            catch(Exception e){
                System.out.println(e);
            }
            if(bestanden(studentenIDs.get(i), lehrveranstaltungsID)){
                bestandenIDs.add(studentenIDs.get(i).toString());
            }
        }
        return bestandenIDs;
    }
    //gibt Liste aller Leute die eine LV NICHT bestanden haben
    public static ArrayList<String> habenLVnichtBestandenIDs(int lehrveranstaltungsID){
        ArrayList<String> bestandenIDs = new ArrayList<>();
        ArrayList<Integer> userIDs = new ArrayList<>();
        ArrayList<Integer> studentenIDs = new ArrayList<>();
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("SELECT * FROM USERINVERANSTALTUNGEN  WHERE VERANSTALTUNGSID = ?");
            ps.setInt(1, lehrveranstaltungsID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                userIDs.add(rs.getInt("USERID"));
            }
            for (int i = 0; i<userIDs.size(); i++) {
                ps = con.prepareStatement("SELECT * FROM USER WHERE ID = ?");
                ps.setInt(1, userIDs.get(i));
                rs = ps.executeQuery();
                while (rs.next()) {
                    if (!rs.getBoolean("LEHRENDER")) {
                        studentenIDs.add(rs.getInt("ID"));
                    }
                }
            }
            con.close();
        }
        catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        for (int i = 0; i<studentenIDs.size(); i++) {
            try{
                TimeUnit.MILLISECONDS.sleep(200);
            }
            catch(Exception e){
                System.out.println(e);
            }
            if(!bestanden(studentenIDs.get(i), lehrveranstaltungsID)){
                bestandenIDs.add(studentenIDs.get(i).toString());
            }
        }
        return bestandenIDs;
    }


    //get FrageIds von einer Bewertung
    public static ArrayList<Integer> getFragenIDs(int bewertungID){
        ArrayList<Integer> frageIDs = new ArrayList<>();
        try{
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("SELECT * FROM BEWERTUNG WHERE BEWERTUNGID=?");
            ps.setInt(1, bewertungID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                frageIDs.add(rs.getInt("B_FRAGEID"));
            }
            con.close();
        }
        catch (SQLException throwables){
            System.out.println(throwables.getMessage());
        }
        return frageIDs;
    }
    //count verschiedene Antworten für eine Frage
    public static ArrayList<Integer> countAntworten(int frageID){
        ArrayList<Integer> countAntworten = new ArrayList<>();
        int countAntwort1 = 0;
        int countAntwort2 = 0;
        int countAntwort3 = 0;
        int countAntwort4 = 0;
        try{
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("SELECT * FROM BEWERTUNGSSTATISTIK WHERE B_FRAGEID=?");
            ps.setInt(1, frageID);
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

    //count verschiedene Antworten für eine Frage von einem User
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

    //Anzahl der Fragen einer Bewertung
    public static int countBewertungsfragen(int bewertungID){
        int anzahl = 0;
        try{
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("SELECT * FROM BEWERTUNG WHERE BEWERTUNGID=?");
            ps.setInt(1, bewertungID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                anzahl++;
            }
            con.close();
        }
        catch (SQLException throwables){
            System.out.println(throwables.getMessage());
        }
        return anzahl;
    }

}
