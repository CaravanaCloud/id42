package id42;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "HealthCheckServlet", urlPatterns = {"/*"})
public class HealthCheckServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, java.io.IOException {
        response.setStatus(200);
        response.addHeader("Content-Type", "text/plain");
        response.getWriter().println("OK");
    }
}
