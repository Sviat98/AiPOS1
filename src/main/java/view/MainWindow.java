package view;

import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainWindow {

    public MainWindow(){
        VBox vBox = new VBox();
        Stage stage = new Stage();

        TextArea textArea  = new TextArea();

        textArea.setEditable(false);

        vBox.getChildren().addAll(textArea);


        stage.setScene(new Scene(vBox,500,500));

        stage.show();
    }

}
