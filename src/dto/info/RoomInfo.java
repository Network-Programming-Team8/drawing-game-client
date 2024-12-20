package dto.info;

import dto.event.Event;

import java.io.Serial;
import java.util.List;

public class RoomInfo extends Event {

    @Serial
    private static final long serialVersionUID = 4138183250339217845L;
    private int id;
    private int drawTimeLimit;
    private int participantLimit;
    private List<UserInfo> userInfoList;
    private int ownerId;

    public RoomInfo(int id, int drawTimeLimit, int participantLimit, List<UserInfo> userInfoList, int ownerId) {
        this.id = id;
        this.drawTimeLimit = drawTimeLimit;
        this.participantLimit = participantLimit;
        this.userInfoList = userInfoList;
        this.ownerId = ownerId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDrawTimeLimit(int drawTimeLimit) {
        this.drawTimeLimit = drawTimeLimit;
    }

    public void setParticipantLimit(int participantLimit) {
        this.participantLimit = participantLimit;
    }

    public void setUserInfoList(List<UserInfo> userInfoList) {
        this.userInfoList = userInfoList;
    }

    public void setOwnerId(int ownerId) {this.ownerId = ownerId;}
    public int getId() {
        return this.id;
    }
    public int getOwnerId() { return this.ownerId; }

    public int getDrawTimeLimit() {
        return this.drawTimeLimit;
    }

    public int getParticipantLimit() {
        return this.participantLimit;
    }

    public List<UserInfo> getUserInfoList() {
        return this.userInfoList;
    }

    public void fromRoomInfo(RoomInfo roomInfo){
        setId(roomInfo.getId());
        setDrawTimeLimit(roomInfo.getDrawTimeLimit());
        setUserInfoList(roomInfo.getUserInfoList());
        setParticipantLimit(roomInfo.getParticipantLimit());
        setOwnerId(roomInfo.getOwnerId());
    }
}
