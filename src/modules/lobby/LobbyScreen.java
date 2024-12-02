package modules.lobby;

import common.screen.Screen;
import dto.info.RoomInfo;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class LobbyScreen extends Screen {
    public static final String screenName = "LOBBY_SCREEN";
    private static JPanel roomInfoPanel;
    public static RoomInfo roomInfo = new RoomInfo(-1, -1, -1, new ArrayList<>());

    // NOTE: swing을 한번 constructor에서 그리고나면, 값이 변경되어도 화면이 다시 그려지지 않음. 그렇기에 invokeLater 호출 필요.
    public static void updateRoomInfoOnSwing(){
        SwingUtilities.invokeLater(() -> {
            if (roomInfo.getId() != -1) {
                roomInfoPanel.removeAll();
                JLabel setting1 = new JLabel(String.format("그리기 제한 시간: %d초", roomInfo.getDrawTimeLimit()));
                JLabel setting2 = new JLabel(String.format("최대 참가자 수: %d명", roomInfo.getParticipantLimit()));
                roomInfoPanel.add(setting1);
                roomInfoPanel.add(setting2);
                roomInfoPanel.revalidate();
                roomInfoPanel.repaint();
            }
        });
    }

    public LobbyScreen() {
        setSize(800, 600);
        setLayout(new GridLayout(1, 2)); // 좌우로 나뉜 레이아웃

        // 좌측 사용자 정보 영역 (GridLayout 사용)
        JPanel userPanel = new JPanel(new GridLayout(0, 3, 10, 10)); // 3열 그리드
        userPanel.setBorder(BorderFactory.createTitledBorder("사용자 목록"));
        userPanel.setBackground(Color.decode("#f1f8e9"));

        // 예시 사용자 정보 추가
        for (int i = 1; i <= 9; i++) {
            JLabel userLabel = new JLabel("User " + i, SwingConstants.CENTER);
            userLabel.setOpaque(true);
            userLabel.setBackground(Color.decode("#c8e6c9"));
            userLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            userPanel.add(userLabel);
        }

        // 우측 채팅 및 설정 영역
        JPanel rightPanel = new JPanel(new GridLayout(2, 1)); // 위아래로 나뉜 레이아웃

        // 채팅 영역
        JPanel chatPanel = new JPanel(new BorderLayout());
        chatPanel.setBorder(BorderFactory.createTitledBorder("채팅 창"));
        chatPanel.setBackground(Color.decode("#e3f2fd"));

        JTextArea chatArea = new JTextArea();
        chatArea.setEditable(false); // 채팅 기록은 읽기 전용
        JScrollPane chatScrollPane = new JScrollPane(chatArea);

        JTextField chatInputField = new JTextField();
        JButton sendButton = new JButton("Send");

        JPanel chatInputPanel = new JPanel(new BorderLayout());
        chatInputPanel.add(chatInputField, BorderLayout.CENTER);
        chatInputPanel.add(sendButton, BorderLayout.EAST);

        chatPanel.add(chatScrollPane, BorderLayout.CENTER);
        chatPanel.add(chatInputPanel, BorderLayout.SOUTH);

        // 설정 및 Ready 버튼 영역
        JPanel settingsPanel = new JPanel(new GridLayout(1, 2)); // 좌우로 나뉨
        settingsPanel.setBorder(BorderFactory.createTitledBorder("설정 및 준비"));

        // 방 설정 정보
        roomInfoPanel = new JPanel();
        roomInfoPanel.setLayout(new BoxLayout(roomInfoPanel, BoxLayout.Y_AXIS));
        roomInfoPanel.setBorder(BorderFactory.createTitledBorder("방 설정 정보"));
        roomInfoPanel.setBackground(Color.decode("#fff9c4"));

        roomInfoPanel = new JPanel();
        roomInfoPanel.setLayout(new BoxLayout(roomInfoPanel, BoxLayout.Y_AXIS));
        roomInfoPanel.setBorder(BorderFactory.createTitledBorder("방 설정 정보"));
        roomInfoPanel.setBackground(Color.decode("#fff9c4"));

        updateRoomInfoOnSwing(); // 초기 정보 설정

        // Ready 버튼
        JPanel readyPanel = new JPanel();
        readyPanel.setLayout(new BoxLayout(readyPanel, BoxLayout.Y_AXIS));
        readyPanel.setBackground(Color.decode("#ffcdd2"));

        JButton readyButton = new JButton("Ready");
        readyPanel.add(Box.createVerticalGlue()); // 위쪽 여백
        readyPanel.add(readyButton);
        readyPanel.add(Box.createVerticalGlue()); // 아래쪽 여백

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
}