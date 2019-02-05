import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import view.MainWindow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage)  {
        new MainWindow();

        Socket s = null;
        BufferedReader br =null;

        try{
            s = new Socket("pop.yandex.ru",110);
            s.getOutputStream().write("USER user".getBytes("iso-8859-1"));
            br = new BufferedReader(new InputStreamReader(s.getInputStream(), "iso-8859-1"));

            while(true) {
                if(!br.ready()) continue;
                String line = br.readLine();
                if (line.startsWith("+OK")){
                    System.out.println(line);
                    break;
                }
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        finally{
            try {
                br.close();
                s.close();
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public static void main (String[] args){

        launch(args);


    }
}
