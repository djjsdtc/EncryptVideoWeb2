package cookiework.encryptvideoweb;

import com.google.gson.JsonElement;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.sql.*;
import java.util.Properties;

/**
 * Created by Administrator on 2017/01/12.
 */
public class Util {
    //helper function that is used to retrieve a database connection
    public static Connection getConn() {
        Connection conn = null;
        Properties prop = new Properties();
        try {
            prop.load(Util.class.getResourceAsStream("/setting.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String url = prop.getProperty("dburl", "jdbc:mysql://localhost:3306/encvideo2?useUnicode=true&characterEncoding=utf-8"); // a JDBC url
        String username = prop.getProperty("dbusername", "root");
        String password = prop.getProperty("dbpassword", "12345678");

        try {
            // Load the JDBC driver
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (Exception e) {
                System.out.println("bad Driver: " + e);
            }

            conn = DriverManager.getConnection(url, username, password);
            System.out.println("connection stablished");


        } catch (SQLException e) {
            System.out.println("bad conn: " + e);
        }
        return conn;
    }

    public static void writeJsonToResponse(HttpServletResponse response, JsonElement element) throws IOException {
        response.setContentType("text/json; charset=UTF-8");
        OutputStream os = response.getOutputStream();
        os.write(element.toString().getBytes("UTF-8"));
        os.flush();
        os.close();
    }

    public static void writeJsonString(HttpServletResponse response, JsonConvertable jsonObject) throws IOException {
        response.setContentType("text/json; charset=UTF-8");
        OutputStream os = response.getOutputStream();
        os.write(jsonObject.toString().getBytes("UTF-8"));
        os.flush();
        os.close();
    }

    public static String hashPw(String pass) {
        try {
            byte[] passbytes = pass.getBytes();
            MessageDigest mdigest = MessageDigest.getInstance("MD5");
            mdigest.update(passbytes);
            byte hashBytes[] = mdigest.digest();
            StringBuffer sbuffer = new StringBuffer();
            for (int i = 0; i < hashBytes.length; i++) {
                String temp = Integer.toHexString(0xff & hashBytes[i]);
                if (temp.length() == 1)
                    sbuffer.append('0');
                sbuffer.append(temp);
            }

            pass = sbuffer.toString();
            String[] result = null;
        } catch (Exception e) {
            return pass;
        }

        return pass;
    }

    public static boolean confirmSession(String sessionID, String username) {
        Connection conn = Util.getConn();
        try {
            PreparedStatement statement = conn.prepareStatement("Select * from users where clientID = ? and sessionID = ?");
            statement.setString(1, username);
            statement.setString(2, sessionID);

            ResultSet results = statement.executeQuery();
            while (results.next())
                return true;

        } catch (Exception ex) {
            return false;
        }

        return false;
    }

    public static String getEncExecAddr(){
        Properties prop = new Properties();
        try {
            prop.load(Util.class.getResourceAsStream("/setting.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop.getProperty("encpath", "http://10.21.235.201/execute_enc.php");
    }
}
