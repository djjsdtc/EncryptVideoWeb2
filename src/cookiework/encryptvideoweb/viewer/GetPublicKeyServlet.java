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
public class GetPublicKeyServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        String destUser = request.getParameter("destUser");
        String publicKey[] = null;
        try {
            publicKey = BusinessLogic.getPublicKey(destUser);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
        JsonObject object = new JsonObject();
        if (publicKey != null) {
            object.addProperty("result", "success");
            object.addProperty("e", publicKey[0]);
            object.addProperty("N", publicKey[1]);
        } else {
            object.addProperty("result", "notexist");
        }
        Util.writeJsonToResponse(response, object);
    }
}
