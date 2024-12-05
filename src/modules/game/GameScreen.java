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
    private static UserInfo guesserInfo;
    private static String selectedTopic;

    private static JPanel userListPanel;

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
                if(userOrder.get(i).getId() == nowTurnUserId){
                    JLabel userLabel = (JLabel)userListPanel.getComponent(i);
                    userLabel.setBorder(BorderFactory.createLineBorder(Color.RED, 5));

                    userListPanel.revalidate();
                    userListPanel.repaint();
                }
            }
        });
    }

    public GameScreen() {
        // DrawingController 초기화
        drawingController = new DrawingController(screenController);

        // 전체 레이아웃 설정: 3분할
        setLayout(new BorderLayout());

        // 1. 왼쪽: 현재 유저 리스트 패널
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