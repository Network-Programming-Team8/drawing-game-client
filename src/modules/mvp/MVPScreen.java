package modules.mvp;

import common.drawing.DrawingController;
import common.screen.Screen;
import dto.event.client.ClientExitRoomEvent;
import dto.event.client.ClientVoteEvent;
import dto.event.client.ClientVoteReadyEvent;
import dto.info.UserInfo;
import dto.info.VoteInfo;
import message.Message;
import message.MessageType;
import modules.game.GameScreen;
import modules.lobby.LobbyScreen;
import modules.roomList.RoomListScreen;
import utils.UnixSeconds;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MVPScreen extends Screen {
    public static final String screenName = "MVP_SCREEN";
    private static JPanel userPanel;
    private static JLabel resultLabel;
    private static Timer voteTimer;
    private static JLabel timerLabel;

    private static Map<Integer, JLabel> voteLabels = new HashMap<>();
    private static JButton returnToLobbyButton;

    public MVPScreen() throws IOException {
        setLayout(new BorderLayout());

        makeUserPanel();

        resultLabel = new JLabel("투표 결과를 기다리는 중...");
        resultLabel.setHorizontalAlignment(JLabel.CENTER);
        add(resultLabel, BorderLayout.SOUTH);

        timerLabel = new JLabel("아직 참여자들이 그리기 과정을 보고있어요!");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timerLabel.setHorizontalAlignment(JLabel.CENTER);
        add(timerLabel, BorderLayout.NORTH);

        returnToLobbyButton = new JButton("로비로 돌아가기");
        returnToLobbyButton.setEnabled(false);
        returnToLobbyButton.addActionListener(e -> {
            try {
                screenController.sendToServer(new Message(MessageType.CLIENT_EXIT_ROOM_EVENT, new ClientExitRoomEvent()));
                resetGameState(); // 게임 상태 초기화
                LobbyScreen.resetState(); // LobbyScreen 상태 초기화
                screenController.showScreen(RoomListScreen.screenName);
            } catch (IOException ex) {
                ex.printStackTrace();
                screenController.showToast("로비로 돌아가는 중 오류가 발생했습니다.");
            }
        });
        add(returnToLobbyButton, BorderLayout.EAST);

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
                    if(voteTimer == null || !voteTimer.isRunning()){
                        screenController.showToast("아직 투표할 수 없어요!");
                    } else {
                        try {
                            screenController.sendToServer(new Message(MessageType.CLIENT_VOTE_EVENT, new ClientVoteEvent(user.getId())));
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

    public static void startVoteTimer(UnixSeconds endTime) {
        if (voteTimer != null) {
            voteTimer.stop();
        }

        voteTimer = new Timer(1000, e -> {
            UnixSeconds now = UnixSeconds.now();
            long remainingSeconds = now.isBefore(endTime) ? now.secondsUntil(endTime) : 0;;
            if (remainingSeconds > 0) {
                timerLabel.setText("투표 시간: " + remainingSeconds + "초");
            } else {
                timerLabel.setText("투표 종료");
                voteTimer.stop();
                disableAllVoteButtons();
                returnToLobbyButton.setEnabled(true);
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
            returnToLobbyButton.setEnabled(true);
        });
    }

    private static UserInfo findUserById(int userId) {
        for(UserInfo user: GameScreen.userOrder){
            if(user.getId() == userId) return user;
        }
        return null;
    }

    public static void resetGameState() {
        if (voteTimer != null) {
            voteTimer.stop();
        }
        voteTimer = null;
        voteLabels.clear();

        // 다른 클래스의 static 변수 초기화
        GameScreen.resetGameState();
        LobbyScreen.resetState();
        DrawingController.resetState();

        // Swing 컴포넌트 초기화
        SwingUtilities.invokeLater(() -> {
            userPanel.removeAll();
            resultLabel.setText("투표 결과를 기다리는 중...");
            timerLabel.setText("아직 참여자들이 그리기 과정을 보고있어요!");
            returnToLobbyButton.setEnabled(false);
            userPanel.revalidate();
            userPanel.repaint();
        });
    }
}