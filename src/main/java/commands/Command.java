package commands;

import model.POP3Connection;

public interface Command {
    String execute(String parameters, POP3Connection connection) throws POP3ClientException, InvalidInputException;


}
