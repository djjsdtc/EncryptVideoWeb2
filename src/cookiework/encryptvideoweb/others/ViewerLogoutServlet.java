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
public class ViewerLogoutServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String sessionID = request.getParameter("sessionID");
        JsonObject jsonObject = new JsonObject();
        try {
            BusinessLogic.viewerLogout(sessionID);
            jsonObject.addProperty("result", "success");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
        Util.writeJsonToResponse(response, jsonObject);
    }
}
