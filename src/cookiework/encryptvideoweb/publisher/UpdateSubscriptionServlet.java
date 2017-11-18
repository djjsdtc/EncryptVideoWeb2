package cookiework.encryptvideoweb.publisher;

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
public class UpdateSubscriptionServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String MPrime = request.getParameter("MPrime");
        JsonObject object = new JsonObject();
        try {
            BusinessLogic.updateSubscription(MPrime, id);
            object.addProperty("result", "success");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
        Util.writeJsonToResponse(response, object);
    }
}
