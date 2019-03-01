package commands;

import model.POP3Connection;
import model.POP3ConnectionException;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class UidlTest {

    @Test
    public void execute() throws POP3ConnectionException {
        POP3Connection connection = new POP3Connection();
        connection.connect("pop.mail.ru", 995);

        String command = "USER POP3Irina@mail.ru\n";
        connection.sendCommand(command);

        String command2 = "PASS POP12345\n";
        connection.sendCommand(command2);

        String command3 = "UIDL\n";
        connection.sendCommand(command3);
        String response = connection.getResponse();

        assertEquals("+OK 3 messages (339273 octets)\n 1 1550302722471\n" +
                "2 1550302722488\n" +
                "3 1550302722998\n", connection.getAllResponseLines(response));
    }
}