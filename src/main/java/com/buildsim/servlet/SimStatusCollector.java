package main.java.com.buildsim.servlet;

import main.java.com.buildsim.init.WatchDogConfig;
import main.java.com.buildsim.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "SimStatusCollector", urlPatterns = "/SimStatusCollector")
public class SimStatusCollector extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String commitId = StringUtil.checkNullAndEmpty(req.getParameter("commit_id"), "CommitId");
        String agent = StringUtil.checkNullAndEmpty(req.getParameter("Agent"), "1");
        String engineTimestamp = StringUtil.checkNullAndEmpty(req.getParameter("timestamp"), String.valueOf(System.currentTimeMillis()));
        String engineURL = StringUtil.checkNullAndEmpty(req.getParameter("url"), "EngineURL");
        String simStatus = StringUtil.checkNullAndEmpty(req.getParameter("status"), "Status");
        String simResult = StringUtil.checkNullAndEmpty(req.getParameter("sim_results"), "Result");

        // return ASAP
        ServletUtil.returnString(resp, "Received");

        String hash = Hasher.hash(commitId, HashMethod.MD5);
        String head = hash.substring(0, 2);

        String dir = WatchDogConfig.readProperty("storage")+head;
        FileUtil.checkFolderCreateIfNotExist(dir);

        String filePath = dir+"\\"+commitId+"_"+agent;
        FileUtil.appendToFile(filePath, lineFormater(engineTimestamp, engineURL, simStatus, simResult));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    private String lineFormater(String timestamp, String engineURL, String status, String result){
        return timestamp+": "+engineURL+" - "+System.lineSeparator()
                +status+System.lineSeparator()
                +result+System.lineSeparator();
    }
}
