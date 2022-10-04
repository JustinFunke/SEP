package Client.FreundeVerwalten;

import Server.ClientStart;
import Client.javaFx.HauptseiteStudentController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.json.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.*;

public class PrivateChatsController {

    @FXML
    private Pane pane;
    @FXML
    private TextField txtMessage;
    @FXML
    private TextArea txtChat;
    @FXML
    private Label currentIDLabel;
    @FXML
    private ChoiceBox<String> choiceBoxNeuerChat;
    @FXML
    private ListView<String> listFreunde;
    @FXML
    private Text chattext;


    private Stage stage;
    private Parent root;
    private Scene scene;
    private int currentUserID;
    //private Timer timer = new Timer();
    TimerTask refreshTask = new TimerTask(){public void run(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                System.out.println("Timer Läuft");
                try {
                    chatrefresh();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("läuft");

            }
        });

    }};
    Timer refresh= new Timer();

    int getFreundID () throws IOException {
        String chat = chattext.getText();
        int freundid = 0;
        if (!chat.equals("Chat") && !chat.equals("")) {
            String freund = chattext.getText();
            String vorname = "";
            String nachname = "";
            System.out.println(freund);
            String[] parts = freund.split(" ");
            vorname = parts[0];
            nachname = parts[1];
            JSONObject json = new JSONObject();
            json.put("Methode","getUserID");
            json.put("P1",vorname);
            json.put("P2",nachname);
            ClientStart.verbinden.send(json);
            JSONObject jsonReturn = ClientStart.verbinden.receiveobj();
            String freundesid = jsonReturn.get("P1")+"";
            if(freundesid != null && !freundesid.equals("")){
                freundid = Integer.parseInt(freundesid);
            }
        }
//            try {
//                String freund = chattext.getText();
//                String vorname = "";
//                String nachname = "";
//                System.out.println(freund);
//                String[] parts = freund.split(" ");
//                vorname = parts[0];
//                nachname = parts[1];
//                Connection con = DriverManager.getConnection(urlDB, user, passwort);
//                PreparedStatement ps = con.prepareStatement("SELECT * FROM USER WHERE VORNAME=? AND NACHNAME=?");
//                ps.setString(1, vorname);
//                ps.setString(2, nachname);
//                ResultSet rs = ps.executeQuery();
//                while (rs.next()) {
//                    freundid = rs.getInt("ID");
//                }
//                con.close();
//            } catch (SQLException throwables) {
//                System.out.println(throwables.getMessage());
//            }
//        }
        return freundid;
    }

    void chatrefresh() throws IOException {
        String chat = chattext.getText();
        int freundid = getFreundID();
        if (!chat.equals("Chat") && freundid != 0) {
            List<Integer> ListeIDS = new ArrayList<>();
            List<String> ListeIDString = new ArrayList<>();
            List<String> ListeNachrichten = new ArrayList<>();
            JSONObject json = new JSONObject();
            json.put("Methode","getChatIDs");
            json.put("P1",currentUserID);
            json.put("P2",freundid);
            ClientStart.verbinden.send(json);
            JSONArray jsonArray = ClientStart.verbinden.receiveary();
            for(int i=0; i<jsonArray.length(); i++){
                ListeIDS.add(jsonArray.getInt(i));
            }
            Collections.sort(ListeIDS);
            System.out.println(ListeIDS);

            JSONArray jsonArraySenden = new JSONArray();

            for(int i=0; i<ListeIDS.size();i++){
                ListeIDString.add(String.valueOf(ListeIDS.get(i)));
            }
            for(int i=0; i<ListeIDString.size();i++){
                jsonArraySenden.put(ListeIDString.get(i));
            }
            String jsonString1 = jsonArraySenden.toString();
            JSONObject json1 = new JSONObject();
            json1.put("Methode","getChatNachrichten");
            json1.put("P1",jsonString1);
            ClientStart.verbinden.send(json1);
            JSONArray jsonArray2 = ClientStart.verbinden.receiveary();
            for(int i=0; i<jsonArray.length(); i++){
                ListeNachrichten.add(jsonArray2.getString(i));
            }
            System.out.println(ListeNachrichten);
            txtChat.clear();
            for (int i = 0;i<ListeNachrichten.size();i++) {
                if(!txtChat.getText().equals("")){
                    txtChat.setText(txtChat.getText() + "\n" + ListeNachrichten.get(i));
                }
                else{
                    txtChat.setText(ListeNachrichten.get(i));
                }
            }
//            try {
//                Connection con = DriverManager.getConnection(urlDB, user, passwort);
//                PreparedStatement ps = con.prepareStatement("SELECT * FROM PRIVATCHAT WHERE ID1=? AND ID2=?");
//                ps.setInt(1, currentUserID);
//                ps.setInt(2, freundid);
//                ResultSet rs = ps.executeQuery();
//                while (rs.next()){
//                    ListeIDS.add(rs.getInt("CHATID"));
//                }
//                ps = con.prepareStatement("SELECT * FROM PRIVATCHAT WHERE ID1=? AND ID2=?");
//                ps.setInt(1, freundid);
//                ps.setInt(2, currentUserID);
//                rs = ps.executeQuery();
//                while (rs.next()){
//                    ListeIDS.add(rs.getInt("CHATID"));
//                }
//                Collections.sort(ListeIDS);
//                System.out.println(ListeIDS);
//                for (int i = 0;i<ListeIDS.size();i++) {
//                    ps = con.prepareStatement("SELECT * FROM PRIVATCHAT WHERE CHATID = ?");
//                    ps.setInt(1, ListeIDS.get(i));
//                    rs = ps.executeQuery();
//                    while (rs.next()) {
//                        UserIDS.add(rs.getInt("ID1"));
//                        ListeNachrichten.add(rs.getString("NACHRICHT"));
//                    }
//                }
//                for (int i = 0;i<ListeIDS.size();i++) {
//                    ps = con.prepareStatement("SELECT * FROM USER WHERE ID = ?");
//                    ps.setInt(1, UserIDS.get(i));
//                    rs = ps.executeQuery();
//                    while (rs.next()) {
//                        ListeNachrichten2.add(rs.getString("Vorname") +" "+rs.getString("Nachname") + ": " + ListeNachrichten.get(i));
//                    }
//                }
//                System.out.println(ListeNachrichten2);
//                txtChat.clear();
//                for (int i = 0;i<ListeNachrichten2.size();i++) {
//                    if(!txtChat.getText().equals("")){
//                        txtChat.setText(txtChat.getText() + "\n" + ListeNachrichten2.get(i));
//                    }
//                    else{
//                        txtChat.setText(ListeNachrichten2.get(i));
//                    }
//                }
//                txtChat.textProperty().addListener(new ChangeListener<Object>() {
//                    @Override
//                    public void changed(ObservableValue<?> observable, Object oldValue,
//                                        Object newValue) {
//                        txtChat.setScrollTop(Double.MAX_VALUE);
//                    }
//                });
//
//                //txtChat.positionCaret(txtChat.getLength());
//
//                con.close();
//            }
//            catch (SQLException throwables) {
//                System.out.println(throwables.getMessage());
//            }
        }
    }

    @FXML
    void cmdNeuerChat(ActionEvent event) throws IOException {
        String name = choiceBoxNeuerChat.getValue();
        System.out.println(name);
        if (name != null) {
            chattext.setText(name);
        }
        chatrefresh();
    }

    @FXML
    void cmdSend(ActionEvent event) throws IOException {
        String nachricht = txtMessage.getText();
        String chat = chattext.getText();
        int freundid = 0;
        if (!txtMessage.getText().equals("") && !chat.equals("Chat")) {
            freundid = getFreundID();
            if (freundid != 0) {
                JSONObject json = new JSONObject();
                json.put("Methode","insertIntoPC");
                json.put("P1",currentUserID);
                json.put("P2",freundid);
                json.put("P3",nachricht);
                ClientStart.verbinden.send(json);
                chatrefresh();

            }
//            try{
//                String freund = chattext.getText();
//                String[] parts = freund.split(" ");
//                String vorname = parts[0];
//                String nachname = parts[1];
//                Connection con = DriverManager.getConnection(urlDB, user, passwort);
//                PreparedStatement ps = con.prepareStatement("SELECT * FROM USER WHERE VORNAME=? AND NACHNAME=?");
//                ps.setString(1,vorname);
//                ps.setString(2,nachname);
//                ResultSet rs = ps.executeQuery();
//                while (rs.next()){
//                    freundid = rs.getInt("ID");
//                }
//                if (freundid != 0) {
//                    ps = con.prepareStatement("INSERT INTO PRIVATCHAT (ID1, ID2, NACHRICHT) VALUES (?, ?, ?)");
//                    ps.setInt(1, currentUserID);
//                    ps.setInt(2, freundid);
//                    ps.setString(3,nachricht);
//                    ps.executeUpdate();
//                    System.out.println("Nachricht versendet");
//                }
//                chatrefresh();
//                con.close();
//            }
//            catch (SQLException throwables) {
//                System.out.println(throwables.getMessage());
//            }
        }
        txtMessage.setText("");
    }

    @FXML
    void cmdBack(ActionEvent event) throws IOException {
        //Zurück zur Hauptseite
        stage = (Stage) pane.getScene().getWindow();
        stage.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Hauptseite(fürStudent).fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        HauptseiteStudentController controller = loader.getController();
        controller.setIDLabel(currentUserID);
        controller.init();
        stage.show();
        refreshTask.cancel();
    }

    @FXML
    void cmdFriendsClick(MouseEvent click) throws IOException {
        if (click.getClickCount() == 2) {
            String ausgewähltesItem = listFreunde.getSelectionModel().getSelectedItem();
            if (ausgewähltesItem != null) {
                chattext.setText(ausgewähltesItem);
            }
        }
        chatrefresh();
    }

    public void setIDLabel(int test){
        Integer integer = test;
        currentIDLabel.setText(integer.toString());
    }
    public void init() throws IOException{
        String test3 = currentIDLabel.getText();
        int test2 = Integer.parseInt(test3);
        currentUserID = test2;

        //choicebox füllen
        List<String> freundeNamen = new ArrayList<>();
        List<String> zwischenergebnis = new ArrayList<>();
        List<String> ergebnis = new ArrayList<>();
        List<String> freundeIDs = new ArrayList<>();
        JSONObject json = new JSONObject();
        json.put("Methode","getGleicheVeranstaltungLeute");
        json.put("P1",currentUserID);
        ClientStart.verbinden.send(json);
        JSONArray jsonArray = ClientStart.verbinden.receiveary();
        for(int i=0; i<jsonArray.length(); i++){
            zwischenergebnis.add(jsonArray.getString(i));
        }
        JSONObject json1 = new JSONObject();
        json1.put("Methode","getFreundeVonUser");
        json1.put("P1",currentUserID);
        ClientStart.verbinden.send(json1);
        JSONArray jsonArray1 = ClientStart.verbinden.receiveary();
        for(int i=0; i<jsonArray1.length(); i++){
            freundeIDs.add(jsonArray1.getString(i));
        }
        //https://stackoverflow.com/questions/15575417/how-to-remove-common-values-from-two-array-lists
        // Prepare a union
        List<String> union = new ArrayList<String>(zwischenergebnis);
        union.addAll(freundeIDs);
        // Prepare an intersection
        List<String> intersection = new ArrayList<String>(freundeIDs);
        intersection.retainAll(zwischenergebnis);
        // Subtract the intersection from the union
        union.removeAll(intersection);

        JSONArray jsonArraySenden = new JSONArray();
        for(int i=0; i<union.size();i++){
            jsonArraySenden.put(union.get(i));
        }
        String jsonString = jsonArraySenden.toString();
        JSONObject json2 = new JSONObject();
        json2.put("Methode","getNamenVonIDs");
        json2.put("P1",currentUserID);
        json2.put("P2",jsonString);
        ClientStart.verbinden.send(json2);
        JSONArray jsonArray2 = ClientStart.verbinden.receiveary();
        for(int i=0; i<jsonArray2.length(); i++){
            ergebnis.add(jsonArray2.getString(i));
        }
        Set<String> set = new HashSet<>(ergebnis);
        ergebnis.clear();
        ergebnis.addAll(set);
        choiceBoxNeuerChat.getItems().clear();
        Collections.sort(ergebnis); //sortieren
        choiceBoxNeuerChat.getItems().addAll(ergebnis);
        if (!choiceBoxNeuerChat.getItems().isEmpty()) {
            choiceBoxNeuerChat.setValue(choiceBoxNeuerChat.getItems().get(0)); //setze Default Value auf den ersten Eintrag
        }

        //Freundesliste füllen
        JSONArray jsonArraySenden1 = new JSONArray();
        for(int i=0; i<freundeIDs.size();i++){
            jsonArraySenden1.put(freundeIDs.get(i));
        }
        String jsonString3 = jsonArraySenden1.toString();
        JSONObject json3 = new JSONObject();
        json3.put("Methode","getNamenVonIDs");
        json3.put("P1",currentUserID);
        json3.put("P2",jsonString3);
        ClientStart.verbinden.send(json3);
        JSONArray jsonArray3 = ClientStart.verbinden.receiveary();
        for(int i=0; i<jsonArray3.length(); i++){
            freundeNamen.add(jsonArray3.getString(i));
        }
        listFreunde.getItems().clear();
        Collections.sort(freundeNamen);  //sortieren
        Collections.reverse(freundeNamen); //sortieren
        listFreunde.getItems().addAll(freundeNamen);

//        try {
//            List<String> EigeneVeranstaltungen = new ArrayList<>();
//            List<String> Zwischenergebnis = new ArrayList<>();
//            List<String> Ergebnis = new ArrayList<>();
//            List<String> FreundeIDs = new ArrayList<>();
//            Connection con = DriverManager.getConnection(urlDB, user, passwort);
//            PreparedStatement ps = con.prepareStatement("SELECT * FROM USERINVERANSTALTUNGEN WHERE USERID=?");
//            ps.setInt(1, currentUserID);
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()){
//                EigeneVeranstaltungen.add(rs.getString("VERANSTALTUNGSID"));
//            }
//            for (int i = 0;i<EigeneVeranstaltungen.size();i++){
//                ps = con.prepareStatement("SELECT * FROM USERINVERANSTALTUNGEN WHERE VERANSTALTUNGSID=?");
//                ps.setInt(1, Integer.parseInt(EigeneVeranstaltungen.get(i)));
//                rs = ps.executeQuery();
//                while (rs.next()){
//                    if (!rs.getString("USERID").equals(test3)) {
//                        Zwischenergebnis.add(rs.getString("USERID"));
//                    }
//                }
//            }
//            PreparedStatement ps2 = con.prepareStatement("SELECT * FROM FREUNDE WHERE USERID=?");
//            ps2.setInt(1, currentUserID);
//            ResultSet rs2 = ps2.executeQuery();
//            while (rs2.next()){
//                FreundeIDs.add(rs2.getString("FREUNDID"));
//            }
//            //https://stackoverflow.com/questions/15575417/how-to-remove-common-values-from-two-array-lists
//            // Prepare a union
//            List<String> union = new ArrayList<String>(Zwischenergebnis);
//            union.addAll(FreundeIDs);
//            // Prepare an intersection
//            List<String> intersection = new ArrayList<String>(FreundeIDs);
//            intersection.retainAll(Zwischenergebnis);
//            // Subtract the intersection from the union
//            union.removeAll(intersection);
//            for (int i = 0;i<union.size();i++){
//                ps = con.prepareStatement("SELECT * FROM USER WHERE ID=?");
//                ps.setInt(1, Integer.parseInt(union.get(i)));
//                rs = ps.executeQuery();
//                while (rs.next()){
//                    if (!rs.getString("ID").equals(test3) && !rs.getBoolean("LEHRENDER")) {
//                        //Ergebnis.add(rs.getString("VORNAME" + " NACHNAME"));
//                        Ergebnis.add(rs.getString("VORNAME") +" "+ rs.getString("NACHNAME"));
//                    }
//                }
//            }
//
//            Set<String> set = new HashSet<>(Ergebnis);
//            Ergebnis.clear();
//            Ergebnis.addAll(set);
//            choiceBoxNeuerChat.getItems().clear();
//            Collections.sort(Ergebnis); //sortieren
//            choiceBoxNeuerChat.getItems().addAll(Ergebnis);
//            if (!choiceBoxNeuerChat.getItems().isEmpty()) {
//                choiceBoxNeuerChat.setValue(choiceBoxNeuerChat.getItems().get(0)); //setze Default Value auf den ersten Eintrag
//            }
//            con.close();
//        }
//        catch (SQLException throwables) {
//            System.out.println(throwables.getMessage());
//        }
        //Freundesliste wird gefüllt
//        try {
//            List<String> FreundeIDs = new ArrayList<>();
//            List<String> Ergebnis = new ArrayList<>();
//            Connection con = DriverManager.getConnection(urlDB, user, passwort);
//            PreparedStatement ps = con.prepareStatement("SELECT * FROM FREUNDE WHERE USERID=?");
//            ps.setInt(1, currentUserID);
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()){
//                FreundeIDs.add(rs.getString("FREUNDID"));
//            }
//            for (int i = 0;i<FreundeIDs.size();i++){
//                ps = con.prepareStatement("SELECT * FROM USER WHERE ID=?");
//                ps.setInt(1, Integer.parseInt(FreundeIDs.get(i)));
//                rs = ps.executeQuery();
//                while (rs.next()){
//                    if (!rs.getString("ID").equals(test3)) {
//                        Ergebnis.add(rs.getString("VORNAME") +" "+ rs.getString("NACHNAME"));
//                    }
//                }
//            }
//            listFreunde.getItems().clear();
//            Collections.sort(Ergebnis);  //sortieren
//            Collections.reverse(Ergebnis); //sortieren
//            listFreunde.getItems().addAll(Ergebnis);
//            con.close();
//        }
//        catch (SQLException throwables){
//            System.out.println(throwables.getMessage());
//
//        }
//        //https://www.youtube.com/watch?v=P45lRNbTcmg
//        timer.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                chatrefresh();
//            }
//        },5*1000, 5*1000);
        refresh.schedule(refreshTask,1,1000*5);

        //txtChat.positionCaret(txtChat.getLength());

        txtChat.textProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<?> observable, Object oldValue,
                                Object newValue) {
                txtChat.setScrollTop(Double.MAX_VALUE);
            }
        });

        stage = (Stage) pane.getScene().getWindow();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                refreshTask.cancel();
                System.exit(0);
            }
        });
    }
}
