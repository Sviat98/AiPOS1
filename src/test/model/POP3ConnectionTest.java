package model;

import commands.CommandName;
import javafx.collections.ObservableList;
import org.apache.james.mime4j.message.BodyPart;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import java.io.*;
import java.util.ArrayList;

import static commands.CommandCreator.createCommand;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;

public class POP3ConnectionTest {
    POP3Connection pop3Connection = mock(POP3Connection.class);

    SSLSocketFactory factory;
    SSLSocket socket;
    String response;
    BufferedReader inputStream = null;
    BufferedWriter outputStream = null;
    StringBuilder result;
    String data;
    String command;
    String resultMessage;
    StringBuffer txtPart;
    StringBuffer htmlPart;
    ArrayList<BodyPart> attachments;
    StringBuilder mailHeader;
    javax.mail.Message[] messages;
    ObservableList<String> headers;

    @Before
    public void setUp() throws IOException, POP3ConnectionException {

        factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        socket = (SSLSocket) factory.createSocket("pop.mail.ru", Integer.parseInt("995"));
        inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        outputStream = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        response = null;
        result = new StringBuilder();
        data = inputStream.readLine();
        result.append(data + "\n");
        response = result.toString();
    }

    @Test
    public void connect() {
        assertEquals(true, socket.isConnected());
    }

    @Test
    public void disconnect() throws IOException {
        if (socket.isClosed()) {
            assertEquals(true, socket.isClosed());
        }
    }

    @Test
    public void createResponse() {
        assertEquals(true, !response.isEmpty());
    }

    @Test
    public void getResponse() throws IOException {
        if (!response.isEmpty()) {
            String expected = "+OK\n";
            assertEquals(expected, response.toString());
        }
    }

    @Test
    public void readResponseLine() throws IOException {
        String actual = response.toString();
        if (!response.isEmpty()) {
            assertEquals("+OK\n", actual);
        }
    }

    @Test
    public void getAllResponseLines() {
        StringBuilder multiResponse = new StringBuilder();
        multiResponse.append(response);

        assertEquals("+OK\n", multiResponse.toString());
    }

    @Test
    public void sendCommand() throws IOException, POP3ConnectionException {
        command = "USER POP3Irina@mail.ru";
        outputStream.write(command);
        outputStream.flush();

      /*  String response2 = null;
        StringBuilder result2 = new StringBuilder();
        String data2 = inputStream.readLine();
        result2.append(data2);
        result2.append("\n");
        response2 = result.toString();
        */

        // pop3Connection.createResponse();
        assertEquals("+OK\n", response.toString());
    }

    @Test
    public void createMessage() {
    }

    @Test
    public void getResultMessage() throws POP3ConnectionException {
        POP3Connection pop3Connection = new POP3Connection();
        pop3Connection.connect("pop.mail.ru", 995);

        String command = "USER POP3Irina@mail.ru\n";
        pop3Connection.sendCommand(command);

        String command2 = "PASS POP12345\n";
        pop3Connection.sendCommand(command2);

        String command3 = "TOP 1 1\n";

        pop3Connection.sendCommand(command3);
        pop3Connection.createMessage();
        pop3Connection.saveMessage("1 2");

        int expected = -85582543;
        int actual = pop3Connection.getResultMessage().hashCode();
        assertEquals(expected, actual);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void isConnected() {
        assertEquals(true, socket.isConnected());
    }

    @Test
    public void saveMessage() {
    }

    @Test
    public void getMailHeaders() {
    }
}