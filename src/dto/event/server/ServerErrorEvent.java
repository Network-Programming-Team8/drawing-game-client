package dto.event.server;

import dto.event.Event;

public class ServerErrorEvent extends Event {
    String errorMsg;
    public ServerErrorEvent(String errorMsg) {
        errorMsg = this.errorMsg;
    }
}