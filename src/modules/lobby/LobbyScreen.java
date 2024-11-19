package modules.lobby;

import common.screen.Screen;
import modules.auth.AuthScreen;
import javax.swing.*;

public class LobbyScreen extends Screen {
    public static final String screenName = "LOBBY_SCREEN";

    public LobbyScreen() {
        setVisible(true);
        add(new JLabel("Lobby Screen"));

        JButton backBtn = new JButton("Back to Auth");
        backBtn.addActionListener(e -> navigateTo(AuthScreen.screenName));
        add(backBtn);
    }
}