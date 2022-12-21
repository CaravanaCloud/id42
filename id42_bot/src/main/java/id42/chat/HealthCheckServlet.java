package id42.chat;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "HealthCheckServlet", urlPatterns = {"/*"})
public class HealthCheckServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Inject HAL hal;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, java.io.IOException {
        var text = request.getParameter("text");
        var input = Input.of(text);
        var reply = hal.ask(input);
        response.setStatus(200);
        response.addHeader("Content-Type", "text/plain");
        var writer = response.getWriter();
        if (text != null)
            writer.println(">> " + text);
        if (reply != null)
            writer.println("<< " + reply);
        writer.println("...");
    }
}
