package commands;

import model.POP3Connection;
import model.POP3ConnectionException;

import static commands.CommandCreator.createCommand;

public class Dele implements Command {
    @Override
    public String execute(String parameters, POP3Connection connection) throws POP3ClientException,InvalidInputException {
        try{
            if (!parameters.matches("[1-9]\\d*")) throw new InvalidInputException("Invalid input");
            String command = createCommand(CommandName.DELE,parameters);
            connection.sendCommand(command);
            return connection.getResponse();
        }
        catch(POP3ConnectionException e){
            throw new POP3ClientException(e.getMessage());
        }

    }
}
