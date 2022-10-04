package Server;

import java.io.ByteArrayInputStream;
import java.rmi.server.ExportException;
import java.sql.*;
import java.util.*;

public class LeonardDBMethods {
    public static final String urlDB ="jdbc:h2:./MavenTest/src/main/java/Server/Datenbank/myDB";
    public static final String user = "sa";
    public static final String passwort = "";

    //get ID when Vor- und Nachname gegeben
    public static String userID(String vorname, String nachname){
        String userid = "";
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("SELECT * FROM USER WHERE VORNAME=? AND NACHNAME=?");
            ps.setString(1,vorname);
            ps.setString(2,nachname);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                userid = rs.getString("ID");
            }
            con.close();
        }
        catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        return userid;
    }
    //gibt vollen Namen von einer UserID
    public static String userName(int currentUserID){
        String name = "";
        try{
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("SELECT * FROM USER WHERE ID=?");
            ps.setInt(1, currentUserID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                name = rs.getString("VORNAME") + " " + rs.getString("NACHNAME");
            }
            con.close();
        }
        catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        return name;
    }

    //gibt Liste mit vollen Namen von Liste mit IDs
    public static ArrayList<String> userNamen(ArrayList<String> ids){
        ArrayList<String> namen= new ArrayList<>();
        try{
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            for (int i = 0;i<ids.size();i++){
                PreparedStatement ps = con.prepareStatement("SELECT * FROM USER WHERE ID=?");
                ps.setInt(1, Integer.parseInt(ids.get(i)));
                ResultSet rs = ps.executeQuery();
                while (rs.next()){
                    namen.add(rs.getString("VORNAME")+" "+rs.getString("NACHNAME"));
                }
            }
            con.close();
        }
        catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        return namen;
    }

    //Freundschaftsanfrage erstellen
    public static void insertIntoFanfragen(int currentUserID, int freundid){
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("INSERT INTO FANFRAGEN (USERID, FID) VALUES (?, ?)");
            ps.setInt(1, currentUserID);
            ps.setInt(2, freundid);
            ps.executeUpdate();
            //System.out.println(freundid);
            con.close();
        }
        catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }

    }

    //Freund bestätigen
    public static void insertIntoFreunde(int currentUserID, int freundid){
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("INSERT INTO FREUNDE (USERID, FREUNDID) VALUES (?, ?)");
            ps.setInt(1, currentUserID);
            ps.setInt(2, freundid);
            ps.executeUpdate();
            ps = con.prepareStatement("INSERT INTO FREUNDE (USERID, FREUNDID) VALUES (?, ?)");
            ps.setInt(1, freundid);
            ps.setInt(2, currentUserID);
            ps.executeUpdate();
            ps = con.prepareStatement("DELETE FROM FANFRAGEN WHERE USERID=? AND FID=?");
            ps.setInt(1, freundid);
            ps.setInt(2, currentUserID);
            ps.executeUpdate();
            System.out.println(freundid);
            System.out.println("Freund angenommen");
            con.close();
        }
        catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
    }
    //Freund ablehnen
    public static void deleteFromFanfragen(int currentUserID, int freundid){
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("DELETE FROM FANFRAGEN WHERE USERID=? AND FID=?");
            ps.setInt(1, freundid);
            ps.setInt(2, currentUserID);
            ps.executeUpdate();
            System.out.println(freundid);
            System.out.println("Freund abgelehnt");
            con.close();
        }
        catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
    }

    //Personen denen der User FA geschickt hat
    public static ArrayList<String> userVonFanfragen(int currentUserID){
        ArrayList<String> freundeIDs= new ArrayList<>();
        try{
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("SELECT * FROM FANFRAGEN WHERE USERID = ?" );
            ps.setInt(1, currentUserID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                freundeIDs.add(rs.getString("FID"));
            }
            con.close();
        }
        catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        return freundeIDs;
    }

    //Personen welche dem User FA geschickt haben
    public static ArrayList<String> freundeVonFanfragen(int currentUserID){
        ArrayList<String> freundeIDs= new ArrayList<>();
        try{
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("SELECT * FROM FANFRAGEN WHERE FID = ?" );
            ps.setInt(1, currentUserID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                freundeIDs.add(rs.getString("USERID"));
            }
            con.close();
        }
        catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        return freundeIDs;
    }

    //Gibt Liste aller Freunde von User
    public static ArrayList<String> alleFreunde(int currentUserID){
        ArrayList<String> alleFreundeIDs = new ArrayList<>();
        try{
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("SELECT * FROM FREUNDE WHERE USERID=?");
            ps.setInt(1, currentUserID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                alleFreundeIDs.add(rs.getString("FREUNDID"));
            }
            con.close();
        }
        catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        return alleFreundeIDs;
    }

    //aus Liste mit IDs Liste mit Vor- und Nachname machen (ohne currentUserID und ohne Lehrende)
    public static ArrayList<String> idsToName(ArrayList<String> ids, int currentUserID){
        ArrayList<String> toName = new ArrayList<>();
        String userID = String.valueOf(currentUserID);
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            for (int i = 0;i<ids.size();i++) {
                PreparedStatement ps = con.prepareStatement("SELECT * FROM USER WHERE ID=?");
                ps.setInt(1, Integer.parseInt(ids.get(i)));
                ResultSet rs = ps.executeQuery();
                while (rs.next()){
                    if (!rs.getString("ID").equals(userID) && !rs.getBoolean("LEHRENDER")) {
                        toName.add(rs.getString("VORNAME") +" "+ rs.getString("NACHNAME"));
                    }
                }
            }
            con.close();
        }
        catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        return toName;
    }

    //Liste mit allen anderen Leuten (IDs) in Veranstaltungen
    public static ArrayList<String> gleicheVeranstaltung(int currentUserID){
        String userID = String.valueOf(currentUserID);
        ArrayList<String> Ergebnis = new ArrayList<>();
        ArrayList<String> EigeneVeranstaltungen = new ArrayList<>();
        try{
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("SELECT * FROM USERINVERANSTALTUNGEN WHERE USERID=?");
            ps.setInt(1, currentUserID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                EigeneVeranstaltungen.add(rs.getString("VERANSTALTUNGSID"));
            }
            for (int i = 0;i<EigeneVeranstaltungen.size();i++){
                ps = con.prepareStatement("SELECT * FROM USERINVERANSTALTUNGEN WHERE VERANSTALTUNGSID=?");
                ps.setInt(1, Integer.parseInt(EigeneVeranstaltungen.get(i)));
                rs = ps.executeQuery();
                while (rs.next()){
                    if (!rs.getString("USERID").equals(userID)) {
                        Ergebnis.add(rs.getString("USERID"));
                    }
                }
            }
            con.close();
        }
        catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        return Ergebnis;
    }

    //get ChatID von Chat zwischen id1 und id2 (unsortiert)
    public static ArrayList<Integer> getChatIDs(int currentUserID, int freundid){
        ArrayList<Integer> ListeIDS = new ArrayList<>();
        try{
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("SELECT * FROM PRIVATCHAT WHERE ID1=? AND ID2=?");
            ps.setInt(1, currentUserID);
            ps.setInt(2, freundid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                ListeIDS.add(rs.getInt("CHATID"));
            }
            ps = con.prepareStatement("SELECT * FROM PRIVATCHAT WHERE ID1=? AND ID2=?");
            ps.setInt(1, freundid);
            ps.setInt(2, currentUserID);
            rs = ps.executeQuery();
            while (rs.next()){
                ListeIDS.add(rs.getInt("CHATID"));
            }
            con.close();
        }
        catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        return ListeIDS;
    }

    //kriegt StringListe von ChatIDs und gibt Nachrichten + Vor- und Nachname
    public static ArrayList<String> getChatNachrichten(ArrayList<String> ListeIDSstring){
        ArrayList<String> ListeNachrichten = new ArrayList<>();
        ArrayList<String> ListeNachrichten2 = new ArrayList<>();
        ArrayList<Integer> ListeIDS = new ArrayList<>();
        ArrayList<Integer> UserIDS = new ArrayList<>();
        //convert ListeIDSstring to IntegerList
        for (int i = 0;i<ListeIDSstring.size();i++){
            ListeIDS.add(Integer.parseInt(ListeIDSstring.get(i)));
        }
        //sortiere ListeIDS
        Collections.sort(ListeIDS);
        try{
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            for (int i = 0;i<ListeIDS.size();i++) {
                PreparedStatement ps = con.prepareStatement("SELECT * FROM PRIVATCHAT WHERE CHATID = ?");
                ps.setInt(1, ListeIDS.get(i));
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    UserIDS.add(rs.getInt("ID1"));
                    ListeNachrichten.add(rs.getString("NACHRICHT"));
                }
            }
            for (int i = 0;i<ListeIDS.size();i++) {
                PreparedStatement ps = con.prepareStatement("SELECT * FROM USER WHERE ID = ?");
                ps.setInt(1, UserIDS.get(i));
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    ListeNachrichten2.add(rs.getString("Vorname") +" "+rs.getString("Nachname") + ": " + ListeNachrichten.get(i));
                }
            }
            con.close();
        }
        catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        return ListeNachrichten2;
    }

    //Insert Nachricht in Privatchat
    public static void insertIntoPrivatchat(int currentUserID, int freundid, String nachricht){
        try{
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("INSERT INTO PRIVATCHAT (ID1, ID2, NACHRICHT) VALUES (?, ?, ?)");
            ps.setInt(1, currentUserID);
            ps.setInt(2, freundid);
            ps.setString(3, nachricht);
            ps.executeUpdate();
            con.close();
        }
        catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
    }


    //boolean ob machen darf oder nicht (in Bezug auf Bewertung der LV)
    public static boolean hatHaelfteBearbeitet(int currentUserID, int lehrveranstaltungsID){
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
//            ps = con.prepareStatement("SELECT * FROM QUIZVERSUCHE WHERE USERID = ?");
//            ps.setInt(1, currentUserID);
//            rs = ps.executeQuery();
//            while (rs.next()) {
//                QuizIDS2.add(rs.getInt("QUIZID"));
//                //andere Möglichkeit
//                //anzahlAnBearbeitet++;
//            }
            anzahlAnBearbeitet = QuizIDS2.size();
            con.close();
        }
        catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        if (anzahlAnFragen != 0){
            double anzahlFragen = anzahlAnFragen;
            double anzahlBearbeitet = anzahlAnBearbeitet;
            double half = anzahlFragen/2;
            if (half <= anzahlBearbeitet){
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
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
            ps = con.prepareStatement("SELECT * FROM QUIZVERSUCHE WHERE USERID = ?");
            ps.setInt(1, currentUserID);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getBoolean("BESTANDEN")) {
                    QuizIDS2.add(rs.getInt("QUIZID"));
                    //andere Möglichkeit
                    //anzahlAnBearbeitet++;
                }
            }
            anzahlAnBestanden = QuizIDS2.size();
            con.close();
        }
        catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        if (anzahlAnFragen != 0){
            double half = anzahlAnFragen / 2;
            if (half <= anzahlAnBestanden){
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
            if(!bestanden(studentenIDs.get(i), lehrveranstaltungsID)){
                bestandenIDs.add(studentenIDs.get(i).toString());
            }
        }
        return bestandenIDs;
    }
    public static ArrayList<String> lvIDsVonUser(int currentUserID){
        ArrayList<String> lvIDs = new ArrayList<>();
        try{
            Connection con = DriverManager.getConnection(urlDB,user,passwort);
            PreparedStatement ps = con.prepareStatement("SELECT * FROM USERINVERANSTALTUNGEN WHERE USERID=?");
            ps.setInt(1, currentUserID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                lvIDs.add(rs.getString("VERANSTALTUNGSID"));
            }
        }
        catch (SQLException throwables){
            System.out.println(throwables.getMessage());

        }
        return lvIDs;
    }
    //Alle Veranstaltungen (Namen) von bestimmtes Semester von User
    public static ArrayList<String> lvNamenVonUser(int currentUserID, String clickedSemester){
        ArrayList<String> lvIDs = new ArrayList<>();
        ArrayList<String> lvName = new ArrayList<>();
        try{
            Connection con = DriverManager.getConnection(urlDB,user,passwort);
            PreparedStatement ps = con.prepareStatement("SELECT * FROM USERINVERANSTALTUNGEN WHERE USERID=?");
            ps.setInt(1, currentUserID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                lvIDs.add(rs.getString("VERANSTALTUNGSID"));
            }
            for (int i = 0;i<lvIDs.size();i++){
                ps = con.prepareStatement("SELECT * FROM LEHRVERANSTALTUNG WHERE ID=? AND SEMESTER=?");
                ps.setInt(1, Integer.parseInt(lvIDs.get(i)));
                ps.setString(2, clickedSemester);
                rs = ps.executeQuery();
                while (rs.next()){
                    lvName.add(rs.getString("NAME"));
                }
            }
            con.close();
        }
        catch (SQLException throwables){
            System.out.println(throwables.getMessage());

        }
        return lvName;
    }

    //get lvID von Name und Semester
    public static String getlvID(String lvName, String clickedSemester){
        String currentLVID = "";
        try{
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("SELECT * FROM LEHRVERANSTALTUNG WHERE NAME=? AND SEMESTER=?");
            ps.setString(1, lvName);
            ps.setString(2, clickedSemester);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                currentLVID = rs.getString("ID");
            }
            con.close();
        }
        catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        return currentLVID;
    }

    //get Semester from LVs
    public static String getSemester(int lvID){
        String semester = "";
        try{
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("SELECT * FROM LEHRVERANSTALTUNG WHERE ID=?");
            ps.setInt(1, lvID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                semester = rs.getString("SEMESTER");
            }
            con.close();
        }
        catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        return semester;
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
        countAntworten.set(0, countAntwort1);
        countAntworten.set(1, countAntwort2);
        countAntworten.set(2, countAntwort3);
        countAntworten.set(3, countAntwort4);
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
        countAntworten.set(0, countAntwort1);
        countAntworten.set(1, countAntwort2);
        countAntworten.set(2, countAntwort3);
        countAntworten.set(3, countAntwort4);
        return countAntworten;
    }
    //countAntwortenVonUser für UserList
    public static ArrayList<Integer> countAntwortenVonUserList(int frageID ,ArrayList<String> userIDs){
        ArrayList<Integer> countAntworten = new ArrayList<>();
        ArrayList<Integer> methodeCount = new ArrayList<>();
        int userID;
        countAntworten.set(0, 0);
        countAntworten.set(1, 0);
        countAntworten.set(2, 0);
        countAntworten.set(3, 0);
        for(int j = 0;j<userIDs.size();j++){
            userID = Integer.parseInt(userIDs.get(j));
            methodeCount = countAntwortenVonUser(frageID , userID);
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

    //get von einem User zur Bewertung Antworten
    public static ArrayList<Integer> getAntworten(int currentUserID, int bewertungID){
        ArrayList<Integer> antworten = new ArrayList<>();
        ArrayList<Integer> frageIDs = new ArrayList<>();
        try{
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("SELECT * FROM BEWERTUNG WHERE BEWERTUNGID=?");
            ps.setInt(1, bewertungID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                frageIDs.add(rs.getInt("B_FRAGEID"));
            }
            for(int i = 0;i<frageIDs.size();i++){
                ps = con.prepareStatement("SELECT * FROM BEWERTUNGSSTATISTIK WHERE B_FRAGEID=? AND USERID = ?");
                ps.setInt(1, frageIDs.get(i));
                ps.setInt(2, currentUserID);
                rs = ps.executeQuery();
                while (rs.next()){
                    antworten.add(rs.getInt("ANTWORT"));
                }
            }
            con.close();
        }
        catch (SQLException throwables){
            System.out.println(throwables.getMessage());
        }
        return antworten;
    }
}

