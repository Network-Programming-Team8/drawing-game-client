package dto.info;

import dto.event.Event;

import java.io.Serial;

public class UserInfo implements Info {

    @Serial
    private static final long serialVersionUID = 842539395360312730L;
    private final int id;
    private final String nickname;
    private final boolean isReady;

    public UserInfo(int id, String nickname, boolean isReady) {
        this.id = id;
        this.nickname = nickname;
        this.isReady = isReady;
    }

    public int getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public boolean isReady() {
        return isReady;
    }
}
