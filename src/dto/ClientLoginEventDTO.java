package dto;

public class ClientLoginEventDTO extends DTO {

    private final String nickname;

    public ClientLoginEventDTO(String nickname){

        this.nickname = nickname;
    }

    public String getNickName(){ return nickname; }
}
