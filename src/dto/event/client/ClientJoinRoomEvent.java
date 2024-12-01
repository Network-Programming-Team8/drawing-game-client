package dto.event.client;

import dto.event.Event;

import java.io.Serial;

public class ClientJoinRoomEvent extends Event {

    @Serial
    private static final long serialVersionUID = 4703348475502347231L;
    private final int roomID;

    public ClientJoinRoomEvent(int roomID){
        this.roomID = roomID;
    }
    public int getRoomID(){
        return roomID;
    }
}
