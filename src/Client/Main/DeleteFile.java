package Client.Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DeleteFile extends Application {
    Stage stage = new Stage();
    @Override
    public void start(Stage stage) throws Exception {
        Parent delete = FXMLLoader.load(getClass().getResource("/Client/UI/DeleteFile.fxml"));
        stage.setTitle("请输入文件名");
        Scene scene = new Scene(delete);
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
