package common.drawing;

import common.screen.ScreenController;
import dto.event.client.ClientDrawEvent;
import dto.event.server.ServerDrawEvent;
import dto.event.server.ServerTurnChangeEvent;
import dto.info.DrawElementInfo;
import message.Message;
import message.MessageType;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class DrawingController {
    private DrawingPanel drawingPanel;
    private int currentDrawer;
    private static int currentUserId;
    private Color currentColor = Color.BLACK;
    private int currentThickness = 2;
    private ScreenController screenController;
    private static int timeout;
    private LocalDateTime startTime;

    public DrawingController(ScreenController screenController) {
        this.screenController = screenController;
        this.drawingPanel = new DrawingPanel(this);
    }

    public void setCurrentDrawer(int drawer) {
        this.currentDrawer = drawer;
        drawingPanel.setCurrentDrawer(drawer);
    }

    public void handleRemoteDrawEvent(ServerDrawEvent event) {
        if (event.getDrawer() == currentDrawer) {
            drawingPanel.addRemoteDrawElement(event);
        }
    }

    public void sendDrawEvent(DrawElementInfo element) {
        try {
            LocalDateTime submittedTime = LocalDateTime.now();
            if(this.startTime.plusSeconds(timeout).isBefore(submittedTime)){
                drawingPanel.setIsCurrentDrawer(false);
                return ;
            }
            ClientDrawEvent event = new ClientDrawEvent(currentUserId, element, submittedTime);
            screenController.sendToServer(new Message(MessageType.CLIENT_DRAW_EVENT, event));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleServerTurnChangeEvent(ServerTurnChangeEvent serverTurnChangeEvent){
        if(serverTurnChangeEvent.isGuessTurn()){

            return ;
        }
        if(serverTurnChangeEvent.getNowTurn() == currentUserId) {
            this.currentDrawer = serverTurnChangeEvent.getNowTurn();
            this.startTime = serverTurnChangeEvent.getStartTime();
            drawingPanel.setCurrentDrawer(this.currentDrawer);
        }
    }

    public static void setCurrentUserId(int userId) {
        currentUserId = userId;

    }

    public static int getCurrentUserId() {
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

    public static void setTimeout(int timeoutArg) {
        timeout = timeoutArg;
    }
}
