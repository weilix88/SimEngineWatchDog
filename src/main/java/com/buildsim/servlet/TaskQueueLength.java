package main.java.com.buildsim.servlet;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
        JsonParser jp = new JsonParser();
        JsonArray list = new JsonArray();

        RedisAccess ra = RedisAccessFactory.getAccess();

        String jsonStr;
        int idx = 0;
        while((jsonStr=ra.lindx("TaskQueue", idx)) != null){
            idx++;

            try {
                JsonObject jo = jp.parse(jsonStr).getAsJsonObject();
                list.add("Commit id: " +
                        jo.get("commit_id").getAsString() + "_" +
                        jo.get("parallel_agent").getAsString());
            }catch (Exception e){
                list.add("Error: "+e.getMessage());
            }
        }

        JsonObject res = new JsonObject();
        res.addProperty("task_queue_len", list.size());
        res.add("tasks", list);
        ServletUtil.returnJsonResult(response, res);
    }
}
