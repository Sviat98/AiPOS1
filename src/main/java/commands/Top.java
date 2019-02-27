package commands;

import model.POP3Connection;
import model.POP3ConnectionException;

import static commands.CommandCreator.createCommand;


public class Top implements Command {
    @Override
    public String execute(String parameters, POP3Connection connection) throws POP3ClientException {
        String command = createCommand(CommandName.TOP,parameters);
        try{
            connection.sendCommand(command);

            String response = connection.getResponse();

            return connection.getAllResponseLines(response);

        }
        catch(POP3ConnectionException e){
            throw new POP3ClientException(e.getMessage());
        }

    }
}
