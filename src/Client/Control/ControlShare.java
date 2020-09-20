package Client.Control;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ControlShare {
    public TextField TextFieldId;
    public TextField TextFieldFileName;
    public Button confirmButton;
    public Button cancelButton;

    public void confirm(ActionEvent event) throws Exception {
        String receiveId = TextFieldId.getText();
        String fileName = TextFieldFileName.getText();
        if(receiveId.equals("")||fileName.equals("")){
            return;
        }

        ((Stage) confirmButton.getScene().getWindow()).close();
        Socket socket = ControlLogin.user.getSocket();

        //向服务器端请求操作“0”
        OutputStream os = socket.getOutputStream();
        os.write("0".getBytes());
        os.flush();

        String sentMassage = fileName + "\n" + receiveId;
        os.write(sentMassage.getBytes());
        os.flush();

        int len = 0;
        byte[] bytes = new byte[1024];
        InputStream is = socket.getInputStream();
        len = is.read(bytes);
        System.out.println(new String(bytes, 0, len));
    }

    public void cancel(ActionEvent event) {
        ((Stage)cancelButton.getScene().getWindow()).close();
    }
}
