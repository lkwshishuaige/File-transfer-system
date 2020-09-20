package Client.Control;

import Client.Main.Data;
import Client.Main.SelectFilePath;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class ControlDownload {
    public Button confirmButton;
    public Button cancelButton;
    public TextField TextFieldFileName;

    public static String filePath;

    public void confirm(ActionEvent event) throws IOException {
        filePath = TextFieldFileName.getText();
        if(!filePath.equals("")) {
            System.out.println(filePath);
            File file = new File(filePath + "\\" + ControlInputFile.fileName);
            if (file.exists()) {
                System.out.println("文件已在当前文件夹");
                return;
            }
            ((Stage) confirmButton.getScene().getWindow()).close();

            Socket socket = ControlLogin.user.getSocket();
            //向服务器端请求操作“2”
            OutputStream os = socket.getOutputStream();
            os.write("2".getBytes());
            os.flush();
            //发送请求下载文件
            os.write(ControlInputFile.fileName.getBytes());
            os.flush();

            int len = 0;
            byte[] bytes = new byte[1024];
            InputStream is = socket.getInputStream();
            len = is.read(bytes);
            if (new String(bytes, 0, len).equals("1")) {

                //建立文件传输连接
                Socket fileSocket = new Socket(Data.ip, 9001);
                DataInputStream dis = new DataInputStream(fileSocket.getInputStream());
                // 文件名和长度
                String fileName = dis.readUTF();
                long fileLength = dis.readLong();

                //创建文件输入流
                FileOutputStream fos = new FileOutputStream(file);

                int counter = 0;
                long TranLen = 0;
                long progress;
                while ((len = dis.read(bytes)) != -1) {//head=input.read(buffer)) > 0
                    fos.write(bytes, 0, len);
                    fos.flush();
                    counter++;
                    TranLen += len;
                    progress = 100 * TranLen / fileLength;
                    System.out.print("| " + progress + "% |");
                    if(counter % 10 == 0)
                        System.out.println();
                }
                System.out.println("下载成功");
                fos.close();
                fileSocket.close();
            }else {
                System.out.println("库存文件中没有此文件");
            }
        }

    }

    public void cancel(ActionEvent event) {
        Stage primaryStage = (Stage) cancelButton.getScene().getWindow();
        primaryStage.close();
    }

    public void scan(ActionEvent event) throws Exception {
        SelectFilePath selectFilePath = new SelectFilePath();
        selectFilePath.start(new Stage());
        if(selectFilePath.filePath != null)
            TextFieldFileName.setText(String.valueOf(selectFilePath.filePath));
    }
}
