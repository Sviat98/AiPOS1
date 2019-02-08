package view;

import controller.Controller;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainWindow {

    private Controller controller;

    private TextArea textArea;

    public MainWindow(Controller controller){
        VBox vBox = new VBox();
        Stage stage = new Stage();

        textArea  = new TextArea();

        textArea.setEditable(false);

        Label userLabel = new Label("Username: ");
        Label passLabel = new Label ("Password: ");

        TextField user = new TextField();

        PasswordField pass = new PasswordField();

        Button login = new Button("Login");

        ToggleGroup group  = new ToggleGroup();

        RadioButton connect = new RadioButton("connect");

        connect.setSelected(true);
        
       connect.setOnAction(e-> controller.connect());

        connect.setToggleGroup(group);


        RadioButton disconnect = new RadioButton("disconnect");

        disconnect.setToggleGroup(group);

        disconnect.setOnAction(e-> controller.disconnect());




        vBox.getChildren().addAll(userLabel,user,passLabel,pass,login,connect,disconnect,textArea);


        stage.setScene(new Scene(vBox,500,500));

        stage.show();
    }

    public void writeMessage (String message){

        textArea.appendText(message);
        textArea.appendText("\n");
    }

}
