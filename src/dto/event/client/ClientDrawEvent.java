package dto.event.client;

import dto.event.Event;
import dto.info.DrawElementInfo;

import java.io.Serial;
import java.time.LocalDateTime;

public class ClientDrawEvent extends Event {

    @Serial
    private static final long serialVersionUID = 1354487476063093542L;
    private final int drawer;
    private final DrawElementInfo drawing;
    private final LocalDateTime submissionTime;

    public ClientDrawEvent(int drawer, DrawElementInfo drawing, LocalDateTime submissionTime) {
        this.drawer = drawer;
        this.drawing = drawing;
        this.submissionTime = submissionTime;
    }

    public int getDrawer(){
        return drawer;
    }
    public DrawElementInfo getDrawing(){
        return drawing;
    }
    public LocalDateTime getSubmissionTime(){
        return submissionTime;
    }
}
