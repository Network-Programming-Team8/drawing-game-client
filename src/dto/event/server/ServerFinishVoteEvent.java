package dto.event.server;

import dto.event.Event;
import dto.info.VoteInfo;

import java.io.Serial;

public class ServerFinishVoteEvent extends Event {

    @Serial
    private static final long serialVersionUID = -7839089208679862097L;
    private final VoteInfo voteInfo;

    public ServerFinishVoteEvent(VoteInfo voteInfo) {
        this.voteInfo = voteInfo;
    }


    public VoteInfo getVoteInfo() {
        return voteInfo;
    }
}
