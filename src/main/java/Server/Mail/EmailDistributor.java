package Server.Mail;

import Server.Datenbank;

import javax.mail.MessagingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Date;

public class EmailDistributor
{
    //"jdbc:h2:./myDB"
    //Semesterende SoSe 30.09
    //Semesterende WS  31.03

    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("dd/MM/yyyy").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String getCurrentSemester()
    {
        //30.09 SS
        //31.03 WS

        Calendar cal = Calendar.getInstance();
        //SimpleDateFormat today = new SimpleDateFormat("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        Integer thisYear = now.getYear();
        String year = thisYear.toString();
        Integer thisMonth = now.getMonthValue();
        String month = thisMonth.toString();
        Integer thisDay = now.getDayOfMonth();
        String day = thisDay.toString();

        Integer nextyear = thisYear+1;

        String semester = null;

        Date dateOnly = parseDate(day +"/"+month+"/"+year);
        Date endWs = parseDate("31/03/"+year);
        Date endSoSe = parseDate("30/09/"+year);
        System.out.println("Today: " +dateOnly);
        System.out.println("WS-End_date: " + endWs);
        System.out.println("SoSe-End_date:: " + endSoSe);


        assert dateOnly != null;
        if(dateOnly.compareTo(endSoSe) >= 0 && dateOnly.compareTo(endWs) < 0)
        {
            System.out.println("SommerSemester "+ year + " beendet");
            semester = "SoSe "+year;
            return semester;
        }
        else if (dateOnly.compareTo(endSoSe) < 0 && dateOnly.compareTo(endWs) >= 0)
        {
            System.out.println("WinterSemester "+ year + "/" + nextyear + " beendet");
            semester = "WiSe "+ year + "/" + nextyear;
            return semester;
        }
        else
        {
            System.out.println("Something went wrong");
            semester = "still running";
            return semester;
        }
    }

    public static boolean SemesterEndCheck()
    {

        //30.09 SS
        //31.03 WS

        Calendar cal = Calendar.getInstance();
        //SimpleDateFormat today = new SimpleDateFormat("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        Integer thisYear = now.getYear();
        String year = thisYear.toString();
        Integer thisMonth = now.getMonthValue();
        String month = thisMonth.toString();
        Integer thisDay = now.getDayOfMonth();
        String day = thisDay.toString();

        Integer nextyear = thisYear+1;

        String semester = null;

        Date dateOnly = parseDate(day +"/"+month+"/"+year);
        Date endWs = parseDate("31/03/"+year);
        Date endSoSe = parseDate("30/09/"+year);
        System.out.println("Today: " +dateOnly);
        System.out.println("WS-End_date: " + endWs);
        System.out.println("SoSe-End_date:: " + endSoSe);


        if(dateOnly.compareTo(endSoSe) == 0)
        {
            System.out.println("SommerSemester "+ year + " beendet");
            semester = "SoSe "+year;
            return true;
        }
        else if (dateOnly.compareTo(endWs) == 0)
        {
            System.out.println("WinterSemester "+ year + "/" + nextyear + " beendet");
            semester = "WiSe "+ year + "/" + nextyear;
            return true;
        }
        else
        {
            System.out.println("Das Semester läuft noch...");
            semester = "still running";
            return false;
        }
    }



    public static void sendResultsToStudents(String semester) throws MessagingException {
        ArrayList<Integer> StudentsInLectures = Datenbank.listeStudentenInLehrveranstaltungen(semester);
        ArrayList<Integer> LecturesWithStudents = Datenbank.listeVeranstaltungenMitStudenten(semester);
        for(int i = 0; i < StudentsInLectures.size(); i++)
        {
            Integer userId = StudentsInLectures.get(i);
            Integer lectureId = LecturesWithStudents.get(i);
            String mail = Datenbank.studentMail(userId);
            System.out.println("User Server.Mail : "+ mail);
            String name = Datenbank.studentName(userId);
            System.out.println("User Name : " + name);
            if(!mail.equals("Lehrender") && !mail.equals("no email found"))
            {
                if(Datenbank.studentResult(userId, lectureId))
                {
                    String html = "Hallo " + name + ", anbei erhalten Sie die Ergebnisse zu der Veranstaltung " + Datenbank.veranstaltungsName(lectureId) + ". Sie haben bestanden. Herzlichen Glückwunsch!";
                    EmailClient.sendAsHtml(mail, "Ergebnisse der Veranstaltung: " + lectureId, html);
                }
                else
                {
                    String html = "Hallo " + name + ", anbei erhalten Sie die Ergebnisse zu der Veranstaltung " + Datenbank.veranstaltungsName(lectureId)  + ". Sie sind leider durchgefallen. Viel Erfolg beim nächsten Mal!";
                    EmailClient.sendAsHtml(mail, "Ergebnisse der Veranstaltung: " + lectureId, html);

                }
            }
        }
    }
}
