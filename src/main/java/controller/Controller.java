package controller;

import model.POP3Connection;
import model.POP3ConnectionException;
import view.MainWindow;

public class Controller {
    private MainWindow mainWindow;
    private POP3Connection pop3Connection;

    public Controller() {
        mainWindow =  new MainWindow(this);
        pop3Connection = new POP3Connection();

    }

    public void connect(String host,int port) {
        try {
            pop3Connection.connect(host,port);
            mainWindow.writeMessage(pop3Connection.createResponse());


        }
        catch(POP3ConnectionException e){


        }
    }

    public void disconnect() {
        try {
            pop3Connection.disconnect();
            mainWindow.writeMessage("disconnected");

        }
        catch(POP3ConnectionException e){

        }
    }
}
