package commands;

import model.POP3Connection;
import model.POP3ConnectionException;

import static commands.CommandCreator.createCommand;

public class User implements Command{
    @Override
    public String execute(String parameters, POP3Connection connection) throws POP3ClientException, InvalidInputException {
        try{
            if(parameters == null || parameters.length() == 0)throw new InvalidInputException("Invalid input of USER command.\n");
                String command = createCommand(CommandName.USER,parameters);

                connection.sendCommand(command);
                return connection.getResponse();

        }
        catch (POP3ConnectionException e){
            throw new POP3ClientException(e.getMessage());
        }
    }
}
