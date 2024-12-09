package modules.game;

import common.drawing.DrawingController;
import common.screen.Screen;
import dto.event.client.ClientGuessEvent;
import dto.event.client.ClientVoteReadyEvent;
import dto.event.server.ServerDrawEvent;
import dto.event.server.ServerStartGameEvent;
import dto.event.server.ServerTurnChangeEvent;
import dto.info.DrawElementInfo;
import dto.info.UserInfo;
import message.Message;
import message.MessageType;
import modules.lobby.LobbyScreen;
import modules.mvp.MVPScreen;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static message.MessageType.CLIENT_GUESS_EVENT;

public class GameScreen extends Screen {
    public static final String screenName = "GAME_SCREEN";
    private static DrawingController drawingController;

    public static ArrayList<UserInfo> userOrder = new ArrayList<UserInfo>();
    private static UserInfo guesserInfo = new UserInfo(1, "", false);
    private static String selectedTopic;

    private static JPanel userListPanel;
    private static JPanel infoPanel;
    private static JPanel drawingPanel;
    private static JLabel statusLabel;

    public static void updateStatusLabelGameStart() {
        SwingUtilities.invokeLater(() -> {
            statusLabel.setText("Game Start!");
        });
    }

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

    public static void startTimeLabel(int remainingSeconds){
        SwingUtilities.invokeLater(() -> {
            JLabel timeLabel = (JLabel)drawingPanel.getComponent(0);
            startCountdown(timeLabel, remainingSeconds);
        });
    }



    public static void updateRoomInfoPanel(){
        SwingUtilities.invokeLater(() -> {
            JPanel topicPanel = (JPanel)infoPanel.getComponent(0);
            JLabel topicLabel = (JLabel)topicPanel.getComponent(0);
            String topic=String.format("Drawing Topic : \n%s", selectedTopic);
            if(guesserInfo.getId() == DrawingController.getCurrentUserId()){
                topic = String.format("You are Guesser!");
            }
            topicLabel.setText(String.format(topic));
            JPanel userPanel = (JPanel)infoPanel.getComponent(2);
            JLabel userLabel = (JLabel)userPanel.getComponent(0);
            userLabel.setText(String.format("Guesser : %s", guesserInfo.getNickname()));

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
        userListPanel.setBorder(BorderFactory.createTitledBorder("Current Users"));
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
        infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));
        infoPanel.setPreferredSize(new Dimension(200, 0));
        infoPanel.setBackground(Color.decode("#fbe9e7"));

        // 주제 정보 영역
        JPanel topicPanel = new JPanel(new BorderLayout());
        topicPanel.setBackground(Color.decode("#ffe0b2"));
        topicPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 패딩 설정
        topicPanel.setMaximumSize(new Dimension(200, 80)); // 고정 크기 설정
        topicPanel.setPreferredSize(new Dimension(200, 80)); // 원하는 크기 설정
        JLabel topicLabel = new JLabel("Other User typing\n Topic", SwingConstants.CENTER);
        topicLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topicPanel.add(topicLabel, BorderLayout.CENTER);

        // 맞히는 사람 정보 영역
        JPanel drawerPanel = new JPanel(new BorderLayout());
        drawerPanel.setBackground(Color.decode("#ffccbc"));
        drawerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 패딩 설정
        drawerPanel.setMaximumSize(new Dimension(200, 80)); // 고정 크기 설정
        drawerPanel.setPreferredSize(new Dimension(200, 80)); // 원하는 크기 설정
        JLabel drawerLabel = new JLabel("Selecting Guesser...", SwingConstants.CENTER);
        drawerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        drawerPanel.add(drawerLabel, BorderLayout.CENTER);

        // 패널 추가
        infoPanel.add(topicPanel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10))); // 패널 간 간격
        infoPanel.add(drawerPanel);
    }

    private void makeDrawingPanel() {
        drawingPanel = new JPanel();
        drawingPanel.setLayout(new BorderLayout());
        drawingPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JPanel drawingArea = drawingController.getDrawingPanel();
        drawingArea.setPreferredSize(new Dimension(300, 300));
        drawingArea.setBackground(Color.WHITE);
        drawingArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        statusLabel = new JLabel("participant is still proposing the topic", SwingConstants.CENTER);
        statusLabel.setForeground(Color.RED);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 20));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setVerticalAlignment(SwingConstants.TOP);

        drawingPanel.add(statusLabel, BorderLayout.NORTH);
        drawingPanel.add(drawingArea, BorderLayout.CENTER);
    }

    // 남은 시간 카운트다운
    private static void startCountdown(JLabel label, int remainingSeconds) {
        Timer timer = new Timer(1000, null);
        final int[] timeLeft = {remainingSeconds};
        timer.addActionListener(e -> {
            timeLeft[0]--;
            if (timeLeft[0] >= 0) {
                label.setText("Time left: " + timeLeft[0]);
            } else {
                timer.stop();
                label.setText("Time is up!");
            }
        });
        timer.start();
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

    public static int getGuesserId(){
        return guesserInfo.getId();
    }

    public static void showGuessInputDialog(JFrame parentFrame) {
        //이전에 그렸던 그림 로드
        drawingController.rePaintPanel();

        // 모달 다이얼로그 생성
        JDialog dialog = new JDialog(parentFrame, true); // 모달 설정
        dialog.setTitle(screenController.getUserInfo().getNickname());
        dialog.setLayout(new BorderLayout());
        dialog.setSize(300, 200);

        // 입력 필드 및 레이블
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel instructionLabel = new JLabel("Please enter predicted topic!");
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        instructionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        inputPanel.add(instructionLabel);

        JTextField inputField = new JTextField(15);
        inputField.setMaximumSize(new Dimension(200, 30));
        inputField.setAlignmentX(Component.CENTER_ALIGNMENT);
        inputField.setFont(new Font("Arial", Font.PLAIN, 12));
        inputPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        inputPanel.add(inputField);

        dialog.add(inputPanel, BorderLayout.CENTER);

        // 버튼
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton confirmButton = new JButton("Submit");

        confirmButton.addActionListener(e -> {
            String inputValue = inputField.getText().trim();
            if (!inputValue.isEmpty()) {
                try {
                    System.out.println("Input Value: " + inputValue);
                    screenController.sendToServer(new Message(CLIENT_GUESS_EVENT, new ClientGuessEvent(inputValue)));
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(dialog, "Error sending input to server: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                dialog.dispose(); // 다이얼로그 닫기
            } else {
                JOptionPane.showMessageDialog(dialog, "Please enter a value.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(confirmButton);

        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // 다이얼로그 위치 설정
        dialog.setLocationRelativeTo(parentFrame);

        // 다이얼로그 표시
        dialog.setVisible(true);
    }

    public static void showGuessResultDialog(JFrame parentFrame, String topic, String guesser_answer, Map<Integer, java.util.List<DrawElementInfo>> drawingMap) {
        // 모달 다이얼로그 생성
        JDialog dialog = new JDialog(parentFrame, true); // 모달 설정
        dialog.setTitle(screenController.getUserInfo().getNickname());
        dialog.setLayout(new BorderLayout());
        dialog.setSize(300, 200);

        // 입력 필드 및 레이블
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel guesserAnswer = new JLabel(String.format("Predicted by Guesser : %s", guesser_answer));
        guesserAnswer.setAlignmentX(Component.CENTER_ALIGNMENT);
        guesserAnswer.setFont(new Font("Arial", Font.BOLD, 14));
        inputPanel.add(guesserAnswer);

        JLabel selectedTopic = new JLabel(String.format("Actual drawing topic : %s", topic));
        selectedTopic.setAlignmentX(Component.CENTER_ALIGNMENT);
        selectedTopic.setFont(new Font("Arial", Font.BOLD, 14));
        inputPanel.add(selectedTopic);

        String guess_result = (topic.equals(guesser_answer)) ? "That's right!" : "Wrong answer..";
        Color resultColor = (topic.equals(guesser_answer)) ? Color.BLUE : Color.RED;

        JLabel guess_resultLabel = new JLabel(guess_result);
        guess_resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        guess_resultLabel.setForeground(resultColor);
        guess_resultLabel.setFont(new Font("Arial", Font.BOLD, 20));
        inputPanel.add(guess_resultLabel);

        // 버튼
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton confirmButton = new JButton("Looking back on the drawing process");

        confirmButton.addActionListener(e -> {
            dialog.dispose();
            drawingController.printDrawingMap(drawingMap);
        });

        buttonPanel.add(confirmButton);

        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(inputPanel, BorderLayout.CENTER);

        // 다이얼로그 위치 설정
        dialog.setLocationRelativeTo(parentFrame);

        // 다이얼로그 표시
        dialog.setVisible(true);
    }

    public static void transitionToMVPScreen() throws IOException{
        screenController.sendToServer(new Message(MessageType.CLIENT_VOTE_READY_EVENT, new ClientVoteReadyEvent(true)));
        SwingUtilities.invokeLater(() -> {
            MVPScreen.updateUserList(userOrder);
            screenController.showScreen(MVPScreen.screenName);
        });
    }

    public static void resetGameState() {
        userOrder.clear();
        guesserInfo = new UserInfo(1, "", false);
        selectedTopic = null;

        SwingUtilities.invokeLater(() -> {
            userListPanel.removeAll();
            infoPanel.removeAll();
            drawingPanel.removeAll();
            statusLabel.setText("participant is still proposing the topic");
            userListPanel.revalidate();
            userListPanel.repaint();
            infoPanel.revalidate();
            infoPanel.repaint();
            drawingPanel.revalidate();
            drawingPanel.repaint();
        });
    }
}