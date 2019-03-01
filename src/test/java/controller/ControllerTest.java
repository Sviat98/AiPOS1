package controller;

import model.POP3Connection;
import model.POP3ConnectionException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class ControllerTest {

    @Test
    public void connect() throws POP3ConnectionException {
        POP3Connection connection = new POP3Connection();
        connection.connect("pop.mail.ru", 995);
        assertEquals("+OK\n", connection.getResponse());
    }

    @Test
    public void disconnect() throws POP3ConnectionException {
        POP3Connection connection = new POP3Connection();
        connection.connect("pop.mail.ru", 995);
        connection.disconnect();
        assertEquals("+OK\n", connection.getResponse());
    }

    @Test
    public void execute() {
    }

    @Test
    public void authorize() {

    }

    @Test
    public void updateStateClient() {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void quit() {
    }
}