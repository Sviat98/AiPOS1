package commands;

import model.POP3Connection;
import model.POP3ConnectionException;
import org.apache.james.mime4j.message.Message;

import static commands.CommandCreator.createCommand;


public class Retr implements Command {
    @Override
    public String execute(String parameters, POP3Connection connection) throws POP3ClientException, InvalidInputException {
        if(!parameters.matches("[1-9]\\d*")) throw new InvalidInputException("Invalid input\n");
        try{

            String command = createCommand(CommandName.RETR,parameters);

            connection.sendCommand(command);

            String response = connection.getResponse();

            return connection.getAllResponseLines(response);

        }catch (POP3ConnectionException e){
            throw new POP3ClientException(e.getMessage());
        }

    }

    public String createMessage(){
        Message message = new Message();


        return message.toString();
    }
}
