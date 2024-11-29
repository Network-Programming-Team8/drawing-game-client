package dto.event.server;

import dto.event.Event;

public class ServerLoginEvent extends Event {

    private final String nickname;
    private final int id;

    public ServerLoginEvent(String nickname, int id){
        this.nickname = nickname;
        this.id = id;
    }
}
