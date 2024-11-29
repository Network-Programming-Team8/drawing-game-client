package dto.event.server;

import dto.event.Event;
import dto.info.RoomInfo;

public class ServerJoinRoomEvent extends Event {
    private final RoomInfo roomInfo;

    public ServerJoinRoomEvent(RoomInfo roomInfo){
        this.roomInfo = roomInfo;
    }
}
