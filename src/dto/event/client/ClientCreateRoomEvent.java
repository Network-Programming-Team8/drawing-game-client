package dto.event.client;

import dto.event.Event;

public class ClientCreateRoomEvent extends Event {

    private static final long serialVersionUID = 3L;
    int drawTimeLimit;
    int participantLimit;

    public ClientCreateRoomEvent(int drawTimeLimit, int participantLimit){
        this.drawTimeLimit = drawTimeLimit;
        this.participantLimit = participantLimit;
    }

    public int getDrawTimeLimit(){
        return drawTimeLimit;
    }

    public int getParticipantLimit() {
        return participantLimit;
    }
}
