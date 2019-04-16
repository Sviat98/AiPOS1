package main;

import controller.Controller;
import javafx.application.Application;

import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.util.Properties;

public class Main extends Application {
    public static String PATH;

    private static final Logger logger = Logger.getLogger(Main.class);


    @Override
    public void start(Stage primaryStage) {

        Controller controller = new Controller();

        System.setProperty("file.encoding", "UTF-8");
    }

    public static void main(String[] args) {

        Properties log4jProperties = new Properties();
        log4jProperties.setProperty("log4j.rootLogger", "INFO, stdout,file");
        log4jProperties.setProperty("log4j.appender.stdout", "org.apache.log4j.ConsoleAppender");
        log4jProperties.setProperty("log4j.appender.stdout.Target", "System.out");
        log4jProperties.setProperty("log4j.appender.stdout.layout", "org.apache.log4j.PatternLayout");
        log4jProperties.setProperty("log4j.appender.stdout.layout.ConversionPattern", "%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n");
        log4jProperties.setProperty("log4j.appender.file", "org.apache.log4j.RollingFileAppender");
        log4jProperties.setProperty("log4j.appender.file.File", "log_file.log");
        log4jProperties.setProperty("log4j.appender.file.MaxFileSize", "5MB");
        log4jProperties.setProperty("log4j.appender.file.layout", "org.apache.log4j.PatternLayout");
        log4jProperties.setProperty("log4j.appender.file.layout.ConversionPattern", "%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n");

        PropertyConfigurator.configure(log4jProperties);

        if (args.length != 0) {
            PATH = args[0];
            launch(args);
        } else {
            logger.error("Choose directory");
        }
    }
}
