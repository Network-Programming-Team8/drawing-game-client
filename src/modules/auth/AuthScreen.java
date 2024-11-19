package modules.auth;

import common.screen.Screen;
import modules.lobby.LobbyScreen;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AuthScreen extends Screen {
    public static final String screenName = "AUTH_SCREEN";
    public AuthScreen() {
        setVisible(true);
        add(new JLabel("Auth Screen"));

        JButton backBtn = new JButton("Back to Lobby");
        backBtn.addActionListener(e -> navigateTo(LobbyScreen.screenName));
        add(backBtn);
    }
}
