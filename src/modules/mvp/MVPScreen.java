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
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MVPScreen extends Screen {
    public static final String screenName = "MVP_SCREEN";
    private static JPanel userPanel;
    private static JLabel resultLabel;
    private static boolean hasVoted = false;
    private static Timer voteTimer;
    private static JLabel timerLabel;

    private static Map<Integer, JLabel> voteLabels = new HashMap<>();

    public MVPScreen() {
        setLayout(new BorderLayout());

        makeUserPanel();

        resultLabel = new JLabel("투표 결과를 기다리는 중...");
        resultLabel.setHorizontalAlignment(JLabel.CENTER);
        add(resultLabel, BorderLayout.SOUTH);

        timerLabel = new JLabel("투표 시간: 30초");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timerLabel.setHorizontalAlignment(JLabel.CENTER);
        add(timerLabel, BorderLayout.NORTH);

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

                JLabel voteLabel = new JLabel("투표 수: 0");
                voteLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                userVotePanel.add(voteLabel);
                voteLabels.put(user.getId(), voteLabel);

                JButton voteButton = new JButton("투표");
                voteButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                voteButton.addActionListener(e -> {
                    if (!hasVoted && voteTimer.isRunning()) {
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

    public static void updateVotes(VoteInfo voteInfo) {
        SwingUtilities.invokeLater(() -> {
            for (Map.Entry<Integer, Integer> entry : voteInfo.getVoteResults().entrySet()) {
                int userId = entry.getKey();
                int voteCount = entry.getValue();
                JLabel voteLabel = voteLabels.get(userId);
                if (voteLabel != null) {
                    voteLabel.setText("투표 수: " + voteCount);
                }
            }
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

    public static void startVoteTimer(LocalDateTime endTime) {
        if (voteTimer != null) {
            voteTimer.stop();
        }

        voteTimer = new Timer(1000, e -> {
            long remainingSeconds = java.time.Duration.between(LocalDateTime.now(), endTime).getSeconds();
            if (remainingSeconds > 0) {
                timerLabel.setText("투표 시간: " + remainingSeconds + "초");
            } else {
                timerLabel.setText("투표 종료");
                voteTimer.stop();
                disableAllVoteButtons();
            }
        });
        voteTimer.start();
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