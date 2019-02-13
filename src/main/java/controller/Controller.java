package controller;

import commands.*;
import model.POP3Connection;
import model.POP3ConnectionException;
import view.MainWindow;


public class Controller {
    private MainWindow mainWindow;
    private POP3Connection pop3Connection;

    private boolean connected;
    private boolean autorized;

    public Controller() {
        mainWindow = new MainWindow(this);
        pop3Connection = new POP3Connection();

    }

    public void connect(String host, int port) {
        try {
            mainWindow.writeMessage("[CLIENT] : connected to host "+host+", port "+port);
            pop3Connection.connect(host, port);
            mainWindow.writeMessage("[SERVER] : "+pop3Connection.getResponse());

            updateStateClient(true,autorized);


        } catch (POP3ConnectionException e) {
            mainWindow.writeMessage("[SERVER] : "+e.getMessage());

        }
    }

    public void disconnect() {
        try {

            mainWindow.writeMessage("[CLIENT] : disconnected");
            pop3Connection.disconnect();
            mainWindow.writeMessage("[SERVER] : "+pop3Connection.getResponse());
            updateStateClient(false,autorized);

        } catch (POP3ConnectionException e) {
            mainWindow.writeMessage("[SERVER] : "+e.getMessage());
        }
    }

    public void authorize(String username,String password){
        execute(CommandName.USER,username);
        boolean state = execute(CommandName.PASS,password);

        updateStateClient(connected,state);

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
        catch ( POP3ClientException | InvaldInputException e){
            mainWindow.writeMessage("[SERVER] : "+e.getMessage());
        }

        return false;
    }

    public void updateStateClient(boolean connected, boolean autorized){
        this.connected = connected;
        this.autorized = autorized;

        mainWindow.changeStateClient(connected,autorized);


    }
}
