package domain;

public class User {

    private final int id;
    private final String nickname;

    public User(int id, String nickname){
        this.id = id;
        this.nickname = nickname;
    }

    public int getID() {
        return id;
    }
    public String getNickname() {
        return nickname;
    }
}
