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
 * Created by Administrator on 2017/01/16.
 */
public class ViewerLoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        JsonObject jsonObject = new JsonObject();
        try {
            String sessionID = BusinessLogic.validateViewer(username, password);
            if(sessionID != null){
                jsonObject.addProperty("result", "success");
                jsonObject.addProperty("sessionID", sessionID);
            }
            else{
                jsonObject.addProperty("result", "invalid");
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
        Util.writeJsonToResponse(response, jsonObject);
    }
}
