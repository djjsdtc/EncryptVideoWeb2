/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cookiework.encryptvideoweb;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.IOException;
import java.sql.*;
import java.util.*;

import static cookiework.encryptvideoweb.SubscriptionInfo.*;

/**
 * @author Andrew
 */
public class BusinessLogic {

    //The following method is used to send a message, it stores the message on the database
    //with the userID, ciphertext, tStar and eMail
    public static int sendToServer(String userID, VideoInfo info, Map<String, String> tagsAndEncKeys) throws SQLException {
        //String dbQuery = "INSERT INTO storedmessages (userID, ciphertext, tStar, eMail) VALUES ('" + userID + "','"+ ct + "','" + tStar +"','"+ eMail +"')";

        //Step 1: insert cipherText
        Connection conn = Util.getConn();
        PreparedStatement query = conn.prepareStatement("INSERT INTO storedvideos (userID, cipherTitle, cipherIntro, cipherAddr, status) VALUES (?,?,?,?,?)");
        query.setString(1, userID);
        query.setString(2, info.getCipherTitle());
        query.setString(3, info.getCipherIntro());
        query.setString(4, info.getCipherAddr());
        query.setString(5, info.getStatus());
        query.executeUpdate();
        //Step 2: get message ID
        query = conn.prepareStatement("SELECT LAST_INSERT_ID() FROM storedvideos");
        ResultSet results = query.executeQuery();
        results.next();
        int videoId = results.getInt(1);
        //Step 3: insert encrypted tags
        for (String tStar : tagsAndEncKeys.keySet()) {
            query = conn.prepareStatement("INSERT INTO storedtags (messageID, tStar, encKey) VALUES (?,?,?)");
            query.setInt(1, videoId);
            query.setString(2, tStar);
            query.setString(3, tagsAndEncKeys.get(tStar));
            query.executeUpdate();
        }
        conn.close();
        return videoId;
    }

    //This method adds a subscription to the database with a status of 0, this status refers to a new
    //subscription request that is sent to the recipient with the M request.
    public static void addSubscription(String userID, String M, String destUserID, String id) throws SQLException {

        //String dbQuery = "UPDATE subscriptions set clientID = '" + clientID + "', email ='"+ email + "', status = '" + status +"' , M = '"+ M +"', destClientID = '"+ destClientID +"', destEmail = '"+destEmail+"', tagName = '"+tagName+"' WHERE id='" + id +"'";
        //String dbQuery = "Insert into subscriptions (clientID, email, status, M, destClientID, destEmail, tagName) values ('"+ clientID+ "','" + email + "','" + status +"','" + M +"','" + destClientID + "','" + destEmail + "','" + tagName + "')";

        Connection conn = Util.getConn();
        PreparedStatement query = conn.prepareStatement("UPDATE subscriptions set userID = ?, status = ? , M = ?, destUserID = ? WHERE id=?");
        query.setString(1, userID);
        query.setString(2, SUBSCRIPTION_ISSUE);
        query.setString(3, M);
        query.setString(4, destUserID);
        query.setInt(5, Integer.parseInt(id));

        query.execute();
        conn.close();
    }

    //Updates the subscription to status of 1, meaning that the subscription has been
    //approved and the MPrime is now stored in the database for this subscription request
    public static void updateSubscription(String MPrime, String id) throws SQLException {
        //String dbQuery = "Update subscriptions Set status = 1, MPrime = '" + MPrime + "' WHERE id=" + id;

        Connection conn = Util.getConn();
        PreparedStatement query = conn.prepareStatement("Update subscriptions Set status = ?, MPrime = ?, M=null WHERE id=?");
        query.setString(1, SUBSCRIPTION_APPROVE);
        query.setString(2, MPrime);
        query.setInt(3, Integer.parseInt(id));
        query.execute();
        conn.close();
    }

    //This method updates the status of the subscription to 2 (finalized) and stores the tStar value
    public static void finalizeSubscription(String tStar, String id) throws SQLException {
        //String dbQuery = "Update subscriptions Set status = 2, tag = '" + tStar + "' WHERE id=" + id;


        Connection conn = Util.getConn();
        PreparedStatement query = conn.prepareStatement("Update subscriptions Set status = ?, tagName = ?, MPrime=null WHERE id=?");
        query.setString(1, SUBSCRIPTION_FINALIZE);
        query.setString(2, tStar);
        query.setInt(3, Integer.parseInt(id));
        query.execute();
        conn.close();

    }

    //This method is used to retrieve the users pending subscriptions for display
    //显示所有我收到的请求
    public static ArrayList<SubscriptionInfo> getMySubscriptions(String username) throws SQLException {
        ArrayList<SubscriptionInfo> mySubscriptions = new ArrayList<>();


        Connection conn = Util.getConn();
        PreparedStatement query = conn.prepareStatement("select userID, M, id from subscriptions where status = ? AND destUserID = ?");
        query.setString(1, SUBSCRIPTION_ISSUE);
        query.setString(2, username);
        //String dbQuery = "select clientID, M, id, tagName from subscriptions where status = 0 AND destClientID ='" + ClientID +"'";

        ResultSet results = query.executeQuery();

        while (results.next()) {
            SubscriptionInfo entry = new SubscriptionInfo();
            entry.setId(results.getInt("id"));
            entry.setUserID(results.getString("userID"));
            entry.setDestUserID(username);
            entry.setM(results.getString("M"));
            entry.setMPrime("");
            entry.setTagName("");
            entry.setStatus(SUBSCRIPTION_ISSUE);

            mySubscriptions.add(entry);
        }
        conn.close();

        return mySubscriptions;
    }

    //显示所有我发出的请求
    public static ArrayList<SubscriptionInfo> getMyPending(String username) throws SQLException {
        ArrayList<SubscriptionInfo> mySubscriptions = new ArrayList<>();


        Connection conn = Util.getConn();
        PreparedStatement query = conn.prepareStatement("select destUserID, id from subscriptions where status = ? AND userID = ?");
        query.setString(1, SUBSCRIPTION_ISSUE);
        query.setString(2, username);

        ResultSet results = query.executeQuery();

        while (results.next()) {
            SubscriptionInfo entry = new SubscriptionInfo();
            entry.setId(results.getInt("id"));
            entry.setUserID(username);
            entry.setDestUserID(results.getString("destUserID"));
            entry.setM("");
            entry.setMPrime("");
            entry.setTagName("");
            entry.setStatus(SUBSCRIPTION_ISSUE);

            mySubscriptions.add(entry);
        }
        conn.close();

        return mySubscriptions;
    }

    //This method is used to retrieve the users finalize subscriptions for display
    public static ArrayList<SubscriptionInfo> getMyFinalize(String username) throws SQLException {
        ArrayList<SubscriptionInfo> myFinalize = new ArrayList<>();


        Connection conn = Util.getConn();
        PreparedStatement query = conn.prepareStatement("select destUserID, MPrime, id from subscriptions where status = ? AND userID = ?");
        //String dbQuery = "select destClientID, MPrime, id, tagName from subscriptions where status = 1 AND clientID ='" + ClientID +"'";
        query.setString(1, SUBSCRIPTION_APPROVE);
        query.setString(2, username);

        ResultSet results = query.executeQuery();

        while (results.next()) {
            SubscriptionInfo entry = new SubscriptionInfo();
            entry.setId(results.getInt("id"));
            entry.setUserID(username);
            entry.setDestUserID(results.getString("destUserID"));
            entry.setM("");
            entry.setMPrime(results.getString("MPrime"));
            entry.setTagName("");
            entry.setStatus(SUBSCRIPTION_APPROVE);

            myFinalize.add(entry);
        }
        conn.close();

        return myFinalize;
    }


    //Retrieves users information (currently just e-mail and public key variables) based on login
    public static String[] getPublicKey(String username) throws SQLException {
        String[] publicKey = null;

        Connection conn = Util.getConn();
        PreparedStatement query = conn.prepareStatement("select * from publishers where clientID = ?");
        query.setString(1, username);
        //String dbQuery = "select clientId, email, e, N from users where clientID = '" +login+ "'";

        ResultSet results = query.executeQuery();
        if (results.next()) {
            String e = results.getString("e");
            String N = results.getString("N");
            publicKey = new String[]{e, N};
        }
        conn.close();

        return publicKey;
    }

    public static boolean isViewerExist(String username) throws SQLException {
        boolean result = false;

        Connection conn = Util.getConn();
        PreparedStatement query = conn.prepareStatement("select * from viewers where clientID = ?");
        query.setString(1, username);
        //String dbQuery = "select clientId, email, e, N from users where clientID = '" +login+ "'";

        ResultSet results = query.executeQuery();
        if (results.next()) {
            result = true;
        }
        conn.close();

        return result;
    }

    //This validates a user by returning an array or clientID and e-mail,
    //if it is empty then user does not exist or login/password combination is incorrect
    public static String validatePublisher(String username, String password) throws SQLException {
        Connection conn = Util.getConn();
        String sessionID = null;

        PreparedStatement query = conn.prepareStatement("select clientID from publishers where clientID = ? AND password = ?");
        query.setString(1, username);
        query.setString(2, password);
        ResultSet results = query.executeQuery();

        if (results.next()) {
            sessionID = UUID.randomUUID().toString();                   //session ID
            query = conn.prepareStatement("update publishers set sessionID = ? where clientID = ? AND password = ?");
            query.setString(1, sessionID);
            query.setString(2, username);
            query.setString(3, password);
            query.executeUpdate();
        }
        conn.close();

        return sessionID;
    }

    public static String validateViewer(String username, String password) throws SQLException {
        Connection conn = Util.getConn();
        String sessionID = null;

        PreparedStatement query = conn.prepareStatement("select clientID from viewers where clientID = ? AND password = ?");
        query.setString(1, username);
        query.setString(2, password);
        ResultSet results = query.executeQuery();

        if (results.next()) {
            sessionID = UUID.randomUUID().toString();                   //session ID
            query = conn.prepareStatement("update viewers set sessionID = ? where clientID = ? AND password = ?");
            query.setString(1, sessionID);
            query.setString(2, username);
            query.setString(3, password);
            query.executeUpdate();
        }
        conn.close();

        return sessionID;
    }

    public static void publisherLogout(String sessionID) throws SQLException {
        Connection conn = Util.getConn();
        PreparedStatement query = conn.prepareStatement("update publishers set sessionID = null where sessionID = ?");
        query.setString(1, sessionID);
        query.executeUpdate();
        conn.close();
    }

    public static void viewerLogout(String sessionID) throws SQLException {
        Connection conn = Util.getConn();
        PreparedStatement query = conn.prepareStatement("update viewers set sessionID = null where sessionID = ?");
        query.setString(1, sessionID);
        query.executeUpdate();
        conn.close();
    }

    //returns a list of current tags based on the login
    public static ArrayList<SubscriptionInfo> getMyTags(String login) throws SQLException {
        ArrayList<SubscriptionInfo> tagInfo = new ArrayList<>();

        Connection conn = Util.getConn();
        PreparedStatement query = conn.prepareStatement("select tagName, destUserID, id from subscriptions where status = ? AND userID = ?");
        query.setString(1, SUBSCRIPTION_FINALIZE);
        query.setString(2, login);
        //String dbQuery = "select tag, tagName, destClientID, id from subscriptions where status = 2 AND clientID = '" +login+ "';";
        //query.executeQuery(dbQuery);

        ResultSet results = query.executeQuery();

        while (results.next()) {
            SubscriptionInfo entry = new SubscriptionInfo();
            entry.setTagName(results.getString("tagName"));
            entry.setId(results.getInt("id"));
            entry.setDestUserID(results.getString("destUserID"));

            tagInfo.add(entry);
        }
        conn.close();

        return tagInfo;
    }

    //获取关注我的人的列表
    public static Map<String, Integer> getMyFollowers(String login) throws SQLException {
        HashMap<String, Integer> followers = new HashMap<>();

        Connection conn = Util.getConn();
        PreparedStatement query = conn.prepareStatement("select userID, count(id) as tagNum from subscriptions where status = ? AND destUserID = ? group by userID");
        query.setString(1, SUBSCRIPTION_FINALIZE);
        query.setString(2, login);
        //String dbQuery = "select tag, tagName, destClientID, id from subscriptions where status = 2 AND clientID = '" +login+ "';";
        //query.executeQuery(dbQuery);

        ResultSet results = query.executeQuery();

        while (results.next()) {
            String userID = results.getString("userID");
            int count = results.getInt("tagNum");
            followers.put(userID, count);
        }
        conn.close();

        return followers;
    }

    //returns stored messages(cipherText,encKey) with a given T* tag
    public static ArrayList<VideoInfo> getMessages(String destUser, String tStar) throws SQLException {
        //tStar = tStar.replace(" ", "+");
        ArrayList<VideoInfo> messages = new ArrayList<>();

        Connection conn = Util.getConn();
        PreparedStatement query = conn.prepareStatement("select messageId,encKey from storedtags where tStar = ?");
        query.setString(1, tStar);
        //String dbQuery = "select ciphertext from storedmessages where tStar = '"+ tStar +"';";
        //query.executeQuery(dbQuery);

        ResultSet results = query.executeQuery();

        while (results.next()) {
            String encKey = results.getString("encKey");
            int messageId = results.getInt("messageId");
            PreparedStatement newQuery = conn.prepareStatement("select cipherTitle, cipherIntro, cipherAddr, status from storedvideos where id=? and userID=?");
            newQuery.setInt(1, messageId);
            newQuery.setString(2, destUser);
            ResultSet newResults = newQuery.executeQuery();
            if (newResults.next()) {
                VideoInfo info = new VideoInfo();
                info.setId(0);
                info.setEncKey(encKey);
                info.setCipherTitle(newResults.getString("cipherTitle"));
                info.setCipherIntro(newResults.getString("cipherIntro"));
                info.setCipherAddr(newResults.getString("cipherAddr"));
                info.setStatus(newResults.getString("status"));
                messages.add(info);
            }
        }
        conn.close();

        return messages;
    }

    //查看所有我的投稿
    public static ArrayList<VideoInfo> getMyMessages(String destUser, String type) throws SQLException {
        //tStar = tStar.replace(" ", "+");
        ArrayList<VideoInfo> messages = new ArrayList<>();

        Connection conn = Util.getConn();
        PreparedStatement query;
        if(type == null || type.equals("") || type.equals("all")){
            query = conn.prepareStatement("select id, cipherTitle, cipherIntro, cipherAddr, status from storedvideos where userID=?");
        } else {
            query = conn.prepareStatement("select id, cipherTitle, cipherIntro, cipherAddr, status from storedvideos where userID=? and status=?");
            query.setString(2, type);
        }
        query.setString(1, destUser);
        ResultSet results = query.executeQuery();
        while (results.next()) {
            VideoInfo info = new VideoInfo();
            info.setId(results.getInt("id"));
            info.setCipherTitle(results.getString("cipherTitle"));
            info.setCipherIntro(results.getString("cipherIntro"));
            info.setCipherAddr(results.getString("cipherAddr"));
            info.setStatus(results.getString("status"));
            info.setEncKey("");
            messages.add(info);
        }
        conn.close();

        return messages;
    }

    public static void addNewPublisher(String e, String N, String username, String pass) throws SQLException {
    //public static void addNewPublisher(String username, String pass) throws SQLException {
        //String dbQuery = "Insert Into users (e, N, password, clientID, email) Values ('" + e + "','" + N + "','" + pass + "','" + clientID + "','" + email + "');";

        Connection conn = Util.getConn();
        PreparedStatement query = conn.prepareStatement("Insert Into publishers (e, N, password, clientID) Values (?,?,?,?)");
        //PreparedStatement query = conn.prepareStatement("Insert Into publishers (password, clientID) Values (?,?)");
        query.setString(1, e);
        query.setString(2, N);
        query.setString(3, pass);
        //query.setString(1, pass);
        query.setString(4, username);
        //query.setString(2, username);

        query.execute();
        conn.close();
    }

    public static void addNewViewer(String username, String pass) throws SQLException {
        Connection conn = Util.getConn();
        PreparedStatement query = conn.prepareStatement("Insert Into viewers (password, clientID) Values (?,?)");
        query.setString(1, pass);
        query.setString(2, username);

        query.execute();
        conn.close();
    }

    //creates a blank entry to get a new subscriptionID
    public static int createNewSubscriptionID() throws SQLException {
        String dbQuery = "Insert Into subscriptions (userID) Values ('');";

        Connection conn = Util.getConn();
        Statement query = conn.createStatement();
        query.execute(dbQuery);
        ResultSet results = query.executeQuery("SELECT LAST_INSERT_ID() FROM subscriptions");
        results.next();

        int result = results.getInt(1);
        conn.close();
        return result;
    }

    public static void finishLive(int id, boolean recorded) throws SQLException{
        Connection conn = Util.getConn();
        PreparedStatement query = conn.prepareStatement("UPDATE storedvideos SET status=? WHERE id=?");
        if(recorded) {
            query.setString(1, "transform");
        }else{
            query.setString(1, "end");
        }
        query.setInt(2, id);

        query.execute();
        conn.close();
    }

    public static boolean checkRecorded(int id) throws SQLException{
        Connection conn = Util.getConn();
        PreparedStatement query = conn.prepareStatement("select status from storedvideos where id = ?");
        query.setInt(1, id);
        ResultSet results = query.executeQuery();
        boolean recorded = false;

        if (results.next()) {
            String status = results.getString(1);
            if(status.equals("live")){
                try {
                    Properties prop = new Properties();
                    prop.load(Util.class.getResourceAsStream("/setting.properties"));
                    recorded = Boolean.parseBoolean(prop.getProperty("record_live", "false"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        conn.close();
        return recorded;
    }
}
