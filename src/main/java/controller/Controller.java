package controller;

import commands.Command;
import commands.CommandDirector;
import commands.CommandName;
import commands.POP3ClientException;
import model.POP3Connection;
import model.POP3ConnectionException;
import view.MainWindow;


public class Controller {
    private MainWindow mainWindow;
    private POP3Connection pop3Connection;

    public Controller() {
        mainWindow = new MainWindow(this);
        pop3Connection = new POP3Connection();

    }

    public void connect(String host, int port) {
        try {
            mainWindow.writeMessage("[CLIENT] : connected to host "+host+", port "+port);
            pop3Connection.connect(host, port);
            mainWindow.writeMessage("[SERVER] : "+pop3Connection.getResponse());


        } catch (POP3ConnectionException e) {


        }
    }

    public void disconnect() {
        try {

            mainWindow.writeMessage("[CLIENT] : disconnected");
            pop3Connection.disconnect();
            mainWindow.writeMessage("[SERVER] : "+pop3Connection.getResponse());

        } catch (POP3ConnectionException e) {

        }
    }


    public boolean execute(CommandName name, String parameters){
        try{
            CommandDirector director = new CommandDirector();
            Command command= director.getCommand(name.toString());
            mainWindow.writeMessage("[CLIENT] : "+name.toString()+" "+parameters);
            String result = command.execute(parameters,pop3Connection);
            mainWindow.writeMessage("[SERVER] : "+result);

            return true;
        }
        catch ( POP3ClientException e){
            e.getMessage();
        }

        return false;
    }
}
