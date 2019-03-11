package commands;

import model.POP3Connection;
import model.POP3ConnectionException;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class DeleTest {

    @Test
    public void execute() throws POP3ConnectionException {
        POP3Connection connection = new POP3Connection();
        connection.connect("pop.mail.ru", 995);

        String command = "USER POP3Irina@mail.ru\n";
        connection.sendCommand(command);

        String command1 = "PASS POP12345\n";
        connection.sendCommand(command1);

        String command2 = "DELE 3\n";
        connection.sendCommand(command2);
        assertEquals("+OK message 3 deleted\n", connection.getResponse());

    }
}