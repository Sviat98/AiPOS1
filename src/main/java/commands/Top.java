package commands;

import model.POP3Connection;
import model.POP3ConnectionException;

import static commands.CommandCreator.createCommand;


public class Top implements Command {
    @Override
    public String execute(String parameters, POP3Connection connection) throws POP3ClientException, InvalidInputException {
        String[] params = parameters.split(" ");
        if (params.length != 2 || !params[0].matches("[1-9]\\d*") || !params[1].matches("[1-9]\\d*")) {
            throw new InvalidInputException("Invalid input of TOP command.\n");
        }
        String command = createCommand(CommandName.TOP, parameters);
        try {
            connection.sendCommand(command);

            connection.createMessage();

            return connection.getResultMessage();

        } catch (POP3ConnectionException e) {
            throw new POP3ClientException(e.getMessage());
        }

    }
}
