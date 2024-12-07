package dto.event.client;

import java.io.Serial;

public class ClientGameReadyEvent extends ClientReadyEvent{
    @Serial
    private static final long serialVersionUID = -7351987534444962937L;

    public ClientGameReadyEvent(boolean isReady) {
        super(isReady);
    }
}
