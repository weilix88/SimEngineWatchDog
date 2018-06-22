package main.java.com.buildsim.servlet;

import com.google.gson.JsonObject;
import main.java.com.buildsim.cloud.RedisAccess;
import main.java.com.buildsim.cloud.RedisAccessFactory;
import main.java.com.buildsim.util.ServletUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "TaskQueueLength", urlPatterns = "/TaskQueueLength")
public class TaskQueueLength extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RedisAccess ra = RedisAccessFactory.getAccess();
        long len = ra.llen("TaskQueue");

        JsonObject jo = new JsonObject();
        jo.addProperty("task_queue_len", len);
        ServletUtil.returnJsonResult(response, jo);
    }
}
