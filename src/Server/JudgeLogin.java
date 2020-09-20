package Server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JudgeLogin {
    public static Users judgeLogin(Socket socket) throws IOException, SQLException {
        InputStream is = null;
        is = socket.getInputStream();
        //读取Socket对象传入的账号密码
        byte[] bytes = new byte[1024];
        int len = is.read(bytes);
        String np = new String(bytes, 0, len);    //账号密码共用一个字符串
        String[] namePassword = np.split("\n");     //分割账号密码
        Connection conn = ConnMysql.getConn();
        Statement stmt = null;
        stmt = conn.createStatement();
        if (namePassword.length == 2) {
            ResultSet rs;
            rs = stmt.executeQuery("SELECT * FROM USER WHERE id = " + namePassword[0] + " AND password = '" + namePassword[1] + "'");
            if (rs.next())
                return new Users(rs.getInt("id"), rs.getString("username"), rs.getString("password"), socket);
//            return user;
        }else if(namePassword.length == 3){
            int rs;
            try {
                rs = stmt.executeUpdate("insert into USER values(" + namePassword[0] + ",'" + namePassword[1] + "','" + namePassword[2] + "')");
                String filePath = FilePath.storePath + namePassword[0] + File.separator;
                new File(filePath).mkdir();
                return new Users(Integer.valueOf(namePassword[0]), namePassword[1], namePassword[2]);
            }catch (SQLException e){
                return new Users(Integer.valueOf(namePassword[0]), null, namePassword[2]);
            }
        }
        return null;
    }
}
