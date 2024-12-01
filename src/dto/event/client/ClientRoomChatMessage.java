package dto.event.client;

import dto.event.Event;

import java.io.Serial;

public class ClientRoomChatMessage extends Event {

    @Serial
    private static final long serialVersionUID = -7849945263563735524L;
    private final String message;

    public ClientRoomChatMessage(String message) {
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
