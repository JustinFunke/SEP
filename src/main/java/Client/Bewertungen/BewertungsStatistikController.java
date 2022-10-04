package Client.Bewertungen;

import Client.Lehrveranstaltung.LVUebersichtsseiteController;
import Server.ClientStart;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.simple.JSONObject;

import javax.print.attribute.HashDocAttributeSet;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class BewertungsStatistikController {

    @FXML
    private Pane pane;

    @FXML
    private ListView<String> listViewBeantwortung;

    @FXML
    private Label labelSatistikFilter;

    @FXML
    private CheckBox checkBoxAlle;

    @FXML
    private CheckBox checkBoxBestanden;

    @FXML
    private CheckBox checkBoxDurchgefallen;

    private int currentUserID;
    private int currentLVID;
    private int bewertungsID;
    private Stage stage;

    //Zahl von 1 bis 4
    public static String antwortUmwandeln(int zahl){
        String antwort = "";
        if (zahl == 1){
            antwort = "Stimme gar nicht zu";
        }
        else if (zahl == 2){
            antwort = "Stimme kaum zu";
        }
        else if (zahl == 3){
            antwort = "Stimme teilweise zu";
        }
        else if (zahl == 4){
            antwort = "Stimme voll zu";
        }
        else {
            antwort = "Keine Antwort ausgewählt";
        }
        return antwort;
    }

    @FXML
    void cmdBack(ActionEvent event) throws IOException, SQLException {
        stage = (Stage) pane.getScene().getWindow();
        stage.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/LVUebersichtsseite.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        LVUebersichtsseiteController controller = loader.getController();
        controller.setIDLabel(currentUserID);
        controller.setLVIDLabel(currentLVID);
        controller.init();
        stage.show();
    }

    @FXML
    void cmdCheckBoxAlle(ActionEvent event) {
        checkBoxAlle.setSelected(true);
        checkBoxBestanden.setSelected(false);
        checkBoxDurchgefallen.setSelected(false);
    }

    @FXML
    void cmdCheckBoxBestanden(ActionEvent event) {
        checkBoxAlle.setSelected(false);
        checkBoxBestanden.setSelected(true);
        checkBoxDurchgefallen.setSelected(false);
    }

    @FXML
    void cmdCheckBoxDurchgefallen(ActionEvent event) {
        checkBoxAlle.setSelected(false);
        checkBoxBestanden.setSelected(false);
        checkBoxDurchgefallen.setSelected(true);
    }

    @FXML
    void cmdStatistikWechseln(ActionEvent event) throws IOException{
        if (checkBoxAlle.isSelected()){
            showAlle();
            labelSatistikFilter.setText("Alle Studenten:");
        }
        else if(checkBoxBestanden.isSelected()){
            showBestanden();
            labelSatistikFilter.setText("Studenten die bestanden haben:");
        }
        else if(checkBoxDurchgefallen.isSelected()){
            showDurchgefallen();
            labelSatistikFilter.setText("Studenten die durchgefallen sind:");
        }
    }

    public void showAlle() throws IOException {
        ArrayList<String> studentenIDs = new ArrayList<>();
        ArrayList<Integer> fragen = new ArrayList<>();
        String antwort = "";
        //kriege Liste mit allen IDs von Studenten, welche die LV bestanden haben
        JSONObject json = new JSONObject();
        json.put("Methode","alleDieTeilnehmen");
        json.put("P1",currentLVID);
        ClientStart.verbinden.send(json);
        JSONArray jsonArray = ClientStart.verbinden.receiveary();
        for(int i=0; i<jsonArray.length(); i++){
            studentenIDs.add(jsonArray.getString(i));
        }
        try{
            TimeUnit.MILLISECONDS.sleep(200);
        }
        catch(Exception e){
            System.out.println(e);
        }

        System.out.println(studentenIDs);

        //Kriege die BewertungsfragenIDs als Liste
        JSONObject json4 = new JSONObject();
        json4.put("Methode","getIDsVonFragen");
        json4.put("P1",bewertungsID);
        ClientStart.verbinden.send(json4);
        JSONArray jsonArray3 = ClientStart.verbinden.receiveary();
        for(int i=0; i<jsonArray3.length(); i++){
            fragen.add(jsonArray3.getInt(i));
        }

        System.out.println(fragen);

        //hol Anzahl an Antworten für jede Frage NOCH NICHT FERTIG
        JSONArray jsonArraySenden = new JSONArray();
        for(int i=0; i<studentenIDs.size();i++){
            jsonArraySenden.put(studentenIDs.get(i));
        }
        String jsonString3 = jsonArraySenden.toString();
        JSONObject json3 = new JSONObject();
        listViewBeantwortung.getItems().clear();
        for(int i=0; i<fragen.size(); i++){
            json3.put("Methode","countAntwortenVonUsern");
            json3.put("P1",fragen.get(i));
            json3.put("P2",jsonString3);
            ClientStart.verbinden.send(json3);
            JSONArray jsonArray2 = ClientStart.verbinden.receiveary();
            //endergebnis.add(jsonArray2.getString(i));
            antwort = "Frage "+ (i+1) + ":\n"
                    + "Stimme gar nicht zu: " + jsonArray2.getInt(0) + "\n"
                    + "Stimme kaum zu: " + jsonArray2.getInt(1) + "\n"
                    + "Stimme teilweise zu: " + jsonArray2.getInt(2) + "\n"
                    + "Stimme voll zu: "+ jsonArray2.getInt(3) + "\n";
            listViewBeantwortung.getItems().add(antwort);

            System.out.println(jsonArray2);

            try{
                TimeUnit.MILLISECONDS.sleep(200);
            }
            catch(Exception e){
                System.out.println(e);
            }
        }

//        ArrayList<Integer> count = new ArrayList<>();
//            for(int i=0; i<fragen.size(); i++) {
//        count = countAntwortenVonUserList(fragen.get(i), studentenIDs);
//        System.out.println(count);
//        antwort = "Frage " + (i + 1) + ":\n"
//                + "Stimme gar nicht zu: " + count.get(0) + "\n"
//                + "Stimme kaum zu: " + count.get(1) + "\n"
//                + "Stimme teilweise zu: " + count.get(2) + "\n"
//                + "Stimme voll zu: " + count.get(3) + "\n";
//        listViewBeantwortung.getItems().add(antwort);
//        }
    }
    public void showBestanden() throws IOException {
        int anzahlBewertungsfragen = 0;
        ArrayList<String> bestandenIDs = new ArrayList<>();
        ArrayList<Integer> fragen = new ArrayList<>();
        String antwort = "";
        //kriege Liste mit allen IDs von Studenten, welche die LV bestanden haben
        JSONObject json = new JSONObject();
        json.put("Methode","alleDieBestanden");
        json.put("P1",currentLVID);
        ClientStart.verbinden.send(json);
        JSONArray jsonArray = ClientStart.verbinden.receiveary();
        for(int i=0; i<jsonArray.length(); i++){
            bestandenIDs.add(jsonArray.getString(i));
        }
        try{
            TimeUnit.MILLISECONDS.sleep(200);
        }
        catch(Exception e){
            System.out.println(e);
        }
        //kriege die Anzahl an Bewertungsfragen
        /*JSONObject json2 = new JSONObject();
        json2.put("Methode","anzahlBewertungsfragen");
        json2.put("P1",currentLVID);
        ClientStart.verbinden.send(json2);
        JSONObject jsonReceive = ClientStart.verbinden.receiveobj();
        String jsonString = jsonReceive.get("P1")+"";
        anzahlBewertungsfragen = Integer.parseInt(jsonString);*/
        //Kriege die BewertungsfragenIDs als Liste
        JSONObject json4 = new JSONObject();
        json4.put("Methode","getIDsVonFragen");
        json4.put("P1",bewertungsID);
        ClientStart.verbinden.send(json4);
        JSONArray jsonArray3 = ClientStart.verbinden.receiveary();
        for(int i=0; i<jsonArray3.length(); i++){
            fragen.add(jsonArray3.getInt(i));
        }
        try{
            TimeUnit.MILLISECONDS.sleep(150);
        }
        catch(Exception e){
            System.out.println(e);
        }
        //hol Anzahl an Antworten für jede Frage NOCH NICHT FERTIG
        JSONArray jsonArraySenden = new JSONArray();
        for(int i=0; i<bestandenIDs.size();i++){
            jsonArraySenden.put(bestandenIDs.get(i));
        }
        String jsonString3 = jsonArraySenden.toString();
        JSONObject json3 = new JSONObject();
        listViewBeantwortung.getItems().clear();
        for(int i=0; i<fragen.size(); i++){
            json3.put("Methode","countAntwortenVonUsern");
            json3.put("P1",fragen.get(i));
            json3.put("P2",jsonString3);
            ClientStart.verbinden.send(json3);
            JSONArray jsonArray2 = ClientStart.verbinden.receiveary();
            //endergebnis.add(jsonArray2.getString(i));
            antwort = "Frage "+ (i+1) + ":\n"
                    + "Stimme gar nicht zu: " + jsonArray2.getInt(0) + "\n"
                    + "Stimme kaum zu: " + jsonArray2.getInt(1) + "\n"
                    + "Stimme teilweise zu: " + jsonArray2.getInt(2) + "\n"
                    + "Stimme voll zu: "+ jsonArray2.getInt(3) + "\n";
            listViewBeantwortung.getItems().add(antwort);
            try{
                TimeUnit.MILLISECONDS.sleep(200);
            }
            catch(Exception e){
                System.out.println(e);
            }
        }
    }
    public void showDurchgefallen() throws IOException {
        int anzahlBewertungsfragen = 0;
        ArrayList<String> durchgefallenIDs = new ArrayList<>();
        ArrayList<Integer> fragen = new ArrayList<>();
        String antwort = "";
        //kriege Liste mit allen IDs von Studenten, welche die LV bestanden haben
        JSONObject json = new JSONObject();
        json.put("Methode","alleDieDurchgefallen");
        json.put("P1",currentLVID);
        ClientStart.verbinden.send(json);
        JSONArray jsonArray = ClientStart.verbinden.receiveary();
        for(int i=0; i<jsonArray.length(); i++){
            durchgefallenIDs.add(jsonArray.getString(i));
        }
        try{
            TimeUnit.MILLISECONDS.sleep(200);
        }
        catch(Exception e){
            System.out.println(e);
        }
        //kriege die Anzahl an Bewertungsfragen
        /*JSONObject json2 = new JSONObject();
        json2.put("Methode","anzahlBewertungsfragen");
        json2.put("P1",currentLVID);
        ClientStart.verbinden.send(json2);
        JSONObject jsonReceive = ClientStart.verbinden.receiveobj();
        String jsonString = jsonReceive.get("P1")+"";
        anzahlBewertungsfragen = Integer.parseInt(jsonString);*/
        //Kriege die BewertungsfragenIDs als Liste
        JSONObject json4 = new JSONObject();
        json4.put("Methode","getIDsVonFragen");
        json4.put("P1",bewertungsID);
        ClientStart.verbinden.send(json4);
        JSONArray jsonArray3 = ClientStart.verbinden.receiveary();
        for(int i=0; i<jsonArray3.length(); i++){
            fragen.add(jsonArray3.getInt(i));
        }
        try{
            TimeUnit.MILLISECONDS.sleep(100);
        }
        catch(Exception e){
            System.out.println(e);
        }
        //hol Anzahl an Antworten für jede Frage NOCH NICHT FERTIG
        JSONArray jsonArraySenden = new JSONArray();
        for(int i=0; i<durchgefallenIDs.size();i++){
            jsonArraySenden.put(durchgefallenIDs.get(i));
        }
        String jsonString3 = jsonArraySenden.toString();
        JSONObject json3 = new JSONObject();
        listViewBeantwortung.getItems().clear();
        for(int i=0; i<fragen.size(); i++){
            json3.put("Methode","countAntwortenVonUsern");
            json3.put("P1",fragen.get(i));
            json3.put("P2",jsonString3);
            ClientStart.verbinden.send(json3);
            JSONArray jsonArray2 = ClientStart.verbinden.receiveary();
            antwort = "Frage "+ (i+1) + ":\n"
                    + "Stimme gar nicht zu: " + jsonArray2.getInt(0) + "\n"
                    + "Stimme kaum zu: " + jsonArray2.getInt(1) + "\n"
                    + "Stimme teilweise zu: " + jsonArray2.getInt(2) + "\n"
                    + "Stimme voll zu: "+ jsonArray2.getInt(3) + "\n";
            listViewBeantwortung.getItems().add(antwort);
            try{
                TimeUnit.MILLISECONDS.sleep(200);
            }
            catch(Exception e){
                System.out.println(e);
            }
        }
    }

    public ArrayList<Integer> countAntwortenVonUserList(int frageID ,ArrayList<String> userIDs) throws IOException{
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
        for(int i = 0;i<userIDs.size();i++){
            userID = Integer.parseInt(userIDs.get(i));
            //methodeCount = countAntwortenVonUser(frageID , userID);
            JSONObject json = new JSONObject();
            json.put("Methode","countAntwortenVonEinem");
            json.put("P1",frageID);
            json.put("P2",currentUserID);
            ClientStart.verbinden.send(json);
            JSONArray jsonArray = ClientStart.verbinden.receiveary();
            for(int j=0; j<jsonArray.length(); j++){
                methodeCount.add(jsonArray.getInt(j));
            }
            countAntworten.set(0, countAntworten.get(0) + methodeCount.get(0));
            countAntworten.set(1, countAntworten.get(1) + methodeCount.get(1));
            countAntworten.set(2, countAntworten.get(2) + methodeCount.get(2));
            countAntworten.set(3, countAntworten.get(3) + methodeCount.get(3));
            try{
                TimeUnit.MILLISECONDS.sleep(200);
            }
            catch(Exception e){
                System.out.println(e);
            }
        }
        return countAntworten;
    }


    public void init(int bewertungsID, int currentUserID, int currentLVID) throws IOException {
        this.bewertungsID = bewertungsID;
        this.currentLVID = currentLVID;
        this.currentUserID = currentUserID;
        checkBoxAlle.setSelected(true);
        checkBoxBestanden.setSelected(false);
        checkBoxDurchgefallen.setSelected(false);
        showAlle();
    }

}
