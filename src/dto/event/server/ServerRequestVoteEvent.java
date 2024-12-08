package dto.event.server;

import dto.event.Event;
import utils.UnixSeconds;

import java.io.Serial;
import java.time.LocalDateTime;

public class ServerRequestVoteEvent extends Event {

    @Serial
    private static final long serialVersionUID = -9092300978127437216L;
    private final long endTime;

    public ServerRequestVoteEvent(long endTime) {
        this.endTime = UnixSeconds.now().plusSeconds(30).toLong();
    }

    public long getEndTime() {
        return endTime;
    }
}
