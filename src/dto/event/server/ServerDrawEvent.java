package dto.event.server;

import dto.event.Event;
import dto.info.DrawElementInfo;

import java.io.Serial;

public class ServerDrawEvent extends Event {

    @Serial
    private static final long serialVersionUID = -6086850118146047662L;
    private final int drawer;
    private final DrawElementInfo drawing;

    public ServerDrawEvent(int drawer, DrawElementInfo drawing) {
        this.drawer = drawer;
        this.drawing = drawing;
    }

    public int getDrawer() {
        return drawer;
    }

    public DrawElementInfo getDrawing() {
        return drawing;
    }
}
