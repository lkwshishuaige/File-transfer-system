package Client.Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DownloadFile extends Application {
    Stage stage = new Stage();
    @Override
    public void start(Stage stage) throws Exception {
        Parent download = FXMLLoader.load(getClass().getResource("/Client/UI/DownloadFile.fxml"));
        stage.setTitle("请输入文件名");
        Scene scene = new Scene(download);
        stage.setScene(scene);
        stage.show();
    }
    public void restart() throws Exception {
        start(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
