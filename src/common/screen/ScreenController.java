package common.screen;

import dto.info.UserInfo;
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

    private final ObjectOutputStream out;

    private UserInfo userInfo;

//    private final Map<String, DTOClass> dtoMapper;

    public ScreenController(ObjectOutputStream out) throws IOException {
        this.out = out;
        cardLayout = new CardLayout();
        setLayout(cardLayout);
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

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }
}
