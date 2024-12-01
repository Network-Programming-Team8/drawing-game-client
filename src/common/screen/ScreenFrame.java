package common.screen;

import modules.lobby.LobbyScreen;
import modules.login.LoginScreen;
import modules.roomList.RoomListScreen;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ScreenFrame extends JFrame {
    private static ScreenController screenController;

    public ScreenFrame() throws IOException {
        super("KUhoot");
        initializeFrame();
        configureScreens();
    }

    private void initializeFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        getContentPane().setBackground(Color.decode("#e0f7fa"));
    }

    private void configureScreens() throws IOException {
        screenController = new ScreenController();
        Screen.setScreenController(screenController);

        screenController.addScreen(new LoginScreen(), LoginScreen.screenName);
        screenController.addScreen(new RoomListScreen(), RoomListScreen.screenName);
        screenController.addScreen(new LobbyScreen(), LobbyScreen.screenName);

        add(screenController);
    }
}