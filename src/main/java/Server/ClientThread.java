package Server;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
//import org.json.*;
import Client.Kalender.Termin;
import Client.Lehrveranstaltung.Lehrveranstaltung;
import Client.Lehrveranstaltung.Projektgruppe;
import Client.javaFx.Thema;
import Server.Mail.EmailDistributor;
import javafx.collections.ObservableList;
import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.mail.MessagingException;

/////////////////////////////Bearbeiten

public class ClientThread extends  Thread{

    private DataInputStream dis;
    private DataOutputStream dos;
    private Socket socket;
    private JSONObject json;
    private JSONObject reespons;
    private String s;
    public boolean running=true;


    public ClientThread(Socket cs, DataInputStream dis,DataOutputStream dos){
        this.socket = cs;
        this.dis= dis;
        this.dos = dos;
    }
    public void run(){
        while (running){
            json = null;
            try{
                // Reader erstellen
                JSONParser parser = new JSONParser();
                BufferedReader in = new BufferedReader(new InputStreamReader(dis,"UTF-8"));
                Object obj;
                s = null;
                //Solange lesen bis s nicht mehr null ist
                while (s==null){
                    s= in.readLine();
                }
                //Gelesene in ein Objekt speichern
                obj = parser.parse(s);
                //Object in json umwandeln
                json = (JSONObject) obj;

                //wenn json nicht leer ist
                if(json != null){
                    String methode = (String) json.get("Methode");
                    reespons = new JSONObject();
                    if(methode.contentEquals("RegistrierungStudent"))
                        registrierungStudent(json);
                    else if(methode.contentEquals("closeConnection"))
                        closeConnection();
                    else if(methode.contentEquals("ResultCheck"))
                        resultCheck(json);
                    else if(methode.contentEquals("Emailvergeben"))
                        Emailvergeben(json);
                    else if(methode.contentEquals("RegistrierungLehrender"))
                        RegistrierungLehrender(json);
                    else if(methode.contentEquals("ProjektUser"))
                        ProjektUser(json);
                    else if(methode.contentEquals("pgNutzerHinzufuegen"))
                        pgNutzerHinzufuegen(json);
                    else if (methode.contentEquals("lehrveranstaltungsMitglieder"))
                        lehrveranstaltungsMitglieder(json);
                    else if(methode.contentEquals("toDoHinzufuegen"))
                        toDoHinzufuegen(json);
                    else if(methode.contentEquals("toDoLoeschen"))
                        toDoLoeschen(json);
                    else if(methode.contentEquals("todoListeAnzeigen"))
                        todoListeAnzeigen(json);
                    else if(methode.contentEquals("todoListeUserAnzeigen"))
                        toDoUserListAnzeigen(json);
                    else if(methode.contentEquals("LernkarteHinzufuegen"))
                        LernkarteHinzufuegen(json);
                    else if(methode.contentEquals("LernkarteNamenVergeben"))
                        LernkarteNamenVergeben(json);
                    else if(methode.contentEquals("LernkartenDropDown"))
                        LernkartenDropDown(json);
                    else if(methode.contentEquals("LernkartenAnzeigen"))
                        LernkartenAnzeigen(json);
                        //ab hier Sebastian
                    else if(methode.contentEquals("holePGInfos"))
                        holePGInfos(json);
                    else if(methode.contentEquals("holePGUser"))
                        holePGUser(json);
                    else if(methode.contentEquals("holePGDateien"))
                        holePGDateien(json);
                    else if(methode.contentEquals("holePGDownloadDatei"))
                        holePGDownloadDatei(json);
                    else if(methode.contentEquals("holeLVInfos"))
                        holeLVInfos(json);
                    else if(methode.contentEquals("holeLVDateien"))
                        holeLVDateien(json);
                    else if(methode.contentEquals("holePGNamen"))
                        holePGNamen(json);
                    else if(methode.contentEquals("holeLM"))
                        holeLM(json);
                    else if(methode.contentEquals("holeProjektID"))
                        holeProjektID(json);
                    else if(methode.contentEquals("holeCheckVorhandenPG"))
                        holeCheckVorhandenPG(json);
                    else if(methode.contentEquals("userPGHinzufuegen"))
                        userPGHinzufuegen(json);
                    else if(methode.contentEquals("holeCheckVorhandenDatei"))
                        holeCheckVorhandenDatei(json);
                    else if(methode.contentEquals("dateiPGHinzufuegen"))
                        dateiPGHinzufuegen(json);
                    else if(methode.contentEquals("holeCheckVorhandenLM"))
                        holeCheckVorhandenLM(json);
                    else if(methode.contentEquals("lmLVHinzufuegen"))
                        lmLVHinzufuegen(json);
                    else if(methode.contentEquals("holeCheckVorhandenProjekt"))
                        holeCheckVorhandenProjekt(json);
                    else if(methode.contentEquals("projektHinzufuegen"))
                        projektHinzufuegen(json);
                    else if(methode.contentEquals("userIDundprojektID"))
                        userIDundprojektID(json);
                    else if (methode.contentEquals("holeCheckVorhandenLV"))
                        holeCheckVorhandenLV(json);
                    else if (methode.contentEquals("lvHinzufuegen"))
                        lvHinzufuegen(json);
                    else if (methode.contentEquals("userIDundlvID"))
                        userIDundlvID(json);
                    else if(methode.contentEquals("NachrichtSenden"))
                        NachrichtSenden(json);
                    else if(methode.contentEquals("NachrichtenAnzeigen"))
                        NachrichtenAnzeigen(json);
                    else if(methode.contentEquals("UserAnzeigen"))
                        UserAnzeigen(json);
                    else if (methode.contentEquals("getUserDaten"))
                        getUserDaten(json);
                    else if (methode.contentEquals("lvDesUsers"))
                        lvDesUsers(json);
                    else if (methode.contentEquals("holeCheckVorhandenThema"))
                        holeCheckVorhandenThema(json);
                    else if (methode.contentEquals("themaHinzufuegen"))
                        themaHinzufuegen(json);
                    else if (methode.contentEquals("holeThemenID"))
                        holeThemenID(json);
                    else if(methode.contentEquals("literaturHinzufuegen"))
                        literaturHinzufuegen(json);
                        // ab hier Jans Sachen
                    else if(methode.contentEquals("ListeAllerLVs"))
                        ListeAllerLVs(json);
                    else if (methode.contentEquals("listeAllerKurseADD"))
                        getLVID(json);
                    else if (methode.contentEquals("listeAllerLvsLehrender"))
                        alleLVsLehrender(json);
                    else if (methode.contentEquals("studentenSucheMatrikel"))
                        searchStudMatrik(json);
                    else if(methode.contentEquals("studentenSucheVorNach"))
                        searchVorNach(json);
                    else if(methode.contentEquals("studentenSucheVor"))
                        searchVor(json);
                    else if(methode.contentEquals("studentenSucheNach"))
                        searchNach(json);
                    else if(methode.contentEquals("studentenSucheAdd"))
                        SucheAdd(json);
                    else if(methode.contentEquals("termineAddOhne"))
                        addTerminOhne(json);
                    else if(methode.contentEquals("termineAddMit"))
                        addTerminMit(json);
                    else if(methode.contentEquals("setlvName"))
                        getLVNAME(json);
                    else if(methode.contentEquals("getStudentenTeilnehmer"))
                        getTeilnehmerStudenten(json);
                    else if(methode.contentEquals("getLehrendeTeilnehmer"))
                        getTeilnehmerLehrende(json);
                    else if(methode.contentEquals("checkLehrender"))
                        checkLehrender(json);
                    else if(methode.contentEquals("tLDoubleClick"))
                        tLDoubleClick(json);
                    else if(methode.contentEquals("TerminList"))
                        getTermine(json);
                    else if(methode.contentEquals("AddTermin"))
                        addTermin(json);
                    else if(methode.contentEquals("KalenderReminderDaten"))
                        checkReminder(json);
                    else if(methode.contentEquals("getThemen"))
                        getThemen(json);
                    else if (methode.contentEquals("getThemaIDTitel"))
                        getThemaIDTitel(json);
                    else if (methode.contentEquals("getThemaDaten"))
                        getThemaDaten(json);
                    else if (methode.contentEquals("getLiteratur"))
                        getLiteratur(json);
                        // Lukas
                    else if (methode.contentEquals("getUserDaten"))
                        getUserDaten(json);
                    else if (methode.contentEquals("lvDesUsers"))
                        lvDesUsers(json);
                    else if (methode.contentEquals("getUserDatenLehrender"))
                        getUserDatenLehrender(json);
                    else if (methode.contentEquals("updateWohnort"))
                        updateWohnort(json);
                    else if (methode.contentEquals("updateStrasse"))
                        updateStrasse(json);
                    else if (methode.contentEquals("updateStudienfach"))
                        updateStudienfach(json);
                    else if (methode.contentEquals("updatePasswort"))
                        updatePasswort(json);
                    else if (methode.contentEquals("updateProfilbild"))
                        updateProfilbild(json);
                    else if (methode.contentEquals("updateLehrstuhl"))
                        updateLehrstuhl(json);
                    else if (methode.contentEquals("updateForschungsgebiet"))
                        updateForschungsgebiet(json);
                        //Ab hier Leonard
                    else if (methode.contentEquals("getUserID"))
                        getUserID(json);
                    else if (methode.contentEquals("freundschaftsAnfrage"))
                        freundschaftsAnfrage(json);
                    else if (methode.contentEquals("getUserIDvonFanfragen"))
                        getUserIDvonFanfragen(json);
                    //Tom
                    else if(methode.contentEquals("login"))
                    {
                        System.out.println("login wird im Thread ausgeführt!");
                        login(json);
                    }
                    //Lukas Client.Quiz-Sachen
                    else if (methode.contentEquals("getQuizfrageIDs"))
                        getQuizfrageIDs(json);
                    else if (methode.contentEquals("setQuizfragenTrue"))
                        setQuizfragenTrue(json);
                    else if (methode.contentEquals("countQuizfragenMarkiert"))
                        countQuizfragenMarkiert(json);
                    else if (methode.contentEquals("getMinFN"))
                        getMinFN(json);
                    else if (methode.contentEquals("getQuiz"))
                        getQuiz(json);
                    else if (methode.contentEquals("setQuizfragenFalse"))
                        setQuizfragenFalse(json);
                    else if (methode.contentEquals("quizVersuchVerwerfen"))
                        quizVersuchVerwerfen(json);
                    else if (methode.contentEquals("antwortAbgeben"))
                        antwortAbgeben(json);
                    else if (methode.contentEquals("updateQuizversuche"))
                        updateQuizversuche(json);
                    else if (methode.contentEquals("updateQuizstatistik"))
                        updateQuizstatistik(json);
                    else if (methode.contentEquals("deleteMarkierteQuizfragen"))
                        deleteMarkierteQuizfragen(json);
                    else if (methode.contentEquals("insertQuizfrage"))
                        insertQuizfrage(json);
                    else if (methode.contentEquals("insertQuiz"))
                        insertQuiz(json);
                    else if (methode.contentEquals("countBestandenUndNichtBestanden"))
                        countBestandenUndNichtBestanden(json);
                    else if (methode.contentEquals("countQuizTeilnehmerUndUserInLV"))
                        countQuizTeilnehmerUndUserInLV(json);
                    else if (methode.contentEquals("getQuizVersuche"))
                        getQuizVersuche(json);
                    else if (methode.contentEquals("getFragenRichtig"))
                        getFragenRichtig(json);
                    else if (methode.contentEquals("getFeedback"))
                        getFeedback(json);
                    //Lukas Bewertungssachen
                    else if (methode.contentEquals("insertBewertungsfrage"))
                        insertBewertungsfrage(json);
                    else if (methode.contentEquals("insertBewertung"))
                        insertBewertung(json);
                    else if (methode.contentEquals("deleteMarkierteBewertungsfragen"))
                        deleteMarkierteBewertungsfragen(json);
                    else if (methode.contentEquals("getBewertungsfragenIDs"))
                        getBewertungsfragenIDs(json);
                    else if (methode.contentEquals("setBewertungsfragenTrue"))
                        setBewertungsfragenTrue(json);
                    else if (methode.contentEquals("countBewertungsfragenMarkiert"))
                        countBewertungsfragenMarkiert(json);
                    else if (methode.contentEquals("getMinFNBewertung"))
                        getMinFNBewertung(json);
                    else if (methode.contentEquals("getBewertung"))
                        getBewertung(json);
                    else if (methode.contentEquals("setBewertungsfragenFalse"))
                        setBewertungsfragenFalse(json);
                    else if (methode.contentEquals("bewertungVersuchVerwerfen"))
                        bewertungVersuchVerwerfen(json);
                    else if (methode.contentEquals("bewertungAbgeben"))
                        bewertungAbgeben(json);
                    else if (methode.contentEquals("getBewertungsliste"))
                        getBewertungsliste(json);
                    else if (methode.contentEquals("filterBewertungTeilnahme")) //in lVUebersicht
                        filterBewertungTeilnahme(json);
                    else if (methode.contentEquals("getQuizliste"))
                        getQuizliste(json);
                    //leonard
                    else if (methode.contentEquals("alleDieBestanden"))
                        alleDieBestanden(json);
                    else if (methode.contentEquals("alleDieDurchgefallen"))
                        alleDieDurchgefallen(json);
                    else if (methode.contentEquals("alleDieTeilnehmen"))
                        alleDieTeilnehmen(json);
                    else if (methode.contentEquals("getIDsVonFragen"))
                        getIDsVonFragen(json);
                    else if (methode.contentEquals("countAntwortenVonUsern"))
                        countAntwortenVonUsern(json);
                    else if (methode.contentEquals("anzahlBewertungsfragen"))
                        anzahlBewertungsfragen(json);
                    else if (methode.contentEquals("hatHaelfteBearbeitet"))
                        hatHaelfteBearbeitet(json);

                    else if (methode.contentEquals("countAntwortenVonEinem"))
                        countAntwortenVonEinem(json);
                    else if (methode.contentEquals("getGleicheVeranstaltungLeute"))
                        getGleicheVeranstaltungLeute(json);
                    else if (methode.contentEquals("getFreundeVonUser"))
                        getFreundeVonUser(json);
                    else if (methode.contentEquals("getNamenVonIDs"))
                        getNamenVonIDs(json);
                    else if (methode.contentEquals("getAntwortenVon"))
                        getAntwortenVon(json);
                    else if (methode.contentEquals("getFreundeIDvonFanfragen"))
                        getFreundeIDvonFanfragen(json);

                    else if (methode.contentEquals("getChatIDs"))
                        getChatIDs(json);
                    else if (methode.contentEquals("getChatNachrichten"))
                        getChatNachrichten(json);
                    else if (methode.contentEquals("insertIntoPC"))
                        insertIntoPC(json);
                    else if (methode.contentEquals("deleteFanfragen"))
                        deleteFanfragen(json);
                    else if (methode.contentEquals("insertFreund"))
                        insertFreund(json);

                    else if (methode.contentEquals("lvNamenVonUser"))
                        lvNamenVonUser(json);
                    else if (methode.contentEquals("getIDfromLV"))
                        getIDfromLV(json);
                    else if (methode.contentEquals("lvIDsVonUser"))
                        lvIDsVonUser(json);
                    else if (methode.contentEquals("getSemesterfromLV"))
                        getSemesterfromLV(json);
                    else if (methode.contentEquals("userName"))
                        userName(json);
                    //Lukas xml Sachen
                    else if (methode.contentEquals("insertQuizfrageXML"))
                        insertQuizfrageXML(json);
                    else if (methode.contentEquals("updateQuiz"))
                        updateQuiz(json);

                }
            } catch (UnsupportedEncodingException | ParseException e) {
                e.printStackTrace();
                running = false;
            } catch (Exception e) {
                e.printStackTrace();
                running = false;
            }
        }
    }


    public void resultCheck(JSONObject semester) throws MessagingException
    {
        String currentSemester = (String) semester.get("Semester");
        EmailDistributor.sendResultsToStudents(currentSemester);
    }

    //Lukas xml
    private void updateQuiz(JSONObject jo) throws IOException{
        int currentLVID = Integer.parseInt("" + jo.get("P1"));
        LukasDBMethods.updateQuiz(currentLVID);
    }
    private void insertQuizfrageXML(JSONObject jo) throws IOException{
        System.out.println("insertQuizfragenXML in ClientThread");
        String frage = (String) jo.get("P1");
        String antwortA = (String) jo.get("P2");
        String antwortB = (String) jo.get("P3");
        String antwortC = (String) jo.get("P4");
        String antwortD = (String) jo.get("P5");
        String korrekteAntwort = (String) jo.get("P6");
        System.out.println("P6 klappt");
        int fragennummer = Integer.parseInt("" + jo.get("P7"));
        System.out.println("P7 klappt");
        LukasDBMethods.insertQuizfrageXML(frage,antwortA,antwortB,antwortC,antwortD,korrekteAntwort,fragennummer);

    }
    //Leonard Methoden
    private void userName(JSONObject jo) throws IOException{
        int currentUserID = Integer.parseInt("" + jo.get("P1"));
        JSONObject json = new JSONObject();
        json.put("P1", LeonardDBMethods.userName(currentUserID));
        String jsonString = json.toJSONString();
        sendenToClient(jsonString);
    }

    private void lvIDsVonUser(JSONObject jo) throws IOException{
        int currentUserID = Integer.parseInt("" + jo.get("P1"));
        arrayListSendenString(LeonardDBMethods.lvIDsVonUser(currentUserID));
    }

    private void getSemesterfromLV(JSONObject jo) throws IOException{
        int lvID = Integer.parseInt("" + jo.get("P1"));
        JSONObject json = new JSONObject();
        json.put("P1", LeonardDBMethods.getSemester(lvID));
        String jsonString = json.toJSONString();
        sendenToClient(jsonString);
    }

    private void getIDfromLV(JSONObject jo) throws IOException{
        String lvName = (String) jo.get("P1");
        String clickedSemester = (String) jo.get("P2");
        JSONObject json = new JSONObject();
        json.put("P1", LeonardDBMethods.getlvID(lvName, clickedSemester));
        String jsonString = json.toJSONString();
        sendenToClient(jsonString);
    }

    private void lvNamenVonUser(JSONObject jo) throws IOException{
        int currentUserID = Integer.parseInt("" + jo.get("P1"));
        String clickedSemester = (String) jo.get("P2");
        arrayListSendenString(LeonardDBMethods.lvNamenVonUser(currentUserID, clickedSemester));
    }

    private void insertFreund(JSONObject jo) throws IOException{
        int currentUserID = Integer.parseInt("" + jo.get("P1"));
        int freundid = Integer.parseInt("" + jo.get("P2"));
        LeonardDBMethods.insertIntoFreunde(currentUserID, freundid);
    }

    private void deleteFanfragen(JSONObject jo) throws IOException{
        int currentUserID = Integer.parseInt("" + jo.get("P1"));
        int freundid = Integer.parseInt("" + jo.get("P2"));
        LeonardDBMethods.deleteFromFanfragen(currentUserID, freundid);
    }

    private void getChatIDs(JSONObject jo) throws IOException{
        int currentUserID = Integer.parseInt("" + jo.get("P1"));
        int freundid = Integer.parseInt("" + jo.get("P2"));
        arrayListSendenInteger(LeonardDBMethods.getChatIDs(currentUserID, freundid));
    }
    private void getChatNachrichten(JSONObject jo) throws IOException{
        ArrayList<String> ids = new ArrayList<>();
        String jsonString = (String) jo.get("P1");
        JSONArray jsonArray = new JSONArray(jsonString);
        for(int i=0; i< jsonArray.length();i++){
            ids.add(jsonArray.getString(i));
        }
        arrayListSendenString(LeonardDBMethods.getChatNachrichten(ids));
    }
    public void insertIntoPC(JSONObject jo) throws IOException{
        int currentUserID = Integer.parseInt("" + jo.get("P1"));
        int freundid = Integer.parseInt("" + jo.get("P2"));
        String nachricht = (String) jo.get("P3");
        LeonardDBMethods.insertIntoPrivatchat(currentUserID, freundid, nachricht);
    }

    private void getGleicheVeranstaltungLeute(JSONObject jo) throws IOException{
        int currentUser = Integer.parseInt("" + jo.get("P1"));
        arrayListSendenString(LeonardDBMethods.gleicheVeranstaltung(currentUser));
    }
    private void getFreundeVonUser(JSONObject jo) throws IOException{
        int currentUser = Integer.parseInt("" + jo.get("P1"));
        arrayListSendenString(LeonardDBMethods.alleFreunde(currentUser));
    }
    private void getNamenVonIDs(JSONObject jo) throws IOException{
        int currentUser = Integer.parseInt("" + jo.get("P1"));
        ArrayList<String> ids = new ArrayList<>();
        String jsonString = (String) jo.get("P2");
        JSONArray jsonArray = new JSONArray(jsonString);
        for(int i=0; i< jsonArray.length();i++){
            ids.add(jsonArray.getString(i));
        }
        arrayListSendenString(LeonardDBMethods.idsToName(ids,currentUser));
    }

    private void getAntwortenVon(JSONObject jo) throws IOException{
        int currentUser = Integer.parseInt("" + jo.get("P1"));
        int bewertungID = Integer.parseInt("" + jo.get("P2"));
        arrayListSendenInteger(LeonardDBMethods.getAntworten(currentUser, bewertungID));
    }

    private void getFreundeIDvonFanfragen(JSONObject jo) throws IOException{
        int currentUser = Integer.parseInt("" + jo.get("P1"));
        arrayListSendenString(LeonardDBMethods.freundeVonFanfragen(currentUser));
    }

    private void getUserID(JSONObject jo) throws IOException{
        String vorname = (String) jo.get("P1");
        String nachname = (String) jo.get("P2");
        JSONObject json = new JSONObject();
        json.put("P1", LeonardDBMethods.userID(vorname, nachname));
        String jsonString = json.toJSONString();
        sendenToClient(jsonString);
    }
    private void freundschaftsAnfrage(JSONObject jo) throws IOException{
        int currentUser = Integer.parseInt("" + jo.get("P1"));
        int freundid = Integer.parseInt("" + jo.get("P2"));
        LeonardDBMethods.insertIntoFanfragen(currentUser, freundid);
    }
    private void getUserIDvonFanfragen(JSONObject jo) throws IOException{
        int currentUser = Integer.parseInt("" + jo.get("P1"));
        arrayListSendenString(LeonardDBMethods.userVonFanfragen(currentUser));
    }


    //Leonard Bewertung-Methoden
    private void hatHaelfteBearbeitet(JSONObject jo) throws IOException{
        int currentUserID = Integer.parseInt("" + jo.get("P1"));
        int lvID = Integer.parseInt("" + jo.get("P2"));
        JSONObject json = new JSONObject();
        json.put("P1", BewertungDBMethods.hatHaelfteBearbeitet(currentUserID,lvID));
        String jsonString = json.toJSONString();
        sendenToClient(jsonString);
    }
    private void alleDieBestanden(JSONObject jo) throws IOException{
        int lehrveranstaltungsID = Integer.parseInt("" + jo.get("P1"));
        arrayListSendenString(BewertungDBMethods.habenLVbestandenIDs(lehrveranstaltungsID));
    }

    private void alleDieDurchgefallen(JSONObject jo) throws IOException{
        int lehrveranstaltungsID = Integer.parseInt("" + jo.get("P1"));
        arrayListSendenString(BewertungDBMethods.habenLVnichtBestandenIDs(lehrveranstaltungsID));
    }

    private void alleDieTeilnehmen(JSONObject jo) throws IOException{
        int lehrveranstaltungsID = Integer.parseInt("" + jo.get("P1"));
        arrayListSendenString(BewertungDBMethods.nehmenAnLVteil(lehrveranstaltungsID));
    }

    private void getIDsVonFragen(JSONObject jo) throws IOException{
        int bewertungID = Integer.parseInt("" + jo.get("P1"));
        arrayListSendenInteger(BewertungDBMethods.getFragenIDs(bewertungID));
    }

    private void countAntwortenVonUsern(JSONObject jo) throws IOException{
        int frageID = Integer.parseInt("" + jo.get("P1"));
        ArrayList<String> ids = new ArrayList<>();
        String jsonString = (String) jo.get("P2");
        JSONArray jsonArray = new JSONArray(jsonString);
        for(int i=0; i< jsonArray.length();i++){
            ids.add(jsonArray.getString(i));
        }
        arrayListSendenInteger(BewertungDBMethods.countAntwortenVonUserList(frageID,ids));
    }

    private void anzahlBewertungsfragen(JSONObject jo) throws IOException{
        int bewertungID = Integer.parseInt("" + jo.get("P1"));
        JSONObject json = new JSONObject();
        json.put("P1", Integer.toString(BewertungDBMethods.countBewertungsfragen(bewertungID)));
        String jsonString = json.toJSONString();
        sendenToClient(jsonString);
    }

    private void countAntwortenVonEinem(JSONObject jo) throws IOException{
        int frageID = Integer.parseInt("" + jo.get("P1"));
        int currentUserID = Integer.parseInt("" + jo.get("P2"));
        arrayListSendenInteger(BewertungDBMethods.countAntwortenVonUser(frageID, currentUserID));
    }
                //Lukas Bewertungs-Methoden
    private void getQuizliste(JSONObject jo) throws IOException{
        int lvID = Integer.parseInt("" + jo.get("P1"));
        arrayListSendenInteger(LukasDBMethods.getQuizliste(lvID));
    }
    private void filterBewertungTeilnahme(JSONObject jo) throws IOException{
        int currentUserID = Integer.parseInt("" + jo.get("P1"));
        int bewertungsid = Integer.parseInt("" + jo.get("P2"));
        JSONObject json = new JSONObject();
        json.put("P1", BewertungDBMethods.filterBewertungTeilnahme(currentUserID,bewertungsid));
        String jsonString = json.toJSONString();
        sendenToClient(jsonString);
    }
    private void getBewertungsliste(JSONObject jo) throws IOException{
        int lvID = Integer.parseInt("" + jo.get("P1"));
        arrayListSendenInteger(BewertungDBMethods.getBewertungsliste(lvID));
    }
    private void bewertungAbgeben(JSONObject jo) throws IOException{
        List<Integer> bewertungsfragenIDs = new ArrayList<>();
        String jsonString = (String) jo.get("P1");
        JSONArray jsonArray = new JSONArray(jsonString);
        for(int i=0; i< jsonArray.length();i++){
            bewertungsfragenIDs.add(jsonArray.getInt(i));
        }
        int currentUserID = Integer.parseInt("" + jo.get("P2"));

        List<Integer> antworten = new ArrayList<>();
        String jsonString2 = (String) jo.get("P3");
        JSONArray jsonArray2 = new JSONArray(jsonString2);
        for(int i=0; i< jsonArray2.length();i++){
            antworten.add(jsonArray2.getInt(i));
        }
        int bewertungsid = Integer.parseInt("" + jo.get("P4"));
        BewertungDBMethods.bewertungAbgeben(bewertungsfragenIDs,currentUserID,antworten,bewertungsid);
    }
    private void bewertungVersuchVerwerfen(JSONObject jo) throws IOException{
        int currentUserID = Integer.parseInt("" + jo.get("P1"));
        int bewertungsid = Integer.parseInt("" + jo.get("P2"));
        BewertungDBMethods.bewertungVersuchVerwerfen(currentUserID,bewertungsid);
        System.out.println("bewertungVersuchVerwerfen");
    }
    private void setBewertungsfragenFalse(JSONObject jo) throws IOException{
        BewertungDBMethods.setBewertungsfragenFalse();
    }
    private void getBewertung(JSONObject jo) throws IOException{
        int minFN = Integer.parseInt("" + jo.get("P1"));
        arrayListSendenString(BewertungDBMethods.getBewertung(minFN));
    }
    private void getMinFNBewertung(JSONObject jo) throws IOException{
        int pos = Integer.parseInt("" + jo.get("P1"));
        JSONObject json = new JSONObject();
        json.put("P1", Integer.toString(BewertungDBMethods.getMinFNBewertung(pos)));
        String jsonString = json.toJSONString();
        sendenToClient(jsonString);
    }
    private void countBewertungsfragenMarkiert(JSONObject jo) throws IOException{
        JSONObject json = new JSONObject();
        json.put("P1", Integer.toString(BewertungDBMethods.countBewertungsfragenMarkiert()));
        String jsonString = json.toJSONString();
        sendenToClient(jsonString);
        System.out.println("senden in countBewertungsfragenMarkiert");
    }
    private void setBewertungsfragenTrue(JSONObject jo) throws IOException{
        System.out.println("setBewertungsfragenTrue");
        int bewertungsfragenID = Integer.parseInt("" + jo.get("P1"));
        BewertungDBMethods.setBewertungsfragenTrue(bewertungsfragenID);
    }
    private void getBewertungsfragenIDs(JSONObject jo) throws IOException{
        System.out.println("getBewertungsfrageID");
        int bewertungsid = Integer.parseInt("" + jo.get("P1"));
        arrayListSendenInteger(BewertungDBMethods.getBewertungsfragenIDs(bewertungsid));
    }
    private void deleteMarkierteBewertungsfragen(JSONObject jo) throws IOException{
        BewertungDBMethods.deleteMarkierteBewertungsfragen();
        System.out.println("deleteMarkierteBewertungsfragen");
    }
    private void insertBewertung(JSONObject jo) throws IOException{
        int currentLVID = Integer.parseInt("" + jo.get("P1"));
        BewertungDBMethods.insertBewertung(currentLVID);
    }
    private void insertBewertungsfrage(JSONObject jo) throws IOException{
        String bewertungsfrage = (String) jo.get("P1");
        BewertungDBMethods.insertBewertungsfrage(bewertungsfrage);
    }
    //Lukas Client.Quiz Methoden
    private void getFeedback(JSONObject jo) throws IOException{
        int quizID = Integer.parseInt("" + jo.get("P1"));
        int currentUserID = Integer.parseInt("" + jo.get("P2"));
        arrayListSendenString(LukasDBMethods.getFeedback(quizID,currentUserID));
    }
    private void getFragenRichtig(JSONObject jo) throws IOException{
        int quizID = Integer.parseInt("" + jo.get("P1"));
        arrayListSendenString(LukasDBMethods.getFragenRichtig(quizID));
    }
    private void getQuizVersuche(JSONObject jo) throws IOException{
        int quizID = Integer.parseInt("" + jo.get("P1"));
        arrayListSendenString(LukasDBMethods.getQuizVersuche(quizID));
    }
    private void countQuizTeilnehmerUndUserInLV(JSONObject jo) throws IOException{
        int currentLVID = Integer.parseInt("" + jo.get("P1"));
        int quizID = Integer.parseInt("" + jo.get("P2"));
        arrayListSendenInteger(LukasDBMethods.countQuizTeilnehmerUndUserInLV(currentLVID,quizID));
    }
    private void countBestandenUndNichtBestanden(JSONObject jo) throws IOException{
        int quizID = Integer.parseInt("" + jo.get("P1"));
        arrayListSendenInteger(LukasDBMethods.countBestandenUndNichtBestanden(quizID));
    }
    private void insertQuiz(JSONObject jo) throws IOException{
        int currentLVID = Integer.parseInt("" + jo.get("P1"));
        LukasDBMethods.insertQuiz(currentLVID);
    }
    private void insertQuizfrage(JSONObject jo) throws IOException{
        String quizfrage = (String) jo.get("P1");
        String antwort1 = (String) jo.get("P2");
        String antwort2 = (String) jo.get("P3");
        String antwort3 = (String) jo.get("P4");
        String antwort4 = (String) jo.get("P5");
        String richtigeA = (String) jo.get("P6");
        LukasDBMethods.insertQuizfrage(quizfrage,antwort1,antwort2,antwort3,antwort4,richtigeA);
        System.out.println("insertQuizfrage");
    }
    private void deleteMarkierteQuizfragen(JSONObject jo) throws IOException{
        LukasDBMethods.deleteMarkierteQuizfragen();
        System.out.println("deleteMarkierteQuizfragen");
    }
    private void updateQuizstatistik(JSONObject jo) throws IOException{
        int currentUserID = Integer.parseInt("" + jo.get("P1"));
        List<Integer> quizfragenIDs = new ArrayList<>();
        String jsonString = (String) jo.get("P2");
        System.out.println("jarray: "+jsonString);
        JSONArray jsonArray = new JSONArray(jsonString);
        System.out.println("JSONArray in updateQuizStatistik: "+jsonArray);
        for(int i=0; i< jsonArray.length();i++){
            quizfragenIDs.add(jsonArray.getInt(i));
        }
        System.out.println(quizfragenIDs);
        int quizID = Integer.parseInt("" + jo.get("P3"));
        LukasDBMethods.updateQuizstatistik(currentUserID,quizfragenIDs,quizID);
    }
    private void updateQuizversuche(JSONObject jo) throws IOException{
        int quizID = Integer.parseInt("" + jo.get("P1"));
        int currentUserID = Integer.parseInt("" + jo.get("P2"));
        LukasDBMethods.updateQuizversuche(quizID,currentUserID);
    }
    private void antwortAbgeben(JSONObject jo) throws IOException{
        int quizfragenID = Integer.parseInt("" + jo.get("P1"));
        int currentUserID = Integer.parseInt("" + jo.get("P2"));
        String selectedA = (String) jo.get("P3");
        LukasDBMethods.antwortAbgeben(quizfragenID,currentUserID,selectedA);
    }
    private void quizVersuchVerwerfen(JSONObject jo) throws IOException{
        int currentUserID = Integer.parseInt("" + jo.get("P1"));
        int quizID = Integer.parseInt("" + jo.get("P2"));
        LukasDBMethods.quizVersuchVerwerfen(currentUserID, quizID);
    }
    private void setQuizfragenFalse(JSONObject jo) throws IOException{
        LukasDBMethods.setQuizfragenFalse();
    }
    private void getQuiz(JSONObject jo) throws IOException{
        int minFN = Integer.parseInt("" + jo.get("P1"));
        arrayListSendenString(LukasDBMethods.getQuiz(minFN));
    }
    private void getMinFN(JSONObject jo) throws IOException{
        int pos = Integer.parseInt("" + jo.get("P1"));
        JSONObject json = new JSONObject();
        json.put("P1", Integer.toString(LukasDBMethods.getMinFN(pos)));
        String jsonString = json.toJSONString();
        sendenToClient(jsonString);
    }
    private void countQuizfragenMarkiert(JSONObject jo) throws IOException{
        JSONObject json = new JSONObject();
        json.put("P1", Integer.toString(LukasDBMethods.countQuizfragenMarkiert()));
        String jsonString = json.toJSONString();
        sendenToClient(jsonString);
        System.out.println("senden in countQuiz...");
    }
    private void setQuizfragenTrue(JSONObject jo) throws IOException{
        System.out.println("setQuizfragenTrue");
        int quizfragenID = Integer.parseInt("" + jo.get("P1"));
        LukasDBMethods.setQuizfragenTrue(quizfragenID);
    }
    private void getQuizfrageIDs(JSONObject jo) throws IOException{
        System.out.println("getQuizfrageID");
        int quizID = Integer.parseInt("" + jo.get("P1"));
        arrayListSendenInteger(LukasDBMethods.getQuizfrageIDs(quizID));
    }
    //Tom Methoden

    private void login(JSONObject j)
    {
        String mail = (String) j.get("mail");
        String password = (String) j.get("password");
        String id = (String) j.get("id");

        System.out.println("mail: "+ mail + "pw:" + password +"id: " + id);
        System.out.println("DB Abfrage wird gestarted");

        JSONObject answer = Datenbank.LoginCheck(mail, password, id);
        System.out.println("DB Abfrage beendet");
        String response = answer.toJSONString();
        sendenToClient(response);
    }


    //Lukas Methoden
    private void updateWohnort(JSONObject jo) throws IOException{
        String wohnort = (String) jo.get("P1");
        int currentUser = Integer.parseInt("" + jo.get("P2"));
        LukasDBMethods.updateWohnort(wohnort,currentUser);
    }
    private void updateStrasse(JSONObject jo) throws IOException{
        String strasse = (String) jo.get("P1");
        int currentUser = Integer.parseInt("" + jo.get("P2"));
        LukasDBMethods.updateStrasse(strasse,currentUser);
    }
    private void updateStudienfach(JSONObject jo) throws IOException{
        String studienfach = (String) jo.get("P1");
        int currentUser = Integer.parseInt("" + jo.get("P2"));
        LukasDBMethods.updateStudienfach(studienfach,currentUser);
    }
    private void updatePasswort(JSONObject jo) throws IOException{
        String passwort = (String) jo.get("P1");
        int currentUser = Integer.parseInt("" + jo.get("P2"));
        LukasDBMethods.updatePasswort(passwort,currentUser);
    }
    private void updateProfilbild(JSONObject jo) throws IOException{
        String profilbild = (String) jo.get("P1");
        int currentUser = Integer.parseInt("" + jo.get("P2"));
        LukasDBMethods.updateProfilbild(profilbild,currentUser);
    }
    private void updateLehrstuhl(JSONObject jo) throws IOException{
        String lehrstuhl = (String) jo.get("P1");
        int currentUser = Integer.parseInt("" + jo.get("P2"));
        LukasDBMethods.updateLehrstuhl(lehrstuhl,currentUser);
    }
    private void updateForschungsgebiet(JSONObject jo) throws IOException{
        String forschungsgebiet = (String) jo.get("P1");
        int currentUser = Integer.parseInt("" + jo.get("P2"));
        LukasDBMethods.updateForschungsgebiet(forschungsgebiet,currentUser);
    }
    private void getUserDaten(JSONObject jo) throws IOException{
        int currentUser = Integer.parseInt("" + jo.get("P1"));
        arrayListSendenString(LukasDBMethods.userDaten(currentUser));
    }
    private void lvDesUsers(JSONObject jo) throws IOException {
        int currentUser = Integer.parseInt("" + jo.get("P1"));
        arrayListSendenString(LukasDBMethods.lvDesUsers(currentUser));
    }
    private void getUserDatenLehrender(JSONObject jo) throws IOException{
        int currentUser = Integer.parseInt("" + jo.get("P1"));
        arrayListSendenString(LukasDBMethods.userDatenLehrender(currentUser));
    }

    //ab hier Sebastians Methoden
    private void literaturHinzufuegen(JSONObject jo) throws IOException{
        int themenID = Integer.parseInt( jo.get("P1")+"");
        String literatur = (String) jo.get("P2");
        Datenbank.DbLiteraturHinzufuegen(themenID, literatur);
    }

    private void holeThemenID(JSONObject jo) throws IOException{
        String titel = (String) jo.get("P1");
        int themenid = Integer.parseInt( jo.get("P2")+"");
        sendeThemenID(Datenbank.themenID(titel, themenid));
    }

    private  void sendeThemenID(int themenID){
        JSONObject json = new JSONObject();
        json.put("P1", themenID);
        String jsonString = json.toJSONString();
        sendenToClient(jsonString);
    }

    private void themaHinzufuegen(JSONObject jo) throws IOException{
        String title = (String) jo.get("P1");
        String beschreibung = (String) jo.get("P2");
        int userID = Integer.parseInt( jo.get("P3")+"");
        Thema nThema = new Thema(title, beschreibung, userID);
        Datenbank.DbThemaHinzufuegen(nThema);
    }

    private void holeCheckVorhandenThema(JSONObject jo) throws IOException{
        String thema = (String) jo.get("P1");
        int userid = Integer.parseInt((jo.get("P2")+""));
        sendeCheckVorhandenThema(Datenbank.checkVorhandenThema(thema, userid));
    }

    private void sendeCheckVorhandenThema(Boolean vorhandenThema){
        JSONObject json = new JSONObject();
        json.put("P1", vorhandenThema);
        String jsonString = json.toJSONString();
        sendenToClient(jsonString);
    }

    private void userIDundlvID(JSONObject jo) throws IOException{
        String nlvName = (String) jo.get("P1");
        String nlvTyp = (String) jo.get("P2");
        String nlvSemester = (String) jo.get("P3");
        int userID = Integer.parseInt( jo.get("P4")+"");
        int lvID = Datenbank.lvID(nlvName, nlvTyp, nlvSemester);
        Datenbank.DbUserLvHinzufügen(userID, lvID);
    }

    private void lvHinzufuegen(JSONObject jo) throws IOException{
        System.out.println("Erfolgreich LV");
        String nlvName = (String) jo.get("P1");
        String nlvTyp = (String) jo.get("P2");
        String nlvSemester = (String) jo.get("P3");
        int userID = Integer.parseInt( jo.get("P4")+"");
        Lehrveranstaltung nlv = new Lehrveranstaltung(nlvName, nlvTyp, nlvSemester);
        Datenbank.DbLVHinzufuegen(nlv, userID);
    }

    private void holeCheckVorhandenLV(JSONObject jo) throws IOException{
        String lvName = (String) jo.get("P1");
        String lvTyp = (String) jo.get("P2");
        String lvSemester = (String) jo.get("P3");
        sendeCheckVorhandenLV(Datenbank.checkVorhandenLV(lvName, lvTyp, lvSemester));
    }

    private void sendeCheckVorhandenLV(Boolean vorhandenLV){
        JSONObject json = new JSONObject();
        json.put("P1", vorhandenLV);
        String jsonString = json.toJSONString();
        sendenToClient(jsonString);
    }

    private void userIDundprojektID(JSONObject jo) throws IOException{
        String pgName = (String) jo.get("P1");
        int lvID = Integer.parseInt( jo.get("P2")+"");
        int userID = Integer.parseInt( jo.get("P3")+"");
        Datenbank.DbUserPGHinzufuegen(userID, Datenbank.projektID(pgName, lvID));
    }

    private void projektHinzufuegen(JSONObject jo) throws IOException{
        String pgName = (String) jo.get("P1"+"");
        int lvID = Integer.parseInt( jo.get("P2")+"");
        int userID = Integer.parseInt( jo.get("P3")+"");
        Projektgruppe neuePG = new Projektgruppe(pgName, lvID);
        Datenbank.DbPGHinzufuegen(neuePG, userID);
    }

    private void holeCheckVorhandenProjekt(JSONObject jo) throws IOException{
        String projektName = (String) jo.get("P1");
        int lvid = Integer.parseInt( jo.get("P2")+"");
        sendeCheckVorhandenProjekt(Datenbank.checkVorhandenProjekt(projektName, lvid));
    }

    private void sendeCheckVorhandenProjekt(Boolean vorhandenProjekt){
        JSONObject json = new JSONObject();
        json.put("P1", vorhandenProjekt);
        String jsonString = json.toJSONString();
        sendenToClient(jsonString);
    }

    private void lmLVHinzufuegen(JSONObject jo) throws IOException{
        int lvid = Integer.parseInt( jo.get("P1")+"");
        String pdfUmgewandelt = (String) jo.get("P2");
        String lmTitle = (String) jo.get("P3");
        Datenbank.DbLMHinzufuegen(lvid, pdfUmgewandelt, lmTitle);
    }

    private void holeCheckVorhandenLM(JSONObject jo) throws IOException{
        String lmName = (String) jo.get("P1");
        int lvid = Integer.parseInt( jo.get("P2")+"");
        sendeCheckVorhandenLM(Datenbank.checkVorhandenLM(lmName, lvid));
    }

    private void sendeCheckVorhandenLM(Boolean vorhandenLM){
        JSONObject json = new JSONObject();
        json.put("P1", vorhandenLM);
        String jsonString = json.toJSONString();
        sendenToClient(jsonString);
    }

    private void dateiPGHinzufuegen(JSONObject jo) throws IOException{
        String dateiTitle = (String) jo.get("P1");
        String pdfUmgewandelt = (String) jo.get("P2");
        int projektid = Integer.parseInt( jo.get("P3")+"");
        Datenbank.DbPDHinzufuegen(dateiTitle, pdfUmgewandelt, projektid);
    }

    private void holeCheckVorhandenDatei(JSONObject jo) throws IOException{
        String dateiName = (String) jo.get("P1");
        int projektid = Integer.parseInt( jo.get("P2")+"");
        sendeCheckVorhandenDatei(Datenbank.checkVorhandenDatei(dateiName, projektid));
    }

    private void sendeCheckVorhandenDatei(Boolean vorhandenDatei){
        JSONObject json = new JSONObject();
        json.put("P1", vorhandenDatei);
        String jsonString = json.toJSONString();
        sendenToClient(jsonString);
    }

    private void userPGHinzufuegen(JSONObject jo) throws IOException{
        int nutzerid = Integer.parseInt( jo.get("P1")+"");
        int projektid = Integer.parseInt( jo.get("P2")+"");
        Datenbank.DbUserPGHinzufuegen(nutzerid, projektid);
    }

    private void holeCheckVorhandenPG(JSONObject jo) throws IOException{
        int nutzerid = Integer.parseInt( jo.get("P1")+"");
        int projektid = Integer.parseInt( jo.get("P2")+"");
        sendeCheckVorhandenPG(Datenbank.checkVorhandenPG(nutzerid, projektid));
    }

    private void sendeCheckVorhandenPG(Boolean vorhandenPG){
        JSONObject json = new JSONObject();
        json.put("P1", vorhandenPG);
        String jsonString = json.toJSONString();
        sendenToClient(jsonString);
    }

    private void holeProjektID(JSONObject jo) throws IOException{
        String pgName = (String) jo.get("P1");
        int lvid = Integer.parseInt( jo.get("P2")+"");
        sendeProjektID(Datenbank.projektID(pgName, lvid));
    }

    private void sendeProjektID(int projektID){
        JSONObject json = new JSONObject();
        json.put("P1", projektID);
        String jsonString = json.toJSONString();
        sendenToClient(jsonString);
    }

    private void holeLM(JSONObject jo) throws IOException {
        int lvid = Integer.parseInt( jo.get("P1")+"");
        String lm = (String) jo.get("P2");
        sendeLM(Datenbank.holeLM(lvid, lm));
    }

    private void sendeLM(String lm){
        JSONObject json = new JSONObject();
        json.put("P1", lm);
        String jsonString = json.toJSONString();
        sendenToClient(jsonString);
    }

    private void holePGNamen(JSONObject jo) throws IOException {
        int lvid = Integer.parseInt( jo.get("P1")+"");
        sendePGNamen(Datenbank.pgNamen(lvid));
    }

    private void sendePGNamen(ObservableList<String> lvPGNamenList){
        JSONArray dateienliste = new JSONArray();
        for(int i=0;i<lvPGNamenList.size();i++){
            dateienliste.put(lvPGNamenList.get(i));
        }
        String jsonString = dateienliste.toString();
        sendenToClient(jsonString);
    }

    private void holeLVDateien(JSONObject jo) throws IOException {
        int lvid = Integer.parseInt( jo.get("P1")+"");
        sendeLVDateien(Datenbank.lvDateien(lvid));
    }

    private void sendeLVDateien(ObservableList<String> lvDateienList){
        JSONArray dateienliste = new JSONArray();
        for(int i=0;i<lvDateienList.size();i++){
            dateienliste.put(lvDateienList.get(i));
        }
        String jsonString = dateienliste.toString();
        sendenToClient(jsonString);
    }
    private void holeLVInfos(JSONObject jo) throws IOException {
        int lvid = Integer.parseInt( jo.get("P1")+"");
        int userid = Integer.parseInt( jo.get("P2")+"");
        sendeLVInfos(Datenbank.lvName(lvid), Datenbank.lvTyp(lvid), Datenbank.lvSemester(lvid),
                Datenbank.checkUserTyp(userid));
    }

    private  void sendeLVInfos(String lvName, String lvTyp, String lvSemester, Boolean userTyp) {
        JSONObject json = new JSONObject();
        json.put("P1", lvName);
        json.put("P2", lvTyp);
        json.put("P3", lvSemester);
        json.put("P4", userTyp);
        String jsonString = json.toJSONString();
        sendenToClient(jsonString);
    }

    private void holePGDownloadDatei(JSONObject jo) throws IOException {
        int pgid = Integer.parseInt( jo.get("P1")+"");
        String datei = (String) jo.get("P2");
        sendePGDownloadDatei(Datenbank.holeDatei(pgid, datei));
    }

    private void sendePGDownloadDatei(String datei){
        JSONObject json = new JSONObject();
        json.put("P1", datei);
        String jsonString = json.toJSONString();
        sendenToClient(jsonString);
    }

    private void holePGDateien(JSONObject jo) throws IOException {
        int pgid = Integer.parseInt( jo.get("P1")+"");
        sendePGDateien(Datenbank.pgDateien(pgid));
    }

    private void sendePGDateien(ObservableList<String> pgDateienList){
        JSONArray dateienliste = new JSONArray();
        for(int i=0;i<pgDateienList.size();i++){
            dateienliste.put(pgDateienList.get(i));
        }
        String jsonString = dateienliste.toString();
        sendenToClient(jsonString);
    }

    private void holePGUser(JSONObject jo) throws IOException {
        int projektid = Integer.parseInt( jo.get("P1")+"");
        sendePGUser(Datenbank.pgUser(projektid));
    }

    private void sendePGUser(ObservableList<Integer> pgUserList){
        JSONArray userliste = new JSONArray();
        for(int i=0;i<pgUserList.size();i++){
            userliste.put(pgUserList.get(i));
        }
        String jsonString = userliste.toString();
        sendenToClient(jsonString);
    }

    private void holePGInfos(JSONObject jo) throws IOException {
        int pgid = Integer.parseInt( jo.get("P1")+"");
        int userid = Integer.parseInt( jo.get("P2")+"");
        sendePGInfos(Datenbank.pgName(pgid), Datenbank.checkUserTyp(userid));
    }

    private void sendePGInfos (String pgName, boolean userTyp){
        JSONObject json = new JSONObject();
        json.put("P1", pgName);
        json.put("P2", userTyp);
        String jsonString = json.toJSONString();
        sendenToClient(jsonString);
    }

    //hier Sebastians Methoden zu Ende


    //Methoden Empfangen
    private void registrierungStudent(JSONObject j) throws IOException {
        String email =(String) j.get("Parameter1");
        String nachname =(String) j.get("Parameter2");
        String passwort =(String) j.get("Parameter3");
        String stadt =(String) j.get("Parameter4");
        String strasse =(String) j.get("Parameter5");
        String vorname =(String) j.get("Parameter6");
        String studienfach= (String) j.get("Parameter7");
        File selectedFile= (File) j.get("Parameter8");
        Registrierung.registrierungStudent(email,nachname,passwort,stadt,strasse,vorname,selectedFile,studienfach);
        Matrikelnummer(email.toLowerCase().replace(" ",""));
    }
    private void RegistrierungLehrender(JSONObject j) throws IOException {
        String email =(String) j.get("Parameter1");
        String passwort =(String) j.get("Parameter2");
        String vorname =(String) j.get("Parameter3");
        String nachname =(String) j.get("Parameter4");
        String stadt =(String) j.get("Parameter5");
        String strasse =(String) j.get("Parameter6");
        File selectedFile= (File) j.get("Parameter7");
        String lehrstuhl =(String) j.get("Parameter8");
        String forschungsgebiet =(String) j.get("Parameter9");
        Registrierung.registrierungLehrender(email,nachname,passwort,stadt,strasse,vorname,selectedFile,lehrstuhl,forschungsgebiet);

    }
    private void Emailvergeben(JSONObject j) throws SQLException {
        String email = (String) j.get("Parameter1");
        System.out.println(email);
        EmailvergebenSendern(Datenbank.Emailvergeben(email));
    }
    private void ProjektUser(JSONObject j) throws SQLException {
        int pgid = Integer.parseInt(j.get("Parameter1")+"");
        pgUserSenden(pgHinzufügen.ProjektUser(pgid));
    }
    private void pgNutzerHinzufuegen(JSONObject j){
        int pgid = Integer.parseInt(j.get("Parameter1")+"");
        int userid =Integer.parseInt(j.get("Parameter2")+"");
        pgHinzufügen.nutzerHinzufügen(pgid,userid);
    }
    private void lehrveranstaltungsMitglieder(JSONObject j) throws SQLException {
        int lvid = Integer.parseInt(j.get("Parameter1")+"");
        String txt = j.get("Parameter2")+"";
        lehrveranstaltungsMitgliederSende(pgHinzufügen.lehrveranstaltungsMitglieder(lvid,txt));

    }
    private void toDoHinzufuegen(JSONObject j) throws SQLException {
        String neuesToDo = j.get("Parameter1")+"";
        int pid = Integer.parseInt(j.get("Parameter2")+"");
        int userid = Integer.parseInt(j.get("Parameter3")+"");
        //pgHinzufügen.toDoHinzufügen(neuesToDo,pid,userid);
        Datenbank.DbToDoPGHinzufuegen(neuesToDo, pid, userid);

    }
    private void toDoLoeschen(JSONObject j) throws SQLException {
        String toDO= j.get("Parameter1")+"";
        int pid = Integer.parseInt(j.get("Parameter2")+"");
        pgHinzufügen.toDoLoeschen(toDO,pid);

    }
    private void todoListeAnzeigen(JSONObject j){
        System.out.println("Bin in todo Angekommen");
        int pid = Integer.parseInt(j.get("Parameter1")+"");
        todoListeAnzeigenSenden(Datenbank.todoListeAnzeigen(pid));
    }
    private void toDoUserListAnzeigen(JSONObject j){
        System.out.println("bin Angekommen");
        int pid = Integer.parseInt(j.get("Parameter1")+"");
        toDoUserListAnzeigenSenden(Datenbank.toDoUserListAnzeigen(pid));

    }
    private void NachrichtSenden(JSONObject j){
        String nachricht= j.get("P1")+"";
        int pgID = Integer.parseInt(j.get("P2")+"");
        int userID = Integer.parseInt(j.get("P3")+"");
        pgHinzufügen.NachrichtSenden(nachricht,pgID,userID);
    }
    private void NachrichtenAnzeigen(JSONObject j){
        int pid = Integer.parseInt(j.get("P1")+"");
        NachrichtenAnzeigenSenden(pgHinzufügen.NachrichtenAnzeigen(pid));
    }
    private void UserAnzeigen(JSONObject j){
        int pid = Integer.parseInt(j.get("P1")+"");
        UserAnzeigenSenden(pgHinzufügen.UserAnzeigen(pid));

    }
    private void LernkarteHinzufuegen(JSONObject j){
        int pid =Integer.parseInt(j.get("P1")+"");
        String name = j.get("P2")+"";
        String frage = j.get("P3")+"";
        String antwort = j.get("P4")+"";
        Lernkarten.LernkarteHinzufuegen(pid,name,frage,antwort);
    }
    private void LernkarteNamenVergeben(JSONObject j){
        int pid = (Integer.parseInt(j.get("P1")+""));
        LernkarteNamenVergebenSenden(Server.Lernkarten.LernkarteNamenVergeben(pid));

    }
    private void LernkartenDropDown(JSONObject j){
        int pid = Integer.parseInt(j.get("P1")+"");
        LernkarteNamenVergebenSenden(Server.Lernkarten.LernkartenDropDown(pid));
    }
    private void LernkartenAnzeigen(JSONObject j){
        int pid = Integer.parseInt(j.get("P1")+"");
        String name =j.get("P2")+"";
        LernkartenAnzeigenSenden(Lernkarten.LernkartenFrage(pid,name));
        LernkartenAnzeigenSenden(Lernkarten.LernkartenAntwort(pid,name));
        System.out.println("LernkartenFrage gesendet");


    }
    //Jans Methoden
    private void ListeAllerLVs(JSONObject j) throws SQLException{
        sendStringList(JanDBMethoden.returnalleLVs());
    }

    private void getLVID(JSONObject j) throws SQLException{
        String lvName = j.get("Parameter1")+"";
        int uID = Integer.parseInt(j.get("Parameter2")+"");
        int lvID = JanDBMethoden.getLVID(lvName);
        Datenbank.DbUserLvHinzufügen(uID,lvID);
    }
    private void alleLVsLehrender(JSONObject j){
        int uID = Integer.parseInt(j.get("Parameter1")+"");
        sendStringList(JanDBMethoden.getLvsLehrender(uID));
    }
    private void searchStudMatrik(JSONObject j){
        int sID = Integer.parseInt(j.get("Parameter1")+"");
        sendStringList(JanDBMethoden.matrikelSuche(sID));
    }
    private void searchVorNach(JSONObject j){
        String sVorname = j.get("Parameter1")+"";
        String sNachname = j.get("Parameter2")+"";
        sendStringList(JanDBMethoden.vorundNachSuche(sVorname,sNachname));
    }
    private void searchVor(JSONObject j){
        String sVorname = j.get("Parameter1")+"";
        sendStringList(JanDBMethoden.vorSuche(sVorname));
    }
    private void searchNach(JSONObject j){
        String sNachname = j.get("Parameter1")+"";
        sendStringList(JanDBMethoden.nachSuche(sNachname));
    }
    private void SucheAdd(JSONObject j){
        String sVorname = j.get("Parameter1")+"";
        String sNachname= j.get("Parameter2")+"";
        String sLVName= j.get("Parameter3")+"";
        JanDBMethoden.addStudentenSuche(sVorname,sNachname,sLVName);
    }
    private void addTerminOhne(JSONObject j){
        String sTerminDatum=j.get("Parameter1")+"";
        String sAnfang=j.get("Parameter2")+"";
        String sDauer=j.get("Parameter3")+"";
        String sDesc=j.get("Parameter4")+"";
        String sLvName=j.get("Parameter5")+"";
        JanDBMethoden.erstelleTerminOhneR(sTerminDatum,sAnfang,sDauer,sDesc,sLvName);
    }
    private void addTerminMit(JSONObject j){
        String sTerminDatum=j.get("Parameter1")+"";
        String sAnfang=j.get("Parameter2")+"";
        String sDauer=j.get("Parameter3")+"";
        String sDesc=j.get("Parameter4")+"";
        String sLvName=j.get("Parameter5")+"";
        String sReminderDatum=j.get("Parameter6")+"";
        String sReminderArt = j.get("Parameter7")+"";
        JanDBMethoden.erstelleTerminMitR(sTerminDatum,sAnfang,sDauer,sDesc,sReminderDatum,sLvName,sReminderArt);
    }
    private void getLVNAME(JSONObject j){
        int id = Integer.parseInt(j.get("Parameter1")+"");
        sendStringList(JanDBMethoden.lVName(id));
    }
    private void getTeilnehmerStudenten(JSONObject j){
        int uID = Integer.parseInt(j.get("Parameter1")+"");
        int lvID= Integer.parseInt(j.get("Parameter2")+"");
        sendStringList(JanDBMethoden.teilnehmerListeStudenten(uID,lvID));
    }
    private void getTeilnehmerLehrende(JSONObject j){
        int uID = Integer.parseInt(j.get("Parameter1")+"");
        int lvID= Integer.parseInt(j.get("Parameter2")+"");
        sendStringList(JanDBMethoden.teilnehmerListeLehrende(uID,lvID));
    }
    private void checkLehrender(JSONObject j){
        int sID = Integer.parseInt(j.get("Parameter1")+"");
        sendStringList(JanDBMethoden.checkLehrender(sID));
    }
    private void tLDoubleClick(JSONObject j){
        String sVorname = j.get("Parameter1")+"";
        String sNachname = j.get("Parameter2")+"";
        int sID = Integer.parseInt(j.get("Parameter3")+"");
        sendStringList(JanDBMethoden.tLDoubleClick(sVorname,sNachname,sID));
    }
    private void getTermine(JSONObject j){
        int sID = Integer.parseInt(j.get("Parameter1")+"");
        sendStringList(JanDBMethoden.KalenderTermineIDs(sID));
    }
    private void addTermin(JSONObject j){
        int tID = Integer.parseInt(j.get("Parameter1")+"");
        sendStringList(JanDBMethoden.terminDaten(tID));
    }
    private void checkReminder(JSONObject j){
        int tID = Integer.parseInt(j.get("Parameter1")+"");
        int uID = Integer.parseInt(j.get("Parameter2")+"");
        sendStringList(JanDBMethoden.KalenderErinnerungswerte(tID,uID));
    }
    private void getThemen(JSONObject j){
        int lID = Integer.parseInt(j.get("Parameter1")+"");
        sendStringList(JanDBMethoden.getThemen(lID));
    }
    private void getThemaIDTitel(JSONObject j){
        String Titel = j.get("Parameter1")+"";
        sendStringList(JanDBMethoden.getThemaIDTitel(Titel));
    }
    private void getThemaDaten(JSONObject j){
        int tID = Integer.parseInt(j.get("Parameter1")+"");
        sendStringList(JanDBMethoden.getThemaDaten(tID));
    }
    private void getLiteratur(JSONObject j){
        int tID = Integer.parseInt(j.get("Parameter1")+"");
        sendStringList(JanDBMethoden.getLiteratur(tID));
    }




    //Methoden senden
    private void Matrikelnummer(String email){
        JSONObject json = new JSONObject();
        json.put("Methode","Matrikelnummer");
        json.put("Parameter1",Datenbank.DbMatrikelnummer(email));
        String jsonString = json.toJSONString();
        sendenToClient(jsonString);
    }

    private void EmailvergebenSendern(ArrayList<String> s){
        JSONArray liste = new JSONArray();
        for(int i=0;i<s.size();i++){
            liste.put(s.get(i));
        }
        String jsonString = liste.toString();
        System.out.println("gesendet");
        sendenToClient(jsonString);
    }
    private void pgUserSenden(ArrayList<Integer> s){
        JSONArray liste = new JSONArray();
        for(int i=0;i<s.size();i++){
            liste.put(s.get(i));
        }
        String jsonString = liste.toString();
        sendenToClient(jsonString);
    }private void LernkartenAnzeigenSenden(ArrayList<String> s){
        JSONArray liste = new JSONArray();
        for(int i=0;i<s.size();i++){
            liste.put(s.get(i));
        }
        String jsonString = liste.toString();
        sendenToClient(jsonString);
    }
    private void lehrveranstaltungsMitgliederSende(ArrayList<String> s){
        JSONArray liste = new JSONArray();
        for(int i=0;i<s.size();i++){
            liste.put(s.get(i));
        }
        String jsonString = liste.toString();
        sendenToClient(jsonString);
    }
    private void todoListeAnzeigenSenden(ArrayList<String> s){
        JSONArray liste = new JSONArray();
        for(int i=0;i<s.size();i++){
            liste.put(s.get(i));
        }
        String jsonString = liste.toString();
        sendenToClient(jsonString);
    }
    private void toDoUserListAnzeigenSenden(ArrayList<Integer> s){
        JSONArray liste = new JSONArray();
        for(int i=0;i<s.size();i++){
            liste.put(s.get(i));
        }
        String jsonString = liste.toString();
        sendenToClient(jsonString);
        System.out.println("gesendet2");

    }
    private void NachrichtenAnzeigenSenden(ArrayList<String> s){
        JSONArray liste = new JSONArray();
        for(int i=0;i<s.size();i++){
            liste.put(s.get(i));
        }
        String jsonString = liste.toString();
        sendenToClient(jsonString);
    }
    private void UserAnzeigenSenden(ArrayList<Integer> s){
        JSONArray liste = new JSONArray();
        for(int i=0;i<s.size();i++){
            liste.put(s.get(i));
        }
        String jsonString = liste.toString();
        sendenToClient(jsonString);
    }
    //Lukas sende
    private void arrayListSendenString (ArrayList<String> s){
        JSONArray liste = new JSONArray();
        for(int i=0;i<s.size();i++){
            liste.put(s.get(i));
        }
        String jsonString = liste.toString();
        sendenToClient(jsonString);
    }
    private void LernkarteNamenVergebenSenden(ArrayList<String> s){
        JSONArray liste = new JSONArray();
        for(int i=0;i<s.size();i++){
            liste.put(s.get(i));
        }
        String jsonString = liste.toString();
        sendenToClient(jsonString);
    }

    private void arrayListSendenInteger (ArrayList<Integer> s){
        JSONArray liste = new JSONArray();
        for(int i=0;i<s.size();i++){
            liste.put(s.get(i));
        }
        String jsonString = liste.toString();
        sendenToClient(jsonString);
    }
    // Jan Senden
    private void sendStringList(ArrayList<String> s){
        JSONArray liste = new JSONArray();
        for(int i = 0;i<s.size();i++){
            liste.put(s.get(i));
        }
        String jsonString = liste.toString();
        sendenToClient(jsonString);
    }
    private void sendTerminList(ArrayList<Termin> zuversenden){
        JSONArray liste = new JSONArray();
        for(int i =0;i<zuversenden.size();i++){
            liste.put(zuversenden.get(i));
        }
        String jsonString = liste.toString();
        sendenToClient(jsonString);
    }







    //wichtige Methoden
    private void sendenToClient(String jsonString){
        System.out.println();
        PrintWriter pw = new PrintWriter(dos);
        pw.println(jsonString);
        pw.flush();
    }

    public static Connection conDB()
    {
        String urlDB ="jdbc:h2:./MavenTest/src/main/java/Server/Datenbank/myDB";
        String user = "sa";
        String passwort = "";
        try
        {
            Connection con = DriverManager.getConnection(urlDB, user, passwort);
            System.out.println("Connection erfolgreich");
            return con;
        } catch (SQLException e) {
            System.err.println("LoginConnection"+e.getMessage());
            return null;
        }
    }
    private void closeConnection(){
        System.out.println("Schließen");
        running =false;
        try{
            socket.close();
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }
}
