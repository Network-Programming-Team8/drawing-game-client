package common.screen;

import modules.auth.AuthScreen;
import modules.lobby.LobbyScreen;

import javax.swing.*;

public class ScreenFrame extends JFrame {
    static ScreenController screenController;

    public ScreenFrame() {
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

    private void configureScreens() {
        screenController = new ScreenController();
        Screen.setScreenController(screenController);

        screenController.addScreen(new AuthScreen(), AuthScreen.screenName);
        screenController.addScreen(new LobbyScreen(), LobbyScreen.screenName);

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