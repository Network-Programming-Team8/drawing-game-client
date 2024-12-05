package dto.event.server;

import dto.event.Event;

import java.io.Serial;
import java.time.LocalDateTime;

public class ServerTurnChangeEvent extends Event {

    @Serial
    private static final long serialVersionUID = 4375947319606530465L;
    private final int nowTurn;
    private final LocalDateTime startTime;
    private final boolean isGuessTurn;

    public ServerTurnChangeEvent(int nowTurn, LocalDateTime startTime, boolean isGuessTurn) {
        this.nowTurn = nowTurn;
        this.startTime = startTime;
        this.isGuessTurn = isGuessTurn;
    }

    public int getNowTurn() {
        return nowTurn;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public boolean isGuessTurn() {
        return isGuessTurn;
    }
}
