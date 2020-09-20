package Client.Control;

import Client.Main.*;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ControlLogin {

    public TextField userId;
    public PasswordField userPassword;
    public Label idPassError;
    public Button loginButton;
    public static User user;

    public void Login(ActionEvent event) throws Exception {
        if(userId.getText().length() < 1 || userPassword.getText().length() < 1) {
            idPassError.setText("账号密码不能为空");
            return;
        }
        Socket socket = new Socket(Data.ip,9000);
        OutputStream os;
        os = socket.getOutputStream();
        int len = 0;
        byte[] bytes = new byte[1024];
        String idPass = userId.getText() + "\n" + userPassword.getText();
        os.write(idPass.getBytes());
        InputStream is;
        is = socket.getInputStream();
        len = is.read(bytes);
        String loginMassage = new String(bytes,0,len);
        if(loginMassage.substring(0,2).equals("u:")) {
            user = new User(Integer.valueOf(userId.getText()),loginMassage.substring(2),userPassword.getText(),socket);
            ((Stage)loginButton.getScene().getWindow()).close();
            new Logined().start(new Stage());
        }else
            idPassError.setText("账号密码错误");
    }

    public void Register(ActionEvent event) throws Exception {
        new Register().start(new Stage());
    }
}
