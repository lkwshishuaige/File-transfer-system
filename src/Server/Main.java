package Server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

public class Main implements Runnable {
    Socket socket;
    Users users;
    Main(){
        this(null,null);
    }
    Main(Socket socket) {
        this(socket,null);
    }
    Main(Socket socket, Users users){
        this.socket = socket;
        this.users = users;
    }

    public static void main(String args[]) throws Exception {
        ConnMysql.connect();
        ServerSocket serverSocket = new ServerSocket(9000);
        System.out.println("Server Start and Listening: ");

        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("Connected");
            new Thread(new Main(socket)).start();
        }
    }

    @Override
    public void run() {
        try {
            if ((this.users = JudgeLogin.judgeLogin(socket)) != null) {
                if(users.getSocket() == null) {
                    OutputStream os = socket.getOutputStream();
                    if (this.users.getUserName() != null) {
                        os.write("1".getBytes());
                    } else {
                        os.write("0".getBytes());
                    }
                }else {
                    OutputStream os = socket.getOutputStream();
                    InputStream is = socket.getInputStream();
                    os.write(("u:" + users.getUserName()).getBytes());
                    while (true) {
                        byte[] bytes = new byte[1024];
                        int len = is.read(bytes);
                        String func = new String(bytes, 0, len);
                        switch (func) {
                            case "0":
                                FileTransfer.share(this.users.getId(),socket);
                                break;
                            case "1":
                                FileTransfer.upload(this.users.getId(), socket);
                                break;
                            case "2":
                                FileTransfer.download(this.users.getId(), socket);
                                break;
                            case "3":
                                FileTransfer.delete(this.users.getId(), socket);
                                break;
                            case "4":
                                FileTransfer.quit(socket);
                                break;
                            default:
                                break;
                        }
                        if(func.equals("4"))
                            break;
                    }
                }
            } else {
                OutputStream os = socket.getOutputStream();
                os.write("error:".getBytes());
            }
        } catch (IOException | SQLException e) {
            System.out.println(e);
        }finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}