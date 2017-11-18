package cookiework.encryptvideoweb.publisher;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import cookiework.encryptvideoweb.BusinessLogic;
import cookiework.encryptvideoweb.Util;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by Administrator on 2017/01/14.
 */
public class GetMyFollowersServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        String username = request.getParameter("username");
        try {
            Map<String, Integer> map = BusinessLogic.getMyFollowers(username);
            JsonArray array = new JsonArray();
            for(String key : map.keySet()){
                JsonObject object = new JsonObject();
                object.addProperty("name", key);
                object.addProperty("tagNum", map.get(key));
                array.add(object);
            }
            Util.writeJsonToResponse(response, array);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
