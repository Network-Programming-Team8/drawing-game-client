package dto.event.server;

import dto.event.Event;

import java.io.Serial;

public class ServerErrorEvent extends Event {

    @Serial
    private static final long serialVersionUID = 7978303806957878122L;
    private final String errorMsg;

    public ServerErrorEvent(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}