package modules.login;

import common.screen.Screen;
import dto.event.client.ClientLoginEvent;
import message.Message;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import static message.MessageType.CLIENT_LOGIN_EVENT;

public class LoginScreen extends Screen {
    public static final String screenName = "LOGIN_SCREEN";

    public LoginScreen() {
        // GridBagLayout을 사용하여 중앙 정렬을 위한 레이아웃 설정
        setLayout(new GridBagLayout());

        // GridBagConstraints 설정
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0; // 첫 번째 행에 배치
        gbc.insets = new Insets(10, 10, 10, 10); // 컴포넌트 간 간격 설정
        gbc.anchor = GridBagConstraints.CENTER; // 중앙 정렬
        gbc.fill = GridBagConstraints.BOTH; // 세로, 가로로 모두 확장

        // mainPanel을 BoxLayout으로 세로 배치
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // 배경색 설정
        mainPanel.setBackground(Color.decode("#f0f0f0"));
        mainPanel.setOpaque(true); // 배경색이 보이도록 설정

        // 1. JPG 이미지 로고 추가
        JLabel logoLabel = new JLabel();
        try {
            // 리소스 경로에서 JPG 파일 로드
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("../../resources/image4.png"));
            Image logoImage = logoIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH); // 크기 조정
            logoLabel.setIcon(new ImageIcon(logoImage));
        } catch (Exception e) {
            logoLabel.setText("Logo not found!");
        }
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(logoLabel);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 20))); // 간격 추가

        // 1.1 서비스 이름 (KUhoot)
        JLabel serviceLabel = new JLabel("KUhoot");
        serviceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        serviceLabel.setFont(new Font("Arial", Font.BOLD, 36)); // 폰트 크기 설정
        serviceLabel.setForeground(Color.decode("#011b2e")); // 색상 설정
        mainPanel.add(serviceLabel);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 30))); // 간격 추가: KUhoot과 nickname 사이

        // 2. 닉네임 라벨
        JLabel nicknameLabel = new JLabel("사용하실 닉네임을 입력해주세요");
        nicknameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(nicknameLabel);

        // 3. 닉네임 입력 필드
        JTextField nicknameField = new JTextField(15);
        nicknameField.setMaximumSize(new Dimension(200, 30));
        nicknameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(nicknameField);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 20))); // 간격 추가

        // 4. 제출 버튼
        JButton submitButton = new JButton("Submit");
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.addActionListener(e -> {
            String nickname = nicknameField.getText();
            if (nickname.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a nickname.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Nickname submitted: " + nickname, "Success", JOptionPane.INFORMATION_MESSAGE);

                try {
                    screenController.sendToServer(new Message(CLIENT_LOGIN_EVENT, new ClientLoginEvent(nickname)));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        mainPanel.add(submitButton);

        // mainPanel을 GridBagLayout의 중앙에 추가
        add(mainPanel, gbc);

        // 화면 표시
        setVisible(true);
    }
}