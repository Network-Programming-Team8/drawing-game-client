package common.screen;

import javax.swing.JPanel;

public abstract class Screen extends JPanel {
    protected static ScreenController screenController;

    public static void setScreenController(ScreenController controller) {
        Screen.screenController = controller;
    }

    protected void navigateTo(String screenName) {
        if (screenController != null) {
            java.awt.CardLayout cardLayout = (java.awt.CardLayout) screenController.getLayout();
            cardLayout.show(screenController, screenName);
        }
    }
}