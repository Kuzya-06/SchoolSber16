package ru.sber.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@WebServlet(name = "ProxyServlet", value = "/proxy")
public class ProxyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String targetUrl = req.getParameter("url");

        if (targetUrl == null || targetUrl.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Отсутствует параметр «url»");
            return;
        }

        URL url = new URL(targetUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        resp.setContentType(connection.getContentType());
        resp.setStatus(connection.getResponseCode());

        try (InputStream inputStream = connection.getInputStream(); OutputStream outputStream = resp.getOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
    }

    @Override
    public void init() throws ServletException {
        System.out.println("метод init()");
    }

    @Override
    public void destroy() {
        System.out.println("метод destroy()");
    }
}
