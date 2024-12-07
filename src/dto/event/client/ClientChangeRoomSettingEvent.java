package dto.event.client;

import dto.event.Event;

import java.io.Serial;

public class ClientChangeRoomSettingEvent extends Event {

    @Serial
    private static final long serialVersionUID = 188014085743026298L;
    private final int drawTimeLimit;
    private final int participantLimit;

    public ClientChangeRoomSettingEvent(int drawTimeLimit, int participantLimit) {
        this.drawTimeLimit = drawTimeLimit;
        this.participantLimit = participantLimit;
    }

    public int getParticipantLimit() {
        return participantLimit;
    }
    public int getDrawTimeLimit() {
        return drawTimeLimit;
    }
}
