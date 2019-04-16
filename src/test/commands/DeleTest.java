package commands;

import model.POP3Connection;
import model.POP3ConnectionException;
import org.junit.Test;

import static commands.CommandCreator.createCommand;
import static junit.framework.TestCase.assertEquals;

public class DeleTest {

    @Test
    public void execute() throws POP3ConnectionException {
        POP3Connection connection = new POP3Connection();
        connection.connect("pop.mail.ru", 995);

        String command = "USER POP3Irina@mail.ru\n";
        connection.sendCommand(command);
        assertEquals("+OK\n", connection.getResponse());

        String command1 = "PASS POP12345\n";
        connection.sendCommand(command1);
        assertEquals("+OK Welcome!\n", connection.getResponse());

        String command2 = createCommand(CommandName.DELE, "2");
        //String command2 = "DELE 2\n";
        connection.sendCommand(command2);
        assertEquals("+OK message 2 deleted\n", connection.getResponse());

    }
}