package Server;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LukasDBMethods {
    public static final String urlDB = "jdbc:h2:./MavenTest/src/main/java/Server/Datenbank/myDB";
    public static final String user = "sa";
    public static final String passwort = "";

    public static ArrayList<String> userDaten(int currentUserID) {
        ArrayList<String> meinUser = new ArrayList<>();
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("SELECT * FROM USER WHERE ID=?");
            ps.setInt(1, currentUserID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                meinUser.add(rs.getString("VORNAME") + " " + rs.getString("NACHNAME"));
                meinUser.add(rs.getString("EMAIL"));
                meinUser.add(rs.getString("STUDIENFACH"));
                meinUser.add(rs.getString("WOHNORT") + ", " + rs.getString("STRASSE"));
                Integer integer = currentUserID;
                meinUser.add(integer.toString());
                meinUser.add(rs.getString("profilbild"));

            }
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return meinUser;
    }

    public static ArrayList<String> userDatenLehrender(int currentUserID) {
        ArrayList<String> meinUser = new ArrayList<>();
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("SELECT * FROM USER WHERE ID=?");
            ps.setInt(1, currentUserID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                meinUser.add(rs.getString("VORNAME") + " " + rs.getString("NACHNAME"));
                meinUser.add(rs.getString("EMAIL"));
                meinUser.add(rs.getString("lehrstuhl"));
                meinUser.add(rs.getString("forschungsgebiet"));
                meinUser.add(rs.getString("WOHNORT") + ", " + rs.getString("STRASSE"));
                meinUser.add(rs.getString("profilbild"));

            }
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return meinUser;
    }

    public static ArrayList<String> lvDesUsers(int currentUserID) {
        ArrayList<String> meinUser = new ArrayList<>();
        List<String> eigeneVeranstaltungen = new ArrayList<>();
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("SELECT * FROM USERINVERANSTALTUNGEN WHERE USERID=?");
            ps.setInt(1, currentUserID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                eigeneVeranstaltungen.add(rs.getString("VERANSTALTUNGSID"));
            }
            for (int i = 0; i < eigeneVeranstaltungen.size(); i++) {
                try {
                    ps = con.prepareStatement("SELECT * FROM LEHRVERANSTALTUNG WHERE ID=?");
                    ps.setInt(1, Integer.parseInt(eigeneVeranstaltungen.get(i)));
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        meinUser.add(rs.getString("NAME"));
                    }
                } catch (SQLException throwables) {
                    System.out.println(throwables.getMessage());
                }
            }
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        return meinUser;
    }

    public static void updateWohnort(String wohnort, int currentUserID) {
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("UPDATE USER SET WOHNORT=? WHERE ID=?");
            ps.setString(1, wohnort);
            ps.setInt(2, currentUserID);
            ps.executeUpdate();
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
    }

    public static void updateStrasse(String strasse, int currentUserID) {
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("UPDATE USER SET STRASSE=? WHERE ID=?");
            ps.setString(1, strasse);
            ps.setInt(2, currentUserID);
            ps.executeUpdate();
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
    }

    public static void updateStudienfach(String studienfach, int currentUserID) {
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("UPDATE USER SET studienfach=? WHERE ID=?");
            ps.setString(1, studienfach);
            ps.setInt(2, currentUserID);
            ps.executeUpdate();
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
    }

    public static void updatePasswort(String passwort, int currentUserID) {
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("UPDATE USER SET PASSWORT=? WHERE ID=?");
            ps.setString(1, passwort);
            ps.setInt(2, currentUserID);
            ps.executeUpdate();
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
    }

    public static void updateProfilbild(String profilbild, int currentUserID) {
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("UPDATE USER SET Profilbild=? WHERE ID=?");
            ps.setString(1, profilbild);
            ps.setInt(2, currentUserID);
            ps.executeUpdate();
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
    }

    public static void updateLehrstuhl(String lehrstuhl, int currentUserID) {
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("UPDATE USER SET lehrstuhl=? WHERE ID=?");
            ps.setString(1, lehrstuhl);
            ps.setInt(2, currentUserID);
            ps.executeUpdate();
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
    }

    public static void updateForschungsgebiet(String forschungsgebiet, int currentUserID) {
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("UPDATE USER SET forschungsgebiet=? WHERE ID=?");
            ps.setString(1, forschungsgebiet);
            ps.setInt(2, currentUserID);
            ps.executeUpdate();
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
    }

    public static ArrayList<Integer> getQuizfrageIDs(int quizID) {
        ArrayList<Integer> quizfrageIDs = new ArrayList<>();
        try {
            //quizfragen-Liste mit allen QUIZFRAGENID's der QUIZ Tabelle füllen (die die übergebene QUIZID besitzen)
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("SELECT * FROM QUIZ WHERE quizid=?");
            ps.setInt(1, quizID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                quizfrageIDs.add(rs.getInt("quizfrageid"));
            }
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        return quizfrageIDs;
    }

    public static void setQuizfragenTrue(int quizfragenID) {
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps2 = con.prepareStatement("Update quizfragen set markiert=true where id=?");
            ps2.setInt(1, quizfragenID);
            ps2.executeUpdate();
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
    }

    public static int countQuizfragenMarkiert() {
        int count = 0;
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM quizfragen WHERE markiert=true");
            while (rs.next()) {
                count++;
            }
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        return count;
    }

    public static int getMinFN(int pos) {
        int minFN = 0;
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            Statement st = con.createStatement();
            ResultSet rs2 = st.executeQuery("select min(fragennummer) as minFN from Quizfragen where markiert=true");
            while (rs2.next()) {
                minFN = rs2.getInt("minFN") + pos;
            }
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        return minFN;
    }

    public static ArrayList<String> getQuiz(int minFN) {
        ArrayList<String> quiz = new ArrayList<>();
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps2 = con.prepareStatement("SELECT * FROM quizfragen WHERE markiert=true and fragennummer=?");
            ps2.setInt(1, minFN);
            ResultSet rs3 = ps2.executeQuery();
            while (rs3.next()) {
                quiz.add(Integer.toString(rs3.getInt("id")));
                quiz.add(rs3.getString("quizfrage"));
                quiz.add(rs3.getString("antwort_a"));
                quiz.add(rs3.getString("antwort_b"));
                quiz.add(rs3.getString("antwort_c"));
                quiz.add(rs3.getString("antwort_d"));
            }
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        return quiz;
    }

    public static void setQuizfragenFalse() {
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            Statement st = con.createStatement();
            st.executeUpdate("update quizfragen set markiert=false where markiert=true");
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
    }

    public static void quizVersuchVerwerfen(int currentUserID, int quizID) {
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("Select * from quizversuche where userid=? and quizid=?");
            ps.setInt(1, currentUserID);
            ps.setInt(2, quizID);
            ResultSet rs = ps.executeQuery();
            int countQV = 0;
            while (rs.next()) {
                countQV = rs.getInt("versuche");
            }
            System.out.println(countQV);
            if (countQV == 0) {
                PreparedStatement ps2 = con.prepareStatement("Insert into quizversuche values (?,?,?,false)");
                ps2.setInt(1, currentUserID);
                ps2.setInt(2, quizID);
                ps2.setInt(3, 1);
                ps2.executeUpdate();
            } else {
                countQV++;
                PreparedStatement ps3 = con.prepareStatement("Update quizversuche set versuche = ?");
                ps3.setInt(1, countQV);
                ps3.executeUpdate();
            }
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
    }

    public static void antwortAbgeben(int quizfragenID, int currentUserID, String selectedA) {
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("Select * from quizfragen where id=?");
            ps.setInt(1, quizfragenID);
            ResultSet rs = ps.executeQuery();
            String richtigeA = "";
            while (rs.next()) {
                richtigeA = rs.getString("richtige_antwort");
            }
            PreparedStatement ps2 = con.prepareStatement("Select * from quizstatistik where userid=? and quizfragenid=?");
            ps2.setInt(1, currentUserID);
            ps2.setInt(2, quizfragenID);
            ResultSet rs2 = ps2.executeQuery();
            int tester = 0;
            if (rs2.next()) {
                tester = 1;
            }
            System.out.println("tester: "+tester);
            if (tester == 0) {
                if (richtigeA.equals(selectedA)) {
                    PreparedStatement ps3 = con.prepareStatement("Insert into quizstatistik values (?,?,?)");
                    ps3.setInt(1, currentUserID);
                    ps3.setInt(2, quizfragenID);
                    ps3.setBoolean(3, true);
                    ps3.executeUpdate();
                } else {
                    PreparedStatement ps3 = con.prepareStatement("Insert into quizstatistik values (?,?,?)");
                    ps3.setInt(1, currentUserID);
                    ps3.setInt(2, quizfragenID);
                    ps3.setBoolean(3, false);
                    ps3.executeUpdate();
                }
            } else {
                if (richtigeA.equals(selectedA)) {
                    PreparedStatement ps3 = con.prepareStatement("update quizstatistik set richtig=true where userid=? and quizfragenid=?");
                    ps3.setInt(1, currentUserID);
                    ps3.setInt(2, quizfragenID);
                    ps3.executeUpdate();
                } else {
                    PreparedStatement ps3 = con.prepareStatement("update quizstatistik set richtig=false where userid=? and quizfragenid=?");
                    ps3.setInt(1, currentUserID);
                    ps3.setInt(2, quizfragenID);
                    ps3.executeUpdate();
                }
            }
            con.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void updateQuizversuche(int quizID, int currentUserID) {
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("Select * from quizversuche where userid=? and quizid=?");
            ps.setInt(1, currentUserID);
            ps.setInt(2, quizID);
            ResultSet rs = ps.executeQuery();
            int countQV = 0;
            while (rs.next()) {
                countQV = rs.getInt("versuche");
            }
            System.out.println(countQV);
            if (countQV == 0) {
                PreparedStatement ps2 = con.prepareStatement("Insert into quizversuche values (?,?,?,false)");
                ps2.setInt(1, currentUserID);
                ps2.setInt(2, quizID);
                ps2.setInt(3, 1);
                ps2.executeUpdate();
            } else {
                countQV++;
                PreparedStatement ps3 = con.prepareStatement("Update quizversuche set versuche = ? where userid=? and quizid=?");
                ps3.setInt(1, countQV);
                ps3.setInt(2, currentUserID);
                ps3.setInt(3, quizID);
                ps3.executeUpdate();
            }
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
    }

    public static void updateQuizstatistik(int currentUserID, List<Integer> quizfragenIDs, int quizID) {
        int countRichtig = 0;
        int countFalsch = 0;
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            for (int i = 0; i < quizfragenIDs.size(); i++) {
                PreparedStatement ps = con.prepareStatement("select * from quizstatistik where userid=? and quizfragenid=? and richtig=true");
                ps.setInt(1, currentUserID);
                ps.setInt(2, quizfragenIDs.get(i));
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    countRichtig++;
                }
            }
            for (int i = 0; i < quizfragenIDs.size(); i++) {
                PreparedStatement ps = con.prepareStatement("select * from quizstatistik where userid=? and quizfragenid=? and richtig=false");
                ps.setInt(1, currentUserID);
                ps.setInt(2, quizfragenIDs.get(i));
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    countFalsch++;
                }
            }
            if (countRichtig >= countFalsch) {
                PreparedStatement ps = con.prepareStatement("update quizversuche set bestanden=true where userid=? and quizid=?");
                ps.setInt(1, currentUserID);
                ps.setInt(2, quizID);
                ps.executeUpdate();
            } else {
                PreparedStatement ps = con.prepareStatement("update quizversuche set bestanden=false where userid=? and quizid=?");
                ps.setInt(1, currentUserID);
                ps.setInt(2, quizID);
                ps.executeUpdate();
            }
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
    }

    public static void deleteMarkierteQuizfragen() {
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("delete from QUIZFRAGEN WHERE MARKIERT=?");
            ps.setBoolean(1, true);
            ps.executeUpdate();
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
    }

    public static void insertQuizfrage(String quizfrage, String antwort1, String antwort2, String antwort3,
                                       String antwort4, String richtigeA) {
        int maxFN = 0;
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            try {
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("select max(fragennummer) as maxFN from Quizfragen where markiert=true");
                while (rs.next()) {
                    maxFN = rs.getInt("maxFN") + 1;
                    System.out.println(maxFN);
                }
            } catch (SQLException throwables) {
                System.out.println(throwables.getMessage());
            }
            PreparedStatement ps = con.prepareStatement("INSERT INTO QUIZFRAGEN (quizfrage,antwort_a,antwort_b,antwort_c,antwort_d,richtige_antwort,markiert,fragennummer)VALUES (?, ?, ?, ?, ?, ?, true,?)");
            ps.setString(1, quizfrage);
            ps.setString(2, antwort1);
            ps.setString(3, antwort2);
            ps.setString(4, antwort3);
            ps.setString(5, antwort4);
            ps.setString(6, richtigeA);
            ps.setInt(7, maxFN);
            ps.executeUpdate();
            System.out.println("Quizfrage hinzugefügt! / Markierung auf TRUE");
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
    }

    public static void insertQuiz(int currentLVID) {
        int quizid = 0;
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select max(QUIZID) as maxquizid from QUIZ");
            while (rs.next()) {
                quizid = rs.getInt("maxquizid") + 1;
            }
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("SELECT * FROM QUIZFRAGEN WHERE MARKIERT=?");
            ps.setBoolean(1, true);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PreparedStatement ps2 = con.prepareStatement("INSERT INTO QUIZ VALUES (?, ?, ?)");
                ps2.setInt(1, rs.getInt("ID"));
                ps2.setInt(2, currentLVID);
                ps2.setInt(3, quizid);
                ps2.executeUpdate();
                PreparedStatement ps3 = con.prepareStatement("UPDATE QUIZFRAGEN SET MARKIERT=? WHERE ID=?");
                ps3.setBoolean(1, false);
                ps3.setInt(2, rs.getInt("ID"));
                ps3.executeUpdate();
                System.out.println("In der Schleife");
            }
            PreparedStatement ps3 = con.prepareStatement("UPDATE QUIZFRAGEN SET MARKIERT=? WHERE MARKIERT=?");
            ps3.setBoolean(1, false);
            ps3.setBoolean(2, true);
            ps3.executeUpdate();
            con.close();
            System.out.println("QUIZ hinzugefügt/ MARKIERT auf false");
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        System.out.println("quizid in insertQuiz: " + quizid);
    }

    public static ArrayList<Integer> countBestandenUndNichtBestanden(int quizID) {
        ArrayList<Integer> beideCounts = new ArrayList<>();
        int countBestanden = 0;
        int countNichtBestanden = 0;
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("select * from quizversuche where bestanden=? and quizid=?");
            ps.setBoolean(1, true);
            ps.setInt(2, quizID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                countBestanden++;
            }
            PreparedStatement ps2 = con.prepareStatement("select * from quizversuche where bestanden=? and quizid=?");
            ps2.setBoolean(1, false);
            ps2.setInt(2, quizID);
            ResultSet rs2 = ps2.executeQuery();
            while (rs2.next()) {
                countNichtBestanden++;
            }
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        beideCounts.add(countBestanden);
        beideCounts.add(countNichtBestanden);
        return beideCounts;
    }

    public static ArrayList<Integer> countQuizTeilnehmerUndUserInLV(int currentLVID, int quizID) {
        ArrayList<Integer> beideCounts = new ArrayList<>();
        int countUserInLV = 0;
        int countQuizTeilnehmer = 0;
        List<Integer> userIDs = new ArrayList<>();
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("SELECT * FROM USERINVERANSTALTUNGEN where veranstaltungsid=?");
            ps.setInt(1, currentLVID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                userIDs.add(rs.getInt("userid"));
            }
            for (int i = 0; i < userIDs.size(); i++) {
                PreparedStatement ps2 = con.prepareStatement("SELECT * FROM User where id=? and lehrender=false");
                ps2.setInt(1, userIDs.get(i));
                ResultSet rs2 = ps2.executeQuery();
                while (rs2.next()) {
                    countUserInLV++;
                }
            }
            PreparedStatement ps2 = con.prepareStatement("SELECT * FROM QUIZVERSUCHE where quizid=?");
            ps2.setInt(1, quizID);
            ResultSet rs2 = ps2.executeQuery();
            while (rs2.next()) {
                countQuizTeilnehmer++;
            }
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        beideCounts.add(countUserInLV);
        beideCounts.add(countQuizTeilnehmer);
        return beideCounts;
    }

    public static ArrayList<String> getQuizVersuche(int quizID) {
        ArrayList<String> versuche = new ArrayList<>();
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("select * from quizversuche where quizid=?");
            ps.setInt(1, quizID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                versuche.add(rs.getInt("userid") + ": " + rs.getInt("versuche") + " Versuche");
            }
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        return versuche;
    }

    public static ArrayList<String> getFragenRichtig(int quizID) {
        ArrayList<String> ergebnis = new ArrayList<>();
        List<Integer> quizIDs = new ArrayList<>();
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("select * from quiz where quizid=?");
            ps.setInt(1, quizID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                quizIDs.add(rs.getInt("quizfrageid"));
            }
            for (int i = 0; i < quizIDs.size(); i++) {
                int qfCount = 0;
                int qfRichtigCount = 0;
                PreparedStatement ps2 = con.prepareStatement("select * from quizstatistik where quizfragenid=?");
                ps2.setInt(1, quizIDs.get(i));
                ResultSet rs2 = ps2.executeQuery();
                while (rs2.next()) {
                    qfCount++;
                }
                PreparedStatement ps3 = con.prepareStatement("select * from quizstatistik where quizfragenid=? and richtig=true");
                ps3.setInt(1, quizIDs.get(i));
                ResultSet rs3 = ps3.executeQuery();
                while (rs3.next()) {
                    qfRichtigCount++;
                }
                int fn = i + 1;
                ergebnis.add("Frage " + fn + ": " + qfRichtigCount + "/" + qfCount + " mal richtig");
            }
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        return ergebnis;
    }

    public static ArrayList<String> getFeedback(int quizID, int currentUserID) {
        ArrayList<String> ergebnis = new ArrayList<>();
        List<Integer> quizfragenIDs = new ArrayList<>();
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("SELECT * FROM QUIZ WHERE quizid=?");
            ps.setInt(1, quizID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                quizfragenIDs.add(rs.getInt("quizfrageid"));
            }
            for (int i = 0; i < quizfragenIDs.size(); i++) {
                PreparedStatement ps3 = con.prepareStatement("select * from quizfragen where id=?");
                ps3.setInt(1, quizfragenIDs.get(i));
                ResultSet rs3 = ps3.executeQuery();
                int fragennummer = 0;
                while (rs3.next()) {
                    fragennummer = rs3.getInt("fragennummer");
                }
                PreparedStatement ps2 = con.prepareStatement("SELECT * FROM quizstatistik WHERE userid=? and quizfragenid=?");
                ps2.setInt(1, currentUserID);
                ps2.setInt(2, quizfragenIDs.get(i));
                ResultSet rs2 = ps2.executeQuery();
                while (rs2.next()) {
                    if (rs2.getBoolean("richtig")) {
                        ergebnis.add("Frage" + fragennummer + ": richtig");
                    } else {
                        ergebnis.add("Frage" + fragennummer + ": falsch");
                    }
                }
            }
            PreparedStatement ps2 = con.prepareStatement("SELECT * FROM quizversuche WHERE userid=? and quizid=?");
            ps2.setInt(1, currentUserID);
            ps2.setInt(2, quizID);
            ResultSet rs2 = ps2.executeQuery();
            boolean bestanden;
            while (rs2.next()) {
                bestanden = rs2.getBoolean("bestanden");
                if (bestanden == true) {
                    ergebnis.add("Sie haben das Quiz bestanden");
                } else {
                    ergebnis.add("Sie haben das Quiz nicht bestanden");
                }
            }
            System.out.println("ergebnis: " + ergebnis);
            con.close();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        return ergebnis;
    }
    public static ArrayList<Integer> getQuizliste(int lvID){
        ArrayList<Integer> quizliste = new ArrayList<>();
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("SELECT * FROM QUIZ where lehrveranstaltungid=?");
            ps.setInt(1, lvID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                quizliste.add(rs.getInt("quizid"));
            }
            con.close();
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        Set<Integer> set = new HashSet<>(quizliste);
        quizliste.clear();
        quizliste.addAll(set);
        return quizliste;
    }
    public static void insertQuizfrageXML(String frage,String antwortA,String antwortB,String antwortC,
    String antwortD, String korrekteAntwort, int fragennummer){
        System.out.println("insertQuizfrageXML anfang");
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("INSERT INTO QUIZFRAGEN (quizfrage,antwort_a,antwort_b,antwort_c,antwort_d,richtige_antwort,markiert,fragennummer)VALUES (?, ?, ?, ?, ?, ?, true,?)");
            ps.setString(1, frage);
            ps.setString(2, antwortA);
            ps.setString(3, antwortB);
            ps.setString(4, antwortC);
            ps.setString(5, antwortD);
            ps.setString(6, korrekteAntwort);
            ps.setInt(7, fragennummer);
            ps.executeUpdate();
            System.out.println("Quizfrage hinzugefügt! / Markierung auf TRUE");
            con.close();
        }
        catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
    }
    public static void updateQuiz(int currentLVID){
        int quizid = 0;
        try{
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select max(QUIZID) as maxquizid from QUIZ");
            while(rs.next()){
                quizid = rs.getInt("maxquizid")+1;
            }
            con.close();
        }
        catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        try {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            PreparedStatement ps = con.prepareStatement("SELECT * FROM QUIZFRAGEN WHERE MARKIERT=?");
            ps.setBoolean(1, true);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                PreparedStatement ps2 = con.prepareStatement("INSERT INTO QUIZ VALUES (?, ?, ?)");
                ps2.setInt(1,rs.getInt("ID"));
                ps2.setInt(2,currentLVID);
                ps2.setInt(3, quizid);
                ps2.executeUpdate();
                PreparedStatement ps3 = con.prepareStatement("UPDATE QUIZFRAGEN SET MARKIERT=? WHERE ID=?");
                ps3.setBoolean(1, false);
                ps3.setInt(2,rs.getInt("ID"));
                ps3.executeUpdate();
                System.out.println("In der Schleife");
            }
            PreparedStatement ps3 = con.prepareStatement("UPDATE QUIZFRAGEN SET MARKIERT=? WHERE MARKIERT=?");
            ps3.setBoolean(1, false);
            ps3.setBoolean(2, true);
            ps3.executeUpdate();
            con.close();
            System.out.println("QUIZ hinzugefügt/ MARKIERT auf false");
        }
        catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        System.out.println(quizid);
    }
}