package common.screen;

import modules.game.GameScreen;
import modules.lobby.LobbyScreen;
import modules.login.LoginScreen;
import modules.roomList.RoomListScreen;
import utils.ClientSocket;
import utils.ServerListener;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ScreenFrame extends JFrame {
    private static ScreenController screenController;

    private ClientSocket clientSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public ScreenFrame() throws IOException {
        super("KUhoot");
        initializeFrame();
        connectWithServer();
        configureScreens();
        setServerListener();
    }

    private void initializeFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        getContentPane().setBackground(Color.decode("#e0f7fa"));
    }

    private void connectWithServer() throws IOException {
        clientSocket = new ClientSocket();
        in = clientSocket.getObjectInputStream();
        out = clientSocket.getObjectOutputStream();
    }

    private void configureScreens() throws IOException {
        screenController = new ScreenController(out);
        Screen.setScreenController(screenController);

        screenController.addScreen(new LoginScreen(), LoginScreen.screenName);
        screenController.addScreen(new RoomListScreen(), RoomListScreen.screenName);
        screenController.addScreen(new LobbyScreen(), LobbyScreen.screenName);
        screenController.addScreen(new GameScreen(), GameScreen.screenName);

        add(screenController);

        SwingUtilities.invokeLater(screenController::initializeGlassPane);
    }

    private void setServerListener() {
        Thread thread = new Thread(new ServerListener(screenController, in));
        thread.start();
    }
}