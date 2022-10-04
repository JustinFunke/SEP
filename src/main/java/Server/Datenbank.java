package Server;

import Client.Kalender.Termin;
import Client.Lehrveranstaltung.Lehrveranstaltung;
import Client.Lehrveranstaltung.Projektgruppe;
import Client.javaFx.Thema;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import org.json.simple.JSONObject;

import java.io.ByteArrayInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;

public class Datenbank {
    public static final String url ="jdbc:h2:./MavenTest/src/main/java/Server/Datenbank/myDB";
    public static final String user = "sa";
    public static final String passwort = "";
    /*
private static String lvname;
private static String lvtyp;
private static String lvsemester;
private static String pgname;
private static String datei;
private static String lm;
private static int lvID;


     */


    //Student/Lehrender einloggen

    public static JSONObject LoginCheck(String mail, String password, String id )
    {
        JSONObject loginData = new JSONObject();
        Integer intId = 0;

        try
        {
            if (!mail.contains("@") )
            {
                intId = Integer.parseInt(mail);
            }
            else
            {
                intId = Integer.parseInt(id);
            }
        }
        catch (Exception ex)
        {
            System.err.println( ex +" parseInt went wrong - method LoginCheck in Datenbank");
        }

        String sql = "Select * FROM USER Where (EMAIL = ? and PASSWORT = ?) OR (ID = ? and PASSWORT = ?)";
        try
        {
            Connection con = ClientThread.conDB();
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            System.out.println("SQL prepared");
            preparedStatement.setString(1, mail.toLowerCase());
            preparedStatement.setString(2, password);
            preparedStatement.setInt(3, intId);
            preparedStatement.setString(4, password);
            System.out.println("Parameer gesetzt");
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Query ausgeführt");

            String lehrender = "LEHRENDER";




            System.out.println("DB Abfrage wird gestarted");

            if (!resultSet.next())
            {
               loginData.put("Exception", "Exception");
               con.close();
            }
            else if (resultSet.getBoolean(lehrender))
            {

                Integer tid = resultSet.getInt("ID");
                String sid = tid.toString();
                System.out.println("id: "+ sid);
                System.out.println("hier: "+ resultSet.getString("VORNAME"));
                String vorname = resultSet.getString("VORNAME");
                System.out.println("vorname: "+ vorname);
                String nachname = resultSet.getString("NACHNAME");
                System.out.println("nachname: "+ nachname);
                String email = resultSet.getString("EMAIL");
                System.out.println("email: "+ email);
                loginData.put("mail", mail);
                loginData.put("password", password);
                loginData.put("id", sid);
                loginData.put("l", "yes");
                loginData.put("vorname", vorname);
                loginData.put("nachname", nachname);
                loginData.put("email", email);
                con.close();
            }
            else
            {

                Integer tid = resultSet.getInt("ID");
                String sid = tid.toString();
                System.out.println("id: "+ sid);
                System.out.println("hier: "+ resultSet.getString("VORNAME"));
                String vorname = resultSet.getString("VORNAME");
                System.out.println("vorname: "+ vorname);
                String nachname = resultSet.getString("NACHNAME");
                System.out.println("nachname: "+ nachname);
                String email = resultSet.getString("EMAIL");
                System.out.println("email: "+ email);
                loginData.put("mail", mail);
                loginData.put("password", password);
                loginData.put("id", sid);
                loginData.put("l", "no");
                loginData.put("vorname", vorname);
                loginData.put("nachname", nachname);
                loginData.put("email", email);
                con.close();
            }
        }
        catch(Exception e)
        {
            System.out.println("Exception occurs: "+ e + " in LoginCheck (DB-Method)");
        }
        return loginData;
    }

    //ListeAllerStudentenInLehrveranstaltungen

    public static ArrayList<Integer> listeStudentenInLehrveranstaltungen(String s) {
        ArrayList<Integer> UserAndLecture = new ArrayList<>();

        try {
            Connection con = DriverManager.getConnection(url, user, passwort);
            PreparedStatement ps = con.prepareStatement("SELECT UI.USERID, UI.VERANSTALTUNGSID FROM USERINVERANSTALTUNGEN UI " +
                    "LEFT OUTER JOIN LEHRVERANSTALTUNG LV on UI.VERANSTALTUNGSID=LV.ID " +
                    "WHERE LV.SEMESTER = ?");
            ps.setString(1, s);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println("User wird gestored");
                UserAndLecture.add(rs.getInt("USERID"));
                System.out.println("User wurde gestored");
            }
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());

        }
        return UserAndLecture;
    }

    public static ArrayList<Integer> listeVeranstaltungenMitStudenten(String s) {
        ArrayList<Integer> UserAndLecture = new ArrayList<>();

        try {
            Connection con = DriverManager.getConnection(url, user, passwort);
            PreparedStatement ps = con.prepareStatement("SELECT UI.USERID , UI.VERANSTALTUNGSID FROM USERINVERANSTALTUNGEN UI " +
                    "LEFT OUTER JOIN LEHRVERANSTALTUNG LV on UI.VERANSTALTUNGSID=LV.ID " +
                    "WHERE LV.SEMESTER = ?");
            ps.setString(1, s);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println("VERANSTALTUNGS wird gestored");
                UserAndLecture.add(rs.getInt("VERANSTALTUNGSID") );
                System.out.println("VERANSTALTUNGS wurde gestored");
            }
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());

        }
        return UserAndLecture;
    }

    public static void dropTableErgebnis() {
        try
        {
            Connection con = DriverManager.getConnection(url, user, passwort);
            PreparedStatement ps = con.prepareStatement("DELETE  FROM USERINVERANSTALTUNGEN Where USERID <> 1000259 AND USERID <> 1000260");
            ps.execute();
            con.close();
            System.out.println("Table Data deleted");
        }
        catch (SQLException throwables) {
            System.out.println(throwables.getMessage());

        }
    }



    //StudentMails

    public static String studentMail(Integer userId)
    {

        String Mail = null;
        String Lehrender = "Lehrender";
        String Exception = "no email found";
        String sql = "Select * FROM USER WHERE ID= ?";

        try {
            Connection con = ClientThread.conDB();
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            System.out.println("SQL prepared");
            preparedStatement.setInt(1, userId);
            System.out.println("Parameter gesetzt");
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Query ausgeführt");
            System.out.println("DB Abfrage wird gestarted");
            if (!resultSet.next())
            {
                return Exception;
            }
            else if(resultSet.getBoolean("LEHRENDER"))
            {

                return Lehrender;
            }
            else
            {
                System.out.println("User vorhanden");
                Mail = resultSet.getString("EMAIL");
                con.close();
                return Mail;
            }
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return Exception;
        }
    }

    //StudentMails

    public static String studentName(Integer userId)
    {

        String Name = null;
        String Exception = "no name found";
        String sql = "Select * FROM USER WHERE ID = ?";

        try {
            Connection con = ClientThread.conDB();
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            System.out.println("SQL prepared");
            preparedStatement.setInt(1, userId);
            System.out.println("Parameter gesetzt");
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Query ausgeführt");
            System.out.println("DB Abfrage wird gestarted");
            if (!resultSet.next())
            {
                return Exception;
            }
            else
            {

                System.out.println("User vorhanden");
                Name = resultSet.getString("VORNAME") + " " + resultSet.getString("NACHNAME");
                con.close();
                return Name;
            }
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return Exception;
        }
    }

    //StudentResults

    public static boolean studentResult(Integer student, Integer lecture) {
        boolean bestanden = false;
        int safe = 0;
        int unsafe = 0;
        //ArrayList<Integer> BestandenProUserUndVeranstaltung = new ArrayList<>();
        try {
            Connection con = DriverManager.getConnection(url, user, passwort);

            PreparedStatement psBestanden = con.prepareStatement(
                    "SELECT Count(QV.BESTANDEN), QV.USERID, Q.LEHRVERANSTALTUNGID " +
                            "FROM QUIZVERSUCHE QV " +
                            "INNER JOIN QUIZ Q ON QV.QUIZID=Q.QUIZID " +
                            "WHERE QV.BESTANDEN = true " +
                            "AND QV.USERID = ? " +
                            "AND Q.LEHRVERANSTALTUNGID = ? " +
                            "GROUP BY " +
                            "QV.USERID, Q.LEHRVERANSTALTUNGID");
            psBestanden.setInt(1, student);
            psBestanden.setInt(2, lecture);
            ResultSet rs = psBestanden.executeQuery();
            rs.next();
            safe = rs.getInt("Count(QV.BESTANDEN)");
            PreparedStatement psDurchgefallen = con.prepareStatement(
                    "SELECT Count(QV.BESTANDEN), QV.USERID, Q.LEHRVERANSTALTUNGID " +
                            "FROM QUIZVERSUCHE QV " +
                            "INNER JOIN QUIZ Q ON QV.QUIZID=Q.QUIZID " +
                            "WHERE QV.BESTANDEN = false " +
                            "AND QV.USERID = ? " +
                            "AND Q.LEHRVERANSTALTUNGID = ? "+
                            "GROUP BY " +
                            "QV.USERID, Q.LEHRVERANSTALTUNGID");
            psDurchgefallen.setInt(1, student);
            psDurchgefallen.setInt(2, lecture);
            ResultSet rsd = psBestanden.executeQuery();
            rsd.next() ;
            unsafe = rsd.getInt("Count(QV.BESTANDEN)");
            if(safe != 0 && safe >= unsafe)
            {
                bestanden = true;
            }
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());

        }
        return bestanden;
    }

    //LectureName
    public static String veranstaltungsName(Integer lectureId) {
        String name = null;

        try {
            Connection con = DriverManager.getConnection(url, user, passwort);
            PreparedStatement ps = con.prepareStatement("SELECT * FROM LEHRVERANSTALTUNG WHERE ID = ?");
            ps.setInt(1, lectureId);
            ResultSet rs = ps.executeQuery();
            rs.next();
            name = rs.getString("NAME");
            System.out.println("VERANSTALTUNGS mit Name: "+ name);

            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
            return lectureId.toString();
        }
        return name;
    }




    //Student Hinzufügen
    public static void DbStudentHinzufuegen(RegistrierungStudent student){
        try {
            Connection con = ClientThread.conDB();
            Statement st = con.createStatement();


            st.executeUpdate("INSERT INTO user (email,passwort,vorname,nachname,wohnort,strasse,studienfach,profilbild,lehrender)"+
                    " VALUES "+"('"+student.getEmail()+"', '"+student.getPasswort()+"', '"+student.getVorname()+"', '"+student.getNachname()+"', '"+ student.getWohnort()+
                    "', '"+student.getStrasse()+"', '" +student.getStudienfach()+"', '"+ student.getProfilbild()+"', "+0+ ")");
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }


    }

    //Lehrender hinzufügen
    public static void DbLehrenderHinzufuegen(RegistrierungLehrender lehrender){
        try {
            Connection con = ClientThread.conDB();
            Statement st = con.createStatement();

            st.executeUpdate("INSERT INTO user (email,passwort,vorname,nachname,wohnort,strasse,profilbild,lehrstuhl,forschungsgebiet, lehrender )"+
                    " VALUES "+"('"+lehrender.getEmail()+"', '"+lehrender.getPasswort()+"', '"+lehrender.getVorname()+"', '"+lehrender.getNachname()+"', '"+ lehrender.getWohnort()+
                    "', '"+lehrender.getStrasse()+"', '"+ lehrender.getProfilbild()+"', '"+lehrender.getLehrstuhl()+"', '"+lehrender.getForschungsgebiet()+"', "+1+ ")");
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());

        }


    }
    //Wiedergeben der Matrikelnummer
    public static Integer DbMatrikelnummer(String email){
        try{
            Connection con = ClientThread.conDB();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT id from user where email = '"+email.toLowerCase()+"'");
            rs.next();
            int ausgabe= rs.getInt("id");
            con.close();
            return ausgabe;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return  null;
        }
    }

    //Profilbild wiedergeben
    public static Image DbProfilbild(int id){
        try{
            Connection con = ClientThread.conDB();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT profilbild from user where id = '"+id+"'");
            rs.next();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(rs.getString("profilbild")));
            Image img = new Image(inputStream);
            con.close();
            return img;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return  null;
        }
    }
    //Eine Client.Lehrveranstaltung zur Datenbank hinzufügen.
    public static void DbLVHinzufuegen(Lehrveranstaltung neueLV, int userID){
        try {
            Connection con = ClientThread.conDB();
            Statement st = con.createStatement();
            //int testID = 1000042;
            st.executeUpdate("INSERT INTO lehrveranstaltung (name,typ,semester,lehrenderid)"+
                    " VALUES "+"('"+neueLV.getName()+"', '"+neueLV.getTyp()+"', '"+neueLV.getSemester()+"', '"+userID+"')");
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());

        }
    }
    //Lehrmaterial zur Datenbank hinzufügen.
    public static void DbLMHinzufuegen(int lvID ,String lehrmaterial, String LMname) {
        try {
            Connection con = ClientThread.conDB();
            Statement st = con.createStatement();
            //int veranstaltungstestID = 100000;
            st.executeUpdate("INSERT INTO lehrmaterial (lehrveranstaltungsid, lehrmaterial, name)"+
                    " VALUES "+"('"+lvID+"', '"+lehrmaterial+"', '"+LMname+"')");
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());

        }
    }

    public static void DbUserLvHinzufügen(int pUserID, int pLVID){

        try {
            Connection con = ClientThread.conDB();
            Statement st = con.createStatement();
            PreparedStatement ps = con.prepareStatement("SELECT * from USERINVERANSTALTUNGEN WHERE USERID=? AND VERANSTALTUNGSID=?");
            ps.setInt(1,pUserID);
            ps.setInt(2,pLVID);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                System.out.println("Der User ist bereits der Client.Lehrveranstaltung hinzugefügt");
                con.close();
            }
            else {
                st.executeUpdate("INSERT INTO USERINVERANSTALTUNGEN (USERID,VERANSTALTUNGSID)" +
                        " VALUES " + "('" + pUserID + "','" + pLVID + "')");
                System.out.println("User wurde der Veranstaltung hinzugefügt");
            }
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());

        }
    }

    //Eine Projektgruppe zur Datenbank hinzufügen. (Zyklus2)
    public static void DbPGHinzufuegen(Projektgruppe neuePG, int userID){
        try {
            Connection con = ClientThread.conDB();
            Statement st = con.createStatement();
            st.executeUpdate("INSERT INTO projektgruppen (name,lvid,nutzerid)"+
                    " VALUES "+"('"+neuePG.getName()+"', '"+neuePG.getLVID()+"', '"+userID+"')");
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());

        }
    }

    //Projektdateien zur Datenbank hinzufügen. (Zyklus2)
    public static void DbPDHinzufuegen(String PDname, String projektdatei, int projektID) {
        try {
            Connection con = ClientThread.conDB();
            Statement st = con.createStatement();
            st.executeUpdate("INSERT INTO projektdateien (name, dateien, projektid)"+
                    " VALUES "+"('"+PDname+"', '"+projektdatei+"', '"+projektID+"')");
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());

        }
    }
    //ProjektID und UserID in einer Table in der Datenbank (Zyklus 2)
    public static void DbUserPGHinzufuegen(int userID, int projektID) {
        try {
            Connection con = ClientThread.conDB();
            Statement st = con.createStatement();
            st.executeUpdate("INSERT INTO projektuser (userid, projektid)"+
                    " VALUES "+"('"+userID+"', '"+projektID+"')");
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());

        }
    }

    //ToDos in Datenbankhinzufügen
    public static void DbToDoPGHinzufuegen(String toDo, int pid, int userid){
        try {
            Connection con = ClientThread.conDB();
            Statement st = con.createStatement();
            st.executeUpdate("Insert INTO TODO (projektgruppe,todo, userid)" +
                    " VALUES " + "(" + pid + ", '" + toDo + "', '" + userid + "')");
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());

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

    public static void DbTerminerstellen(Termin pTermin){
        try {
            Connection con = ClientThread.conDB();
            Statement st = con.createStatement();
            String filler = "none";
            if(pTermin.isRVorhanden()) {
                st.executeUpdate("INSERT INTO TERMIN (DATUM,ANFANG,DAUER,DESCRIPTION,RVORHANDEN,ERINNERUNG,ERINNERUNGSART)" +
                        " VALUES " + "('" + pTermin.getDatum() + "', '" + pTermin.getAnfang() + "', '" + pTermin.getDauer() + "', '" + pTermin.getDesc() + "', '" +1+
                        "', '" + pTermin.getrErinnerung() +"','"+ pTermin.getErinnerungsart() + "')");
                System.out.println("Termin erstellt");

            }
            else {
                st.executeUpdate("INSERT INTO TERMIN (DATUM,ANFANG,DAUER,DESCRIPTION,RVORHANDEN,ERINNERUNG,ERINNERUNGSART)" +
                        " VALUES " + "('" + pTermin.getDatum() + "', '" + pTermin.getAnfang() + "', '" + pTermin.getDauer() + "', '" + pTermin.getDesc() + "', '" +0+
                        "', '" + filler+"','"+ filler + "')");
                System.out.println("Termin erstellt");
            }
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());

        }
    }
    public static void DBUserTerminHinzufuegen(int pUserUD, int pTerminID) {
        try {
            Connection con = ClientThread.conDB();
            Statement st = con.createStatement();
            st.executeUpdate("INSERT INTO USERINTERMIN (USERID,TERMINID)"+
                    " VALUES "+"('"+pUserUD+"', '"+pTerminID+"')");
            System.out.println("User mit ID: "+pUserUD+ " Termin mit folgender ID hinzugefügt"+pTerminID);
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());

        }
    }

    //Neu
    public static ArrayList<String> Emailvergeben(String email) {
       ArrayList<String> emailList= new ArrayList<>();
            try {
                Connection con = ClientThread.conDB();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT email from user");
                emailList.clear();
                while (rs.next()) {
                    emailList.add(rs.getString("Email"));
                }
                con.close();

            }catch (SQLException ex){
                System.out.println(ex.getMessage());
            }
            return emailList;


    }

    //ab hier Sebastians Methoden Zyklus2+3

    public static String pgName(int pgid){
        String pgname = "";
        //boolean checkUserTyp;
        try {
            Connection con = DriverManager.getConnection(url, user, passwort);
            PreparedStatement pgName = con.prepareStatement("SELECT NAME FROM PROJEKTGRUPPEN WHERE ID=?");
            pgName.setInt(1, pgid);
            ResultSet abfrageName = pgName.executeQuery();
            while(abfrageName.next()) {
                pgname = abfrageName.getString("NAME");
            }
            /*
            PreparedStatement userTyp = con.prepareStatement("SELECT LEHRENDER FROM USER WHERE ID =?");
            userTyp.setInt(1, userID);
            ResultSet abfrageuserTyp = userTyp.executeQuery();
            abfrageuserTyp.next();
            checkUserTyp = abfrageuserTyp.getBoolean("LEHRENDER");
             */

            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());

        }
        return pgname;
    }

    public static boolean checkUserTyp(int userID){
        Boolean checkUserTyp = false;
        try{
            Connection con = DriverManager.getConnection(url, user, passwort);
            PreparedStatement userTyp = con.prepareStatement("SELECT LEHRENDER FROM USER WHERE ID =?");
            userTyp.setInt(1, userID);
            ResultSet abfrageuserTyp = userTyp.executeQuery();
            while(abfrageuserTyp.next()){
                checkUserTyp = abfrageuserTyp.getBoolean("LEHRENDER");
            }


            System.out.println("lvCheckUser clear");
            con.close();
        }catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        return checkUserTyp;
    }

    public static ObservableList<Integer> pgUser(int pgID){
        ObservableList<Integer> userList = FXCollections.observableArrayList();
        try {
            Connection con = DriverManager.getConnection(url, user, passwort);
            PreparedStatement abfrageUser = con.prepareStatement("SELECT USERID FROM PROJEKTUSER WHERE PROJEKTID=?");
            abfrageUser.setInt(1, pgID);
            ResultSet abfrageUserErgebnis = abfrageUser.executeQuery();
            while (abfrageUserErgebnis.next()) {
                userList.add(abfrageUserErgebnis.getInt("USERID"));
            }
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        return userList;
    }

    public static ObservableList<String> pgDateien(int pgID){
        ObservableList<String> dateienNameList = FXCollections.observableArrayList();
        try {
            Connection con = DriverManager.getConnection(url, user, passwort);
            PreparedStatement abfrageDateien = con.prepareStatement("SELECT NAME FROM PROJEKTDATEIEN WHERE PROJEKTID=?");
            abfrageDateien.setInt(1, pgID);
            ResultSet abfrageDateienErgebnis = abfrageDateien.executeQuery();
            while (abfrageDateienErgebnis.next()) {
                dateienNameList.add(abfrageDateienErgebnis.getString("NAME"));
            }
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        return dateienNameList;
    }

    public static String holeDatei(int pgID, String gewaehlteDatei){
       String datei = "";
        try {
            Connection con = DriverManager.getConnection(url, user, passwort);
            PreparedStatement abfrageDatei = con.prepareStatement("SELECT DATEIEN FROM PROJEKTDATEIEN WHERE PROJEKTID=? AND NAME=?");
            abfrageDatei.setInt(1, pgID);
            abfrageDatei.setString(2, gewaehlteDatei);
            ResultSet abfrageDateiErgebnis = abfrageDatei.executeQuery();
            while(abfrageDateiErgebnis.next()) {
                datei = abfrageDateiErgebnis.getString("DATEIEN");
            }
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        return datei;
    }

    public static String lvName(int lvID){
        String lvname = "";
        System.out.println("bin in lvName");
        try {
            Connection con = DriverManager.getConnection(url,user,passwort);
            PreparedStatement lvName = con.prepareStatement("SELECT NAME FROM LEHRVERANSTALTUNG WHERE ID=?");
            lvName.setInt(1, lvID);
            ResultSet abfrageName = lvName.executeQuery();
            while(abfrageName.next()) {
                lvname = abfrageName.getString("NAME");
            }
            System.out.println("lvName clear");
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        return lvname;
    }

    public static String lvTyp(int lvID){
        String lvtyp = "";
        try {
            Connection con = DriverManager.getConnection(url,user,passwort);
            PreparedStatement lvTyp = con.prepareStatement("SELECT TYP FROM LEHRVERANSTALTUNG WHERE ID=?");
            lvTyp.setInt(1, lvID);
            ResultSet abfrageTyp = lvTyp.executeQuery();
            while (abfrageTyp.next()) {
                lvtyp = abfrageTyp.getString("TYP");
            }
            System.out.println("lvTyp clear");
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        return lvtyp;
    }

    public static String lvSemester(int lvID){
        String lvsemester = "";
        try {
            Connection con = DriverManager.getConnection(url,user,passwort);
            PreparedStatement lvSemester = con.prepareStatement("SELECT SEMESTER FROM LEHRVERANSTALTUNG WHERE ID=?");
            lvSemester.setInt(1, lvID);
            ResultSet abfrageSemester = lvSemester.executeQuery();
            while(abfrageSemester.next()) {
                lvsemester = abfrageSemester.getString("Semester");
            }
            System.out.println("lvSemester clear");
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        return lvsemester;
    }

    public static ObservableList<String> lvDateien(int lvID){
        ObservableList<String> lmNameList = FXCollections.observableArrayList();
        try {
            Connection con = DriverManager.getConnection(url, user, passwort);
            PreparedStatement meineLMnames = con.prepareStatement("SELECT NAME FROM LEHRMATERIAL WHERE LEHRVERANSTALTUNGSID=?");
            meineLMnames.setInt(1, lvID);
            ResultSet abfrageLMnames = meineLMnames.executeQuery();
            while(abfrageLMnames.next()){
                lmNameList.add(abfrageLMnames.getString("NAME"));
            }
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        return lmNameList;
    }

    public static ObservableList<String> pgNamen(int lvID){
        ObservableList<String> pgNameList = FXCollections.observableArrayList();
        try {
            Connection con = DriverManager.getConnection(url, user, passwort);
            PreparedStatement meinePGnames = con.prepareStatement("SELECT NAME FROM PROJEKTGRUPPEN WHERE LVID=?");
            meinePGnames.setInt(1, lvID);
            ResultSet abfragePGnames = meinePGnames.executeQuery();
            while(abfragePGnames.next()){
                pgNameList.add(abfragePGnames.getString("NAME"));
            }
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        return pgNameList;
    }

    public static String holeLM(int lvID, String lmName){
        String lm = "";
        try {
            Connection con = DriverManager.getConnection(url, user, passwort);
            PreparedStatement meinLM = con.prepareStatement("SELECT LEHRMATERIAL FROM LEHRMATERIAL WHERE LEHRVERANSTALTUNGSID=? AND NAME=?");
            meinLM.setInt(1, lvID);
            meinLM.setString(2, lmName);
            ResultSet abfrageLM = meinLM.executeQuery();
            while (abfrageLM.next()){
                lm = abfrageLM.getString("LEHRMATERIAL");
            }
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        return lm;
    }

    public static int projektID(String pgName, int lvID){
        int projektID = 0;
        try {
            //hole ProjektID
            Connection con = DriverManager.getConnection(url, user, passwort);
            PreparedStatement holePGID = con.prepareStatement("SELECT ID FROM PROJEKTGRUPPEN WHERE NAME=? AND LVID=?");
            holePGID.setString(1, pgName);
            holePGID.setInt(2, lvID);
            ResultSet abfragePGID = holePGID.executeQuery();
            while (abfragePGID.next()) {
                projektID = abfragePGID.getInt("ID");
            }
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        return projektID;
    }

    public static boolean checkVorhandenPG(int nutzerID, int projektID){
        boolean vorhandenPG = false;
        try {
            //hole ProjektID
            Connection con = DriverManager.getConnection(url, user, passwort);
            //Checken ob User bereits Teilnehmer der Projekgruppe ist
            PreparedStatement checkPG = con.prepareStatement("SELECT USERID FROM PROJEKTUSER WHERE USERID=? AND PROJEKTID=?");
            checkPG.setInt(1, nutzerID);
            checkPG.setInt(2, projektID);
            ResultSet abfrageCheckPG = checkPG.executeQuery();
            vorhandenPG = abfrageCheckPG.next();
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        return vorhandenPG;
    }

    public static boolean checkVorhandenDatei(String dateiName, int projektID){
        boolean vorhandenDatei = false;
        try{
            Connection con = DriverManager.getConnection(url,user,passwort);
            PreparedStatement abfrage = con.prepareStatement("SELECT NAME FROM PROJEKTDATEIEN WHERE NAME=? AND PROJEKTID=?");
            abfrage.setString(1, dateiName);
            abfrage.setInt(2, projektID);
            ResultSet abfrageErgebnis = abfrage.executeQuery();
            vorhandenDatei = abfrageErgebnis.next();
            con.close();
        }
        catch (SQLException throwables){
            System.out.println(throwables.getMessage());
        }
        return vorhandenDatei;
    }

    public static boolean checkVorhandenLM(String nameLM, int lvID){
        boolean vorhandenLM = false;
        try{
            Connection con = DriverManager.getConnection(url,user,passwort);
            PreparedStatement abfrage = con.prepareStatement("SELECT NAME FROM LEHRMATERIAL WHERE NAME=? AND LEHRVERANSTALTUNGSID=?");
            abfrage.setString(1, nameLM);
            abfrage.setInt(2, lvID);
            ResultSet abfrageErgebnis = abfrage.executeQuery();
            vorhandenLM = abfrageErgebnis.next();
            con.close();
        }
        catch (SQLException throwables){
            System.out.println(throwables.getMessage());
        }
        return vorhandenLM;
    }

    public static boolean checkVorhandenProjekt(String projektName, int lvID){
        boolean vorhandenProjekt = false;
        try{
            Connection con = DriverManager.getConnection(url,user,passwort);
            PreparedStatement abfrage = con.prepareStatement("SELECT NAME FROM PROJEKTGRUPPEN WHERE NAME=? AND LVID=?");
            abfrage.setString(1, projektName);
            abfrage.setInt(2, lvID);
            ResultSet abfrageErgebnis = abfrage.executeQuery();
            vorhandenProjekt = abfrageErgebnis.next();
            con.close();
        }
        catch (SQLException throwables){
            System.out.println(throwables.getMessage());
        }
        return vorhandenProjekt;
    }

    public static boolean checkVorhandenLV(String lvName, String lvTyp, String lvSemester){
        boolean vorhandenLV = false;
        try{
            Connection con = DriverManager.getConnection(url,user,passwort);
            PreparedStatement abfrage = con.prepareStatement("SELECT NAME FROM LEHRVERANSTALTUNG WHERE NAME=? AND TYP=? AND SEMESTER=?");
            abfrage.setString(1, lvName);
            abfrage.setString(2, lvTyp);
            abfrage.setString(3, lvSemester);
            ResultSet abfrageErgebnis = abfrage.executeQuery();
            vorhandenLV = abfrageErgebnis.next();
            con.close();
        }
        catch (SQLException throwables){
            System.out.println(throwables.getMessage());
        }
        return vorhandenLV;
    }

    public static int lvID(String lvName, String lvTyp, String lvSemester){
        int lvID = 0;
        try {
            //hole lvID
            Connection con = DriverManager.getConnection(url, user, passwort);
            PreparedStatement holeLVID = con.prepareStatement("SELECT ID FROM LEHRVERANSTALTUNG WHERE NAME=? AND TYP=? AND SEMESTER=?");
            holeLVID.setString(1, lvName);
            holeLVID.setString(2, lvTyp);
            holeLVID.setString(3, lvSemester);
            System.out.println(lvName + lvTyp + lvSemester);
            ResultSet abfrageneueLVID = holeLVID.executeQuery();
            while(abfrageneueLVID.next()){
                lvID = abfrageneueLVID.getInt("ID");
            }
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        System.out.println(lvID);
        return lvID;
    }

    public static boolean checkVorhandenThema(String thema, int userid){
        boolean vorhandenThema = false;
        try{
            Connection con = DriverManager.getConnection(url,user,passwort);
            PreparedStatement abfrage = con.prepareStatement("SELECT TITEL FROM THEMENANGEBOTE WHERE NUTZERID=? AND TITEL=?");
            abfrage.setInt(1, userid);
            abfrage.setString(2, thema);
            ResultSet abfrageErgebnis = abfrage.executeQuery();
            vorhandenThema = abfrageErgebnis.next();
            con.close();
        }
        catch (SQLException throwables){
            System.out.println(throwables.getMessage());
        }
        return vorhandenThema;
    }

    public static void DbThemaHinzufuegen(Thema neuesThema){
        try {
            Connection con = ClientThread.conDB();
            Statement st = con.createStatement();
            st.executeUpdate("INSERT INTO THEMENANGEBOTE (nutzerid,titel,beschreibung)"+
                    " VALUES "+"('"+neuesThema.getUserID()+"', '"+neuesThema.getTitel()+"', '"+neuesThema.getBeschreibung()+"')");
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());

        }
    }

    public static int themenID(String titel, int themenID){
        int meineThemenID = 0;
        try {
            //hole ThemenID
            Connection con = DriverManager.getConnection(url, user, passwort);
            PreparedStatement holeThemenID = con.prepareStatement("SELECT ID FROM THEMENANGEBOTE WHERE TITEL=? AND NUTZERID=?");
            holeThemenID.setString(1, titel);
            holeThemenID.setInt(2, themenID);
            ResultSet abfrageThemenID = holeThemenID.executeQuery();
            while (abfrageThemenID.next()) {
                meineThemenID = abfrageThemenID.getInt("ID");
            }
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        return meineThemenID;
    }

    public static void DbLiteraturHinzufuegen(int themenID, String literatur){
        try {
            Connection con = ClientThread.conDB();
            PreparedStatement speicherLiteratur = con.prepareStatement("INSERT INTO LITERATURLISTE (themenid, literatur)" + "values (?, ?)");
            speicherLiteratur.setInt(1, themenID);
            speicherLiteratur.setString(2, literatur);
            speicherLiteratur.execute();

            /*
            Statement st = con.createStatement();
            System.out.println("4. bibtexAulesen start: \n" + literatur + " :bibtexAuslesen Ende .4");
            st.executeUpdate("INSERT INTO LITERATURLISTE (themenid,literatur)"+
                    " VALUES "+"('"+themenID+"', '"+literatur+"')");

             */
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());

        }
    }
    //hier Sebastians Methoden Zyklus 2+3 Ende

}
