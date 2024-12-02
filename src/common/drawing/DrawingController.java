package common.drawing;

import common.screen.ScreenController;
import dto.event.client.ClientDrawEvent;
import dto.info.DrawElementInfo;
import message.Message;
import message.MessageType;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class DrawingController {
    private DrawingPanel drawingPanel;
    private int currentDrawer;
    private int currentUserId;
    private Color currentColor = Color.BLACK;
    private int currentThickness = 2;
    private ScreenController screenController;

    public DrawingController(ScreenController screenController) {
        this.screenController = screenController;
        this.drawingPanel = new DrawingPanel(this);
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

    public void sendDrawEvent(DrawElementInfo element) {
        try {
            ClientDrawEvent event = new ClientDrawEvent(currentUserId, element);
            screenController.sendToServer(new Message(MessageType.CLIENT_DRAW_EVENT, event));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setCurrentUserId(int userId) {
        this.currentUserId = userId;
    }

    public int getCurrentUserId() {
        return currentUserId;
    }

    public Color getCurrentColor() {
        return currentColor;
    }

    public int getCurrentThickness() {
        return currentThickness;
    }

    public void setCurrentColor(Color color) {
        this.currentColor = color;
    }

    public void setCurrentThickness(int thickness) {
        this.currentThickness = thickness;
    }

    public DrawingPanel getDrawingPanel() {
        return drawingPanel;
    }

    public void clearDrawing() {
        drawingPanel.clearDrawing();
    }

    public void setInitialDrawing(List<DrawElementInfo> elements) {
        drawingPanel.setDrawElements(elements);
    }
}
