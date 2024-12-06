package modules.mvp;

import common.screen.Screen;
import dto.info.UserInfo;
import modules.lobby.LobbyScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

public class MVPScreen extends Screen {
    public static final String screenName = "MVP_SCREEN";
    private static JFrame parentFrame;

    public MVPScreen(JFrame parentFrameArg) {
        parentFrame = parentFrameArg;
    }

    public static void showVoteDialog() {
        // 다이얼로그 생성
        JDialog dialog = new JDialog(parentFrame, "MVP", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(parentFrame);

        // 옵션 패널
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        ButtonGroup buttonGroup = new ButtonGroup();

        for (UserInfo userInfo : LobbyScreen.roomInfo.getUserInfoList()) {
            JRadioButton radioButton = new JRadioButton(userInfo.getNickname());
            radioButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            buttonGroup.add(radioButton);
            optionsPanel.add(radioButton);
        }

        // 버튼 패널
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton submitButton = new JButton("Submit");

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 선택된 버튼 가져오기
                String selectedOption = null;
                for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements(); ) {
                    AbstractButton button = buttons.nextElement();
                    if (button.isSelected()) {
                        selectedOption = button.getText();
                        break;
                    }
                }

                if (selectedOption != null) {
                    JOptionPane.showMessageDialog(dialog, "You selected: " + selectedOption);
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Please select an option.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        buttonPanel.add(submitButton);

        // 다이얼로그에 패널 추가
        dialog.add(new JLabel("Please select one option:", SwingConstants.CENTER), BorderLayout.NORTH);
        dialog.add(optionsPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // 다이얼로그 표시
        dialog.setVisible(true);
    }
}
