package controller;

import commands.*;
import model.POP3Connection;
import model.POP3ConnectionException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import view.MainWindow;

import java.util.Properties;


public class Controller {
    private MainWindow mainWindow;
    private POP3Connection pop3Connection;

    private boolean connected;
    private boolean authorized;


    private static final Logger logger =  Logger.getLogger(Controller.class);



    //private static final Log logger = LogFactory.getLog(Controller.class);


    public Controller() {
        mainWindow = new MainWindow(this);
        pop3Connection = new POP3Connection();

        Properties log4jProperties = new Properties();
        log4jProperties.setProperty("log4j.rootLogger","INFO, stdout,file");
        log4jProperties.setProperty("log4j.appender.stdout","org.apache.log4j.ConsoleAppender");
        log4jProperties.setProperty("log4j.appender.stdout.Target","System.out");
        log4jProperties.setProperty("log4j.appender.stdout.layout","org.apache.log4j.PatternLayout");
        log4jProperties.setProperty("log4j.appender.stdout.layout.ConversionPattern","%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n");
        log4jProperties.setProperty("log4j.appender.file","org.apache.log4j.RollingFileAppender");
        log4jProperties.setProperty("log4j.appender.file.File","log_file.log");
        log4jProperties.setProperty("log4j.appender.file.MaxFileSize","5MB");
        log4jProperties.setProperty("log4j.appender.file.layout","org.apache.log4j.PatternLayout");
        log4jProperties.setProperty("log4j.appender.file.layout.ConversionPattern","%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n");
        PropertyConfigurator.configure(log4jProperties);


    }

    public void connect(String host, int port) {
        try {
            mainWindow.writeMessage("[CLIENT] : connected to host "+host+", port "+port);
            pop3Connection.connect(host, port);
            mainWindow.writeMessage("\n[SERVER] : "+pop3Connection.getResponse());
            logger.info("CONNECTED");
            updateStateClient(true,authorized);


        } catch (POP3ConnectionException e) {

            logger.error("Error while connection: ",e.fillInStackTrace());
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

    public void authorize(String host, String port,String username,String password){
        execute(CommandName.USER,username);
        boolean state = execute(CommandName.PASS,password);

        updateStateClient(connected,state);

        if(state){

            mainWindow.writeHeaders(pop3Connection.getMailHeaders(host,port,username, password));
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
