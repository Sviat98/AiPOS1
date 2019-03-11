package model;

import commands.Command;
import commands.CommandName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import java.io.*;

import static junit.framework.TestCase.assertEquals;

public class POP3ConnectionTest {
    POP3Connection connection = new POP3Connection();
    SSLSocketFactory factory;
    SSLSocket socket;
    String response;
    private BufferedReader inputStream = null;
    private BufferedWriter outputStream = null;

    @Before
    public void setUp() throws IOException {
        factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        socket = (SSLSocket) factory.createSocket("pop.mail.ru", Integer.parseInt("995"));
        inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        outputStream = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        response = null;

    }

    @Test
    public void connect() {
        assertEquals(true, socket.isConnected());
    }

    @Test
    public void disconnect() throws IOException {
        socket.close();
        assertEquals(true, socket.isClosed());
    }

    @Test
    public void createResponse() {
    }

    @Test
    public void readResponseLine() {
//        assertEquals("", response.isEmpty());
    }

    @Test
    public void getAllResponseLines() {
    }

    @Test
    public void getResultMessage() {
    }

    @Test
    public void createMessage() {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void isConnected() {
        assertEquals(true, socket.isConnected());
    }

    @Test
    public void createResponse1() {
    }

    @Test
    public void getResponse1() {
    }

    @Test
    public void readResponseLine1() {
    }

    @Test
    public void getAllResponseLines1() {
    }

   /* @Test
    public void sendCommand() throws IOException, POP3ConnectionException {
        String command = "USER POP3Irina@mail.ru";
        outputStream.write(command);
        outputStream.flush();
        connection.createResponse();
        assertEquals("", connection.getResponse());
    }*/

    @Test
    public void getResultMessage1() {
    }

    @Test
    public void createMessage1() {
    }

    @Test
    public void saveMessage() {
    }

    @Test
    public void getMailHeaders() {
    }
}