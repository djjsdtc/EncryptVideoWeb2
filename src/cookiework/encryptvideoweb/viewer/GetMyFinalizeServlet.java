package cookiework.encryptvideoweb.viewer;

import com.google.gson.JsonArray;
import cookiework.encryptvideoweb.BusinessLogic;
import cookiework.encryptvideoweb.SubscriptionInfo;
import cookiework.encryptvideoweb.Util;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/01/14.
 */
public class GetMyFinalizeServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        String username = request.getParameter("username");
        try {
            ArrayList<SubscriptionInfo> infos = BusinessLogic.getMyFinalize(username);
            JsonArray array = new JsonArray();
            for(SubscriptionInfo info : infos){
                array.add(info.toJsonElement());
            }
            Util.writeJsonToResponse(response, array);
        } catch (SQLException e) {
            throw new ServletException(e);
        }

    }
}
