package modules.game;

import common.drawing.DrawingController;
import common.screen.Screen;
import common.screen.ScreenController;
import dto.event.client.ClientDrawEvent;
import dto.info.DrawElementInfo;

import java.util.List;

public class GameScreen extends Screen {
    public static final String screenName = "GAME_SCREEN";
    private DrawingController drawingController;

    public GameScreen(ScreenController screenController) {
        this.drawingController = new DrawingController(screenController);
        add(drawingController.getDrawingPanel());

        // 기타 UI 요소 추가
        setVisible(true);
    }

    // 서버로부터 현재 그리는 사람 정보를 받았을 때
    public void setCurrentDrawer(int drawerId) {
        drawingController.setCurrentDrawer(drawerId);
    }

    // 서버로부터 이전 그림 데이터를 받았을 때
    public void setInitialDrawing(List<DrawElementInfo> elements) {
        drawingController.setInitialDrawing(elements);
    }

    // 서버로부터 그리기 이벤트를 받았을 때
    public void handleDrawEvent(ClientDrawEvent event) {
        drawingController.handleRemoteDrawEvent(event);
    }
}
