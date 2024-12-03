package utils;

import common.screen.ScreenController;
import dto.event.server.*;
import dto.info.RoomInfo;
import dto.info.UserInfo;
import message.Message;
import modules.game.GameScreen;
import modules.lobby.LobbyScreen;
import modules.roomList.RoomListScreen;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;

public class ServerListener implements Runnable {

    private final ScreenController screenController;
    private final ObjectInputStream in;

    public ServerListener(ScreenController screenController, ObjectInputStream in) {
        this.screenController = screenController;
        this.in = in;
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
        }
    }

    private void handleServerLoginEvent(Message message){
        ServerLoginEvent serverLoginEvent = (ServerLoginEvent) message.getMsgDTO();
        screenController.setUserInfo(new UserInfo(serverLoginEvent.getId(), serverLoginEvent.getNickname(), true));
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

        LobbyScreen.updateRoomInfoOnSwing();
        LobbyScreen.updateUserFieldOnSwing();

        screenController.showScreen(LobbyScreen.screenName);
    }

    private void handleServerRoomChatMessage(Message message){
        ServerRoomChatMessage serverRoomChatMessage = (ServerRoomChatMessage) message.getMsgDTO();

        LobbyScreen.receiveChatMessage(serverRoomChatMessage.getSpeaker() + ": " + serverRoomChatMessage.getMessage());
    }

    private void handleServerRequestTopicEvent(Message message){
        ServerRequestTopicEvent serverRequestTopicEvent = (ServerRequestTopicEvent) message.getMsgDTO();

        LobbyScreen.showTopicInputDialog();
        screenController.showScreen(GameScreen.screenName);
    }
}
