package dto.event.client;

import dto.event.Event;

import java.io.Serial;

public class ClientVoteEvent extends Event {

    @Serial
    private static final long serialVersionUID = 7803322504652785479L;
    private final int voteUser;

    public ClientVoteEvent(int voteUser) {
        this.voteUser = voteUser;
    }

    public int getVoteUser(){
        return voteUser;
    }
}
