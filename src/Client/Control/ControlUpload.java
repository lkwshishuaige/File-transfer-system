package Client.Control;

import Client.Main.Data;
import Client.Main.OpenFile;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;


public class ControlUpload {
    public TextField TextFieldFileName;
    public Button confirmButton;
    public Button cancelButton;

    public static String filePath;

    public void confirm(ActionEvent event) throws IOException {
        filePath = TextFieldFileName.getText();
        if(!filePath.equals("")) {
            System.out.println(filePath);
            File file = new File(filePath);
            if (!file.exists()) {
                return;
            }
            ((Stage) confirmButton.getScene().getWindow()).close();
            Socket socket = ControlLogin.user.getSocket();

            //向服务器端请求操作“1”
            OutputStream os = socket.getOutputStream();
            os.write("1".getBytes());
            os.flush();


            Socket fileSocket = new Socket(Data.ip, 9001);
            FileInputStream fis = new FileInputStream(file);
            DataOutputStream dos = new DataOutputStream(fileSocket.getOutputStream());

            //文件名和长度
            dos.writeUTF(file.getName());
            dos.flush();
            dos.writeLong(file.length());
            dos.flush();


            int len = 0;
            byte[] bytes = new byte[1024];
            InputStream is = socket.getInputStream();
            len = is.read(bytes);
            if (new String(bytes, 0, len).equals("1")) {
                //开始传输文件
                int counter = 0;
                long TranLen = 0;
                long progress;
                while ((len = fis.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                    dos.flush();
                    counter++;
                    TranLen += len;
                    progress = 100 * TranLen / file.length();
                    System.out.print("| " + progress + "% |");
                    if(counter%10==0){
                        System.out.println();
                    }
                }
            }
            fileSocket.shutdownOutput();
            fileSocket.close();
            len = is.read(bytes);
            System.out.println(new String(bytes, 0, len));
        }
    }

    public void cancel(ActionEvent event) {
        ((Stage)cancelButton.getScene().getWindow()).close();
    }

    public void scan(ActionEvent event) throws Exception {
        OpenFile openFile = new OpenFile();
        openFile.start(new Stage());
        if(openFile.file != null)
            TextFieldFileName.setText(String.valueOf(openFile.file));
    }
}
