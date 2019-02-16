package commands;

import model.POP3Connection;
import model.POP3ConnectionException;
import org.junit.Test;

import static commands.CommandCreator.createCommand;


import static org.junit.Assert.*;

public class UserTest {

    @Test
    public void execute() throws POP3ConnectionException {
        POP3Connection connection = new POP3Connection();

        connection.connect("pop.mail.ru",995);
         //String command1 = createCommand(CommandName.USER,"bashkevich.98@mail.ru");

         String command = "USER bashkevich.98@mail.ru\n";

         connection.sendCommand(command);


         assertEquals("+OK",connection.getResponse());
    }
}