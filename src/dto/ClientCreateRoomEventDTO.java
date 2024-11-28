package dto;

public class ClientCreateRoomEventDTO extends DTO{
    int drawTimeLimit;
    int participantLimit;

    public ClientCreateRoomEventDTO(int drawTimeLimit, int participantLimit){
        this.drawTimeLimit = drawTimeLimit;
        this.participantLimit = participantLimit;
    }

    public int getDrawTimeLimit(){
        return drawTimeLimit;
    }

    public int getParticipantLimit() {
        return participantLimit;
    }
}
