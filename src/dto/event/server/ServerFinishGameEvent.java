package dto.event.server;

import dto.event.Event;
import dto.info.DrawElementInfo;

import java.io.Serial;
import java.util.List;
import java.util.Map;

public class ServerFinishGameEvent extends Event {
    @Serial
    private static final long serialVersionUID = 3893920113617596864L;
    private final String answer;
    private final String submittedAnswer;
    private final Map<Integer, List<DrawElementInfo>> drawingMap;
    public ServerFinishGameEvent(String answer, String submittedAnswer, Map<Integer, List<DrawElementInfo>> drawingMap) {
        this.answer = answer;
        this.submittedAnswer = submittedAnswer;
        this.drawingMap = drawingMap;
    }

    public String getAnswer() {
        return answer;
    }

    public String getSubmittedAnswer() {
        return submittedAnswer;
    }

    public Map<Integer, List<DrawElementInfo>> getDrawingMap() {
        return drawingMap;
    }
}