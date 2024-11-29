package dto.event.server;

import dto.event.Event;

public class ServerErrorEvent extends Event {

    private static final long serialVersionUID = 6L;
    String errorMsg;
    public ServerErrorEvent(String errorMsg) {
        errorMsg = this.errorMsg;
    }
}