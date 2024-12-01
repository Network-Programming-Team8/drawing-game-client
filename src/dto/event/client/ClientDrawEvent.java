package dto.event.client;

import dto.event.Event;
import dto.info.DrawElementInfo;

import java.io.Serial;

public class ClientDrawEvent extends Event {

    @Serial
    private static final long serialVersionUID = 1354487476063093542L;
    private final int drawer;
    private final DrawElementInfo drawing;

    public ClientDrawEvent(int drawer, DrawElementInfo drawing) {
        this.drawer = drawer;
        this.drawing = drawing;
    }

    public int getDrawer(){
        return drawer;
    }
    public DrawElementInfo getDrawing(){
        return drawing;
    }
}
