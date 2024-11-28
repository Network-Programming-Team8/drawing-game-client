package dto;

public class ClientJoinRoomEventDTO extends DTO{

    int roomID;

    public ClientJoinRoomEventDTO(int roomID){

        this.roomID = roomID;
    }

    public int getRoomID(){ return roomID; }
}
