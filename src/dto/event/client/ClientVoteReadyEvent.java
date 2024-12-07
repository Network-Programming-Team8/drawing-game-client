package dto.event.client;

import java.io.Serial;

public class ClientVoteReadyEvent extends ClientReadyEvent{

    @Serial
    private static final long serialVersionUID = 536551476585336020L;

    public ClientVoteReadyEvent(boolean isReady) {
        super(isReady);
    }
}
