package Server;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnMysql {
    private static Connection conn;
    public static void connect() {
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://47.114.130.91/FTS?useSSL=false&serverTimezone=GMT", "root", "lkw19991124.");
        } catch (Exception se) {
            se.printStackTrace();
        }
    }

    public static Connection getConn(){
        return conn;
    }

    public void disconnect(Connection conn) {
        try {
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Connection conn = null;
        ConnMysql.connect();
        conn = ConnMysql.getConn();
        if (conn != null)
            System.out.println("连接成功");
    }
}
