package controller;

import model.POP3Connection;
import model.POP3ConnectionException;
import org.junit.Test;

import static org.junit.Assert.*;

public class ControllerTest {

    @Test
    public void connect() throws POP3ConnectionException {
        POP3Connection connection = new POP3Connection();

        connection.connect("pop.mail.ru",995);

        assertEquals("+OK",connection.getResponse());

    }

    @Test
    public void disconnect() {
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
}