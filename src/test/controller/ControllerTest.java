package controller;

import commands.*;
import model.POP3Connection;
import model.POP3ConnectionException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito.*;

import java.util.Set;

import static org.junit.Assert.*;

public class ControllerTest {
    private POP3Connection connection;

    @Before
    public void setUp() throws Exception {
        connection = new POP3Connection();
    }

    @Test
    public void connect() throws POP3ConnectionException {

        connection.connect("pop.mail.ru",995);

        assertEquals("+OK\n",connection.getResponse());

    }
/*
    @Test
    public void disconnect() throws  POP3ConnectionException{

        if(connection.isConnected()){
            connection.disconnect();
        }

        assertEquals("+OK\n",connection.getResponse());
    }
*/

    @Test
    public void execute() {
        CommandDirector director = new CommandDirector();

        //Command command = new CommandDirector().getCommand(CommandName.valueOf());

    }

    @Test
    public void authorize() {

    }

    @Test
    public void updateStateClient() {
    }
}