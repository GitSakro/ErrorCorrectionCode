package app;

import communication.Channel;
import communication.Receiver;
import communication.Transmitter;
import algorithm.TripleRepetitionCode;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainApp extends Application {
    public TextField fxml_bitsInput;
//    @FXML
//    public TextField fxml_bitInput,fxml_bitsToNegate ;
    @FXML
    private Text fxml_BitsTransmitter, fxml_BitsChannel, fxml_BitsReciver;
    @FXML
    private ToggleGroup NegateBitsGroup, AlgorithmGroup;

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("main.fxml"));

        Scene scene = new Scene(root,  913.0,434.0);
        primaryStage.setResizable(false);

        primaryStage.setTitle("FXML Welcome");
        primaryStage.setScene(scene);
        primaryStage.show();

    }
    @FXML
    private void onSaveBits(ActionEvent event){

        String a = fxml_bitsInput.getText();
        fxml_BitsTransmitter.setText(a);
    }
    @FXML
    private void onStartTransmission(ActionEvent event){

    }
    public static void main(String[] args) {
        launch(args);
    }

    public void transmision() {
        Transmitter trans = new Transmitter();
        String msg = "12314";
        System.out.println("Message to be sent:" + msg);
        String sendedMessage = trans.sendMessage(msg, new TripleRepetitionCode());

        Channel channel = new Channel();
        System.out.println("StartTransmision");
        channel.startTransmission(sendedMessage);
        System.out.println("Negate random bit");
        channel.negateRandomBit();
        System.out.println("Number of bits in channel");
        System.out.println(channel.numberOfBitsInChannel());

        Receiver reciver = new Receiver();
        String messageFromReciver = reciver.getMessage(channel.endTransmission(), new TripleRepetitionCode());
        System.out.println("Recived message: " + messageFromReciver);

    }
}