package main.java.com.buildsim.util;

import com.google.gson.JsonElement;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ServletUtil {
    public static void returnString(HttpServletResponse resp, String str) {
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text");
        try (PrintWriter pw = resp.getWriter()) {
            pw.write(str);
            pw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void returnJsonResult(HttpServletResponse resp, JsonElement jo){
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("json");
        try (PrintWriter pw = resp.getWriter()){
            pw.print(jo);
            pw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
