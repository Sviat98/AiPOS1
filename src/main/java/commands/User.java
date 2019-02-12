package commands;

import model.POP3Connection;
import model.POP3ConnectionException;

import static commands.CommandCreator.createCommand;

public class User implements Command{
    @Override
    public String execute(String parameters, POP3Connection connection) throws POP3ClientException{
        try{
            //if(!parameters.isEmpty()){
                String command = createCommand(CommandName.USER,parameters);

                connection.sendCommand(command);
                return connection.getResponse();
            //}
            //else throw
        }
        catch (POP3ConnectionException e){
            throw new POP3ClientException(e.getMessage());
        }
    }
}
