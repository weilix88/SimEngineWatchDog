package main.java.com.buildsim.servlet;

import main.java.com.buildsim.cloud.CloudFileUtil;
import main.java.com.buildsim.cloud.CloudFileUtilFactory;
import main.java.com.buildsim.cloud.RedisAccess;
import main.java.com.buildsim.cloud.RedisAccessFactory;
import main.java.com.buildsim.file.Compressor;
import main.java.com.buildsim.init.WatchDogConfig;
import main.java.com.buildsim.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@WebServlet(name = "SimLogCollector", urlPatterns = "/SimLogCollector")
public class SimLogCollector extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String commitId = StringUtil.checkNullAndEmpty(req.getParameter("commit_id"), "CommitId");
        String agent = StringUtil.checkNullAndEmpty(req.getParameter("agent"), "1");
        String engineTimestamp = StringUtil.checkNullAndEmpty(req.getParameter("timestamp"), String.valueOf(System.currentTimeMillis()));
        String engineURL = StringUtil.checkNullAndEmpty(req.getParameter("url"), "EngineURL");
        String msg = StringUtil.checkNullAndEmpty(req.getParameter("msg"), "empty msg");
        String type = StringUtil.checkNullAndEmpty(req.getParameter("type"), "log");

        // return ASAP
        ServletUtil.returnString(resp, "Received");

        String hash = Hasher.hash(commitId, HashMethod.MD5);
        String head = hash.substring(0, 2);

        String dir = WatchDogConfig.readProperty("storage") + head;
        FileUtil.checkFolderCreateIfNotExist(dir);

        String filePath = dir + "\\" + commitId + "_" + agent;
        FileUtil.appendToFile(filePath, lineFormater(engineTimestamp, engineURL, msg, type.toUpperCase()));

        if(type.equalsIgnoreCase("severe_error")){
            try {
                RedisAccess access = RedisAccessFactory.getAccess();
                access.set("TaskSevereError#" + commitId+"_"+agent, msg);
                access.close();
            } catch (IOException e) {}
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    private String lineFormater(String timestamp, String engineURL, String status, String result) {
        return timestamp + ": " + engineURL + " - " + System.lineSeparator()
                + status + System.lineSeparator()
                + result + System.lineSeparator();
    }
}
