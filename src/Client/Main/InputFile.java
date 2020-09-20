package Client.Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class InputFile extends Application {
    Stage stage = new Stage();
    @Override
    public void start(Stage stage) throws Exception {
        Parent inputFile = FXMLLoader.load(getClass().getResource("/Client/UI/InputFile.fxml"));
        stage.setTitle("输入需要下载的文件名");
        Scene scene = new Scene(inputFile);
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
