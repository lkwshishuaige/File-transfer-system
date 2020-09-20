package Server;

import java.net.ServerSocket;
import java.net.Socket;

public class Users {
    private int id;
    private String userName;
    private String password;
    private Socket socket;

    Users(int id, String userName, String password){
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.socket = null;
    }

    Users(int id, String userName, String password, Socket socket){
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.socket = socket;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
