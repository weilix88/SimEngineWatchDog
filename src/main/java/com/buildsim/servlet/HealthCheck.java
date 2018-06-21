package main.java.com.buildsim.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet(name = "HealthCheck", urlPatterns = "/HealthCheck")
public class HealthCheck extends HttpServlet {
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //LOG.info("Receive request");
    	
    	response.setStatus(HttpServletResponse.SC_OK);
    }
}
