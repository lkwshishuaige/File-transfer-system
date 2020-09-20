package Client.Main;

import Server.ConnMysql;
import javafx.beans.property.SimpleStringProperty;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public final class FileData {
    private final SimpleStringProperty fileName;
    private final SimpleStringProperty fileSize;
    private final SimpleStringProperty uploadDate;
    private final SimpleStringProperty fileSource;

    public FileData(){
        this(null,null,null,null);
    }
    public FileData(String fileName,String fileSize,String uploadData,String fileSource){
        this.fileName = new SimpleStringProperty(fileName);
        this.fileSize = new SimpleStringProperty(fileSize);
        this.uploadDate = new SimpleStringProperty(uploadData);
        this.fileSource = new SimpleStringProperty(fileSource);
    }

    public String getFileName() {
        return fileName.get();
    }
    public void setFileName(String fileName) {
        this.fileName.set(fileName);
    }

    public SimpleStringProperty fileNameProperty() {
        return fileName;
    }

    public String getFileSize() {
        return fileSize.get();
    }

    public void setFileSize(String fileSize) {
        this.fileSize.set(fileSize);
    }

    public SimpleStringProperty fileSizeProperty() {
        return fileSize;
    }

    public String getUploadDate() {
        return uploadDate.get();
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate.set(uploadDate);
    }

    public SimpleStringProperty uploadDateProperty() {
        return uploadDate;
    }

    public String getFileSource() {
        return fileSource.get();
    }

    public void setFileSource(String fileSource) {
        this.fileSource.set(fileSource);
    }

    public SimpleStringProperty fileSourceProperty() {
        return fileSource;
    }
}
