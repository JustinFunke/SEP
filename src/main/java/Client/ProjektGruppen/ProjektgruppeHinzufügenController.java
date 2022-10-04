package Client.ProjektGruppen;

import Server.Datenbank;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProjektgruppeHinzufügenController {
    public TextField txtSuche;
    public ListView ListViewSuche;
    public Button cmdHinzufuegen;
    public RadioButton btnMatrikelnummer;
    public Label currentPGIDLabel;
    public Label currentLVIDLabel;

    public ArrayList<Integer> pgUser;
    ArrayList<String>lvMitglieder = new ArrayList<>();

    public void setLVIDLabel(int pUID){
        Integer integer = pUID;
        currentLVIDLabel.setText(integer.toString());
    }

    //PGID übernehmen und umwandeln
    public void setPGIDLabel(int pUID){
        Integer integer = pUID;
        currentPGIDLabel.setText(integer.toString());
    }

    public void cmdHinzufuegen_Clicked(MouseEvent mouseEvent) throws SQLException, IOException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Fehler");
        alert.setHeaderText("Fehler");
        if(!(ListViewSuche.getSelectionModel().isEmpty())){
            String pg = currentPGIDLabel.getText();
            System.out.println("Erwarte Liste");
            pgUser = ProjektGruppeStudentHinzufügen.ProjektUser(Integer.parseInt(pg));
            System.out.println("Hab Liste");
            for (int i=0;i<pgUser.size();i++) {
                if (ListViewSuche.getSelectionModel().getSelectedItem().equals(pgUser.get(i).toString())) {
                    alert.setContentText("Der Nutzer ist bereits in der Projektgruppe");
                    alert.showAndWait();
                    return;
                }
            }
            System.out.println("Jetzt Hinzufügen");
            int meinePG = Integer.parseInt(pg);
            int meinUser = Integer.parseInt(ListViewSuche.getSelectionModel().getSelectedItem()+"");
            System.out.println("meinePG " + meinePG);
            System.out.println("meinUser " + meinUser);
            ProjektGruppeStudentHinzufügen.nutzerHinzufügen(meinePG, meinUser);
            //Datenbank.DbUserPGHinzufuegen(meinUser, meinePG); //?????
            //System.out.println((String) ListViewSuche.getSelectionModel().getSelectedItem());
        }
    }

    public void txtSuche_KeyTyped(KeyEvent keyEvent) throws SQLException, IOException {
        ListViewSuche.getItems().clear();
        lvMitglieder.clear();
        String lv = currentLVIDLabel.getText();
        lvMitglieder= ProjektGruppeStudentHinzufügen.lehrveranstaltungsMitglieder(Integer.parseInt(lv),txtSuche.getText());
        ListViewSuche.getItems().addAll(lvMitglieder);
        for(int i =0;i<lvMitglieder.size();i++){
            System.out.print(lvMitglieder.get(i)+" ");
        }

    }
}
