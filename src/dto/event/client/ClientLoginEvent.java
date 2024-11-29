package dto.event.client;

import dto.event.Event;

public class ClientLoginEvent extends Event {

    private final String nickname;

    public ClientLoginEvent(String nickname){
        this.nickname = nickname;
    }

    public String getNickName(){ return nickname; }
}
