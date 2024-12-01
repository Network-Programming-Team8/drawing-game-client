package dto.event.server;

import dto.event.Event;
import dto.info.RoomInfo;

import java.io.Serial;

public class ServerRoomUpdateEvent extends Event {

    @Serial
    private static final long serialVersionUID = 7497242005030018921L;
    private final RoomInfo roomInfo;

    public ServerRoomUpdateEvent(RoomInfo roomInfo) {
        this.roomInfo = roomInfo;
    }
}
