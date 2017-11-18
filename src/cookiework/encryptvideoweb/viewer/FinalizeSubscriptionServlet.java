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
public class FinalizeSubscriptionServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String tStar = request.getParameter("tStar");
        JsonObject jsonObject = new JsonObject();
        try {
            BusinessLogic.finalizeSubscription(tStar, id);
            jsonObject.addProperty("result", "success");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Util.writeJsonToResponse(response, jsonObject);
    }
}
