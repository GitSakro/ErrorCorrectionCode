package app;

import CommunicationFlow.Channel;
import CommunicationFlow.Receiver;
import CommunicationFlow.Transmitter;
import algorithm.TripleRepetitionCode;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(e -> {
            System.out.println("Hello World!");
        });
        transmision();
        StackPane root = new StackPane();
        root.getChildren().add(btn);

        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
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