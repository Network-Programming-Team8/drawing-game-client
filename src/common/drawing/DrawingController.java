package common.drawing;

import common.screen.ScreenController;
import dto.event.client.ClientDrawEvent;
import dto.event.server.ServerDrawEvent;
import dto.event.server.ServerTurnChangeEvent;
import dto.info.DrawElementInfo;
import message.Message;
import message.MessageType;
import modules.game.GameScreen;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DrawingController {
    private DrawingPanel drawingPanel;
    private int currentDrawer;
    private static int currentUserId;
    private Color currentColor = Color.BLACK;
    private int currentThickness = 2;
    private ScreenController screenController;
    public static int timeout;
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
        if(serverTurnChangeEvent.getNowTurn() == currentUserId) {
            this.currentDrawer = serverTurnChangeEvent.getNowTurn();
            this.startTime = serverTurnChangeEvent.getStartTime();
            drawingPanel.setCurrentDrawer(this.currentDrawer);
            GameScreen.startTimeLabel();
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

    public void printDrawingMap(Map<Integer, List<DrawElementInfo>> drawingMap) {
        drawingPanel.clearDrawing();

        //drawingMap에 있는 DrawElement를 0.01초 간격으로 화면에 print 하는 로직
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        List<DrawElementInfo> elements = drawingMap.values().stream()
                .flatMap(List::stream)
                .toList();

        final int[] index = {0};
        scheduler.scheduleAtFixedRate(() -> {
            if (index[0] < elements.size()) {
                drawingPanel.addDrawElement(elements.get(index[0]));
                index[0]++;
            } else {
                scheduler.shutdown();
            }
        }, 0, 10, TimeUnit.MILLISECONDS);
    }
}
