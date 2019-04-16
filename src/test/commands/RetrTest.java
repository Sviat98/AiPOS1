package commands;

import model.POP3Connection;
import model.POP3ConnectionException;
import org.apache.james.mime4j.message.Message;
import org.junit.Test;

import static commands.CommandCreator.createCommand;
import static junit.framework.TestCase.assertEquals;

public class RetrTest {

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

        String command3 = createCommand(CommandName.RETR, "2");
       // String command3 = "RETR 2\n";
        connection.sendCommand(command3);
        assertEquals("+OK 105700 octets\n", connection.getResponse());
    }

    @Test
    public void createMessage() {

    }
}