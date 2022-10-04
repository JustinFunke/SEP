package Client.Kalender;

import Server.Mail.EmailClient;
import Server.ClientStart;
import Client.javaFx.HauptseiteLehrenderController;
import Client.javaFx.HauptseiteStudentController;

import Server.Mail.EmailDistributor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.simple.JSONObject;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CalendarController {
    @FXML
    private Label currentIDLabel;
    @FXML
    private Button BackButton;
    @FXML
    private Button newTerminButton;
    @FXML
    private Label currentDate;
    @FXML
    private DatePicker dateChanger;
    @FXML
    private TableView tabelle;
    @FXML
    private TableColumn<Termin,String> dateTable ;
    @FXML
    private TableColumn<Termin,String> beginTable;
    @FXML
    private TableColumn<Termin,String> dauerTable;
    @FXML
    private TableColumn<Termin,String> descTable;

    private int currentUserID;
    private int currentLVID;

    public void setIDLabel(int pUID) {
        Integer integer = pUID;
        currentIDLabel.setText(integer.toString());
        String test3 = currentIDLabel.getText();
        int test2 = Integer.parseInt(test3);
        currentUserID = test2;
    }
    public void changeDate(ActionEvent event) throws MessagingException, InterruptedException {
        LocalDate myDate = dateChanger.getValue();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/uuuu");
        currentDate.setText(dtf.format(myDate));


        String mySecondDate = dtf.format(myDate);
        Integer thisYear = myDate.getYear();
        Integer thisYear2 = myDate.getYear()+1;
        String yearson = thisYear.toString();
        String yearson2 = thisYear2.toString();


        if(mySecondDate.equals("30/09/" + yearson))
        {
            System.out.println("Leite SemesterCheck Ein");
            JSONObject check = new JSONObject();
            check.put("Methode","ResultCheck");
            check.put("Semester","SoSe "+ yearson);
            ClientStart.verbinden.send(check);
            TimeUnit.MILLISECONDS.sleep(500);
        }

        else if(mySecondDate.equals("31/03/" + yearson))
        {
            System.out.println("Leite SemesterCheck Ein");
            JSONObject check = new JSONObject();
            check.put("Methode","ResultCheck");
            check.put("Semester","WiSe "+ yearson +"/"+yearson2);
            ClientStart.verbinden.send(check);
            TimeUnit.MILLISECONDS.sleep(500);
        }

        List<Integer> TerminList = new ArrayList<>();

        try {
//            Connection con = ClientThread.conDB();
//            PreparedStatement psL = con.prepareStatement("SELECT * FROM USERINTERMIN WHERE USERID=?");
//            psL.setInt(1,currentUserID);
//            ResultSet rsl = psL.executeQuery();
//            while (rsl.next()){
//                TerminList.add(rsl.getInt("TERMINID"));
//            }
//
//
//            PreparedStatement psT = con.prepareStatement("SELECT * FROM TERMIN WHERE ID=?");
//            for (int i =0;i<TerminList.size();i++){
//                psT.setInt(1,TerminList.get(i));
//                ResultSet rsT = psT.executeQuery();
//                rsT.next();
//                Termin tmpTermin = new Termin(rsT.getString("DATUM"),rsT.getString("ANFANG"), rsT.getString("DAUER"),rsT.getString("DESCRIPTION"),rsT.getBoolean("RVORHANDEN"),"","");
//                Termine.add(tmpTermin);
//            }
            JSONObject jsonTermineAdd = new JSONObject();
            jsonTermineAdd.put("Methode","TerminList");
            jsonTermineAdd.put("Parameter1",currentUserID);
            ClientStart.verbinden.send(jsonTermineAdd);
            JSONArray jsonArrayTermine = ClientStart.verbinden.receiveary();
            if(jsonArrayTermine!=null){
                for(int i = 0;i<jsonArrayTermine.length();i++) {
                    TerminList.add(Integer.parseInt(jsonArrayTermine.get(i)+""));
                }
            }
//            if(!TerminList.isEmpty()){
//                for(int i =0;i<TerminList.size();i++) {
//                    JSONObject jsonAddTermin = new JSONObject();
//                    jsonAddTermin.put("Methode", "AddTermin");
//                    jsonAddTermin.put("Parameter1",TerminList.get(i));
//                    ClientStart.verbinden.send(jsonAddTermin);
//                    JSONArray jsoneinzelnTermin = ClientStart.verbinden.receiveary();
//                    Termin tmpTermin = new Termin(jsoneinzelnTermin.get(0)+"",jsoneinzelnTermin.get(1)+"",jsoneinzelnTermin.get(2)+"",jsoneinzelnTermin.get(3)+"",Boolean.parseBoolean(jsoneinzelnTermin.get(4)+""),"","");
//                    Termine.add(tmpTermin);
//                }
//            }


            for (int k=0;k<TerminList.size();k++){
//                PreparedStatement npsT = con.prepareStatement("SELECT * FROM TERMIN WHERE ID=?");
//                PreparedStatement mail = con.prepareStatement("SELECT * FROM USER WHERE ID=?");
//                npsT.setInt(1,TerminList.get(k));
//                ResultSet rsN = npsT.executeQuery();
//                rsN.next();
//                mail.setInt(1,currentUserID);
//                ResultSet mailSet = mail.executeQuery();
//                mailSet.next();
                JSONObject json = new JSONObject();
                json.put("Methode","KalenderReminderDaten");
                json.put("Parameter1",TerminList.get(k));
                json.put("Parameter2",currentUserID);
                ClientStart.verbinden.send(json);
                JSONArray jsonreminder = ClientStart.verbinden.receiveary();
                Boolean rVorhanden = Boolean.parseBoolean(jsonreminder.get(0)+"");
                String erinnerung = jsonreminder.get(1)+"";
                String erinnerungsart = jsonreminder.get(2)+"";
                String description = jsonreminder.get(3)+"";
                String datum = jsonreminder.get(4)+"";
                String vorname = jsonreminder.get(5)+"";
                String email = jsonreminder.get(6)+"";

                if(rVorhanden&& erinnerung.equals(currentDate.getText())){



                    if(erinnerungsart.equals("per Email")){
                        System.out.println("Erinnerung per Server.Mail");
                        String userMail = email;
                        String mailTitle = "Erinnerung eingestellt!";
                        String termin = description;
                        String datumTermin = datum;
                        String html = "Hallo " +vorname + ", deine Erinnerung für den Termin "+ termin + " am " + datumTermin +" wurde erfolgreich eingestellt";
                        EmailClient.sendAsHtml(userMail, mailTitle, html);
                    }
                    else{
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Warning Dialog");
                        alert.setHeaderText("Erinnerung an Termin");
                        alert.setContentText("Termin: "+ description+" an folgendem Datum: "+datum);
                        alert.showAndWait();
                    }
                }
            }
        }

        catch (MessagingException | IOException throwables) {
            System.out.println(throwables.getMessage());

        }


    }

    public void init(){
        String test3 = currentIDLabel.getText();
        int test2 = Integer.parseInt(test3);
        currentUserID = test2;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/uuuu");
        LocalDate localDate = LocalDate.now();
        currentDate.setText(dtf.format(localDate));
        dateTable.setCellValueFactory(new PropertyValueFactory<Termin,String>("datum"));
        beginTable.setCellValueFactory(new PropertyValueFactory<Termin,String>("anfang"));
        dauerTable.setCellValueFactory(new PropertyValueFactory<Termin,String>("dauer"));
        descTable.setCellValueFactory(new PropertyValueFactory<Termin,String>("desc"));
        List<Integer> TerminList = new ArrayList<>();
        List<Termin> Termine = new ArrayList<>();
        try {
//            Connection con = ClientThread.conDB();
//            PreparedStatement psL = con.prepareStatement("SELECT * FROM USERINTERMIN WHERE USERID=?");
//            psL.setInt(1,currentUserID);
//            ResultSet rsl = psL.executeQuery();
//            while (rsl.next()){
//                TerminList.add(rsl.getInt("TERMINID"));
//            }
//
//
//            PreparedStatement psT = con.prepareStatement("SELECT * FROM TERMIN WHERE ID=?");
//            for (int i =0;i<TerminList.size();i++){
//                psT.setInt(1,TerminList.get(i));
//                ResultSet rsT = psT.executeQuery();
//                rsT.next();
//                Termin tmpTermin = new Termin(rsT.getString("DATUM"),rsT.getString("ANFANG"), rsT.getString("DAUER"),rsT.getString("DESCRIPTION"),rsT.getBoolean("RVORHANDEN"),"","");
//                Termine.add(tmpTermin);
//            }
            JSONObject jsonTermineAdd = new JSONObject();
            jsonTermineAdd.put("Methode","TerminList");
            jsonTermineAdd.put("Parameter1",currentUserID);
            ClientStart.verbinden.send(jsonTermineAdd);
            JSONArray jsonArrayTermine = ClientStart.verbinden.receiveary();
            if(jsonArrayTermine!=null){
                for(int i = 0;i<jsonArrayTermine.length();i++) {
                    TerminList.add(Integer.parseInt(jsonArrayTermine.get(i)+""));
                }
            }
            if(!TerminList.isEmpty()){
                for(int i =0;i<TerminList.size();i++) {
                    JSONObject jsonAddTermin = new JSONObject();
                    jsonAddTermin.put("Methode", "AddTermin");
                    jsonAddTermin.put("Parameter1",TerminList.get(i));
                    ClientStart.verbinden.send(jsonAddTermin);
                    JSONArray jsoneinzelnTermin = ClientStart.verbinden.receiveary();
                    Termin tmpTermin = new Termin(jsoneinzelnTermin.get(0)+"",jsoneinzelnTermin.get(1)+"",jsoneinzelnTermin.get(2)+"",jsoneinzelnTermin.get(3)+"",Boolean.parseBoolean(jsoneinzelnTermin.get(4)+""),"","");
                    Termine.add(tmpTermin);
                }
            }
            if(!Termine.isEmpty()){
                for(int j = 0;j<Termine.size();j++){
                    tabelle.getItems().add(Termine.get(j));
                }
            }


            for (int k=0;k<TerminList.size();k++){
//                PreparedStatement npsT = con.prepareStatement("SELECT * FROM TERMIN WHERE ID=?");
//                PreparedStatement mail = con.prepareStatement("SELECT * FROM USER WHERE ID=?");
//                npsT.setInt(1,TerminList.get(k));
//                ResultSet rsN = npsT.executeQuery();
//                rsN.next();
//                mail.setInt(1,currentUserID);
//                ResultSet mailSet = mail.executeQuery();
//                mailSet.next();
                JSONObject json = new JSONObject();
                json.put("Methode","KalenderReminderDaten");
                json.put("Parameter1",TerminList.get(k));
                json.put("Parameter2",currentUserID);
                ClientStart.verbinden.send(json);
                JSONArray jsonreminder = ClientStart.verbinden.receiveary();
                Boolean rVorhanden = Boolean.parseBoolean(jsonreminder.get(0)+"");
                String erinnerung = jsonreminder.get(1)+"";
                String erinnerungsart = jsonreminder.get(2)+"";
                String description = jsonreminder.get(3)+"";
                String datum = jsonreminder.get(4)+"";
                String vorname = jsonreminder.get(5)+"";
                String email = jsonreminder.get(6)+"";
                if(rVorhanden&& erinnerung.equals(currentDate.getText())){
                    if(erinnerungsart.equals("per Email")){
                        System.out.println("Erinnerung per Server.Mail");
                        String userMail = email;
                        String mailTitle = "Erinnerung eingestellt!";
                        String termin = description;
                        String datumTermin = datum;
                        String html = "Hallo " +vorname + ", deine Erinnerung für den Termin "+ termin + " am " + datumTermin +" wurde erfolgreich eingestellt";
                        EmailClient.sendAsHtml(userMail, mailTitle, html);
                    }
                    else{
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Warning Dialog");
                        alert.setHeaderText("Erinnerung an Termin");
                        alert.setContentText("Termin: "+ description+" an folgendem Datum: "+datum);
                        alert.showAndWait();
                    }
                }
            }
        }

        catch (MessagingException | IOException throwables) {
            System.out.println(throwables.getMessage());

        }
    }
    public void createTermin() throws IOException {
        Boolean LehrenderAusgabe = false;
        ArrayList<String> tmp = new ArrayList<>();
        JSONObject json = new JSONObject();
        json.put("Methode","checkLehrender");
        json.put("Parameter1",currentUserID);
        ClientStart.verbinden.send(json);
        JSONArray jsonArray1 = ClientStart.verbinden.receiveary();
        ///immer für String Arrays
        if(jsonArray1 != null) {
            for (int i = 0; i < jsonArray1.length(); i++) {
                tmp.add((String) jsonArray1.get(i) + "");
            }
        }
        LehrenderAusgabe= Boolean.parseBoolean(tmp.get(0));
        if(LehrenderAusgabe){
            Stage stage1 = (Stage) BackButton.getScene().getWindow();
            stage1.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/TermineErstellen.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            TermineController Terminecontroller = loader.getController();
            Terminecontroller.setIDLabel(currentUserID);
            Terminecontroller.init();
            stage.show();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Sie dürfen keine Termine erstellen");
            alert.setContentText("Nur Lehrende können Termine erstellen");
            alert.showAndWait();
        }
    }

    public void BackButtonOnAction(ActionEvent event) throws IOException {
        Boolean LehrenderAusgabe = false;
        ArrayList<String> tmp = new ArrayList<>();
        JSONObject json = new JSONObject();
        json.put("Methode","checkLehrender");
        json.put("Parameter1",currentUserID);
        ClientStart.verbinden.send(json);
        JSONArray jsonArray1 = ClientStart.verbinden.receiveary();
        ///immer für String Arrays
        if(jsonArray1 != null) {
            for (int i = 0; i < jsonArray1.length(); i++) {
                tmp.add((String) jsonArray1.get(i) + "");
            }
        }
//        try{
//            Connection con = DriverManager.getConnection(urlDB,user,passwort);
//            Statement st = con.createStatement();
//            PreparedStatement ps = con.prepareStatement("SELECT LEHRENDER FROM USER WHERE ID=?");
//            ps.setInt(1,currentUserID);
//            ResultSet rs = ps.executeQuery();
//            rs.next();
//            LehrenderAusgabe = rs.getBoolean("LEHRENDER");
//            System.out.println(LehrenderAusgabe);
//            con.close();
//        }
//
//        catch (SQLException throwables){
//            System.out.println(throwables.getMessage());
//
//        }
        LehrenderAusgabe= Boolean.parseBoolean(tmp.get(0));
        if(LehrenderAusgabe){
            Stage stage1 = (Stage) BackButton.getScene().getWindow();
            stage1.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Hauptseite(fürLehrender).fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            HauptseiteLehrenderController LehrenderMaincontroller = loader.getController();
            LehrenderMaincontroller.setIDLabel(currentUserID);
            LehrenderMaincontroller.init();
            stage.show();
        }
        else {
            Stage stage1 = (Stage) BackButton.getScene().getWindow();
            stage1.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Hauptseite(fürStudent).fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            HauptseiteStudentController StudentMaincontroller = loader.getController();
            StudentMaincontroller.setIDLabel(currentUserID);
            StudentMaincontroller.init();
            stage.show();

        }
    }
}
