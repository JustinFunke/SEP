//Quellen: https://code.makery.ch/blog/javafx-8-event-handling-examples/

package Client.Login;


import Client.Registrierung.RegistrierenWindowLehrenderController;
import Client.Registrierung.RegistrierenWindowStudentController;
import Server.ClientStart;
import Server.ClientThread;
import Client.javaFx.HauptseiteLehrenderController;
import Client.javaFx.HauptseiteStudentController;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import Server.Mail.EmailClient;
import javafx.stage.WindowEvent;
import org.json.simple.JSONObject;

import java.util.concurrent.ThreadLocalRandom;

public class LoginController {

    private int currentUserID;




    public int getCurrentUserID()
    {
        return currentUserID;
    }

    public void setCurrentUserID(int currentUserID)
    {
        this.currentUserID = currentUserID;
    }

    private String mail;

    public String getMail()
    {
        return mail;
    }

    public void setMail(String mail)
    {
        this.mail = mail;
    }

    private String name;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    private int code;

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    private int typeInCode;

    public int getTypeInCode()
    {
        return typeInCode;
    }

    public void setTypeInCode(int typeInCode)
    {
        this.typeInCode = typeInCode;
    }


    private boolean lehrender;

    public boolean getLehrender()
    {
        return this.lehrender;
    }

    public void setLehrender(boolean lehrender)
    {
        this.lehrender = lehrender;
    }

    @FXML
    private Label txtID;
    @FXML
    private Label txtErrorMessage;
    @FXML
    public TextField Email;
    @FXML
    public TextField Password;
    @FXML
    public Pane paneLogin;
    @FXML
    private Stage stage;
    @FXML
    private Button LoginButton;
    @FXML
    private Label close;
    @FXML
    private TitledPane CodeEingabe;
    @FXML
    private TextField CodeField;
    @FXML
    private Button SubmitButton;
    @FXML
    private Label CodeLabel;
    @FXML
    private Label LoadingSign;
    @FXML
    private Label WrongLabel;



    private Parent root;
    private Scene scene;
    Socket aktuell;

    JSONObject answer = new JSONObject();



    public void handleLoginAction(MouseEvent event) throws InterruptedException, IOException, MessagingException {

        if(event.getSource() == close)
        {
            System.out.println("close");
            System.exit(0);
        }
        if(event.getSource() == LoginButton)
        {
            String password = Password.getText();
            String email = Email.getText();
            String id = "0";

            JSONObject transfer = new JSONObject();
            transfer.put("Methode", "login");
            transfer.put("mail", email);
            transfer.put("password", password);
            transfer.put("id", id);

            ClientStart.verbinden.send(transfer);
            TimeUnit.SECONDS.sleep(5L);
            this.answer = ClientStart.verbinden.receiveobj();

            if(this.answer.containsKey("Exception"))
            {
                System.err.println("Wrong Client.Login, Keine Matrikelnummer oder Email");
                txtErrorMessage.setTextFill(Color.TOMATO);
                txtErrorMessage.setText("Wrong Credentials");
            }
            else
            {
                String ID = (String) this.answer.get("id");
                Integer userId = 0;

                try
                {
                    userId = Integer.parseInt(ID);
                    System.out.println("userid:" + userId);
                }
                catch (Exception ex)
                {
                    System.err.println( ex +" parseInt went wrong - method HandleLogin");
                }

                setCurrentUserID(userId);
                setMail((String) this.answer.get("email"));
                String vorname = (String) this.answer.get("vorname");
                String nachname = (String) this.answer.get("nachname");
                try {
                    setName(vorname + " " + nachname);
                    this.LoadingSign.setVisible(true);
                    sendMail();
                    this.LoadingSign.setVisible(false);
                    this.CodeEingabe.setVisible(true);

                } catch (MessagingException e) {
                    System.err.println(e.getMessage());
                }
            }
        }

        if(event.getSource() == SubmitButton) {

            try
            {
                String stringCode = CodeField.getText();
                if(stringCode.length() > 6 || stringCode.isEmpty())
                {
                    txtErrorMessage.setTextFill(Color.RED);
                    txtErrorMessage.setText("WRONG Security Code...");
                    TimeUnit.SECONDS.sleep(5);
                    this.CodeEingabe.setVisible(false);
                    this.CodeField.clear();
                }
                else
                {
                    setTypeInCode(Integer.parseInt(stringCode));
                }
            }
            catch (NumberFormatException | InterruptedException e)
            {
                e.printStackTrace();
                txtErrorMessage.setTextFill(Color.RED);
                txtErrorMessage.setText("WRONG Security Code...");
                TimeUnit.SECONDS.sleep(5);
                this.CodeEingabe.setVisible(false);
                this.CodeField.clear();
            }


            boolean transfer;

            try {

                if (this.typeInCode == this.code)
                {
                    transfer = true;
                }
                else {
                    transfer = false;
                }

                if (transfer && this.answer.containsValue("no")) {
                    ((Stage) paneLogin.getScene().getWindow()).close();
                    stage = new Stage();
                    stage.setMaximized(true);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Hauptseite(fürStudent).fxml"));
                    Stage stage = new Stage();
                    stage.setScene(new Scene(loader.load()));
                    HauptseiteStudentController controller = loader.getController();
                    controller.setIDLabel(currentUserID);
                    controller.init();
                    stage.show();
                }
                else if (transfer && this.answer.containsValue("yes")) {
                    ((Stage) paneLogin.getScene().getWindow()).close();
                    stage = new Stage();
                    stage.setMaximized(true);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Hauptseite(fürLehrender).fxml"));
                    Stage stage = new Stage();
                    stage.setScene(new Scene(loader.load()));
                    HauptseiteLehrenderController controller = loader.getController();
                    controller.setIDLabel(currentUserID);
                    controller.init();
                    stage.show();
                }
                else {
                    txtErrorMessage.setTextFill(Color.RED);
                    txtErrorMessage.setText("WRONG Security Code...");
                    TimeUnit.SECONDS.sleep(5);
                    this.CodeEingabe.setVisible(false);
                    this.CodeField.clear();
                }
            } catch (IOException | InterruptedException e) {
                System.err.println(e.getMessage());
            }
        }



    }

    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    public void init(){
        this.aktuell = aktuell;
        System.out.println("Hier");
        stage = (Stage) paneLogin.getScene().getWindow();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {

                System.out.println("Ausloggen");
                JSONObject json = new JSONObject();
                json.put("Methode", "closeConnection");
                ClientStart.verbinden.send(json);
                System.exit(0);
            }
        });

    }

    private String logIn() throws SQLException {

        Connection con = ClientThread.conDB();
        String password = Password.getText();
        int id = 0;

        try
        {
            if (!Email.getText().contains("@") )
            {
                String sid = Email.getText();
                id = Integer.parseInt(sid);
            }
        }
        catch (Exception ex)
        {
            System.err.println("Wrong Client.Login, Keine Matrikelnummer oder Email");
            txtErrorMessage.setTextFill(Color.TOMATO);
            txtErrorMessage.setText("Wrong Credentials");
            con.close();
            return "Exception";
        }

        String email = Email.getText();
        String lehrender = "LEHRENDER";

        String sql = "Select * FROM USER Where (EMAIL = ? and PASSWORT = ?) OR (ID = ? and PASSWORT = ?)";

        try {
            preparedStatement = con.prepareStatement(sql);
            //preparedStatement.setString(1, id);
            preparedStatement.setString(1, email.toLowerCase());
            preparedStatement.setString(2, password);
            preparedStatement.setInt(3, id);
            preparedStatement.setString(4, password);
            resultSet = preparedStatement.executeQuery();
            if (!resultSet.next())
            {
                txtErrorMessage.setTextFill(Color.TOMATO);
                txtErrorMessage.setText("Wrong Credentials");
                System.err.println("Wrong Client.Login");
                con.close();
                return "Error";

            }
            else if (resultSet.getBoolean(lehrender))
            {
                this.lehrender = resultSet.getBoolean(lehrender);
                txtErrorMessage.setTextFill(Color.GREEN);
                txtErrorMessage.setText("Client.Login Successful...Lehrenden...weiterleiten");
                //txtID.setTextFill(Color.GREEN);
                //txtID.setText(resultSet.getString("ID"));
                setCurrentUserID(resultSet.getInt("ID"));
                setMail(resultSet.getString("EMAIL"));
                setName(resultSet.getString("VORNAME")+ " " + resultSet.getString("NACHNAME") );
                con.close();
                return "SuccessLehrender";
            }
            else
            {
                this.lehrender = resultSet.getBoolean(lehrender);
                txtErrorMessage.setTextFill(Color.GREEN);
                txtErrorMessage.setText("Client.Login Successful...Student...weiterleiten");
                //txtID.setTextFill(Color.GREEN);
                //txtID.setText(resultSet.getString("ID"));
                setCurrentUserID(resultSet.getInt("ID"));
                setMail(resultSet.getString("EMAIL"));
                setName(resultSet.getString("VORNAME")+ " " + resultSet.getString("NACHNAME") );
                con.close();
                return "SuccessStudent";
            }
        }
        catch (Exception ex)
        {
            System.err.println("Wrong Client.Login");
            txtErrorMessage.setTextFill(Color.TOMATO);
            txtErrorMessage.setText("Wrong Credentials");
            return "Exception";
        }
    }

    private void showDialog(String info, String header, String title)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(info);
        alert.setHeaderText(header);
        alert.showAndWait();

    }

    public void sendMail() throws MessagingException {

        String mail;
        String html;
        String title;

        this.code = ThreadLocalRandom.current().nextInt(100000,999999);

        mail = getMail();
        html = "Hello "+ name + ", this is your code: " + code;
        title = "Secure Code for: " + name ;

        EmailClient.sendAsHtml(mail, title, html);
    }


    public void cmdRegisterLehrender_Clicked(MouseEvent mouseEvent) throws IOException {

        ((Stage) paneLogin.getScene().getWindow()).close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/RegistrierenWindowLehrender.fxml"));
        stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        RegistrierenWindowLehrenderController controller =loader.getController();
        controller.init();
        stage.show();

    }

    public void cmdRegisterStudent_Clicked(MouseEvent mouseEvent) throws IOException {
        ((Stage) paneLogin.getScene().getWindow()).close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/RegistrierenWindowStudent.fxml"));
        stage = new Stage();
        stage.setScene(new Scene(loader.load()));

        RegistrierenWindowStudentController controller = loader.getController();
        controller.init();
        stage.show();
    }

}
