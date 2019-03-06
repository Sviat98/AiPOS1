package controller;

import commands.*;
import model.POP3Connection;
import model.POP3ConnectionException;
import org.apache.log4j.Logger;
import view.MainWindow;


public class Controller {
    private MainWindow mainWindow;
    private POP3Connection pop3Connection;

    private boolean connected;
    private boolean authorized;

    private static final Logger logger =  Logger.getLogger(Controller.class);


    public Controller() {
        mainWindow = new MainWindow(this);
        pop3Connection = new POP3Connection();


    }

    public void connect(String host, int port) {
        try {
            mainWindow.writeMessage("[CLIENT] : connected to host "+host+", port "+port);
            pop3Connection.connect(host, port);
            mainWindow.writeMessage("\n[SERVER] : "+pop3Connection.getResponse());
            logger.info("CONNECTED");
            updateStateClient(true,authorized);


        } catch (POP3ConnectionException e) {
            //e.printStackTrace();
            //(for StackTraceElement[] :)
            logger.error("Error while connection: ",e.fillInStackTrace());
            //logger.error(e);
            mainWindow.writeMessage("\n[SERVER] : "+e.getMessage());

        }
    }

    public void disconnect() {
        try {

            mainWindow.writeMessage("[CLIENT] : disconnected");
            pop3Connection.disconnect();
            mainWindow.writeMessage("\n[SERVER] : "+pop3Connection.getResponse());
            updateStateClient(false,authorized);
            logger.info("DISCONNECTED");

        } catch (POP3ConnectionException e) {
            mainWindow.writeMessage("\n[SERVER] : "+e.getMessage());
            logger.error("Error while disconnection",e);
        }
    }

    public void authorize(String username,String password){
        execute(CommandName.USER,username);
        boolean state = execute(CommandName.PASS,password);

        updateStateClient(connected,state);

        if(state){
            mainWindow.writeMessage(pop3Connection.getMailHeaders(username, password));
        }


    }

    public void quit(){
        execute(CommandName.QUIT,"");
        updateStateClient(connected,false);
    }


    public boolean execute(CommandName name, String parameters){
        try{
            CommandDirector director = new CommandDirector();
            Command command= director.getCommand(name.toString());
            mainWindow.writeMessage("[CLIENT] : "+name.toString()+" "+parameters);
            String result = command.execute(parameters,pop3Connection);
            mainWindow.writeMessage("\n[SERVER] : "+result);

            return true;
        }
        catch ( POP3ClientException | InvalidInputException e){
            mainWindow.writeMessage("\n[SERVER] : "+e.getMessage());
        }

        return false;
    }

    public void updateStateClient(boolean connected, boolean autorized){
        this.connected = connected;
        this.authorized = autorized;

        mainWindow.changeStateClient(connected,autorized);

    }
}
