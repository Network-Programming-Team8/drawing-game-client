import common.screen.ScreenFrame;

import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ScreenFrame mainFrame = null;
            try {
                mainFrame = new ScreenFrame();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            mainFrame.setVisible(true);
        });
    }
}