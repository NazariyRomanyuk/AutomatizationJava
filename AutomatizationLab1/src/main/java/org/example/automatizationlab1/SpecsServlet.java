package org.example.automatizationlab1;

import java.io.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;

@WebServlet(name = "specsServlet", value = "/specs-servlet")
public class SpecsServlet extends HttpServlet {
    private String[] specs;

    public void init() {
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hardwareAbstractionLayer = systemInfo.getHardware();
        CentralProcessor centralProcessor = hardwareAbstractionLayer.getProcessor();
        specs = new String[3];
        specs[0] = "CPU: " + centralProcessor.getProcessorIdentifier().getName();
        specs[1] = "RAM: " + String.format("%.2f", (hardwareAbstractionLayer.getMemory().getTotal() / (1024.0 * 1024 * 1024))) + " GB";
        specs[2] = "GPU: " + hardwareAbstractionLayer.getGraphicsCards().get(0).getName();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        for (String spec : specs) {
            out.println("<h2>" + spec + "</h2>");
        }
        out.println("</body></html>");
    }

    public void destroy() {
    }
}
