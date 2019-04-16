package commands;

import model.POP3Connection;
import model.POP3ConnectionException;
import org.junit.Assert;
import org.junit.Test;

import static commands.CommandCreator.createCommand;
import static junit.framework.TestCase.assertEquals;

public class TopTest {

    @Test
    public void execute() throws POP3ConnectionException {
        POP3Connection connection = new POP3Connection();
        connection.connect("pop.mail.ru", 995);

        String command = "USER POP3Irina@mail.ru\n";
        connection.sendCommand(command);
        assertEquals("+OK\n", connection.getResponse());

        String command2 = "PASS POP12345\n";
        connection.sendCommand(command2);
        assertEquals("+OK Welcome!\n", connection.getResponse());

        String command3 = createCommand(CommandName.TOP, "1 1");
       // String command3 = "TOP 1 1\n";
        connection.sendCommand(command3);

        try {
            assertEquals("+OK \n", connection.getResponse());
        } catch (IllegalArgumentException expected) {
            Assert.assertEquals("Invalid input of TOP command.\n", expected.getMessage());
        }
    }
}