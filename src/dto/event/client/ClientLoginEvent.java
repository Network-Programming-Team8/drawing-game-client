package dto.event.client;

import dto.event.Event;

public class ClientLoginEvent extends Event {

    private static final long serialVersionUID = 1L;
    private final String nickname;

    public ClientLoginEvent(String nickname){
        this.nickname = nickname;
    }

    public String getNickName(){ return nickname; }
}
