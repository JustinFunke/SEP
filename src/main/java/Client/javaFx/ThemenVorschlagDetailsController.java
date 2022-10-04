package Client.javaFx;

import Server.ClientStart;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class ThemenVorschlagDetailsController {
    @FXML
    private Label currentIDLabel;
    @FXML
    private Label ThemaNameLabel;
    @FXML
    private Label beschreibungLabel;
    @FXML
    private ListView LiteraturListeView;
    @FXML
    private Label currentThemaLabel;
    @FXML
    private Button BackButton;
    @FXML
    private Pane pane;
    @FXML
    private Label currentLVIDLabel;
    @FXML
    private Label lehrerIDLabel;
    @FXML
    private Label werkeAnzahlLabel;
    @FXML
    private ChoiceBox eintragChoice;
    @FXML
    private Button OverviewButton;

    private Stage stage;

    private int currentThemaID;
    private int currentUserID;
    private int currentLVID;
    private int lehrenderID;






   public void init() throws IOException {
       LiteraturListeView.getItems().clear();
       currentUserID = Integer.parseInt(currentIDLabel.getText());
       currentThemaID = Integer.parseInt(currentThemaLabel.getText());
       currentLVID = Integer.parseInt(currentLVIDLabel.getText());
       lehrenderID = Integer.parseInt(lehrerIDLabel.getText());
       ArrayList<String> Literaturliste = new ArrayList<>();
       JSONObject jsonThemaTitel = new JSONObject();
       jsonThemaTitel.put("Methode","getThemaDaten");
       jsonThemaTitel.put("Parameter1",currentThemaID);
       ClientStart.verbinden.send(jsonThemaTitel);
       JSONArray jsonThemaDaten = ClientStart.verbinden.receiveary();
       if(jsonThemaDaten!=null){
           ThemaNameLabel.setText(jsonThemaDaten.get(0)+"");
           beschreibungLabel.setText(jsonThemaDaten.get(1)+"");
       }
       JSONObject jsonLiteratur = new JSONObject();
       jsonLiteratur.put("Methode","getLiteratur");
       jsonLiteratur.put("Parameter1",currentThemaID);
       ClientStart.verbinden.send(jsonLiteratur);
       JSONArray jsonLiteraturArray = ClientStart.verbinden.receiveary();
       if(jsonLiteraturArray!=null){
           for(int i = 0;i<jsonLiteraturArray.length();i++) {
               Literaturliste.add((String) jsonLiteraturArray.get(i) + "");
           }
       }
       werkeAnzahlLabel.setText("Anzahl an Werken in Literaturliste: "+ Literaturliste.size());
       for(int i = 1;i<=Literaturliste.size();i++){
           eintragChoice.getItems().add(String.valueOf(i));
       }
       for(int i =0;i<Literaturliste.size();i++){
           Scanner scan = new Scanner(Literaturliste.get(i));
           while (scan.hasNextLine()){
               String current = scan.nextLine();
               if(current.contains("title = ")){
                   current = current.replace("{","");
                   current = current.replace("}","");
                   current = current.replace(",","");
                   current = current.replace("title = ","");
                   int j = i+1;
                   String anzeige = "Werk Nr.: "+j+current;
                   LiteraturListeView.getItems().add(anzeige);
               }
           }
       }

   }

    public void WerkAnzeigen(Event event) throws IOException {
       LiteraturListeView.getItems().clear();
        ArrayList<String> Literaturliste = new ArrayList<>();
        JSONObject jsonLiteratur = new JSONObject();
        jsonLiteratur.put("Methode","getLiteratur");
        jsonLiteratur.put("Parameter1",currentThemaID);
        ClientStart.verbinden.send(jsonLiteratur);
        JSONArray jsonLiteraturArray = ClientStart.verbinden.receiveary();
        if(jsonLiteraturArray!=null){
            for(int i = 0;i<jsonLiteraturArray.length();i++) {
                Literaturliste.add((String) jsonLiteraturArray.get(i) + "");
            }
        }
        int index = Integer.parseInt((String) eintragChoice.getValue());
        Scanner neuscan = new Scanner(Literaturliste.get(index-1));
        ArrayList<String> liste = new ArrayList<String>();
        String art2 = neuscan.nextLine();
        int idx2  =art2.lastIndexOf("{");
        if(idx2 != -1){
            art2 = art2.substring(0,idx2);
        }
        while (neuscan.hasNextLine()){
            String tmp = neuscan.nextLine();
            tmp = tmp.replace("{","");
            tmp = tmp.replace("}","");
            tmp = tmp.replace(",","");
            if(!tmp.equals("")) {
                tmp = tmp.substring(1,tmp.length());
                liste.add(tmp);
            }
        }
        Collections.sort(liste);
        LiteraturListeView.getItems().add("Art des Werks = "+art2);
        LiteraturListeView.getItems().addAll(liste);
        System.out.println(art2);
        for(int i = 0;i<liste.size();i++){
            System.out.println(liste.get(i));
        }
    }



   public void BackButtonOnAction() throws IOException {

       Stage stage1 = (Stage) BackButton.getScene().getWindow();
       stage1.close();
       FXMLLoader loader = new FXMLLoader(getClass().getResource("/NutzerprofilLehrender.fxml"));
       Stage stage = new Stage();
       stage.setScene(new Scene(loader.load()));
       NPLehrenderController controller = loader.getController();
       controller.setIDLabel(currentUserID);
       controller.setLVIDLabel(currentLVID);
       controller.setLehrerIDLabel(lehrenderID);
       controller.init();
       stage.show();
   }
    public void setIDLabel(int id){
        Integer integer = id;
        currentIDLabel.setText(integer.toString());
    }
    public void setTIDLabel(int id){
       Integer integer = id;
       currentThemaLabel.setText(integer.toString());
    }
    public void setLVIDLabel(int id){
        Integer integer = id;
        currentLVIDLabel.setText(integer.toString());
    }
    public void setLehrerIDLabel(int id){
        Integer integer = id;
        lehrerIDLabel.setText(integer.toString());
    }
}
