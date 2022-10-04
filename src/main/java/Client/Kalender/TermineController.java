package Client.Kalender;

import Server.ClientStart;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TermineController {
    @FXML
    private Label currentIDLabel;
    @FXML
    private DatePicker terminDate;
    @FXML
    private ChoiceBox <String> terminAnfangChoice;
    @FXML
    private ChoiceBox <String> terminDauerChoice;
    @FXML
    private TextField reminderDescText;
    @FXML
    private CheckBox reminderCheck;
    @FXML
    private ChoiceBox <String> reminderArtChoice;
    @FXML
    private DatePicker erinnerungsDate;
    @FXML
    private Button backButton;
    @FXML
    private Button erstellenButton;
    @FXML
    private ChoiceBox <String> lvChoice;

    private int currentUserID;
    private int currentLVID;
    private final String[] erinnerungsarten ={"per Email","per Alert"};
    private final String[] TerminAnfang ={"7:00","8:00","9:00","10:00","11:00","12:00","13:00","14:00","15:00","16:00","17:00","18:00"};
    private final String[] TerminDauer ={"Deadline","30 Minuten","1 Stunde","2 Stunden","3 Stunden","3+Stunden"};

    public void setIDLabel(int pUID) {
        Integer integer = pUID;
        currentIDLabel.setText(integer.toString());
        String test3 = currentIDLabel.getText();
        int test2 = Integer.parseInt(test3);
        currentUserID = test2;
    }
    public void init() throws IOException {
        String test3 = currentIDLabel.getText();
        int test2 = Integer.parseInt(test3);
        currentUserID = test2;
        List<String> LVListe = new ArrayList<>();
        JSONObject json = new JSONObject();
        json.put("Methode","listeAllerLvsLehrender");
        json.put("Parameter1",currentUserID);
        ClientStart.verbinden.send(json);
        System.out.println();
        JSONArray jsonArray = ClientStart.verbinden.receiveary();
        ///immer für String Arrays
        if(jsonArray != null){
            for(int i =0;i<jsonArray.length();i++){
                LVListe.add((String) jsonArray.get(i) +"");
            }
        }
        System.out.println("angekommen");
        lvChoice.getItems().addAll(LVListe); // Hinzufügen aller Lehrveranstaltungen beendet
        reminderArtChoice.getItems().addAll(erinnerungsarten);
        terminAnfangChoice.getItems().addAll(TerminAnfang);
        terminDauerChoice.getItems().addAll(TerminDauer);
    }
    public void backButtonOnAction() throws IOException {
        Stage stage1 = (Stage) backButton.getScene().getWindow();
        stage1.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Calendar.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        CalendarController controller = loader.getController();
        controller.setIDLabel(currentUserID);
        controller.init();
        stage.show();
    }
    public void erstellenButtonOnAction(){
        List<Integer> TeilnehmerIDsList = new ArrayList<>();
        if(lvChoice.getValue()==null || terminDate.getValue()==null|| terminAnfangChoice.getValue()==null||terminDauerChoice.getValue()==null||reminderDescText.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Es wurden nicht alle Werte ausgefüllt");
            alert.setContentText("Bitte wählen sie tragen sie für alle Boxen des Termins einen Wert ein");
            alert.showAndWait();
        }
        else if(!reminderCheck.isSelected()){
            System.out.println("Kein Reminder wird erstellt");
            LocalDate myDate = terminDate.getValue();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/uuuu");
            String terminDatum = dtf.format(myDate);
            System.out.println(terminDatum);
            JSONObject json = new JSONObject();
            json.put("Methode","termineAddOhne");
            json.put("Parameter1",terminDatum);
            json.put("Parameter2",terminAnfangChoice.getValue());
            json.put("Parameter3",terminDauerChoice.getValue());
            json.put("Parameter4",reminderDescText.getText());
            json.put("Parameter5",lvChoice.getValue());
            ClientStart.verbinden.send(json);
            System.out.println("ohneRemindergesendet");
//            Termin neuerTermin = new Termin(terminDatum,terminAnfangChoice.getValue(),terminDauerChoice.getValue(),reminderDescText.getText(),false,"","");
//            Datenbank.DbTerminerstellen(neuerTermin);
//            try{
//                Connection con = ClientThread.conDB();
//                PreparedStatement psl = con.prepareStatement("SELECT * FROM LEHRVERANSTALTUNG WHERE NAME=?");
//                psl.setString(1, lvChoice.getValue());
//                ResultSet rslv = psl.executeQuery();
//                rslv.next();
//                int VeranstaltungsID = rslv.getInt("ID");
//                PreparedStatement ps = con.prepareStatement("SELECT * FROM USERINVERANSTALTUNGEN WHERE VERANSTALTUNGSID=?");
//                ps.setInt(1,VeranstaltungsID);
//                ResultSet rs = ps.executeQuery();
//                while (rs.next()){
//                    TeilnehmerIDsList.add(rs.getInt("USERID"));
//                }
//                PreparedStatement pst = con.prepareStatement("SELECT * FROM TERMIN WHERE DESCRIPTION=?");
//                pst.setString(1,neuerTermin.getDesc());
//                ResultSet tID= pst.executeQuery();
//                tID.next();
//                int TerminID=tID.getInt("ID");
//                for(int i =0;i<TeilnehmerIDsList.size();i++){
//                    Datenbank.DBUserTerminHinzufuegen(TeilnehmerIDsList.get(i),TerminID);
//                }
//            }
//            catch (Exception e){
//                System.out.println(e);
//            }
        }
        else if(reminderCheck.isSelected()){
            if(reminderArtChoice.getValue()==null||erinnerungsDate.getValue()==null){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Es wurden nicht alle Werte ausgefüllt");
                alert.setContentText("Bitte wählen sie tragen sie für alle Boxen des Termins und des Reminders Werte ein");
                alert.showAndWait();
            }
            else{
                System.out.println("Reminder wird erstellt");
                LocalDate myDate = terminDate.getValue();
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/uuuu");
                String terminDatum = dtf.format(myDate);
                System.out.println(terminDatum);
                LocalDate rDate = erinnerungsDate.getValue();
                DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("dd/MM/uuuu");
                String reminderDatum = dtf1.format(rDate);
                System.out.println(reminderDatum);
                JSONObject json = new JSONObject();
                json.put("Methode","termineAddMit");
                json.put("Parameter1",terminDatum);
                json.put("Parameter2",terminAnfangChoice.getValue());
                json.put("Parameter3",terminDauerChoice.getValue());
                json.put("Parameter4",reminderDescText.getText());
                json.put("Parameter5",lvChoice.getValue());
                json.put("Parameter6",reminderDatum);
                json.put("Parameter7",reminderArtChoice.getValue());
                ClientStart.verbinden.send(json);
                System.out.println("Send TerminMit");
//                Termin neuerTermin = new Termin(terminDatum,terminAnfangChoice.getValue(),terminDauerChoice.getValue(),reminderDescText.getText(),true,reminderDatum,reminderArtChoice.getValue());
//                Datenbank.DbTerminerstellen(neuerTermin);
//                try{
//                    Connection con = ClientThread.conDB();
//                    PreparedStatement psl = con.prepareStatement("SELECT * FROM LEHRVERANSTALTUNG WHERE NAME=?");
//                    psl.setString(1, lvChoice.getValue());
//                    ResultSet rslv = psl.executeQuery();
//                    rslv.next();
//                    int VeranstaltungsID = rslv.getInt("ID");
//                    PreparedStatement ps = con.prepareStatement("SELECT * FROM USERINVERANSTALTUNGEN WHERE VERANSTALTUNGSID=?");
//                    ps.setInt(1,VeranstaltungsID);
//                    ResultSet rs = ps.executeQuery();
//                    while (rs.next()){
//                        TeilnehmerIDsList.add(rs.getInt("USERID"));
//                    }
//                    PreparedStatement pst = con.prepareStatement("SELECT * FROM TERMIN WHERE DESCRIPTION=?");
//                    pst.setString(1,neuerTermin.getDesc());
//                    ResultSet tID= pst.executeQuery();
//                    tID.next();
//                    int TerminID=tID.getInt("ID");
//                    for(int i =0;i<TeilnehmerIDsList.size();i++){
//                        Datenbank.DBUserTerminHinzufuegen(TeilnehmerIDsList.get(i),TerminID);
//                    }
//                    con.close();
//                }
//                catch (Exception e){
//                    System.out.println(e);
//                }
            }
        }
    }
}
