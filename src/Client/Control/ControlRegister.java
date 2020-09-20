package Client.Control;

import Client.Main.Data;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ControlRegister {
    public Button RegisterButton;
    public TextField userId;
    public TextField userName;
    public Label idPassError;
    public TextField userPassword;

    public void Register(ActionEvent event) throws IOException {
        if(userId.getText().length() < 1 || userName.getText().length() < 1 || userPassword.getText().length() < 1) {
            idPassError.setText("账号、用户名、密码不能为空");
            return;
        }
        Socket socket = new Socket(Data.ip,9000);
        OutputStream os = socket.getOutputStream();
        int len = 0;
        byte[] bytes = new byte[1024];
        String idPass = userId.getText() + "\n"  + userName.getText() + "\n" + userPassword.getText();
        os.write(idPass.getBytes());
        InputStream is = socket.getInputStream();
        len = is.read(bytes);
        String registerMassage = new String(bytes,0,len);
        if(registerMassage.equals("1")) {
            ((Stage) RegisterButton.getScene().getWindow()).close();
        }else{
            userId.clear();
            idPassError.setText("账号已被占用，请重新设置账号");
        }
        //释放资源
        os.close();
        is.close();
    }
}
