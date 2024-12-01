package common.drawing;

import dto.event.client.ClientDrawEvent;

//TODO:
public class DrawingController {
    private DrawingPanel drawingPanel;
    private int currentDrawer;

    public DrawingController(DrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
    }

    public void setCurrentDrawer(int drawer) {
        this.currentDrawer = drawer;
        drawingPanel.setCurrentDrawer(drawer);
    }

    public void handleRemoteDrawEvent(ClientDrawEvent event) {
        if (event.getDrawer() == currentDrawer) {
            drawingPanel.addRemoteDrawElement(event);
        }
    }

    // 기타 필요한 메소드들...
}
