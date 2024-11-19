import common.screen.ScreenFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ScreenFrame mainFrame = new ScreenFrame();
            mainFrame.setVisible(true);
        });
    }
}