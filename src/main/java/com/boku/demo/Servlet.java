package com.boku.demo;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

@WebServlet("/")
public class Servlet extends HttpServlet {
    AtomicLong totalSum = new AtomicLong();
    boolean isReadyForResponse = false;


    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resetTotalSum();
        isReadyForResponse = false;
        String requestBody = req.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);
        if (requestBody.equals("end")) {
            isReadyForResponse = true;
            resp.getWriter().write(Long.toString(totalSum.get()));
        }
        else {
            try {
                long number = Long.parseLong(requestBody);
                totalSum.addAndGet(number);
                while(!isReadyForResponse) {
                    Thread.onSpinWait();
                }
                resp.getWriter().write(Long.toString(totalSum.get()));
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Invalid input. Please provide a valid number or 'end'.");
            }
        }
    }

    void resetTotalSum() {
        if (isReadyForResponse) totalSum.set(0);
    }
}