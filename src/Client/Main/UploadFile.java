package Client.Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UploadFile extends Application {
    Stage stage = new Stage();
    @Override
    public void start(Stage stage) throws Exception {
        Parent upload = FXMLLoader.load(getClass().getResource("/Client/UI/UploadFile.fxml"));
        stage.setTitle("请输入文件路径");
        Scene scene = new Scene(upload);
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
