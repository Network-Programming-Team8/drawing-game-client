package modules.mvp;

import common.screen.Screen;
import dto.event.client.ClientVoteEvent;
import dto.info.UserInfo;
import dto.info.VoteInfo;
import message.Message;
import message.MessageType;
import modules.lobby.LobbyScreen;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class MVPScreen extends Screen {
    public static final String screenName = "MVP_SCREEN";
    private static JPanel userPanel;
    private static JLabel resultLabel;
    private static boolean hasVoted = false;

    public MVPScreen() {
        setLayout(new BorderLayout());

        makeUserPanel();

        resultLabel = new JLabel("투표 결과를 기다리는 중...");
        resultLabel.setHorizontalAlignment(JLabel.CENTER);
        add(resultLabel, BorderLayout.SOUTH);

        for(UserInfo user: LobbyScreen.roomInfo.getUserInfoList()){
            System.out.println("user...!");
            System.out.println(user.getId());
        }
    }

    private void makeUserPanel() {
        userPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        userPanel.setBorder(BorderFactory.createTitledBorder("MVP 투표"));
        userPanel.setBackground(Color.decode("#f1f8e9"));

        JScrollPane scrollPane = new JScrollPane(userPanel);
        scrollPane.setPreferredSize(new Dimension(700, 400));
        add(scrollPane, BorderLayout.CENTER);
    }

    public static void updateUserList(List<UserInfo> users) {
        System.out.println("Updating user list. Users count: " + users.size());
        SwingUtilities.invokeLater(() -> {
            userPanel.removeAll();
            for (UserInfo user : users) {
                JPanel userVotePanel = new JPanel();
                userVotePanel.setLayout(new BoxLayout(userVotePanel, BoxLayout.Y_AXIS));
                userVotePanel.setBackground(Color.decode("#c8e6c9"));
                userVotePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                userVotePanel.setPreferredSize(new Dimension(200, 100));

                JLabel userLabel = new JLabel(String.format("<html>[ID]: %d<br>[Nickname]: %s</html>", user.getId(), user.getNickname()));
                userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                userVotePanel.add(userLabel);

                JButton voteButton = new JButton("투표");
                voteButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                voteButton.addActionListener(e -> {
                    if (!hasVoted) {
                        try {
                            screenController.sendToServer(new Message(MessageType.CLIENT_VOTE_EVENT, new ClientVoteEvent(user.getId())));
                            hasVoted = true;
                            disableAllVoteButtons();
                            screenController.showToast("투표가 완료되었습니다.");
                        } catch (IOException ex) {
                            ex.printStackTrace();
                            screenController.showToast("투표 중 오류가 발생했습니다.");
                        }
                    }
                });
                userVotePanel.add(voteButton);

                userPanel.add(userVotePanel);
            }
            userPanel.revalidate();
            userPanel.repaint();
        });
    }

    private static void disableAllVoteButtons() {
        for (Component component : userPanel.getComponents()) {
            if (component instanceof JPanel) {
                JPanel panel = (JPanel) component;
                for (Component innerComponent : panel.getComponents()) {
                    if (innerComponent instanceof JButton) {
                        innerComponent.setEnabled(false);
                    }
                }
            }
        }
    }

    public static void showVoteResult(VoteInfo voteInfo) {
        SwingUtilities.invokeLater(() -> {
            StringBuilder resultText = new StringBuilder("<html>투표 결과:<br>");
            for (Integer userId : voteInfo.getVoteResults().keySet()) {
                int voteCount = voteInfo.getVoteResults().get(userId);
                UserInfo user = findUserById(userId);
                if (user != null) {
                    resultText.append(String.format("%s: %d표<br>", user.getNickname(), voteCount));
                }
            }
            resultText.append("</html>");
            resultLabel.setText(resultText.toString());
        });
    }

    private static UserInfo findUserById(int userId) {
        // 이 메서드는 userId에 해당하는 UserInfo를 찾아 반환해야 합니다.
        // GameScreen의 userOrder 리스트나 다른 적절한 데이터 구조를 사용하여 구현해야 합니다.
        return null; // 임시 반환값
    }
}