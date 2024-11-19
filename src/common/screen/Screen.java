package common.screen;

import javax.swing.JPanel;

public abstract class Screen extends JPanel {
    protected static ScreenViewer screenController;

    public static void setScreenController(ScreenViewer controller) {
        Screen.screenController = controller;
    }

    protected void navigateTo(String screenName) {
        if (screenController != null) {
            screenController.showScreen(screenName);
        }
    }
}