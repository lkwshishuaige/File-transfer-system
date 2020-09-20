package Client.Control;

import Client.Main.*;
import Server.ConnMysql;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class ControlLogined implements Initializable {
    public Button buttonLogout;
    public TableView storeFile;
    public TableColumn<FileData,String> fileName;
    public TableColumn<FileData,String> fileSize;
    public TableColumn<FileData,String> uploadDate;
    public TableColumn fileSource;
    public Label userName;


    public void ClickShare(ActionEvent event) throws Exception {
        ShareFile shareFile = new ShareFile();
        Stage stage = new Stage();
        try{
            shareFile.start(stage);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void ClickUpdate(ActionEvent event) throws Exception {
        UploadFile uploadFile = new UploadFile();
        Stage stage = new Stage();
        try{
            uploadFile.start(stage);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void ClickDownload(ActionEvent event) {
        InputFile inputFile = new InputFile();
        Stage stage = new Stage();
        try{
            inputFile.start(stage);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void ClickDelete(ActionEvent event) {
        DeleteFile deleteFile = new DeleteFile();
        Stage stage = new Stage();
        try{
            deleteFile.start(stage);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void ClickLogout(ActionEvent event) throws IOException {
        Socket socket = ControlLogin.user.getSocket();
        OutputStream os = socket.getOutputStream();
        os.write("4".getBytes());
        Stage primaryStage = (Stage) buttonLogout.getScene().getWindow();//将submit(登录按钮)与Main类中的primaryStage(新窗口)绑定 并执行close()
        primaryStage.close();//打开新的窗口 所以要关闭当前的窗口
        //mStage.close();
        Login we = new Login();//新窗口类
        Stage stage = new Stage();
        try {
            we.start(stage);//打开新窗口
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ClickExit(ActionEvent event) throws IOException {
        Socket socket = ControlLogin.user.getSocket();
        OutputStream os = socket.getOutputStream();
        os.write("4".getBytes());
        Stage primaryStage = (Stage) buttonLogout.getScene().getWindow();//将submit(登录按钮)与Main类中的primaryStage(新窗口)绑定 并执行close()
        primaryStage.close();//打开新的窗口 所以要关闭当前的窗口
    }

    public void refresh(ActionEvent event) {
        showList(ControlLogin.user.getId());
//        showList(ControlLogin.user.getId());
    }

    public void showList(int userId) {
        ObservableList<FileData> cellData = FXCollections.observableArrayList();
        fileName.setCellValueFactory(new PropertyValueFactory("fileName"));
        fileSize.setCellValueFactory(new PropertyValueFactory("fileSize"));
        uploadDate.setCellValueFactory(new PropertyValueFactory("uploadDate"));
        fileSource.setCellValueFactory(new PropertyValueFactory("fileSource"));
        Connection conn = ConnMysql.getConn();
        Statement statement = null;
        String searchDataBase = "select filename,filesize,uploaddate,filesource from file where id = " + userId;
        try {
            statement = conn.createStatement();
            ResultSet rs;
            rs = statement.executeQuery(searchDataBase);
            while (rs.next()){
                cellData.add(new FileData(rs.getString("filename"),rs.getString("filesize"),rs.getString("uploaddate"),rs.getString("filesource")));
            }
            storeFile.setItems(cellData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ConnMysql.connect();
        showList(ControlLogin.user.getId());
        userName.setText(ControlLogin.user.getUserName());
//        showList(ControlLogin.user.getId());
    }

}
