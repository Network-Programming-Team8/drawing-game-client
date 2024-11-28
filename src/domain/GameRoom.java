package domain;

import java.util.ArrayList;
import java.util.List;

public class GameRoom {

    int id;
    int drawTimeLimit;
    int participantLimit;
    User owner;
    List<User> userList = new ArrayList<User>();

    public GameRoom(int id, int drawTimeLimit, int participantLimit, User owner){
        this.id = id;
        this.drawTimeLimit = drawTimeLimit;
        this.participantLimit = participantLimit;
        this.owner = owner;
        userList.add(owner);
    }

    public void addUser(User user) {
        userList.add(user);
    }

    public int getId() {
        return id;
    }

    public int getDrawTimeLimit() {
        return drawTimeLimit;
    }

    public int getParticipantLimit() {
        return participantLimit;
    }

    public List<User> getUserList() {
        return userList;
    }
}
