package common.screen;

import modules.auth.AuthScreen;
import modules.lobby.LobbyScreen;
import modules.login.LoginScreen;
import modules.roomList.RoomListScreen;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ScreenFrame extends JFrame {
    private static ScreenController screenController;

    public ScreenFrame() throws IOException {
        super("Application");
        initializeFrame();
        configureScreens();
        createMenuBar();
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
        screenController.addScreen(new AuthScreen(), AuthScreen.screenName);
        screenController.addScreen(new LobbyScreen(), LobbyScreen.screenName);
        screenController.addScreen(new RoomListScreen(), RoomListScreen.screenName);

        add(screenController);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu navigationMenu = new JMenu("네비게이션 (추후 컨트롤러에서 navigate 해줄 파트)");

        JMenuItem authMenuItem = new JMenuItem("Auth Screen");
        authMenuItem.addActionListener(e -> screenController.showScreen(AuthScreen.screenName));

        JMenuItem lobbyMenuItem = new JMenuItem("Lobby Screen");
        lobbyMenuItem.addActionListener(e -> screenController.showScreen(LobbyScreen.screenName));

        navigationMenu.add(authMenuItem);
        navigationMenu.add(lobbyMenuItem);

        menuBar.add(navigationMenu);

        setJMenuBar(menuBar);
    }
}