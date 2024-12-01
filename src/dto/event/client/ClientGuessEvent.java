package dto.event.client;

import dto.event.Event;

import java.io.Serial;
import java.time.LocalDateTime;

public class ClientGuessEvent extends Event {

    @Serial
    private static final long serialVersionUID = -8020794494854676020L;
    private final String submissionAnswer;
    private final LocalDateTime submissionTime;

    public ClientGuessEvent(String submissionAnswer) {
        this.submissionAnswer = submissionAnswer;
        this.submissionTime = LocalDateTime.now();
    }

    public String getSubmissionAnswer(){
        return submissionAnswer;
    }
    public LocalDateTime getSubmissionTime(){
        return submissionTime;
    }
}
