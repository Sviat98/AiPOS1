package main;

import controller.Controller;
import model.POP3Connection;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class MainTest {
    static Logger logger;
    Controller controller;
    Properties log4jProperties;

    @Before
    public void setUp() throws Exception {
        logger = Logger.getLogger(Main.class);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void start() {
        controller = mock(Controller.class);
        System.setProperty("file.encoding", "UTF-8");
    }

    @Test
    public void main() {
        log4jProperties = new Properties();
        PropertyConfigurator.configure(log4jProperties);
    }
}