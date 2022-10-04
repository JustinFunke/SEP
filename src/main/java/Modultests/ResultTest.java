package Modultests;

import Server.ClientStart;
import Server.Datenbank;
import Server.Mail.EmailDistributor;
import Server.ServerNeu;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResultTest {

    public Integer studentID;
    public Integer lectureID;

    public static final String url ="jdbc:h2:./myDB";
    public static final String user = "sa";
    public static final String passwort = "";


    @BeforeAll
    static void before()
    {
        ServerNeu S = new ServerNeu();
        S.serverStart();
    }
    @Test
    public  void testBestanden() throws IOException
    {
        studentID = 1000259;
        lectureID = 67;
        //insertStudentAndLecture(studentID, lectureID);
        boolean bestanden = studentResult(studentID, lectureID);
        //boolean durchgefallen = Server.Datenbank.studentResult(studentID, lectureID);
        assertTrue(bestanden);
        //deleteStudentAndLecture(studentID, lectureID);
    }
/*
    public static void insertStudentAndLecture(Integer student, Integer lecture)
    {
        try {

            Connection con = DriverManager.getConnection(url, user, passwort);

            PreparedStatement insertStudent = con.prepareStatement(
                    "INSERT INTO USER (ID, EMAIL, PASSWORT, VORNAME, NACHNAME, WOHNORT, STRASSE, STUDIENFACH, LEHRENDER) " +
                            "VALUES (?, tom.kkaefer@gmx.de, 123, Tom, Kaefer, hier, da, WiInf, false) " );
            insertStudent.setInt(1, student);
            insertStudent.executeQuery();

            PreparedStatement psBestanden = con.prepareStatement(
                    "INSERT INTO QUIZVERSUCHE (USERID, QUIZID, VERSUCHE, BESTANDEN) " +
                            "VALUES (?, 100, 1, true) " );
            psBestanden.setInt(1, student);
            psBestanden.executeQuery();

            PreparedStatement psVeranstaltung = con.prepareStatement(
                    "INSERT INTO USERINVERANSTALTUNGEN (USERID, VERANSTALTUNGSID) " +
                            "VALUES (?, ?) " );
            psVeranstaltung.setInt(1, student);
            psVeranstaltung.setInt(2, lecture);
            psVeranstaltung.executeQuery();

            PreparedStatement psQuiz = con.prepareStatement(
                    "INSERT INTO QUIZ (QUIZFRAGEID, LEHRVERANSTALTUNGSID, QUIZID) " +
                            "VALUES (1, ? ,100) " );
            psQuiz.setInt(1, lecture);
            psQuiz.executeQuery();

            con.close();


        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public static void deleteStudentAndLecture(Integer student, Integer lecture)
    {
        try {
            Connection con = DriverManager.getConnection(url, user, passwort);

            PreparedStatement insertStudent = con.prepareStatement(
                    "DELETE FROM USER WHERE ID = ? AND EMAIL = tom.kkaefer@gmx.de" );
            insertStudent.setInt(1, student);
            insertStudent.executeQuery();

            PreparedStatement psBestanden = con.prepareStatement(
                    "DELETE FROM QUIZVERSUCHE WHERE USERID = ? AND QUIZID= 100 AND VERSUCHE=1 AND BESTANDEN= true");
            psBestanden.setInt(1, student);
            psBestanden.executeQuery();

            PreparedStatement psVeranstaltung = con.prepareStatement(
                    "DELETE FROM USERINVERANSTALTUNGEN WHERE USERID = ? AND VERANSTALTUNGSID= ? ");
            psVeranstaltung.setInt(1, student);
            psVeranstaltung.setInt(2, lecture);
            psVeranstaltung.executeQuery();

            PreparedStatement psQuiz = con.prepareStatement(
                    "DELETE FROM QUIZ WHERE QUIZFRAGEID = 1 LEHRVERANSTALTUNGSID = ? QUIZID = 100 ");
            psQuiz.setInt(1, lecture);
            psQuiz.executeQuery();

            con.close();


        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }*/

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
}
