package common.screen;

import message.Message;
import utils.ClientSocket;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ScreenController extends JPanel {
    private static final long serialVersionUID = 1L;
    private CardLayout cardLayout;

    private ClientSocket clientSocket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;

//    private final Map<String, DTOClass> dtoMapper;

    public ScreenController() throws IOException {
        clientSocket = new ClientSocket();
        out = clientSocket.getObjectOutputStream();
        in = clientSocket.getObjectInputStream();
        cardLayout = new CardLayout();
        setLayout(cardLayout);
    }

    public void run() throws IOException, ClassNotFoundException {
        while(true){
            //TODO : outputStream 에서 계속 읽어서 DTO 값에 따라 ScreenViewer 에 데이터 넘겨주면서 어떤 화면 보여줄지 결정
//            in.readObject();
        }
    }

    public void addScreen(Screen screen, String screenName) {
        add(screen, screenName);
    }
    public void showScreen(String screenName) {
        cardLayout.show(this, screenName);
    }

    public void sendToServer(Message message) throws IOException {
        out.writeObject(message);
    }

    public Message receiveFromServer() throws IOException, ClassNotFoundException {
        return (Message) in.readObject();
    }
}
