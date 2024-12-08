package dto.event.client;

import dto.event.Event;
import utils.UnixSeconds;

import java.io.Serial;
import java.time.LocalDateTime;

public class ClientGuessEvent extends Event {

    @Serial
    private static final long serialVersionUID = -8020794494854676020L;
    private final String submissionAnswer;
    private final long submissionTime;

    public ClientGuessEvent(String submissionAnswer) {
        this.submissionAnswer = submissionAnswer;
        this.submissionTime = UnixSeconds.now().toLong();
    }

    public String getSubmissionAnswer(){
        return submissionAnswer;
    }
    public long getSubmissionTime(){
        return submissionTime;
    }
}
