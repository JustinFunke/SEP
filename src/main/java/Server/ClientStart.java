package Server;

import Client.Login.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;


public class ClientStart extends Application{

    public static ClientVerbinden verbinden;

    public static void main(String[] args) {
        launch(args);
    }


    //////////////////////////////Nicht Bearbeiten


    @Override
    public void start(Stage stage) throws Exception {
        try {
            verbinden = new ClientVerbinden("127.0.0.1");
            verbinden.connectToServer();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            LoginController controller = loader.getController();
//            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
//                @Override
//                public void handle(WindowEvent windowEvent) {
//                    System.out.println("Schließen");
//                }
//            });
            controller.init();
            stage.show();





        }catch (Exception ex){
            System.err.println("IOException: Server is not available!");
            System.err.println(ex.getStackTrace());
            Parent root = FXMLLoader.load(Objects.requireNonNull(ClientStart.class.getResource("/Error.fxml")));
            stage.setTitle("404");
            root.setId("Error");
            stage.setScene(new Scene(root));
            stage.show();
        }
    }
}
