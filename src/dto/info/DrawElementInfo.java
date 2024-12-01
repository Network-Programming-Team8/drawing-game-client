package dto.info;

import java.awt.*;
import java.awt.event.MouseEvent;

public class DrawElementInfo {

    private final Point coordinate;
    private final Color color;
    private final int thickness;
    private final MouseEvent mouseAct;

    public DrawElementInfo(Point coordinate, Color color, int thickness, MouseEvent mouseAct) {
        this.coordinate = coordinate;
        this.color = color;
        this.thickness = thickness;
        this.mouseAct = mouseAct;
    }
}
