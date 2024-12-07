package utils;

import common.drawing.DrawingController;
import common.screen.ScreenController;
import dto.event.server.*;
import dto.info.RoomInfo;
import dto.info.UserInfo;
import dto.info.VoteInfo;
import message.Message;
import modules.game.GameScreen;
import modules.lobby.LobbyScreen;
import modules.mvp.MVPScreen;
import modules.roomList.RoomListScreen;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.Map;

public class ServerListener implements Runnable {

    private final ScreenController screenController;
    private final ObjectInputStream in;
    private final JFrame frame;

    public ServerListener(ScreenController screenController, ObjectInputStream in, JFrame parentFrame) {
        this.screenController = screenController;
        this.in = in;
        this.frame = parentFrame;
    }

    @Override
    public void run() {
        while(true){
            try {
                Message message = (Message) in.readObject();
                handleMessage(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void handleMessage(Message message) {
        switch (message.getType()) {
            case SERVER_LOGIN_EVENT:
                handleServerLoginEvent(message);
                break;
            case SERVER_CREATE_ROOM_EVENT:
                handleServerCreateRoomEvent(message);
                break;
            case SERVER_ROOM_UPDATE_EVENT:
                handleServerRoomUpdateEvent(message);
                break;
            case SERVER_ROOM_CHAT_MESSAGE:
                handleServerRoomChatMessage(message);
                break;
            case SERVER_REQUEST_TOPIC_EVENT:
                handleServerRequestTopicEvent(message);
                break;
            case SERVER_ERROR_EVENT:
                handleServerErrorEvent(message);
                break;
            case SERVER_START_GAME_EVENT:
                handleServerStartGameEvent(message);
                break;
            case SERVER_TURN_CHANGE_EVENT:
                handleServerTurnChangeEvent(message);
                break;
            case SERVER_DRAW_EVENT:
                handleServerDrawEvent(message);
                break;
            case SERVER_FINISH_GAME_EVENT:
                handleServerFinishGameEvent(message);
                break;
            case SERVER_FINISH_VOTE_EVENT:
                handleServerFinishVoteEvent(message);
                break;
            case SERVER_VOTE_EVENT:
                handleServerVoteEvent(message);
                break;
            case SERVER_REQUEST_VOTE_EVENT:
                handleServerRequestVoteEvent(message);
                break;
        }
    }

    private void handleServerLoginEvent(Message message){
        ServerLoginEvent serverLoginEvent = (ServerLoginEvent) message.getMsgDTO();
        screenController.setUserInfo(new UserInfo(serverLoginEvent.getId(), serverLoginEvent.getNickname(), false));
        DrawingController.setCurrentUserId(serverLoginEvent.getId());
        screenController.showScreen(RoomListScreen.screenName);
    }

    private void handleServerCreateRoomEvent(Message message){
        ServerCreateRoomEvent serverCreateRoomEvent = (ServerCreateRoomEvent) message.getMsgDTO();

        RoomInfo roomInfo = new RoomInfo(
                serverCreateRoomEvent.getId(),
                serverCreateRoomEvent.getDrawTimeLimit(),
                serverCreateRoomEvent.getParticipantLimit(),
                Arrays.asList(screenController.getUserInfo())
        );
        LobbyScreen.roomInfo.fromRoomInfo(roomInfo);

        LobbyScreen.updateRoomInfoOnSwing();
        LobbyScreen.updateUserFieldOnSwing();

        screenController.showScreen(LobbyScreen.screenName);
    }

    private void handleServerRoomUpdateEvent(Message message){
        ServerRoomUpdateEvent serverRoomUpdateEvent = (ServerRoomUpdateEvent) message.getMsgDTO();

        LobbyScreen.roomInfo.fromRoomInfo(serverRoomUpdateEvent.getRoomInfo());
        DrawingController.setTimeout(serverRoomUpdateEvent.getRoomInfo().getDrawTimeLimit());

        LobbyScreen.updateRoomInfoOnSwing();
        LobbyScreen.updateUserFieldOnSwing();

        for(Component component : screenController.getComponents()){
            if(component instanceof RoomListScreen && component.isVisible()){
                screenController.showScreen(LobbyScreen.screenName);
                break;
            }
        }
    }

    private void handleServerRoomChatMessage(Message message){
        ServerRoomChatMessage serverRoomChatMessage = (ServerRoomChatMessage) message.getMsgDTO();

        if(screenController.getUserInfo().getNickname().equals(serverRoomChatMessage.getSpeaker())){
            return ;
        }
        LobbyScreen.receiveChatMessage(serverRoomChatMessage.getSpeaker() + ": " + serverRoomChatMessage.getMessage());
    }

    private void handleServerRequestTopicEvent(Message message){
        ServerRequestTopicEvent serverRequestTopicEvent = (ServerRequestTopicEvent) message.getMsgDTO();

        LobbyScreen.showTopicInputDialog(frame);
    }

    private void handleServerErrorEvent(Message message){
        ServerErrorEvent serverErrorEvent = (ServerErrorEvent) message.getMsgDTO();
        screenController.showToast(serverErrorEvent.getErrorMsg());
    }

    private void handleServerStartGameEvent(Message message){
        ServerStartGameEvent serverStartGameEvent = (ServerStartGameEvent) message.getMsgDTO();

        GameScreen.setGameInfoFromDTO(serverStartGameEvent);
        GameScreen.updateUserList();
        GameScreen.updateRoomInfoPanel();

        screenController.showScreen(GameScreen.screenName);
    }

    private void handleServerTurnChangeEvent(Message message){
        ServerTurnChangeEvent serverTurnChangeEvent = (ServerTurnChangeEvent) message.getMsgDTO();
        if(serverTurnChangeEvent.isGuessTurn() && DrawingController.getCurrentUserId() == serverTurnChangeEvent.getNowTurn()){
            GameScreen.updateCurrentUser(serverTurnChangeEvent.getNowTurn());
            GameScreen.showGuessInputDialog(frame);
            return;
        }
        GameScreen.handleServerTurnChangeEvent(serverTurnChangeEvent);
    }

    private void handleServerDrawEvent(Message message){
        ServerDrawEvent serverDrawEvent = (ServerDrawEvent) message.getMsgDTO();

        GameScreen.handleRemoteDrawEvent(serverDrawEvent);
    }

    private void handleServerFinishGameEvent(Message message){
        ServerFinishGameEvent serverFinishGameEvent = (ServerFinishGameEvent) message.getMsgDTO();

        GameScreen.showGuessResultDialog(frame, serverFinishGameEvent.getAnswer(), serverFinishGameEvent.getSubmittedAnswer(), serverFinishGameEvent.getDrawingMap());
    }

    private void handleServerFinishVoteEvent(Message message) {
        ServerFinishVoteEvent serverFinishVoteEvent = (ServerFinishVoteEvent) message.getMsgDTO();
        SwingUtilities.invokeLater(() -> {
            System.out.println("Server Finish Vote Event");
            MVPScreen.showVoteResult(serverFinishVoteEvent.getVoteInfo());
        });
    }

    private void handleServerVoteEvent(Message message) {
        ServerVoteEvent serverVoteEvent = (ServerVoteEvent) message.getMsgDTO();
        for (Map.Entry<Integer, Integer> entry : serverVoteEvent.getVoteInfo().getVoteResults().entrySet()) {
            System.out.println(String.format("[SERVER_VOTE_EVENT] USER_ID: %d, COUNT: %d", entry.getKey(), entry.getValue()));
        }

        SwingUtilities.invokeLater(() -> {
            MVPScreen.updateVotes(serverVoteEvent.getVoteInfo());
        });
    }

    private void handleServerRequestVoteEvent(Message message) {
        ServerRequestVoteEvent event = (ServerRequestVoteEvent) message.getMsgDTO();

        SwingUtilities.invokeLater(() -> {
            MVPScreen.startVoteTimer(event.getEndTime());
        });
    }
}
