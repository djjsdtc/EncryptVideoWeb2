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
public class ViewerRegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        JsonObject jsonObject = new JsonObject();
        try{
            if(!BusinessLogic.isViewerExist(username)){     //没有重名
                BusinessLogic.addNewViewer(username, password);
                jsonObject.addProperty("result", "success");
            }
            else{
                jsonObject.addProperty("result", "user_exists");
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
        Util.writeJsonToResponse(response, jsonObject);
    }
}
