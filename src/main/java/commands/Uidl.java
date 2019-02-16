package commands;

import model.POP3Connection;
import model.POP3ConnectionException;

import static commands.CommandCreator.createCommand;


public class Uidl implements Command {
    @Override
    public String execute(String parameters, POP3Connection connection) throws POP3ClientException,InvalidInputException {
        String command;
        String response;
        try{
            if(parameters.isEmpty()){
                command = createCommand(CommandName.UIDL);

                connection.sendCommand(command);
                response = connection.getResponse();
                return connection.getAllResponseLines(response);
        }
        else{
                if(!parameters.matches("[1-9]\\d*")) throw new InvalidInputException("Invalid input");

                command = createCommand(CommandName.UIDL,parameters);
                connection.sendCommand(command);

                return connection.getResponse();
            }

        }
        catch(POP3ConnectionException e){
            throw new POP3ClientException(e.getMessage());
        }
    }
}
