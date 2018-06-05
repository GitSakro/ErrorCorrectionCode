package app;

import CommunicationFlow.Channel;
import CommunicationFlow.Receiver;
import CommunicationFlow.Transmitter;
import JavaFxHelpers.Shapes;
import algorithm.TripleRepetitionCode;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        Rectangle rectangle = Shapes.createRectangle(50.0f,75.0f,300.0f,150.0f);
        Rectangle rectangle2 = Shapes.createRectangle(650.0f,75.0f,300.0f,150.0f);

        rectangle.setFill(Paint.valueOf("yellow"));
        StackPane rectanglePane = new StackPane();
        rectanglePane.getChildren().add(rectangle);
        rectanglePane.getChildren().add(new Label("Kotek"));
        //Creating a Group object
        Group root = new Group(rectanglePane);
        root.getChildren().add(rectangle2);
        //Creating a scene object
        Scene scene = new Scene(root, 1000, 300);

        //Setting title to the Stage
        primaryStage.setTitle("Drawing a Rectangle");

        //Adding scene to the stage
        primaryStage.setScene(scene);

        //Displaying the contents of the stage
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    public void transmision(){
        Transmitter trans = new Transmitter();
        String msg = "1101";
        System.out.println("Message to be sent:" +msg );
        String sendedMessage = trans.sendMessage(msg,new TripleRepetitionCode());

        Channel channel = new Channel();
        System.out.println("StartTransmision");
        channel.startTransmision(sendedMessage);
        System.out.println("Negate random bit");
        channel.negateRandomBit();
        System.out.println("Number of bits in channel");
        System.out.println(channel.numberOfBitsInChannel());

        Receiver reciver = new Receiver();
        String messageFromReciver = reciver.getMessage(channel.endTransmission(),new TripleRepetitionCode());
        System.out.println("Recived message: " +messageFromReciver);

    }
}