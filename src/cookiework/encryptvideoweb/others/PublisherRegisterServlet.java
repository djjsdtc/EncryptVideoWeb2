package cookiework.encryptvideoweb.others;

import com.google.gson.JsonObject;
import cookiework.encryptvideoweb.BusinessLogic;
import cookiework.encryptvideoweb.Util;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Administrator on 2017/01/13.
 */
public class PublisherRegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String e = request.getParameter("e");
        String N = request.getParameter("N");
        JsonObject jsonObject = new JsonObject();
        try{
            String[] testPk = BusinessLogic.getPublicKey(username);
            if(testPk == null){     //没有重名
                BusinessLogic.addNewPublisher(e, N, username, password);
                jsonObject.addProperty("result", "success");
            }
            else{
                jsonObject.addProperty("result", "user_exists");
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
        Util.writeJsonToResponse(response, jsonObject);
    }
}
