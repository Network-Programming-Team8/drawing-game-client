package common.screen;

import modules.auth.AuthScreen;
import modules.lobby.LobbyScreen;

import javax.swing.*;
import java.io.IOException;

public class ScreenFrame extends JFrame {
    private static ScreenViewer screenViewer;
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
    }

    private void configureScreens() throws IOException {
        screenController = new ScreenController();
        screenViewer = new ScreenViewer();
        Screen.setScreenController(screenViewer);

        screenViewer.addScreen(new AuthScreen(), AuthScreen.screenName);
        screenViewer.addScreen(new LobbyScreen(), LobbyScreen.screenName);

        add(screenViewer);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu navigationMenu = new JMenu("네비게이션 (추후 컨트롤러에서 navigate 해줄 파트)");

        JMenuItem authMenuItem = new JMenuItem("Auth Screen");
        authMenuItem.addActionListener(e -> screenViewer.showScreen(AuthScreen.screenName));

        JMenuItem lobbyMenuItem = new JMenuItem("Lobby Screen");
        lobbyMenuItem.addActionListener(e -> screenViewer.showScreen(LobbyScreen.screenName));

        navigationMenu.add(authMenuItem);
        navigationMenu.add(lobbyMenuItem);

        menuBar.add(navigationMenu);

        setJMenuBar(menuBar);
    }
}