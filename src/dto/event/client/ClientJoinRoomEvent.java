package dto.event.client;

import dto.event.Event;

public class ClientJoinRoomEvent extends Event {

    private static final long serialVersionUID = 4L;
    int roomID;

    public ClientJoinRoomEvent(int roomID){
        this.roomID = roomID;
    }

    public int getRoomID(){ return roomID; }
}
