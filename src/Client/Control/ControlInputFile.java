package Client.Control;

import Client.Main.DownloadFile;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControlInputFile {
    public Button confirmButton;
    public Button cancelButton;
    public TextField TextFieldFileName;

    public static String fileName;

    public void confirm(ActionEvent event) {
        fileName = TextFieldFileName.getText();
        if(fileName.equals("")){
            return;
        }
        ((Stage)confirmButton.getScene().getWindow()).close();
        DownloadFile downloadFile = new DownloadFile();
        Stage stage = new Stage();
        try {
            downloadFile.start(stage);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void cancel(ActionEvent event) {
        Stage primaryStage = (Stage) cancelButton.getScene().getWindow();
        primaryStage.close();
    }
}
