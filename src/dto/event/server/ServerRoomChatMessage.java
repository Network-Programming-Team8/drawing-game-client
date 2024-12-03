package dto.event.server;

import dto.event.Event;

import java.io.Serial;

public class ServerRoomChatMessage extends Event {

    @Serial
    private static final long serialVersionUID = -3873441732855365590L;
    private final String speaker;
    private final String message;

    public ServerRoomChatMessage(String speaker, String message) {
        this.speaker = speaker;
        this.message = message;
    }

    public String getSpeaker() {
        return speaker;
    }

    public String getMessage() {
        return message;
    }
}
