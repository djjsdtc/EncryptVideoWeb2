package cookiework.encryptvideoweb.viewer;

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
 * Created by Administrator on 2017/01/14.
 */
public class AddSubscriptionServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("utf-8");
        String username = request.getParameter("username");
        String M = request.getParameter("M");
        String destUser = request.getParameter("destUser");
        String id = request.getParameter("id");
        JsonObject jsonObject = new JsonObject();
        try {
            BusinessLogic.addSubscription(username, M, destUser, id);
            jsonObject.addProperty("result", "success");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
        Util.writeJsonToResponse(response, jsonObject);
    }
}
