package dto.event.server;

import dto.event.Event;

public class ServerCreateRoomEvent extends Event {
    int id;
    int drawTimeLimit;
    int participantLimit;

    public ServerCreateRoomEvent(int id, int drawTimeLimit, int participantLimit){
        this.id = id;
        this.drawTimeLimit = drawTimeLimit;
        this.participantLimit = participantLimit;
    }

    public int getId() { return id; }
    public int getDrawTimeLimit(){
        return drawTimeLimit;
    }
    public int getParticipantLimit() {
        return participantLimit;
    }
}
