package common.drawing;

import common.screen.ScreenController;
import dto.event.client.ClientDrawEvent;
import dto.event.server.ServerDrawEvent;
import dto.event.server.ServerTurnChangeEvent;
import dto.info.DrawElementInfo;
import message.Message;
import message.MessageType;
import modules.game.GameScreen;
import modules.mvp.MVPScreen;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Iterator;
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
    private boolean isDone = false;

    public DrawingController(ScreenController screenController) {
        this.screenController = screenController;
        this.drawingPanel = new DrawingPanel(this);
    }

    public void setCurrentDrawer(int drawer) {
        this.currentDrawer = drawer;
        drawingPanel.setCurrentDrawer(drawer);
    }

    public void handleRemoteDrawEvent(ServerDrawEvent event) {
        if (isDone) {
            drawingPanel.addRemoteDrawElement(event);
            return ;
        }
        drawingPanel.addDrawElement(event.getDrawing());
    }

    public void sendDrawEvent(DrawElementInfo element) {
        try {
            LocalDateTime submittedTime = LocalDateTime.now();
            if(this.startTime.plusSeconds(timeout).isBefore(submittedTime)){
                drawingPanel.setIsCurrentDrawer(false);
                isDone = true;
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
            drawingPanel.rePaint();
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

        // drawingMap의 Entry들을 orderList에 따라 정렬
        List<Map.Entry<Integer, List<DrawElementInfo>>> sortedEntries = drawingMap.entrySet().stream()
                .sorted(Comparator.comparingInt(entry -> GameScreen.userOrder.indexOf(entry.getKey())))
                .toList();

        // 정렬된 Entry의 value들을 순차적으로 출력
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        Iterator<Map.Entry<Integer, List<DrawElementInfo>>> entryIterator = sortedEntries.iterator();

        // 상태를 저장할 객체
        final Map.Entry<Integer, List<DrawElementInfo>>[] currentEntry = new Map.Entry[]{null};
        final int[] currentIndex = {0};

        scheduler.scheduleAtFixedRate(() -> {
            if (currentEntry[0] == null && entryIterator.hasNext()) {
                currentEntry[0] = entryIterator.next();
                currentIndex[0] = 0; // 새 엔트리의 value 리스트 시작
            }

            if (currentEntry[0] != null) {
                GameScreen.updateCurrentUser(currentEntry[0].getKey());
                List<DrawElementInfo> valueList = currentEntry[0].getValue();
                if (currentIndex[0] < valueList.size()) {
                    // value 리스트의 요소를 순차적으로 추가
                    drawingPanel.addDrawElementAndRepaint(valueList.get(currentIndex[0]));
                    currentIndex[0]++;
                } else {
                    // 현재 엔트리의 value 리스트를 다 처리했으면 다음 엔트리로 넘어감
                    currentEntry[0] = null;
                }
            } else {
                // 더 이상 처리할 엔트리가 없으면 종료
                scheduler.shutdown();
                GameScreen.transitionToMVPScreen();
            }
        }, 0, 10, TimeUnit.MILLISECONDS);
    }

    public void rePaintPanel(){
        drawingPanel.repaint();
    }
}
