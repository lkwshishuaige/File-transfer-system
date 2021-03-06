package Client.Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Logined extends Application {
    Stage stage = new Stage();
    @Override
    public void start(Stage stage) throws Exception {
        Parent login = FXMLLoader.load(getClass().getResource("/Client/UI/Logined.fxml"));
        stage.setTitle("kaiwei lee's cloud disk");
        Scene scene = new Scene(login);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);//设定窗口无边框
        stage.show();
    }
    public void restart() throws Exception {
        start(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
