// ScreenController.java
package common.screen;

import java.awt.CardLayout;
import javax.swing.JPanel;

public class ScreenViewer extends JPanel {
    private static final long serialVersionUID = 1L;
    private CardLayout cardLayout;

    public ScreenViewer() {
        cardLayout = new CardLayout();
        setLayout(cardLayout);
    }

    public void addScreen(Screen screen, String screenName) {
        add(screen, screenName);
    }

    public void showScreen(String screenName) {
        cardLayout.show(this, screenName);
    }
}
