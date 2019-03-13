package commands;

import model.POP3Connection;
import model.POP3ConnectionException;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class RsetTest {

    @Test
    public void execute() throws POP3ConnectionException {
        POP3Connection connection = new POP3Connection();
        connection.connect("pop.mail.ru", 995);

        String command = "USER POP3Irina@mail.ru\n";
        connection.sendCommand(command);

        String command2 = "PASS POP12345\n";
        connection.sendCommand(command2);

        String command3 = "DELE 3\n";
        connection.sendCommand(command3);

        String command4 = "RSET\n";
        connection.sendCommand(command4);

        assertEquals("+OK maildrop has 3 messages (339273 octets)\n", connection.getResponse());
    }
}