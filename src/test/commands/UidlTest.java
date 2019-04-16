package commands;

import model.POP3Connection;
import model.POP3ConnectionException;
import org.junit.Assert;
import org.junit.Test;

import static commands.CommandCreator.createCommand;
import static junit.framework.TestCase.assertEquals;

public class UidlTest {

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

        String command3 = createCommand(CommandName.UIDL);
       // String command3 = "UIDL\n";
        connection.sendCommand(command3);
        String response = connection.getResponse();

        try {
            assertEquals("+OK 3 messages (275862 octets)\n1 1550302722471\n" +
                    "2 1550302722488\n3 1553930714861\n", connection.getAllResponseLines(response));
        } catch (IllegalArgumentException expected) {
            assertEquals("Invalid input of UIDL command.\n", expected.getMessage());
        }
    }
}