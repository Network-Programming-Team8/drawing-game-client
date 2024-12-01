package dto.event.server;

import dto.event.Event;

import java.io.Serial;

public class ServerCreateRoomEvent extends Event {

    @Serial
    private static final long serialVersionUID = -866235245133370534L;
    private final int id;
    private final int drawTimeLimit;
    private final int participantLimit;

    public ServerCreateRoomEvent(int id, int drawTimeLimit, int participantLimit){
        this.id = id;
        this.drawTimeLimit = drawTimeLimit;
        this.participantLimit = participantLimit;
    }

    public int getId() {
        return id;
    }

    public int getDrawTimeLimit() {
        return drawTimeLimit;
    }

    public int getParticipantLimit() {
        return participantLimit;
    }
}
