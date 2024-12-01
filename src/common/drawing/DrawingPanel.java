package common.drawing;

import dto.event.client.ClientDrawEvent;
import dto.info.DrawElementInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

//TODO:
public class DrawingPanel extends JPanel {
    private List<DrawElementInfo> drawElements;
    private int currentDrawer;
    private boolean isCurrentDrawer;

    public DrawingPanel() {
        drawElements = new ArrayList<>();
        setBackground(Color.WHITE);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (isCurrentDrawer) {
                    addDrawElement(e);
                }
            }
        });
        setSize(400, 400);
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isCurrentDrawer) {
                    addDrawElement(e);
                }
            }
        });
    }

    private void addDrawElement(MouseEvent e) {
        DrawElementInfo element = new DrawElementInfo(e.getPoint(), getCurrentColor(), getCurrentThickness(), e);
        drawElements.add(element);
        repaint();
//        TODO: socket event
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
        this.isCurrentDrawer = (drawer == getCurrentUserId());
    }

    public void addRemoteDrawElement(ClientDrawEvent event) {
        drawElements.add(event.getDrawing());
        repaint();
    }

    private Color getCurrentColor() {
//        TODO:
        return Color.BLACK;
    }

    private int getCurrentThickness() {
//        TODO:
        return 2;
    }

    private int getCurrentUserId() {
//        TODO:
        return 0;
    }
}
