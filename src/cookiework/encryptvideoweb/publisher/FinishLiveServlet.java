package cookiework.encryptvideoweb.publisher;

import cookiework.encryptvideoweb.BusinessLogic;
import cookiework.encryptvideoweb.Util;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;

/**
 * Created by Administrator on 2017/05/05.
 */
public class FinishLiveServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        String id_str = request.getParameter("id");
        String input = request.getParameter("input");
        try {
            int id = Integer.parseInt(id_str);
            boolean recorded = BusinessLogic.checkRecorded(id);
            BusinessLogic.finishLive(id, recorded);
            PrintWriter out = response.getWriter();
            if(recorded){
                String url = Util.getEncExecAddr();
                url += "?input=" + input + "&id=" + id_str;
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                if(connection.getResponseCode() != HttpURLConnection.HTTP_OK){
                    out.print("error");
                }else {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    out.print(reader.readLine());
                }
            }else{
                out.print("success");
            }
            out.flush();
            out.close();
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
