package modules.game;

import common.drawing.DrawingController;
import common.screen.Screen;
import dto.event.server.ServerDrawEvent;
import dto.event.server.ServerStartGameEvent;
import dto.event.server.ServerTurnChangeEvent;
import dto.info.UserInfo;
import modules.lobby.LobbyScreen;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class GameScreen extends Screen {
    public static final String screenName = "GAME_SCREEN";
    private static DrawingController drawingController;

    private static ArrayList<UserInfo> userOrder = new ArrayList<UserInfo>();
    private static UserInfo guesserInfo = new UserInfo(1, "", false);
    private static String selectedTopic;

    private static JPanel userListPanel;
    private static JPanel infoPanel;
    private static JPanel drawingPanel;

    public static void updateUserList(){
        SwingUtilities.invokeLater(() -> {
            for(int i=0; i < userOrder.size();i++){
                JLabel userLabel = (JLabel)userListPanel.getComponent(i);
                userLabel.setText(userOrder.get(i).getNickname());
                userLabel.setFont(new Font("Arial", Font.BOLD, 20));
                userLabel.setVisible(true);
            }
        });
    }

    public static void updateCurrentUser(int nowTurnUserId){
        SwingUtilities.invokeLater(() -> {
            for(int i=0; i < userOrder.size();i++){
                JLabel userLabel = (JLabel)userListPanel.getComponent(i);
                userLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                if(userOrder.get(i).getId() == nowTurnUserId){
                    userLabel.setBorder(BorderFactory.createLineBorder(Color.RED, 5));
                }
            }
            userListPanel.revalidate();
            userListPanel.repaint();
        });
    }

    public static void updateRoomInfoPanel(){
        SwingUtilities.invokeLater(() -> {
            JPanel topicPanel = (JPanel)infoPanel.getComponent(0);
            JLabel topicLabel = (JLabel)topicPanel.getComponent(0);
            String topic=String.format("그리기 주제 : %s", selectedTopic);
            if(guesserInfo.getId() == DrawingController.getCurrentUserId()){
                topic = String.format("당신은 맞히는 사람입니다.");
            }
            topicLabel.setText(String.format(topic));
            JPanel userPanel = (JPanel)infoPanel.getComponent(2);
            JLabel userLabel = (JLabel)userPanel.getComponent(0);
            userLabel.setText(String.format("맞히는 사람 : %s", guesserInfo.getNickname()));

            infoPanel.revalidate();
            infoPanel.repaint();
        });
    }

    public GameScreen() {
        // DrawingController 초기화
        drawingController = new DrawingController(screenController);

        // 전체 레이아웃 설정: 3분할
        setLayout(new BorderLayout());

        // 1. 왼쪽: 현재 유저 리스트 패널
        makeUserPanel();

        // 2. 오른쪽: 정보 패널
        makeInfoPanel();

        // 3. 가운데: Drawing 패널
        makeDrawingPanel();

        // 패널 추가
        add(userListPanel, BorderLayout.WEST);
        add(infoPanel, BorderLayout.EAST);
        add(drawingPanel, BorderLayout.CENTER);

        // 화면 표시
        setVisible(true);
    }

    private void makeUserPanel(){
        userListPanel = new JPanel(new GridLayout(9,1, 10,10 ));
        userListPanel.setBorder(BorderFactory.createTitledBorder("현재 유저들"));
        userListPanel.setPreferredSize(new Dimension(150, 0));
        userListPanel.setBackground(Color.decode("#e8f5e9"));

        // 더미 데이터로 초기 유저 리스트 추가
        for (int i = 0; i < 9; i++) {
            JLabel userLabel = new JLabel("", SwingConstants.CENTER);
            userLabel.setOpaque(true);
            userLabel.setBackground(Color.decode("#c8e6c9"));
            userLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            userLabel.setVisible(false);
            userListPanel.add(userLabel);
        }
    }

    private void makeInfoPanel(){
        infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createTitledBorder("정보"));
        infoPanel.setPreferredSize(new Dimension(200, 0));
        infoPanel.setBackground(Color.decode("#fbe9e7"));

        // 주제 정보 영역
        JPanel topicPanel = new JPanel(new BorderLayout());
        topicPanel.setBackground(Color.decode("#ffe0b2"));
        topicPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 패딩 설정
        topicPanel.setMaximumSize(new Dimension(200, 80)); // 고정 크기 설정
        topicPanel.setPreferredSize(new Dimension(200, 80)); // 원하는 크기 설정
        JLabel topicLabel = new JLabel(String.format("그리기 주제: %s", selectedTopic), SwingConstants.CENTER);
        topicLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topicPanel.add(topicLabel, BorderLayout.CENTER);

        // 맞히는 사람 정보 영역
        JPanel drawerPanel = new JPanel(new BorderLayout());
        drawerPanel.setBackground(Color.decode("#ffccbc"));
        drawerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 패딩 설정
        drawerPanel.setMaximumSize(new Dimension(200, 80)); // 고정 크기 설정
        drawerPanel.setPreferredSize(new Dimension(200, 80)); // 원하는 크기 설정
        JLabel drawerLabel = new JLabel(String.format("맞히는 사람: %s", guesserInfo.getNickname()), SwingConstants.CENTER);
        drawerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        drawerPanel.add(drawerLabel, BorderLayout.CENTER);

        // 패널 추가
        infoPanel.add(topicPanel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10))); // 패널 간 간격
        infoPanel.add(drawerPanel);
    }

    private void makeDrawingPanel(){
        drawingPanel = new JPanel();
        drawingPanel.setLayout(new OverlayLayout(drawingPanel)); // OverlayLayout 설정
        drawingPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Drawing 영역 (기본 Panel)
        JPanel drawingArea = drawingController.getDrawingPanel();
        drawingArea.setBackground(Color.WHITE); // 기본 배경
        drawingPanel.add(drawingArea);

        // JLabel 추가
        JLabel label = new JLabel("Sample Text", SwingConstants.CENTER);
        label.setForeground(Color.RED); // 텍스트 색상 설정
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setAlignmentX(0.5f); // 중앙 정렬
        label.setAlignmentY(0.5f);

        drawingPanel.add(label);
    }

    public static void setGameInfoFromDTO(ServerStartGameEvent dto){
        Set<UserInfo> userInfos = new HashSet<>(LobbyScreen.roomInfo.getUserInfoList());

        for(Integer userId : dto.getOrderList()){
            UserInfo matchedUserInfo = userInfos.stream()
                    .filter(userInfo -> userInfo.getId() == userId)
                    .findFirst()
                    .orElse(null);

            userOrder.add(matchedUserInfo);
        }

        guesserInfo = userInfos.stream()
                .filter(userInfo -> userInfo.getId() == dto.getSelectedUser())
                .findFirst()
                .orElse(null);

        selectedTopic = dto.getSelectedTopic();
    }

    public static void handleRemoteDrawEvent(ServerDrawEvent event){
        drawingController.handleRemoteDrawEvent(event);
    }

    public static void handleServerTurnChangeEvent(ServerTurnChangeEvent event){
        drawingController.handleServerTurnChangeEvent(event);
        updateCurrentUser(event.getNowTurn());
    }
}