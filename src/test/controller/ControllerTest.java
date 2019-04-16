package controller;

import commands.*;
import model.POP3Connection;
import model.POP3ConnectionException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static commands.CommandName.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.mockito.Mockito.*;

public class ControllerTest {
    Controller controller;
    POP3Connection connection;
    CommandDirector director;

    @Before
    public void setUp() throws Exception {
       controller = mock(Controller.class);
        connection = new POP3Connection();
        director = new CommandDirector();
        POP3Connection connection1 = mock(POP3Connection.class);
        controller.connect("pop.mail.ru", 995);
        connection.connect("pop.mail.ru", 995);
        when(!connection1.isConnected()).thenThrow(POP3ConnectionException.class);
    }



    @Test
    public void connect() throws POP3ConnectionException {
        verify(controller,atLeastOnce()).connect("pop.mail.ru", 995);
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
        assertEquals("+OK\n", connection.getResponse());

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
        CommandDirector director1 = mock(CommandDirector.class);


        Command command1 = director.getCommand(USER.toString());
        assertEquals("+OK\n",command1.execute("POP3Irina@mail.ru", connection));
        try {
            String result1False = command1.execute("", connection);
            assertEquals(result1False,connection.getResponse());
        }
        catch(InvalidInputException | POP3ClientException e){
            e.getMessage();
        }


        Command command2 = director.getCommand(PASS.toString());
        assertEquals("+OK Welcome!\n", command2.execute("POP12345", connection));
        try {
            assertEquals("Invalid input of PASS command.\n",command2.execute("", connection));
        }
        catch(InvalidInputException | POP3ClientException e){
            e.getMessage();
        }

        Command command3 = director.getCommand(RSET.toString());
        assertEquals("+OK maildrop has 3 messages (275862 octets)\n", command3.execute("", connection));
        try {
            assertEquals("Invalid input. RSET is a command without parameters\n",command3.execute("2 2 2", connection));
        }
        catch(InvalidInputException | POP3ClientException e){
            e.getMessage();
        }

        Command command5 = director.getCommand(LIST.toString());
        assertEquals("+OK 1 153005\n", command5.execute("1", connection));
        assertEquals("+OK 3 messages (275862 octets)\n" +
                "1 153005\n" +
                "2 105700\n" +
                "3 17157\n", command5.execute("", connection));
        try {
            String result5False = command5.execute("2", connection);
            assertEquals(result5False,connection.getResponse());
        }
        catch(InvalidInputException | POP3ClientException e){
            e.getMessage();
        }


  /*      Command command6 = director.getCommand(LIST.toString());
        String result6 = command6.execute("", connection);
        String response = connection.getResponse();
        assertEquals(result6, connection.getAllResponseLines(response));
  */

        Command command7 = director.getCommand(NOOP.toString());
        assertEquals("+OK \n", command7.execute("", connection));
        try {
            assertEquals("Invalid input. NOOP is a command without parameters\n",command7.execute("2", connection));
        }
        catch(InvalidInputException | POP3ClientException e){
            e.getMessage();
        }

        Command command8 = director.getCommand(UIDL.toString());
        assertEquals("+OK 2 1550302722488\n", command8.execute("2", connection));
        assertEquals("+OK 3 messages (275862 octets)\n1 1550302722471\n" +
                "2 1550302722488\n3 1553930714861\n",command8.execute("", connection));
        try {
            assertEquals("Invalid input of UIDL command.\n",command8.execute("2 2", connection));
        }
        catch(InvalidInputException | POP3ClientException e){
            e.getMessage();
        }

        Command command9 = director.getCommand(STAT.toString());
        assertEquals("+OK 3 275862\n", command9.execute("", connection));
        try {
            assertEquals("Invalid input. STAT is a command without parameters\n",command9.execute("2", connection));
        }
        catch(InvalidInputException | POP3ClientException e){
            e.getMessage();
        }

        Command command10 = director.getCommand(QUIT.toString());
        assertEquals("+OK POP3 server at mail.ru signing off\n", command10.execute("", connection));
        try {
            assertEquals("Invalid input. QUIT is a command without parameters\n",command10.execute("2", connection));
        }
        catch(InvalidInputException | POP3ClientException e){
            e.getMessage();
        }
        /*

        Command command4 = director.getCommand(DELE.toString());
        String result4 = command4.execute("3", connection);
        assertEquals(result4, connection.getResponse());
        */
    }
    //*/

    @After
    public void tearDown() throws Exception {
        controller = null;
    }
}