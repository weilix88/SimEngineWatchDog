package main.java.com.buildsim.servlet;

import com.google.gson.JsonObject;
import main.java.com.buildsim.cloud.RedisAccess;
import main.java.com.buildsim.cloud.RedisAccessFactory;
import main.java.com.buildsim.util.ServletUtil;
import main.java.com.buildsim.util.StringUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "InspectRedis", urlPatterns = "/InspectRedis")
public class InspectRedis extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonObject res = new JsonObject();

        String key = request.getParameter("key0");
        if(StringUtil.isNullOrEmpty(key)){
            res.addProperty("status", "error");
            res.addProperty("error_msg", "No key found");
            ServletUtil.returnJsonResult(response, res);
            return;
        }

        RedisAccess ra = RedisAccessFactory.getAccess();
        String content = ra.get(key);

        res.addProperty("status", "success");
        res.addProperty("content", content);
        ServletUtil.returnJsonResult(response, res);
    }
}
