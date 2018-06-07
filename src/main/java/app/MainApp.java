package app;

import algorithm.HammingCode;
import algorithm.MultidimensionalParityCodeCoder;
import algorithm.TripleRepetitionCode;
import communication.Channel;
import communication.Receiver;
import communication.Transmitter;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import utils.Utils;

public class MainApp extends Application {
    private enum AlgorithmType {Triple, Multidimensional, Hamming}

    ;

    private enum NegateType {Bit, Bits}

    ;

    @FXML
    public Label fxml_Communicat;
    @FXML
    public TextField fxml_bitsInput, fxml_bitsToNegate;
    @FXML
    private Text fxml_BitsTransmitter2, fxml_BitsChannel2, fxml_BitsReceiver2;
    @FXML
    private ToggleGroup negateBitsGroup, algorithmGroup;

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("main.fxml"));

        Scene scene = new Scene(root, 913.0, 434.0);
        primaryStage.setResizable(false);

        primaryStage.setTitle("FXML Welcome");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    @FXML
    private void onStartTransmission(ActionEvent event) {
        fxml_BitsTransmitter2.setText("");
        fxml_Communicat.setText("");
        fxml_BitsReceiver2.setText("");

        String message = fxml_bitsInput.getText();
        AlgorithmType algType = getAlgorithmType();
        NegateType negType = getNegateType();

        if (algType == null || negType == null) {
            fxml_Communicat.setText("Select algorithm type and negation type");
            return;
        }

        Transmitter trans = new Transmitter();
        System.out.println("Message to be sent:" + message);

        String sentMessage = null;
        switch (algType) {
            case Triple:
                fxml_BitsTransmitter2.setText(message);
                sentMessage = trans.sendMessage(message, new TripleRepetitionCode());
                break;
            case Multidimensional:
                fxml_BitsTransmitter2.setText(Utils.convertMessageToBinary(fxml_bitsInput.getText()));
                sentMessage = trans.sendMessage(message, new MultidimensionalParityCodeCoder());
                break;
            case Hamming:
                fxml_BitsTransmitter2.setText(message);
                sentMessage = trans.sendMessage(message, new HammingCode());
                break;
        }

        if (sentMessage == null) {
            fxml_Communicat.setText("Problem with encoding message");
            return;
        }

        Channel channel = new Channel();
        System.out.println("StartTransmision");
        channel.startTransmission(sentMessage);
        System.out.println("Negate random bit");

        switch (negType) {
            case Bit:
                channel.negateRandomBit();
                break;
            case Bits:
                try {
                    if (fxml_bitsToNegate.getText() == null) {
                        fxml_Communicat.setText("Type amount of bits");
                        return;
                    }
                    channel.negateRandomBits(Integer.parseInt(fxml_bitsToNegate.getText()));
                } catch (NumberFormatException e) {
                    fxml_Communicat.setText("Type amount of bits");
                    return;
                }

                break;
        }

        System.out.println("Number of bits in channel");
        System.out.println(channel.numberOfBitsInChannel());

        String channelMessage = channel.endTransmission();
        if (channelMessage == null) {
            fxml_BitsChannel2.setText("");
            fxml_Communicat.setText("Problem with communication channel");
            return;
        }

        System.out.println("channel message " + channelMessage);

        fxml_BitsChannel2.setText(channelMessage);
        Receiver reciver = new Receiver();


        String messageFromReciver = null;

        switch (algType) {
            case Triple:
                messageFromReciver = reciver.getMessage(channelMessage, new TripleRepetitionCode());
                break;
            case Multidimensional:
                messageFromReciver = reciver.getMessage(channelMessage, new MultidimensionalParityCodeCoder());
                break;
            case Hamming:
                messageFromReciver = reciver.getMessage(channelMessage, new HammingCode());
                break;
        }

        System.out.println("Received message: " + messageFromReciver);

        if (messageFromReciver != null) {
            fxml_BitsReceiver2.setText(messageFromReciver);
            fxml_Communicat.setText("");
        } else {
            fxml_Communicat.setText("Problem with decoding");
            fxml_BitsReceiver2.setText("");
        }
    }

    private AlgorithmType getAlgorithmType() {
        if (algorithmGroup.getSelectedToggle() == null || algorithmGroup.getSelectedToggle().getUserData() == null) {
            return null;
        }

        String selected = algorithmGroup.getSelectedToggle().getUserData().toString();
        if (selected != null) {
            switch (selected) {
                case "triple":
                    return AlgorithmType.Triple;
                case "parity":
                    return AlgorithmType.Multidimensional;
                case "hamming":
                    return AlgorithmType.Hamming;
            }
        }

        return null;
    }

    private NegateType getNegateType() {
        if (negateBitsGroup.getSelectedToggle() == null || negateBitsGroup.getSelectedToggle().getUserData() == null) {
            return null;
        }

        String selected = negateBitsGroup.getSelectedToggle().getUserData().toString();
        if (selected != null) {
            if (selected.equals("negateBit")) {
                return NegateType.Bit;
            } else if (selected.equals("negateBits")) {
                return NegateType.Bits;
            }
        }

        return null;
    }

    public static void main(String[] args) {
        launch(args);
    }
}