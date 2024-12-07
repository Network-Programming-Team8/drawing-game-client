package dto.event.server;

import dto.event.Event;
import dto.info.VoteInfo;

import java.io.Serial;

public class ServerVoteEvent extends Event {

    @Serial
    private static final long serialVersionUID = -5308037910095869290L;
    private final VoteInfo voteInfo;

    public ServerVoteEvent(VoteInfo voteInfo) {
        this.voteInfo = voteInfo;
    }

    public VoteInfo getVoteInfo() {
        return voteInfo;
    }
}
