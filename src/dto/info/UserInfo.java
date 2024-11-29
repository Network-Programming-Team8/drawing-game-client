package dto.info;

public class UserInfo {
    private final int id;
    private final String nickname;
    private final boolean isReady;

    public UserInfo(int id, String nickname, boolean isReady) {
        this.id = id;
        this.nickname = nickname;
        this.isReady = isReady;
    }
}
