package view;

import controller.Controller;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import main.Main;
import model.POP3Connection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class MainWindowTest {

    private MainWindow mainWindow;
   // TextArea textArea;
    POP3Connection pop3Connection;
 //   ListView<String> headers = new ListView<>();

    @Before
    public void setUp() throws InterruptedException,Exception {
        Controller controller = mock(Controller.class);

        Thread thread = new Thread(() -> {
            new JFXPanel();
            Platform.runLater(() -> mainWindow = new MainWindow(controller));
        });
        thread.start();
        Thread.sleep(1000);
        //MainWindow mainWindow = new MainWindow(controller);
        pop3Connection = new POP3Connection();
        //  POP3Connection pop3Connection = mock(POP3Connection.class);
//        ListView<String> headers = new ListView<>();
        pop3Connection.connect("pop.mail.ru", 995);
        String command = "USER POP3Irina@mail.ru\n";
        pop3Connection.sendCommand(command);
        String command1 = "PASS POP12345\n";
        pop3Connection.sendCommand(command1);

    }

    @Test
    public void writeHeaders() {
    }

    @Test
    public void writeMessage() {
        //   String message = "message";
      //   textArea.appendText(message);
        // String expected = textArea.getText();
        // String actual = "";
        // assertEquals(expected, actual);
    }

    @Test
    public void changeStateClient() {
        assertEquals(true, pop3Connection.isConnected());
        assertEquals("+OK Welcome!\n", pop3Connection.getResponse());
    }

    @After
    public void tearDown() throws Exception {
        mainWindow = null;
    }
}