package view;

import commands.CommandDirector;
import controller.Controller;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import commands.CommandName;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.internet.MimeUtility;
import java.util.ArrayList;
import java.util.List;


public class MainWindow {

    private Controller controller;

    private TextField hostField;
    private TextField portField;

    private TextArea textArea;

    private GridPane connection;
    private GridPane login;
    private GridPane mailHeaders;
    private HBox commandPane;
    private List<CheckBox> pickMails;
    private CheckBox pickMail;

    private Button loginButton;

    private Button quitFromMailbox;





    public MainWindow(Controller controller){
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10));
        Stage stage = new Stage();

        textArea  = new TextArea();

        textArea.setEditable(false);

        textArea.setMinSize(500,500);

        connection  = new GridPane();
        connection.setHgap(10);
        connection.setVgap(10);

        connection.setAlignment(Pos.TOP_CENTER);


        login = new GridPane();
        login.setPadding(new Insets(10));
        login.setVgap(10);
        login.setHgap(10);
        login.setAlignment(Pos.TOP_CENTER);


        login.setDisable(true);

        mailHeaders = new GridPane();
        mailHeaders.setHgap(10);
        mailHeaders.setVgap(10);
        mailHeaders.setPadding(new Insets(10));
        mailHeaders.setAlignment(Pos.TOP_CENTER);

        commandPane = new HBox();
        commandPane.setDisable(true);
        commandPane.setAlignment(Pos.TOP_CENTER);
        commandPane.setSpacing(10);


        Label userLabel = new Label("Username: ");
        login.add(userLabel,0,0);

        Label passLabel = new Label ("Password: ");
        login.add(passLabel,0,1);

        TextField user = new TextField();
        user.setText("POP3Irina@mail.ru");
        login.add(user,1,0);

        PasswordField pass = new PasswordField();
        pass.setText("POP12345");
        login.add(pass,1,1);

        Label hostLabel = new Label("Host: ");
        connection.add(hostLabel,0,0);

        Label portLabel = new Label ("Port: ");
        connection.add(portLabel,0,1);

        hostField = new TextField("pop.mail.ru");
        connection.add(hostField,1,0);

        portField = new TextField("995");
        connection.add(portField,1,1);

        int port = Integer.parseInt(portField.getText());


        loginButton = new Button("Login");
        login.add(loginButton,2,0);

        quitFromMailbox = new Button("Quit");
        login.add(quitFromMailbox,2,1);
        quitFromMailbox.setDisable(true);

        loginButton.setOnAction(e->{
            controller.authorize(hostField.getText(),portField.getText(),user.getText(),pass.getText());

        });

        quitFromMailbox.setOnAction(e->{
            controller.quit();

        });


        ToggleGroup connectGroup  = new ToggleGroup();

        RadioButton connect = new RadioButton("connect");
        connect.setToggleGroup(connectGroup);

        connection.add(connect,2,0);


        RadioButton disconnect = new RadioButton("disconnect");

        disconnect.setToggleGroup(connectGroup);
        connection.add(disconnect,2,1);

        ComboBox<String> commands = new ComboBox<>();



        for(CommandName commandName : CommandName.values()){
            boolean isNotInComboBox = new CommandDirector().isNotInComboBox(commandName);
            if(isNotInComboBox){
                continue;
            }
            commands.getItems().addAll(commandName.toString());
        }


        TextField paramField = new TextField();

        Button sendCommand = new Button("Send");




        sendCommand.setOnAction(e->{
                if(commands.getSelectionModel().getSelectedItem() != null){
                    controller.execute(CommandName.valueOf(commands.getSelectionModel().getSelectedItem()),getSelectedText());
                }
        });

        commandPane.getChildren().addAll(commands, paramField,sendCommand);

       connect.setOnAction(e-> controller.connect(hostField.getText(),port));



        disconnect.setOnAction(e-> controller.disconnect());



        vBox.getChildren().addAll(connection,login,mailHeaders,textArea,commandPane);


        stage.setScene(new Scene(vBox,1200,1200));

        stage.show();
    }

    public String getSelectedText(){
        for (int i=0;i<pickMails.size();i++) {
            boolean selected = pickMails.get(i).isSelected();
            if(selected){
                return pickMails.get(i).getText();
            }
        }

        return null;
    }



    public void writeMessage (String message){
        textArea.appendText(message);
    }

    public void writeHeaders(Message[] messages){

        pickMails = new ArrayList<>();


        try{
            for (int i = 0; i< messages.length; i++){
                Message message = messages[i];
                pickMail = new CheckBox(i+1+"");
                pickMails.add(pickMail);
                mailHeaders.add(pickMail,0,i);
                Label dateLabel = new Label (message.getSentDate().toString());
                mailHeaders.add(dateLabel,1,i);
                Label fromLabel = new Label(MimeUtility.decodeText(message.getFrom()[0].toString()));
                mailHeaders.add(fromLabel,2,i);
                Label subjectLabel = new Label(message.getSubject());
                mailHeaders.add(subjectLabel,3,i);
            }
        }
        catch(NoSuchProviderException e){
            e.printStackTrace();
        }
        catch (MessagingException e){
            e.printStackTrace();
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    public void changeStateClient(boolean connected,boolean autorized){
       login.setDisable(!connected && !autorized);
       loginButton.setDisable(autorized);
       quitFromMailbox.setDisable(!autorized);
       commandPane.setDisable(!autorized);
    }
}
