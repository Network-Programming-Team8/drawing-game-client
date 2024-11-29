package dto.event.server;

import dto.event.Event;

public class ServerLoginEvent extends Event {

    private static final long serialVersionUID = 2L;
    private final String nickname;
    private final int id;

    public ServerLoginEvent(String nickname, int id){
        this.nickname = nickname;
        this.id = id;
    }

    public int getId(){
        return id;
    }
}
