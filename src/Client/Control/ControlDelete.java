package Client.Control;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ControlDelete {
    public Button confirmButton;
    public Button cancelButton;
    public TextField TextFieldFileName;

    public static String fileName;

    public void confirm(ActionEvent event) throws IOException {
        fileName = TextFieldFileName.getText();
        if(!fileName.equals(null)){
            System.out.println(fileName);
            Stage primaryStage = (Stage) confirmButton.getScene().getWindow();
            primaryStage.close();
            Socket socket = ControlLogin.user.getSocket();
            OutputStream os = socket.getOutputStream();
            os.write("3".getBytes());
            os.flush();
            os.write(fileName.getBytes());
            os.flush();
            InputStream is = socket.getInputStream();
            int len = 0;
            byte[] bytes = new byte[1024];
            len = is.read(bytes);
            System.out.println(new String(bytes, 0, len));
        }
    }

    public void cancel(ActionEvent event) {
        Stage primaryStage = (Stage) cancelButton.getScene().getWindow();
        primaryStage.close();
    }
}