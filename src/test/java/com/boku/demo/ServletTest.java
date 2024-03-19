package com.boku.demo;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.*;


public class ServletTest {
    private Servlet servlet;
    @Mock
    private HttpServletRequest mockRequest;
    @Mock
    private HttpServletResponse mockResponse;
    @Mock
    private HttpServletRequest mockRequestB;
    @Mock
    private HttpServletResponse mockResponseB;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        servlet = new Servlet();
    }


    @Test
    public void testDoPost_withValidNumber() throws Exception {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        StringWriter endStringWriter = new StringWriter();
        PrintWriter endWriter = new PrintWriter(endStringWriter);
        Thread threadA = new Thread(() -> {
            try {
                when(mockRequest.getReader()).thenReturn(new BufferedReader(new StringReader("10")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                when(mockResponse.getWriter()).thenReturn(writer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                servlet.doPost(mockRequest, mockResponse);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        Thread threadB = new Thread(() -> {
            try {
                when(mockRequestB.getReader()).thenReturn(new BufferedReader(new StringReader("end")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                when(mockResponseB.getWriter()).thenReturn(endWriter);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                servlet.doPost(mockRequestB, mockResponseB);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        threadA.start();
        threadB.start();
        threadA.join();
        threadB.join();

        writer.flush();
        assertEquals("10", stringWriter.toString());
    }

    @Test
    public void testDoPost_withEndSignal() throws Exception {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(mockRequest.getReader()).thenReturn(new BufferedReader(new StringReader("end")));
        when(mockResponse.getWriter()).thenReturn(writer);

        servlet.doPost(mockRequest, mockResponse);

        writer.flush();
        assertEquals("0", stringWriter.toString());
    }

    @Test
    public void testDoPost_withInvalidInput() throws Exception {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(mockRequest.getReader()).thenReturn(new BufferedReader(new StringReader("invalid")));
        when(mockResponse.getWriter()).thenReturn(writer);

        servlet.doPost(mockRequest, mockResponse);

        writer.flush();
        assertEquals("Invalid input. Please provide a valid number or 'end'.", stringWriter.toString());
    }

    @Test
    public void testResetTotalSum_whenReadyForResponse() {
        servlet.isReadyForResponse = true;
        servlet.totalSum.set(10);
        servlet.resetTotalSum();
        assertEquals(0, servlet.totalSum.get());
    }

    @Test
    public void testResetTotalSum_whenNotReadyForResponse() {
        servlet.isReadyForResponse = false;
        servlet.totalSum.set(100);
        servlet.resetTotalSum();
        assertEquals(100, servlet.totalSum.get());
    }
}