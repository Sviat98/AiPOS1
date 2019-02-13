package commands;

import model.POP3Connection;

public class Retr implements Command {
    @Override
    public String execute(String parameters, POP3Connection connection) throws POP3ClientException {
        return null;
    }
}
