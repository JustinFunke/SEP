package Client.javaFx;

import Server.ClientStart;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.simple.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class NPStudentFürLController {

    @FXML
    private Pane pane;
    @FXML
    private ImageView imageProfilbild;
    @FXML
    private Text txtName;
    @FXML
    private Text txtAdresse;
    @FXML
    private Text txtEmail;
    @FXML
    private Text txtMatrikelnummer;
    @FXML
    private Text txtStudienfach;
    @FXML
    private ListView<String> listViewLV;
    @FXML
    private Label idLabel;
    @FXML
    private Label currentLVIDLabel;
    @FXML
    private Label studentenIDLabel;



    private Stage stage;
    private Parent root;
    private Scene scene;
    private int currentUserID;
    private int currentLVID;
    private int studentID;

    public int getStudentenID() { return studentID; }

    public void setStudentenID(int studentenID) { this.studentID = studentenID; }

    public int getCurrentUserID() {
        return currentUserID;
    }

    public void setCurrentUserID(int currentUserID) {
        this.currentUserID = currentUserID;
    }

    public int getCurrentLVID() {
        return currentLVID;
    }

    public void setCurrentLVID(int currentLVID) {
        this.currentLVID = currentLVID;
    }

    @FXML
    void cmdBack(ActionEvent event) throws IOException {
        //Zurück zur TeilnehmerListeView
        stage = (Stage) pane.getScene().getWindow();
        stage.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/TeilnehmerListeView.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        TeilnehmerListeController controller = loader.getController();
        controller.setIDLabel(currentUserID);
        controller.setLVIDLabel(currentLVID);
        controller.init();
        stage.show();
    }
    public void setIDLabel(int id){
        Integer integer = id;
        idLabel.setText(integer.toString());
    }
    public void setLVIDLabel(int id){
        Integer integer = id;
        currentLVIDLabel.setText(integer.toString());
    }
    public void setStudentenIDLabel(int id){
        Integer integer = id;
        studentenIDLabel.setText(integer.toString());
    }

    public void init() throws IOException {
        String zs = idLabel.getText();
        currentUserID = Integer.parseInt(zs);
        String lvzs = currentLVIDLabel.getText();
        currentLVID = Integer.parseInt(lvzs);
        String szs = studentenIDLabel.getText();
        studentID = Integer.parseInt(szs);
        JSONObject json = new JSONObject();
        json.put("Methode","getUserDaten");
        json.put("P1",studentID);
        ClientStart.verbinden.send(json);
        JSONArray jsonArray = ClientStart.verbinden.receiveary();
        txtName.setText(jsonArray.getString(0));
        txtEmail.setText(jsonArray.getString(1));
        txtStudienfach.setText(jsonArray.getString(2));
        txtAdresse.setText(jsonArray.getString(3));
        txtMatrikelnummer.setText(jsonArray.getString(4));
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(jsonArray.getString(5)));
            Image image = new Image(inputStream);
            imageProfilbild.setImage(image);
        }
        catch (Exception exception){
            System.out.println("Kein Profilbild vorhanden");
        }
        List<String> ergebnis = new ArrayList<>();
        JSONObject json2 = new JSONObject();
        json2.put("Methode","lvDesUsers");
        json2.put("P1",studentID);
        ClientStart.verbinden.send(json2);
        JSONArray jsonArray2 = ClientStart.verbinden.receiveary();
        for(int i=0; i<jsonArray2.length(); i++){
            ergebnis.add(jsonArray2.getString(i));
        }
        listViewLV.getItems().clear();
        listViewLV.getItems().addAll(ergebnis);
    }
}
