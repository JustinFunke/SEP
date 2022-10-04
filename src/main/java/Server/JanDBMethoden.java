package Server;

import Client.Kalender.Termin;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JanDBMethoden {
    public static final String urlDB = "jdbc:h2:./MavenTest/src/main/java/Server/Datenbank/myDB";
    public static final String user = "sa";
    public static final String passwort = "";

    public static ArrayList<String> returnalleLVs() {
        ArrayList<String> VeranstaltungList = new ArrayList<>();
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * from LEHRVERANSTALTUNG");
            VeranstaltungList.clear();
            while (rs.next()) {
                VeranstaltungList.add(rs.getString("NAME"));
            }

            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());

        }
        return VeranstaltungList;
    }

    public static Integer getLVID(String pName) {
        int SearchedLVID = 0;
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            Statement st = con.createStatement();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM LEHRVERANSTALTUNG WHERE NAME=?");
            ps.setString(1, pName);
            ResultSet rs = ps.executeQuery();
            rs.next();
            SearchedLVID = rs.getInt("ID");
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());

        }
        return SearchedLVID;
    }

    public static ArrayList<String> getLvsLehrender(int pUID) {
        ArrayList<String> VeranstaltungList = new ArrayList<>();
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement psL = con.prepareStatement("SELECT * FROM LEHRVERANSTALTUNG WHERE LEHRENDERID=?");
            psL.setInt(1, pUID);
            ResultSet rsl = psL.executeQuery();
            while (rsl.next()) {
                VeranstaltungList.add(rsl.getString("NAME"));
            }

            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());

        }
        return VeranstaltungList;
    }

    public static ArrayList<String> matrikelSuche(int pUID) {
        ArrayList<String> StudentenList = new ArrayList<>();
        try {                                       // Suche Über Matrikelnummer falls vorhanden

            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("SELECT * FROM USER WHERE ID=?");
            ps.setInt(1, pUID);
            ResultSet rs = ps.executeQuery();
            StudentenList.clear();
            while (rs.next()) {
                if (!rs.getBoolean("LEHRENDER")) {
                    String NameZusammen = rs.getString("VORNAME") + " " + rs.getString("NACHNAME");
                    StudentenList.add(NameZusammen);
                }
            }
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());

        }
        return StudentenList;
    }

    public static ArrayList<String> vorundNachSuche(String pVorname, String pNachname) {
        ArrayList<String> StudentenList = new ArrayList<>();

        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("SELECT * FROM USER WHERE VORNAME=? AND NACHNAME= ?");
            ps.setString(1, pVorname);
            ps.setString(2, pNachname);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (!rs.getBoolean("LEHRENDER")) {
                    String NameZusammen = rs.getString("VORNAME") + " " + rs.getString("NACHNAME");
                    StudentenList.add(NameZusammen);
                }
            }
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        return StudentenList;
    }

    public static ArrayList<String> vorSuche(String pVorname) {
        ArrayList<String> StudentenList = new ArrayList<>();
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("SELECT * FROM USER WHERE VORNAME=?");
            ps.setString(1, pVorname);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (!rs.getBoolean("LEHRENDER")) {
                    String NameZusammen = rs.getString("VORNAME") + " " + rs.getString("NACHNAME");
                    StudentenList.add(NameZusammen);
                }
            }
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());

        }
        return StudentenList;
    }

    public static ArrayList<String> nachSuche(String pNachname) {
        ArrayList<String> StudentenList = new ArrayList<>();
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("SELECT * FROM USER WHERE NACHNAME=?");
            ps.setString(1, pNachname);
            ResultSet rs = ps.executeQuery();
            StudentenList.clear();
            while (rs.next()) {
                if (!rs.getBoolean("LEHRENDER")) {
                    String NameZusammen = rs.getString("VORNAME") + " " + rs.getString("NACHNAME");
                    StudentenList.add(NameZusammen);
                }
            }
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());

        }
        return StudentenList;
    }

    public static void addStudentenSuche(String pVorName, String pNachName, String pLvName) {
        if(!pVorName.equals("") ||!pNachName.equals("")|| !pLvName.equals("")){

            try {
                Connection con = DriverManager.getConnection(urlDB, user, passwort);
                Statement st = con.createStatement();
                PreparedStatement ps = con.prepareStatement("SELECT * FROM USER WHERE VORNAME=? AND NACHNAME=?");
                ps.setString(1, pVorName);
                ps.setString(2, pNachName);
                ResultSet rs = ps.executeQuery();
                rs.next();
                int SearchedID = rs.getInt("ID");
                PreparedStatement psLV = con.prepareStatement("SELECT * FROM LEHRVERANSTALTUNG WHERE NAME=?");
                psLV.setString(1, pLvName);
                ResultSet rsLV = psLV.executeQuery();
                rsLV.next();
                int SearchedLVID = rsLV.getInt("ID");
                con.close();
                Datenbank.DbUserLvHinzufügen(SearchedID, SearchedLVID);

            } catch (SQLException throwables) {
                System.out.println(throwables.getMessage());

            }
        }
    }

    public static void erstelleTerminOhneR(String pTerminDatum, String pAnfang, String pDauer, String pDesc, String pLvName) {
        List<Integer> TeilnehmerIDsList = new ArrayList<>();
        Termin neuerTermin = new Termin(pTerminDatum, pAnfang, pDauer, pDesc, false, "", "");
        Datenbank.DbTerminerstellen(neuerTermin);
        try {
            Connection con = ClientThread.conDB();
            PreparedStatement psl = con.prepareStatement("SELECT * FROM LEHRVERANSTALTUNG WHERE NAME=?");
            psl.setString(1, pLvName);
            ResultSet rslv = psl.executeQuery();
            rslv.next();
            int VeranstaltungsID = rslv.getInt("ID");
            PreparedStatement ps = con.prepareStatement("SELECT * FROM USERINVERANSTALTUNGEN WHERE VERANSTALTUNGSID=?");
            ps.setInt(1, VeranstaltungsID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                TeilnehmerIDsList.add(rs.getInt("USERID"));
            }
            PreparedStatement pst = con.prepareStatement("SELECT * FROM TERMIN WHERE DESCRIPTION=?");
            pst.setString(1, neuerTermin.getDesc());
            ResultSet tID = pst.executeQuery();
            tID.next();
            int TerminID = tID.getInt("ID");
            for (int i = 0; i < TeilnehmerIDsList.size(); i++) {
                Datenbank.DBUserTerminHinzufuegen(TeilnehmerIDsList.get(i), TerminID);
            }
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void erstelleTerminMitR(String pTerminDatum, String pAnfang, String pDauer, String pDesc, String pReminderDatum, String pLvName, String pReminderArt) {
        List<Integer> TeilnehmerIDsList = new ArrayList<>();
        Termin neuerTermin = new Termin(pTerminDatum, pAnfang, pDauer, pDesc, true, pReminderDatum, pReminderArt);
        Datenbank.DbTerminerstellen(neuerTermin);
        try {
            Connection con = ClientThread.conDB();
            PreparedStatement psl = con.prepareStatement("SELECT * FROM LEHRVERANSTALTUNG WHERE NAME=?");
            psl.setString(1, pLvName);
            ResultSet rslv = psl.executeQuery();
            rslv.next();
            int VeranstaltungsID = rslv.getInt("ID");
            PreparedStatement ps = con.prepareStatement("SELECT * FROM USERINVERANSTALTUNGEN WHERE VERANSTALTUNGSID=?");
            ps.setInt(1, VeranstaltungsID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                TeilnehmerIDsList.add(rs.getInt("USERID"));
            }
            PreparedStatement pst = con.prepareStatement("SELECT * FROM TERMIN WHERE DESCRIPTION=?");
            pst.setString(1, neuerTermin.getDesc());
            ResultSet tID = pst.executeQuery();
            tID.next();
            int TerminID = tID.getInt("ID");
            for (int i = 0; i < TeilnehmerIDsList.size(); i++) {
                Datenbank.DBUserTerminHinzufuegen(TeilnehmerIDsList.get(i), TerminID);
            }
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static ArrayList<String> teilnehmerListeStudenten(int pUserID, int pLvID) {
        List<Integer> TeilnehmerIDsList = new ArrayList<>();
        ArrayList<String> StudentTeilnehmerNamenList = new ArrayList<>();
        ArrayList<String> tmp = new ArrayList<>();
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);

            PreparedStatement ps = con.prepareStatement("SELECT * FROM USERINVERANSTALTUNGEN WHERE VERANSTALTUNGSID=?");
            ps.setInt(1, pLvID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                TeilnehmerIDsList.add(rs.getInt("USERID"));
            }
            PreparedStatement psnamen = con.prepareStatement("Select * FROM USER WHERE ID=?");
            for (int i = 0; i < TeilnehmerIDsList.size(); i++) {
                psnamen.setInt(1, TeilnehmerIDsList.get(i));
                ResultSet rsn = psnamen.executeQuery();
                rsn.next();
                String newName = rsn.getString("Vorname") + " " + rsn.getString("Nachname");
                if (pUserID == rsn.getInt("ID") && !rsn.getBoolean("LEHRENDER")) {
                    StudentTeilnehmerNamenList.add(newName);
                } else if (!rsn.getBoolean("LEHRENDER")) {
                    tmp.add(newName);
                }

            }
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());

        }
        StudentTeilnehmerNamenList.addAll(tmp);
        return StudentTeilnehmerNamenList;
    }

    public static ArrayList<String> lVName(int LVID) {
        ArrayList<String> name = new ArrayList<>();
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement psLVName = con.prepareStatement("SELECT * FROM LEHRVERANSTALTUNG WHERE ID=?");
            psLVName.setInt(1, LVID);
            ResultSet rsLVName = psLVName.executeQuery();
            rsLVName.next();
            name.add(rsLVName.getString("NAME"));
            con.close();
        } catch (SQLException throwables) {

        }
        return name;
    }

    public static ArrayList<String> teilnehmerListeLehrende(int pUserID, int pLvID) {
        List<Integer> TeilnehmerIDsList = new ArrayList<>();
        ArrayList<String> LehrendeTeilnehmerNamenList = new ArrayList<>();
        ArrayList<String> tmp = new ArrayList<>();
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);

            PreparedStatement ps = con.prepareStatement("SELECT * FROM USERINVERANSTALTUNGEN WHERE VERANSTALTUNGSID=?");
            ps.setInt(1, pLvID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                TeilnehmerIDsList.add(rs.getInt("USERID"));
            }
            PreparedStatement psnamen = con.prepareStatement("Select * FROM USER WHERE ID=?");
            for (int i = 0; i < TeilnehmerIDsList.size(); i++) {
                psnamen.setInt(1, TeilnehmerIDsList.get(i));
                ResultSet rsn = psnamen.executeQuery();
                rsn.next();
                String newName = rsn.getString("Vorname") + " " + rsn.getString("Nachname");
                if (pUserID == rsn.getInt("ID") && rsn.getBoolean("LEHRENDER")) {
                    LehrendeTeilnehmerNamenList.add(newName);
                } else if (rsn.getBoolean("LEHRENDER")) {
                    tmp.add(newName);
                }

            }
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());

        }
        LehrendeTeilnehmerNamenList.addAll(tmp);
        return LehrendeTeilnehmerNamenList;
    }

    public static ArrayList<String> checkLehrender(int pID) {
        ArrayList<String> result = new ArrayList<>();
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            Statement st = con.createStatement();
            PreparedStatement ps = con.prepareStatement("SELECT LEHRENDER FROM USER WHERE ID=?");
            ps.setInt(1, pID);
            ResultSet rs = ps.executeQuery();
            rs.next();
            result.add(String.valueOf(rs.getBoolean("Lehrender"))); //rs.getBoolean("LEHRENDER")
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());

        }

        return result;
    }

    public static ArrayList<String> tLDoubleClick(String pVorname, String pNachname, int pSucherID) {
        ArrayList<String> result = new ArrayList<>();
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("SELECT * FROM USER WHERE VORNAME=? AND NACHNAME=?");
            ps.setString(1, pVorname);
            ps.setString(2, pNachname);
            ResultSet rs = ps.executeQuery();
            rs.next();
            int SearchedID = rs.getInt("ID");
            Boolean SearchedLehrer = rs.getBoolean("LEHRENDER");
            PreparedStatement psCurrent = con.prepareStatement("SELECT * FROM USER WHERE ID=?");
            psCurrent.setInt(1, pSucherID);
            ResultSet rsCurrent = psCurrent.executeQuery();
            rsCurrent.next();
            Boolean currentLehrender = rsCurrent.getBoolean("LEHRENDER");
            result.add(String.valueOf(SearchedID));
            result.add(String.valueOf(SearchedLehrer));
            result.add(String.valueOf(currentLehrender));
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }

        return result;
    }

    public static ArrayList<String> KalenderTermineIDs(int pUserID) {
        ArrayList<String> TerminList = new ArrayList<>();
        try {
            Connection con = ClientThread.conDB();
            PreparedStatement psL = con.prepareStatement("SELECT * FROM USERINTERMIN WHERE USERID=?");
            psL.setInt(1, pUserID);
            ResultSet rsl = psL.executeQuery();
            while (rsl.next()) {
                TerminList.add(String.valueOf(rsl.getInt("TERMINID")));
            }


//            PreparedStatement psT = con.prepareStatement("SELECT * FROM TERMIN WHERE ID=?");
//            for (int i = 0; i < TerminList.size(); i++) {
//                psT.setInt(1, TerminList.get(i));
//                ResultSet rsT = psT.executeQuery();
//                rsT.next();
//                Termin tmpTermin = new Termin(rsT.getString("DATUM"), rsT.getString("ANFANG"), rsT.getString("DAUER"), rsT.getString("DESCRIPTION"), rsT.getBoolean("RVORHANDEN"), "", "");
//                Termine.add(tmpTermin);
//            }
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        return TerminList;
    }
    public static ArrayList<String> terminDaten(int pTerminID){
        ArrayList<String> daten = new ArrayList<>();
        try{
            Connection con = ClientThread.conDB();
            PreparedStatement psT = con.prepareStatement("SELECT * FROM TERMIN WHERE ID=?");
            psT.setInt(1,pTerminID);
            ResultSet rsT = psT.executeQuery();
            rsT.next();
            daten.add(rsT.getString("DATUM"));
            daten.add(rsT.getString("ANFANG"));
            daten.add(rsT.getString("DAUER"));
            daten.add(rsT.getString("DESCRIPTION"));
            daten.add(String.valueOf(rsT.getBoolean("RVORHANDEN")));
            con.close();
        }
        catch (SQLException throwables){
            System.out.println(throwables.getMessage());
        }
        return daten;
    }
    public static ArrayList<String> KalenderErinnerungswerte(int pTerminID,int pUID){
        ArrayList<String> daten = new ArrayList<>();
        try{
            Connection con = ClientThread.conDB();
            PreparedStatement npsT = con.prepareStatement("SELECT * FROM TERMIN WHERE ID=?");
            PreparedStatement mail = con.prepareStatement("SELECT * FROM USER WHERE ID=?");
            npsT.setInt(1,pTerminID);
            ResultSet rsN = npsT.executeQuery();
            rsN.next();
            mail.setInt(1,pUID);
            ResultSet mailSet = mail.executeQuery();
            mailSet.next();
            daten.add(String.valueOf(rsN.getBoolean("RVORHANDEN")));
            daten.add(rsN.getString("ERINNERUNG"));
            daten.add(rsN.getString("ERINNERUNGSART"));
            daten.add(rsN.getString("DESCRIPTION"));
            daten.add(rsN.getString("DATUM"));
            daten.add(mailSet.getString("VORNAME"));
            daten.add(mailSet.getString("EMAIL"));
            con.close();
        }
        catch (SQLException throwables){
            System.out.println(throwables.getMessage());
        }
        return daten;
    }
    public static ArrayList<String> getThemen(int pID){
        ArrayList<String> daten = new ArrayList<>();
        try{
            Connection con = ClientThread.conDB();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM THEMENANGEBOTE WHERE NUTZERID=?");
            ps.setInt(1,pID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                daten.add(String.valueOf(rs.getString("Titel")));
            }
            con.close();
        }
        catch (SQLException throwables){
            System.out.println(throwables.getMessage());
        }
        return daten;
    }
    public static ArrayList<String> getThemaIDTitel(String pTitel){
        ArrayList<String> daten = new ArrayList<>();
        try{
            Connection con = ClientThread.conDB();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM THEMENANGEBOTE WHERE TITEL=?");
            ps.setString(1,pTitel);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                daten.add(String.valueOf(rs.getInt("ID")));
            }
            con.close();
        }
        catch (SQLException throwables){
            System.out.println(throwables.getMessage());
        }
        return daten;
    }
    public static ArrayList<String> getThemaDaten(int pID){
        ArrayList<String> daten = new ArrayList<>();
        try{
            Connection con = ClientThread.conDB();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM THEMENANGEBOTE WHERE ID=?");
            ps.setInt(1,pID);
            ResultSet rs = ps.executeQuery();
            rs.next();
            daten.add(String.valueOf(rs.getString("TITEL")));
            daten.add(String.valueOf(rs.getString("BESCHREIBUNG")));
            con.close();
        }
        catch (SQLException throwables){
            System.out.println(throwables.getMessage());
        }
        return daten;
    }
    public static ArrayList<String> getLiteratur(int pThemaID){
        ArrayList<String> daten = new ArrayList<>();
        try{
           Connection con = ClientThread.conDB();
           PreparedStatement ps = con.prepareStatement("SELECT * FROM LITERATURLISTE WHERE THEMENID=?");
           ps.setInt(1,pThemaID);
           ResultSet rs = ps.executeQuery();
           rs.next();
           String[] inhalt = rs.getString("LITERATUR").split("@");

           for(int i = 1;i<inhalt.length;i++){
               daten.add(inhalt[i].substring(0,inhalt[i].length()-3));
           }
           System.out.println(daten.get(0));
           con.close();
        }
        catch (SQLException throwables){
            System.out.println(throwables.getMessage());
        }
        return daten;
    }

}

