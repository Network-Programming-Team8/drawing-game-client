package dto.info;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.Serial;

public class DrawElementInfo implements Info {

    @Serial
    private static final long serialVersionUID = 1022185716081833550L;
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

    public Color getColor() {
        return color;
    }

    public int getThickness() {
        return thickness;
    }

    public Point getCoordinate() {
        return coordinate;
    }
}
