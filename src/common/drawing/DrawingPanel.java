package common.drawing;

import dto.event.client.ClientDrawEvent;
import dto.event.server.ServerDrawEvent;
import dto.info.DrawElementInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DrawingPanel extends JPanel {
    private List<DrawElementInfo> drawElements;
    private int currentDrawer;
    private boolean isCurrentDrawer;
    private DrawingController controller;

    public DrawingPanel(DrawingController controller) {
        this.controller = controller;
        drawElements = new ArrayList<>();
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(400, 400));
        setupMouseListeners();
    }

    private void setupMouseListeners() {
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (isCurrentDrawer) {
                    addDrawElement(e);
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (isCurrentDrawer) {
                    addDrawElement(e);
                }
            }
        };
        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
    }

    private void addDrawElement(MouseEvent e) {
        DrawElementInfo element = new DrawElementInfo(e.getPoint(), controller.getCurrentColor(), controller.getCurrentThickness(), e);
        drawElements.add(element);
        repaint();
        controller.sendDrawEvent(element);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        for (DrawElementInfo element : drawElements) {
            g2d.setColor(element.getColor());
            g2d.setStroke(new BasicStroke(element.getThickness()));
            g2d.drawLine(element.getCoordinate().x, element.getCoordinate().y,
                    element.getCoordinate().x, element.getCoordinate().y);
        }
    }

    public void setCurrentDrawer(int drawer) {
        this.currentDrawer = drawer;
        this.isCurrentDrawer = (drawer == controller.getCurrentUserId());
    }

    public void setIsCurrentDrawer(boolean isCurrentDrawer) {
        this.isCurrentDrawer = isCurrentDrawer;
    }

    public void addRemoteDrawElement(ServerDrawEvent event) {
        drawElements.add(event.getDrawing());
        repaint();
    }

    public void clearDrawing() {
        drawElements.clear();
        repaint();
    }

    public void setDrawElements(List<DrawElementInfo> elements) {
        this.drawElements = new ArrayList<>(elements);
        repaint();
    }
}
