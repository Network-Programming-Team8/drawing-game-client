package dto.event.client;

import dto.event.Event;

import java.io.Serial;

public class ClientLoginEvent extends Event {

    @Serial
    private static final long serialVersionUID = -7817904659621476741L;
    private final String nickname;

    public ClientLoginEvent(String nickname){
        this.nickname = nickname;
    }

    public String getNickName(){
        return nickname;
    }
}
