package common.screen;

import dto.info.UserInfo;
import message.Message;
import utils.ClientSocket;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ScreenController extends JPanel {
    private static final long serialVersionUID = 1L;
    private CardLayout cardLayout;

    private final ObjectOutputStream out;

    private UserInfo userInfo;

    private JPanel toastPanel;
    private Timer toastTimer;
    private JPanel glassPane;

    public ScreenController(ObjectOutputStream out) throws IOException {
        this.out = out;
        cardLayout = new CardLayout();
        setLayout(cardLayout);

        glassPane = new JPanel();
        glassPane.setOpaque(false);
        glassPane.setLayout(new BorderLayout());

        SwingUtilities.invokeLater(this::initializeGlassPane);
    }

    public void addScreen(Screen screen, String screenName) {
        add(screen, screenName);
    }
    public void showScreen(String screenName) {
        cardLayout.show(this, screenName);
    }

    public void sendToServer(Message message) throws IOException {
        out.writeObject(message);
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    void initializeGlassPane() {
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window instanceof JFrame) {
            glassPane = new JPanel();
            glassPane.setOpaque(false);
            glassPane.setLayout(new BorderLayout());
            ((JFrame) window).setGlassPane(glassPane);
        }
    }


    public void showToast(String message) {
        if (toastPanel != null) {
            glassPane.remove(toastPanel);
        }

        toastPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(50, 50, 50, 200));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2d.dispose();
            }
        };
        toastPanel.setOpaque(false);
        toastPanel.setLayout(new GridBagLayout());

        JLabel label = new JLabel(message);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        toastPanel.add(label);

        toastPanel.setMaximumSize(new Dimension(300, 80));
        toastPanel.setPreferredSize(new Dimension(300, 80));

        glassPane.add(toastPanel, BorderLayout.SOUTH);
        glassPane.setVisible(true);

        revalidate();
        repaint();

        if (toastTimer != null && toastTimer.isRunning()) {
            toastTimer.stop();
        }

        toastTimer = new Timer(2000, e -> {
            glassPane.remove(toastPanel);
            glassPane.setVisible(false);
            revalidate();
            repaint();
        });
        toastTimer.setRepeats(false);
        toastTimer.start();

    }
}
