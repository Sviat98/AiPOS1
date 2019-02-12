package view;

import controller.Controller;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import commands.CommandName;


public class MainWindow {

    private Controller controller;

    private TextField hostField;
    private TextField portField;

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

        Label hostLabel = new Label("Host: ");
        Label portLabel = new Label ("Port: ");

        hostField = new TextField("pop.mail.ru");

        portField = new TextField("995");

        int port = Integer.parseInt(portField.getText());

        Button login = new Button("Login");

        ToggleGroup group  = new ToggleGroup();

        RadioButton connect = new RadioButton("connect");

        ComboBox<String> commands = new ComboBox<>();

        for(CommandName commandName : CommandName.values()){
            commands.getItems().addAll(commandName.toString());
        }


        TextField paramField = new TextField();

        Button sendCommand = new Button("send");

        sendCommand.setOnAction(e->{
                if(commands.getSelectionModel().getSelectedItem() != null){
                    controller.execute(CommandName.valueOf(commands.getSelectionModel().getSelectedItem()),paramField.getText());
                }
        });

       connect.setOnAction(e-> controller.connect(hostField.getText(),port));

        connect.setToggleGroup(group);


        RadioButton disconnect = new RadioButton("disconnect");

        disconnect.setToggleGroup(group);

        disconnect.setOnAction(e-> controller.disconnect());



        vBox.getChildren().addAll(userLabel,user,passLabel,pass,hostLabel,hostField,portLabel,portField,
                login,connect,disconnect,textArea,commands,paramField,sendCommand);


        stage.setScene(new Scene(vBox,500,1000));

        stage.show();
    }

    public void writeMessage (String message){

        textArea.appendText(message);
        textArea.appendText("\n");
    }

}
