package main.java.com.buildsim.servlet;

import main.java.com.buildsim.init.WatchDogConfig;
import main.java.com.buildsim.util.FileUtil;
import main.java.com.buildsim.util.StringUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "DaemonInfoCollector", urlPatterns = "/DaemonInfoCollector")
public class DaemonInfoCollector extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String engineTimestamp = StringUtil.checkNullAndEmpty(req.getParameter("timestamp"), String.valueOf(System.currentTimeMillis()));
        String engineURL = StringUtil.checkNullAndEmpty(req.getParameter("url"), "EngineURL");
        String msg = StringUtil.checkNullAndEmpty(req.getParameter("msg"), "empty msg");
        String type = StringUtil.checkNullAndEmpty(req.getParameter("type"), "log");

        FileUtil.appendToFile(WatchDogConfig.readProperty("daemonLog"), lineFormater(engineURL, engineTimestamp, msg, type));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    private String lineFormater(String engineURL, String timestamp, String msg, String type) {
        return engineURL +":" + timestamp + " - " + System.lineSeparator()
                + msg + System.lineSeparator()
                + type + System.lineSeparator();
    }
}
