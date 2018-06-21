package main.java.com.buildsim.util;

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
}
