package dto.event.client;

import dto.event.Event;

import java.io.Serial;

public abstract class ClientReadyEvent extends Event {

    @Serial
    private static final long serialVersionUID = 2855884091669521611L;
    private final boolean isReady;

    public ClientReadyEvent(boolean isReady) {
        this.isReady = isReady;
    }

    public boolean getIsReady(){
        return isReady;
    }
}
