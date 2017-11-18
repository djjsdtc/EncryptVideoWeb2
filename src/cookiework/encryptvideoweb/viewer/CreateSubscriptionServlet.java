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
public class CreateSubscriptionServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = 0;
        try {
            id = BusinessLogic.createNewSubscriptionID();
        } catch (SQLException e) {
            throw new ServletException(e);
        }
        JsonObject object = new JsonObject();
        object.addProperty("id", id);
        Util.writeJsonToResponse(response, object);
    }
}
