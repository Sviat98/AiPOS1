package controller;

import commands.*;
import model.POP3Connection;
import model.POP3ConnectionException;
import org.junit.Before;
import org.junit.Test;

import static commands.CommandName.PASS;
import static commands.CommandName.QUIT;
import static commands.CommandName.USER;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class ControllerTest {
    private POP3Connection connection;
    private Controller controller;
    private boolean connected;

    @Before
    public void setUp() throws Exception {
        connection = new POP3Connection();
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
        //String command = "USER POP3Irina@mail.ru\n";
        controller.execute(USER, "POP3Irina@mail.ru\n");
        boolean state = controller.execute(PASS, "POP12345");
        controller.updateStateClient(connected, state);
        //connection.sendCommand(command);
        assertEquals("+OK\n", connection.getMailHeaders("pop.mail.ru", "995", "POP3Irina@mail.ru",
                "POP12345"));

    }

    @Test
    public void updateStateClient() {
    }

    @Test
    public void quit() {
        controller.execute(QUIT, "");
        assertEquals("", connection.getResponse());
    }

    @Test
    public void execute() throws POP3ClientException, InvalidInputException {
        CommandDirector director = new CommandDirector();
        Command command = director.getCommand(USER.toString());

        String result = command.execute("POP3Irina@mail.ru", connection);
        assertEquals(result, connection.getResponse());
    }
}