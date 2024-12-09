package modules.roomList;

import common.drawing.DrawingController;
import common.screen.Screen;
import dto.event.client.ClientCreateRoomEvent;
import dto.event.client.ClientJoinRoomEvent;
import message.Message;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import static message.MessageType.CLIENT_CREATE_ROOM_EVENT;
import static message.MessageType.CLIENT_JOIN_ROOM_EVENT;

public class RoomListScreen  extends Screen {
    public static final String screenName = "ROOM_LIST_SCREEN";

    public RoomListScreen() {
        // GridLayout으로 좌우로 나뉜 레이아웃 설정
        setLayout(new GridLayout(1, 2));

        // 배경색 설정
        Color leftPanelBg = Color.decode("#e8f5e9");
        Color rightPanelBg = Color.decode("#e3f2fd");

        // 1. 방 생성 패널 (좌측)
        JPanel createPanel = new JPanel();
        createPanel.setBackground(leftPanelBg);
        createPanel.setLayout(new BoxLayout(createPanel, BoxLayout.Y_AXIS));

        // 방 생성 라벨
        JLabel createTitle = new JLabel("Create Room");
        createTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        createTitle.setFont(new Font("Arial", Font.BOLD, 24));
        createPanel.add(Box.createRigidArea(new Dimension(0, 20))); // 간격 추가
        createPanel.add(createTitle);

        // 그리기 시간 제한 입력
        JTextField timeLimitField = new JTextField(15);
        timeLimitField.setBorder(BorderFactory.createTitledBorder("Time limit (second)"));
        timeLimitField.setMaximumSize(new Dimension(200, 40));
        createPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        createPanel.add(timeLimitField);

        // 참여자 수 제한 입력
        JTextField participantLimitField = new JTextField(15);
        participantLimitField.setBorder(BorderFactory.createTitledBorder("Participants limit"));
        participantLimitField.setMaximumSize(new Dimension(200, 40));
        createPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        createPanel.add(participantLimitField);

        // 방 생성 버튼
        JButton createButton = new JButton("Create Room");
        createButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        createButton.addActionListener(e -> {
            ClientCreateRoomEvent clientCreateRoomEvent = getCreateRoomDTOFromTextField(createPanel, timeLimitField, participantLimitField);
            try {
                screenController.sendToServer(new Message(CLIENT_CREATE_ROOM_EVENT, clientCreateRoomEvent));
                DrawingController.setTimeout(clientCreateRoomEvent.getDrawTimeLimit());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        createPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        createPanel.add(createButton);

        // 2. 방 참가 패널 (우측)
        JPanel joinPanel = new JPanel();
        joinPanel.setBackground(rightPanelBg);
        joinPanel.setLayout(new BoxLayout(joinPanel, BoxLayout.Y_AXIS));

        // 방 참가 라벨
        JLabel joinTitle = new JLabel("Participate room");
        joinTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        joinTitle.setFont(new Font("Arial", Font.BOLD, 24));
        joinPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        joinPanel.add(joinTitle);

        // Room ID 입력
        JTextField roomIdField = new JTextField(15);
        roomIdField.setBorder(BorderFactory.createTitledBorder("Room ID"));
        roomIdField.setMaximumSize(new Dimension(200, 40));
        joinPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        joinPanel.add(roomIdField);

        // Join 버튼
        JButton joinButton = new JButton("Join");
        joinButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        joinButton.addActionListener(e->{
            ClientJoinRoomEvent clientJoinRoomEvent = getJoinRoomDTOFromTextField(createPanel, roomIdField);
            try {
                screenController.sendToServer(new Message(CLIENT_JOIN_ROOM_EVENT, clientJoinRoomEvent));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        joinPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        joinPanel.add(joinButton);

        // 좌측과 우측 패널을 메인 프레임에 추가
        add(createPanel);
        add(joinPanel);

        // 화면 표시
        setVisible(true);
    }

    private ClientJoinRoomEvent getJoinRoomDTOFromTextField(JPanel createPanel, JTextField roomIdField) {
        String roomId = roomIdField.getText();

        if (roomId.isEmpty() || roomId.isEmpty()) {
            JOptionPane.showMessageDialog(createPanel, "Please fill in all the fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        int roomIdInt;
        try {
            roomIdInt = Integer.parseInt(roomId);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(createPanel, "The time limit and the number of participants must be entered as numbers.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        return new ClientJoinRoomEvent(roomIdInt);
    }

    private ClientCreateRoomEvent getCreateRoomDTOFromTextField(JPanel createPanel, JTextField timeLimitField, JTextField participantLimitField) {
        String timeLimitText = timeLimitField.getText();
        String participantLimitText = participantLimitField.getText();

        if (timeLimitText.isEmpty() || participantLimitText.isEmpty()) {
            JOptionPane.showMessageDialog(createPanel, "Please fill in all the fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        int timeLimit;
        int participantLimit;
        try {
            timeLimit = Integer.parseInt(timeLimitText);
            participantLimit = Integer.parseInt(participantLimitText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(createPanel, "The time limit and the number of participants must be entered as numbers.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        if(participantLimit > 9) {
            JOptionPane.showMessageDialog(createPanel, "The number of participants is limited to 9", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        return new ClientCreateRoomEvent(timeLimit, participantLimit);
    }
}
