package modules.game;

import common.drawing.DrawingController;
import common.screen.Screen;
import dto.event.client.ClientDrawEvent;
import dto.info.DrawElementInfo;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GameScreen extends Screen {
    public static final String screenName = "GAME_SCREEN";
    private DrawingController drawingController;

    public GameScreen() {
        // DrawingController 초기화
        this.drawingController = new DrawingController(screenController);

        // 전체 레이아웃 설정: 3분할
        setLayout(new BorderLayout());

        // 1. 왼쪽: 현재 유저 리스트 패널
        JPanel userListPanel = new JPanel();
        userListPanel.setLayout(new BoxLayout(userListPanel, BoxLayout.Y_AXIS));
        userListPanel.setBorder(BorderFactory.createTitledBorder("현재 유저들"));
        userListPanel.setPreferredSize(new Dimension(200, 0));
        userListPanel.setBackground(Color.decode("#e8f5e9"));

        // 더미 데이터로 초기 유저 리스트 추가 (나중에 서버에서 동적 업데이트 가능)
        for (int i = 0; i < 10; i++) {
            JLabel userLabel = new JLabel("User " + (i + 1), SwingConstants.LEFT);
            userLabel.setOpaque(true);
            userLabel.setBackground(Color.decode("#c8e6c9"));
            userLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            userListPanel.add(userLabel);
        }

        // 2. 오른쪽: 정보 패널
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createTitledBorder("정보"));
        infoPanel.setPreferredSize(new Dimension(200, 0));
        infoPanel.setBackground(Color.decode("#fbe9e7"));

        JLabel roundLabel = new JLabel("현재 라운드: 1");
        JLabel timerLabel = new JLabel("남은 시간: 60초");
        JLabel drawerLabel = new JLabel("그리는 사람: User1");

        infoPanel.add(roundLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        infoPanel.add(timerLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        infoPanel.add(drawerLabel);

        // 3. 가운데: Drawing 패널
        JPanel drawingPanel = drawingController.getDrawingPanel();
        drawingPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // 패널 추가
        add(userListPanel, BorderLayout.WEST);
        add(infoPanel, BorderLayout.EAST);
        add(drawingPanel, BorderLayout.CENTER);

        // 화면 표시
        setVisible(true);
    }

    // 서버로부터 현재 그리는 사람 정보를 받았을 때
    public void setCurrentDrawer(int drawerId) {
        drawingController.setCurrentDrawer(drawerId);
    }

    // 서버로부터 이전 그림 데이터를 받았을 때
    public void setInitialDrawing(List<DrawElementInfo> elements) {
        drawingController.setInitialDrawing(elements);
    }

    // 서버로부터 그리기 이벤트를 받았을 때
    public void handleDrawEvent(ClientDrawEvent event) {
        drawingController.handleRemoteDrawEvent(event);
    }
}