package dto;

public class ServerErrorEventDTO extends DTO{
    String errorMsg;
    public ServerErrorEventDTO(String errorMsg) {
        errorMsg = this.errorMsg;
    }
}