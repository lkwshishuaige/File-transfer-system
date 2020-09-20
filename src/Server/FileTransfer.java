package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FileTransfer {
    public static boolean share(int userId,Socket socket) throws IOException {
        InputStream is = socket.getInputStream();
        OutputStream os = socket.getOutputStream();
        byte[] bytes = new byte[1024];
        int len = 0;
        len = is.read(bytes);
        String file = new String(bytes, 0, len);
        String fileName = file.split("\n")[0];
        String fileRecipient = file.split("\n")[1];
        File sourceFile = new File(FilePath.storePath + userId + File.separator + fileName);
        File filePath = new File(FilePath.storePath + fileRecipient);
        if(!filePath.exists()){
            os.write("不存在该用户".getBytes());
            os.flush();
            return false;
        }
        File destinationFile = new File(FilePath.storePath + fileRecipient + File.separator + fileName);
        if(!sourceFile.exists()){
            os.write("服务器中没有此文件".getBytes());
            os.flush();
            return false;
        }
        if(destinationFile.exists()){
            os.write("分享的用户已存在此文件".getBytes());
            os.flush();
            return false;
        }
        //新文件输出流
        FileInputStream fileInputStream = new FileInputStream(sourceFile);
        FileOutputStream fileOutputStream = new FileOutputStream (destinationFile);
//将文件流信息读取文件缓存区，如果读取结果不为-1就代表文件没有读取完毕，反之已经读取完毕
        while ((len=fileInputStream.read(bytes))!=-1) {
            fileOutputStream.write(bytes, 0, len);
            fileOutputStream.flush();
        }
        fileInputStream.close();
        fileOutputStream.close();
        String saveDataBase;
        long fileLength = sourceFile.length();
        if(fileLength > 1024 * 1024) {
            saveDataBase = "INSERT INTO file VALUES(" + fileRecipient + ",'" + fileName + "','" + String.format("%.2f",(double)fileLength/1024/1024) + "MB" + "','" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()) + "','" + userId + "的分享')";
        }else {
            saveDataBase = "INSERT INTO file VALUES(" + fileRecipient + ",'" + fileName + "','" + String.format("%.2f",(double)fileLength/1024) + "KB" + "','" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()) + "','" + userId + "的分享')";
        }
        Statement stmt;
        try {
            stmt = ConnMysql.getConn().createStatement();
            int rs;
            rs = stmt.executeUpdate(saveDataBase);
            if (rs != 0)
                socket.getOutputStream().write("分享成功".getBytes());
            else
                socket.getOutputStream().write("分享失败".getBytes());
        }catch (SQLException e){
            e.printStackTrace();
        }
        return true;
    }
    public static boolean upload(int userId, Socket socket) throws IOException {
        ServerSocket serverFileSocket = new ServerSocket(9001);
        Socket fileSocket = serverFileSocket.accept();
        DataInputStream dis = new DataInputStream(fileSocket.getInputStream());
        // 文件名和长度
        String fileName = dis.readUTF();
        long fileLength = dis.readLong();
        File file = new File(FilePath.storePath + userId + File.separator + fileName);//File.separatorChar
        if (file.exists()) {
            OutputStream os = socket.getOutputStream();
            os.write("0".getBytes());
            os.flush();
            os.write("上传失败".getBytes());
            os.flush();
            return false;
        }
        OutputStream os = socket.getOutputStream();
        os.write("1".getBytes());
        os.flush();
//5.创建一个本地字节输出流FileOutputStream对象,构造方法中绑定要输出的目的地
        FileOutputStream fos = new FileOutputStream(file);
        //6.使用网络字节输入流InputStream对象中的方法read,读取客户端上传的文件
        int len = 0;
        byte[] bytes = new byte[1024];
        while ((len = dis.read(bytes)) != -1) {//head=input.read(buffer)) > 0
            fos.write(bytes, 0, len);
            fos.flush();
        }
        dis.close();
        fos.close();
        fileSocket.close();
        serverFileSocket.close();


//            8.使用Socket对象中的方法getOutputStream,获取到网络字节输出流OutputStream对象
//            9.使用网络字节输出流OutputStream对象中的方法write,给客户端回写"上传成功"
        try {
            Statement stmt = null;
            String saveDataBase;
            if(fileLength > 1024 * 1024) {
                saveDataBase = "INSERT INTO file VALUES(" + userId + ",'" + fileName + "','" + String.format("%.2f",(double)fileLength/1024/1024) + "MB" + "','" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()) + "','文件上传')";
            }else {
                saveDataBase = "INSERT INTO file VALUES(" + userId + ",'" + fileName + "','" + String.format("%.2f",(double)fileLength/1024) + "KB" + "','" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()) + "','文件上传')";
            }
            stmt = ConnMysql.getConn().createStatement();
            int rs;
            rs = stmt.executeUpdate(saveDataBase);
            if (rs != 0)
                socket.getOutputStream().write("上传成功".getBytes());
            else
                socket.getOutputStream().write("上传失败".getBytes());
//            10.释放资源(FileOutputStream,Socket,ServerSocket)
        } catch (SQLException e) {
            System.out.println(e);
        }
        return true;
    }
    public static boolean download(int userId, Socket socket) throws IOException {
        String fileName;
        byte[] bytes = new byte[1024];
        int len = 0;
        InputStream is = socket.getInputStream();
        len = is.read(bytes);
        fileName = new String(bytes,0,len);
        File file = new File(FilePath.storePath + userId + File.separator + fileName);
        if(!file.exists()){
            OutputStream os = socket.getOutputStream();
            os.write("0".getBytes());
            os.flush();
            return false;
        }
        OutputStream os = socket.getOutputStream();
        os.write("1".getBytes());
        os.flush();
        ServerSocket serverFileSocket = new ServerSocket(9001);
        Socket fileSocket = serverFileSocket.accept();

        DataOutputStream dos = new DataOutputStream(fileSocket.getOutputStream());
        FileInputStream fis = new FileInputStream(file);
        // 文件名和长度
        dos.writeUTF(file.getName());
        dos.flush();
        dos.writeLong(file.length());
        dos.flush();

        //开始传输文件
        while((len = fis.read(bytes))!=-1){
            dos.write(bytes,0,len);
            dos.flush();
        }
        dos.close();
        fis.close();
        fileSocket.close();
        serverFileSocket.close();
        return true;
    }
    public static boolean delete(int userId, Socket socket) throws IOException {
        InputStream is = socket.getInputStream();
        OutputStream os = socket.getOutputStream();
        byte[] bytes = new byte[1024];
        int len = 0;
        len = is.read(bytes);
        String fileName = new String(bytes, 0, len);
        File file = new File(FilePath.storePath + userId + File.separator + fileName);
        if(!file.exists()){
            os.write("服务器中没有此文件".getBytes());
            os.flush();
            return false;
        }
        file.delete();
        try {
            Statement stmt = null;
            String saveDataBase;
                saveDataBase = "DELETE FROM file WHERE id =" + userId + " AND filename ='" + fileName + "'";
            stmt = ConnMysql.getConn().createStatement();
            int rs;
            rs = stmt.executeUpdate(saveDataBase);
            if (rs != 0) {
                os.write("删除成功".getBytes());
                os.flush();
            }
            else {
                os.write("删除失败".getBytes());
                os.flush();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }
    public static void quit(Socket socket) throws IOException {
        socket.close();
    }
}
