package cookiework.encryptvideoweb.publisher;

import com.google.gson.JsonObject;
import cookiework.encryptvideoweb.BusinessLogic;
import cookiework.encryptvideoweb.Util;
import cookiework.encryptvideoweb.VideoInfo;
import org.bouncycastle.util.encoders.UrlBase64;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/02/22.
 */
public class PublishVideoServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //String userID, VideoInfo info, Map<String, String> tagsAndEncKeys
        /*
        query.setString(2, info.getCipherTitle());
        query.setString(3, info.getCipherIntro());
        query.setString(4, info.getCipherAddr());
        query.setString(5, info.getStatus());
         */
        request.setCharacterEncoding("utf-8");
        String userID = request.getParameter("userID");
        String title = request.getParameter("title");
        String intro = request.getParameter("intro");
        String addr = request.getParameter("addr");
        String status = request.getParameter("status");
        VideoInfo info = new VideoInfo();
        info.setCipherTitle(title);
        info.setCipherIntro(intro);
        info.setCipherAddr(addr);
        info.setStatus(status);

        String tags_b64 = new String(UrlBase64.decode(request.getParameter("tags")), "utf-8");
        String[] tags = tags_b64.split(" ");
        String encKeys_b64 = new String(UrlBase64.decode(request.getParameter("encKeys")), "utf-8");
        String[] encKeys = encKeys_b64.split(" ");
        HashMap<String, String> tagsAndEncKeys = new HashMap<>();
        for(int i = 0; i < tags.length; i++){
            tagsAndEncKeys.put(tags[i], encKeys[i]);
        }

        JsonObject jsonObject = new JsonObject();
        try {
            int id = BusinessLogic.sendToServer(userID, info, tagsAndEncKeys);
            jsonObject.addProperty("id", id);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
        Util.writeJsonToResponse(response, jsonObject);
    }
}
