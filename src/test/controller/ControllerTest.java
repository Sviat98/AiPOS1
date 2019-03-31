package controller;

import commands.*;
import model.POP3Connection;
import model.POP3ConnectionException;
import org.junit.Before;
import org.junit.Test;

import static commands.CommandName.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;

public class ControllerTest {
    POP3Connection connection;

    @Before
    public void setUp() throws Exception {
        connection = new POP3Connection();
        connection.connect("pop.mail.ru", 995);
    }

    @Test
    public void connect() throws POP3ConnectionException {
        connection.connect("pop.mail.ru", 995);
        assertEquals("+OK\n", connection.getResponse());
    }

    @Test
    public void disconnect() throws POP3ConnectionException {
        if (connection.isConnected()) {
            connection.disconnect();
        } else connection.connect("pop.mail.ru", 995);
        connection.disconnect();

        assertEquals("+OK\n", connection.getResponse());
    }

    @Test
    public void authorize() throws POP3ConnectionException {
        String command = "USER POP3Irina@mail.ru\n";
        connection.sendCommand(command);

        String command1 = "PASS POP12345\n";
        connection.sendCommand(command1);

        assertEquals("+OK Welcome!\n", connection.getResponse());
    }

    @Test
    public void quit() throws POP3ConnectionException {
        String command3 = "QUIT\n";
        connection.sendCommand(command3);
        assertEquals("+OK POP3 server at  signing off\n", connection.getResponse());
    }

    @Test
    public void execute() throws POP3ClientException, InvalidInputException, POP3ConnectionException {
        CommandDirector director = new CommandDirector();
        Command command1 = director.getCommand(USER.toString());

        String result1 = command1.execute("POP3Irina@mail.ru", connection);
        assertEquals(result1, connection.getResponse());

        Command command2 = director.getCommand(PASS.toString());
        String result2 = command2.execute("POP12345", connection);
        assertEquals(result2, connection.getResponse());


        Command command3 = director.getCommand(RSET.toString());
        String result3 = command3.execute("", connection);
        assertEquals(result3, connection.getResponse());


        Command command5 = director.getCommand(LIST.toString());
        String result5 = command5.execute("1", connection);
        assertEquals(result5, connection.getResponse());
        /*

        Command command6 = director.getCommand(LIST.toString());
        String result6 = command6.execute("", connection);
        assertEquals(result6, connection.getAllResponseLines(connection.getResponse()));
        */


        Command command7 = director.getCommand(NOOP.toString());
        String result7 = command7.execute("", connection);
        //String result7False = command7.execute("2", connection);
        assertEquals(result7, connection.getResponse());
        //fai("Invalid input. NOOP is a command without parameters",result7False,connection.getResponse());


        Command command8 = director.getCommand(UIDL.toString());
        String result8 = command8.execute("2", connection);
        //String result6False = command8.execute("3", connection);
        assertEquals(result8, connection.getResponse());

        Command command9 = director.getCommand(STAT.toString());
        String result9 = command9.execute("", connection);
        //String result6False = command8.execute("3", connection);
        assertEquals(result9, connection.getResponse());

        Command command10 = director.getCommand(QUIT.toString());
        String result10 = command10.execute("", connection);
        //String result6False = command8.execute("3", connection);
        assertEquals(result10, connection.getResponse());
        /*

        Command command4 = director.getCommand(DELE.toString());
        String result4 = command4.execute("3", connection);
        assertEquals(result4, connection.getResponse());
        */
    }
}