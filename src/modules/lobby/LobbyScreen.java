package modules.lobby;

import common.screen.Screen;
import dto.event.client.*;
import dto.info.RoomInfo;
import dto.info.UserInfo;
import message.Message;
import modules.game.GameScreen;
import modules.roomList.RoomListScreen;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

import static message.MessageType.*;

public class LobbyScreen extends Screen {
    public static final String screenName = "LOBBY_SCREEN";

    private static JPanel roomInfoPanel;
    private static JPanel userPanel;
    private static JPanel readyPanel;
    private static DefaultListModel<String> chatModel;
    private static JList<String> chatList;

    public static RoomInfo roomInfo = new RoomInfo(-1, -1, -1, new ArrayList<>());
    private static boolean isReady = false;

    // NOTE: swing을 한번 constructor에서 그리고나면, 값이 변경되어도 화면이 다시 그려지지 않음. 그렇기에 invokeLater 호출 필요.
    public static void updateRoomInfoOnSwing(){
        SwingUtilities.invokeLater(() -> {
            if (roomInfo.getId() != -1) {
                //방 설정 정보 update
                roomInfoPanel.removeAll();
                JLabel roomIdLabel = new JLabel(String.format("방 ID: %d", roomInfo.getId()));
                JLabel setting1 = new JLabel(String.format("그리기 제한 시간: %d초", roomInfo.getDrawTimeLimit()));
                JLabel setting2 = new JLabel(String.format("참여 참가자 수: %d명", roomInfo.getParticipantLimit()));
                roomInfoPanel.add(roomIdLabel);
                roomInfoPanel.add(setting1);
                roomInfoPanel.add(setting2);
                roomInfoPanel.revalidate();
                roomInfoPanel.repaint();
            }
        });
    }

    public static void updateUserFieldOnSwing(){
        SwingUtilities.invokeLater(() -> {
            //초기화
            for (int i = 0; i < 9; i++) {
                JLabel targetUserArea = (JLabel) userPanel.getComponent(i);
                targetUserArea.setVisible(false);
            }

            //유저 목록 update
            java.util.List<UserInfo> userList = roomInfo.getUserInfoList();
            for (int i = 0; i < userList.size(); i++) {
                JLabel targetUserArea = (JLabel) userPanel.getComponent(i);
                UserInfo user = userList.get(i);
                targetUserArea.setText(String.format("<html>[ID]: %d<br>[Nickname]: %s</html>", user.getId(), user.getNickname()));
                targetUserArea.setVisible(true);
            }
            userPanel.revalidate();
            userPanel.repaint();

            updateUserReadyStatus();
        });
    }

    public static void updateReadyStatus() {
        SwingUtilities.invokeLater(() -> {
            JLabel readyStatus = (JLabel) readyPanel.getComponent(1);
            if (isReady) {
                readyStatus.setText("준비 완료");
            } else {
                readyStatus.setText("준비 해주세요");
            }
            updateUserReadyStatus();
        });
    }

    public static void updateUserReadyStatus(){
        SwingUtilities.invokeLater(()->{
            java.util.List<UserInfo> userList = roomInfo.getUserInfoList();

            for (int i = 0; i < userList.size(); i++) {
                String path = String.format("../../resources/person%d.png",(i%3)+1);
                if(userList.get(i).isReady()){
                    path = String.format("../../resources/person%d_ready.png",(i%3)+1);
                }

                JLabel userLabel = (JLabel) userPanel.getComponent(i);

                ImageIcon logoIcon = new ImageIcon(LobbyScreen.class.getResource(path));
                Image logoImage = logoIcon.getImage().getScaledInstance(120, 140, Image.SCALE_SMOOTH); // 크기 조정

                userLabel.setIcon(new ImageIcon(logoImage));
                userLabel.setVisible(true);
            }
        });
    }

    public LobbyScreen() {
        setSize(800, 600);
        setLayout(new GridLayout(1, 2)); // 좌우로 나뉜 레이아웃

        // 좌측 사용자 정보 영역 (GridLayout 사용)
        makeUserFieldPanel();
        // 우측 채팅 및 설정 영역
        JPanel rightPanel = new JPanel(new GridLayout(2, 1)); // 위아래로 나뉜 레이아웃
        //채팅 영역
        JPanel chatPanel = makeChatPanel();

        // 설정 및 Ready 버튼 영역
        JPanel settingsPanel = new JPanel(new GridLayout(1, 2)); // 좌우로 나뉨
        settingsPanel.setBorder(BorderFactory.createTitledBorder("설정 및 준비"));
        // 방 설정 정보
        makeRoomInfoPanel();
        // Ready 버튼
        readyPanel = makeReadyPanel();
        // 설정 및 Ready 버튼 영역 추가
        settingsPanel.add(roomInfoPanel);
        settingsPanel.add(readyPanel);

        // 우측 패널 구성 요소 추가
        rightPanel.add(chatPanel);
        rightPanel.add(settingsPanel);

        // 메인 화면에 좌측 및 우측 패널 추가
        add(userPanel);
        add(rightPanel);

        setVisible(true);
    }

    private void makeUserFieldPanel(){
        userPanel = new JPanel(new GridLayout(0, 3, 10, 10)); // 3열 그리드
        userPanel.setBorder(BorderFactory.createTitledBorder("사용자 목록"));
        userPanel.setBackground(Color.decode("#f1f8e9"));

        for (int i = 0; i < 9; i++) {
            JLabel userLabel = new JLabel("", SwingConstants.CENTER);

            userLabel.setOpaque(true);
            userLabel.setHorizontalTextPosition(JLabel.CENTER); // 텍스트를 수평으로 가운데 정렬
            userLabel.setVerticalTextPosition(JLabel.BOTTOM);   // 텍스트를 이미지 아래로 배치
            userLabel.setVerticalAlignment(JLabel.TOP);         // JLabel 전체를 상단 정렬

            userLabel.setBackground(Color.decode("#c8e6c9"));
            userLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            userPanel.add(userLabel);
        }

        updateUserFieldOnSwing(); // 유저 field 초기화
    }

    private JPanel makeChatPanel(){
        // 채팅 영역
        JPanel chatPanel = new JPanel(new BorderLayout());
        chatPanel.setBorder(BorderFactory.createTitledBorder("채팅 창"));
        chatPanel.setBackground(Color.decode("#e3f2fd"));

        // 채팅 기록 모델 및 JList 설정
        chatModel = new DefaultListModel<>();
        chatList = new JList<>(chatModel);
        chatList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        chatList.setVisibleRowCount(10); // 보여질 행 수
        chatList.setFixedCellHeight(20);
        chatList.setFixedCellWidth(300);

        // 채팅 스크롤 패널
        JScrollPane chatScrollPane = new JScrollPane(chatList);
        chatPanel.add(chatScrollPane, BorderLayout.CENTER);

        // 채팅 입력 및 전송 버튼
        JTextField chatInputField = new JTextField();
        JButton sendButton = new JButton("Send");

        // 전송 버튼 동작
        sendButton.addActionListener(e -> {
            String message = chatInputField.getText().trim();
            if (!message.isEmpty()) {
                try {
                    // 서버로 전송
                    ClientRoomChatMessage dto = new ClientRoomChatMessage(message);
                    screenController.sendToServer(new Message(CLIENT_ROOM_CHAT_MESSAGE, dto));

                    // 클라이언트에 채팅 추가 (서버 응답 처리 이전 가정)
                    addChatMessage(chatModel, "나: " + message);
                    chatInputField.setText(""); // 입력 필드 초기화
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(chatPanel, "메시지 전송 실패!", "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        JPanel chatInputPanel = new JPanel(new BorderLayout());
        chatInputPanel.add(chatInputField, BorderLayout.CENTER);
        chatInputPanel.add(sendButton, BorderLayout.EAST);

        chatPanel.add(chatScrollPane, BorderLayout.CENTER);
        chatPanel.add(chatInputPanel, BorderLayout.SOUTH);

        return chatPanel;
    }

    // 채팅 추가 함수
    private static void addChatMessage(DefaultListModel<String> chatModel, String message) {
        int maxMessages = 10; // 최대 채팅 기록 수
        if (chatModel.getSize() >= maxMessages) {
            chatModel.remove(0); // 오래된 메시지 제거
        }
        chatModel.addElement(message); // 새 메시지 추가
        chatList.ensureIndexIsVisible(chatModel.getSize() - 1); // 스크롤 최신화
    }

    // 서버로부터 메시지를 받을 때 처리
    public static void receiveChatMessage(String serverMessage) {
        SwingUtilities.invokeLater(() -> addChatMessage(chatModel, serverMessage));
    }

    private void makeRoomInfoPanel(){
        roomInfoPanel = new JPanel();
        roomInfoPanel.setLayout(new BoxLayout(roomInfoPanel, BoxLayout.Y_AXIS));
        roomInfoPanel.setBorder(BorderFactory.createTitledBorder("방 설정 정보"));
        roomInfoPanel.setBackground(Color.decode("#fff9c4"));

        updateRoomInfoOnSwing(); // 초기 정보 설정
    }

    private JPanel makeReadyPanel(){
        JPanel readyPanel = new JPanel();
        readyPanel.setLayout(new BoxLayout(readyPanel, BoxLayout.Y_AXIS));
        readyPanel.setBackground(Color.decode("#ffcdd2"));

        JLabel readyStatus = new JLabel("준비 해주세요");
        readyStatus.setOpaque(true);

        JButton readyButton = new JButton("Ready");
        readyButton.addActionListener(e->{
            try {
                isReady = !isReady;
                screenController.sendToServer(new Message(CLIENT_GAME_READY_EVENT, new ClientGameReadyEvent(isReady)));
                updateReadyStatus();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e->{
            try {
                screenController.sendToServer(new Message(CLIENT_EXIT_ROOM_EVENT, new ClientExitRoomEvent()));
                screenController.showScreen(RoomListScreen.screenName);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        readyPanel.add(Box.createVerticalGlue()); // 위쪽 여백
        readyPanel.add(readyStatus);
        readyPanel.add(readyButton);
        readyPanel.add(exitButton);
        readyPanel.add(Box.createVerticalGlue()); // 아래쪽 여백

        return readyPanel;
    }

    public static void showTopicInputDialog(JFrame parentFrame) {
        // 모달 다이얼로그 생성
        JDialog dialog = new JDialog(parentFrame, true); // 모달 설정
        dialog.setTitle(screenController.getUserInfo().getNickname());
        dialog.setLayout(new BorderLayout());
        dialog.setSize(300, 200);

        // 입력 필드 및 레이블
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel instructionLabel = new JLabel("그림 주제를 제안해주세요");
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
        JButton confirmButton = new JButton("Confirm");

        confirmButton.addActionListener(e -> {
            String inputValue = inputField.getText().trim();
            if (!inputValue.isEmpty()) {
                try {
                    System.out.println("Input Value: " + inputValue);
                    screenController.sendToServer(new Message(CLIENT_SUGGEST_TOPIC_EVENT, new ClientSuggestTopicEvent(inputValue)));
                    screenController.showScreen(GameScreen.screenName);
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

    public static void resetState() {
        roomInfo = new RoomInfo(-1, -1, -1, new ArrayList<>());
        isReady = false;

        SwingUtilities.invokeLater(() -> {
            updateRoomInfoOnSwing();
            updateUserFieldOnSwing();
            updateReadyStatus();
        });
    }
}