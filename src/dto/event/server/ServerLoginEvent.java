package dto.event.server;

import dto.event.Event;

import java.io.Serial;

public class ServerLoginEvent extends Event {

    @Serial
    private static final long serialVersionUID = -5877864820788199715L;
    private final String nickname;
    private final int id;

    public ServerLoginEvent(String nickname, int id){
        this.nickname = nickname;
        this.id = id;
    }
}
