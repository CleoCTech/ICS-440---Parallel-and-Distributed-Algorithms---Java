package com.abc.draw.geometry;

import com.abc.draw.Drawable;

import java.awt.*;

public class Triangle implements Drawable {
    private final Point p1;
    private final Point p2;
    private final Point p3;

    public Triangle(Point p1, Point p2, Point p3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    public Point getP1() {
        return p1;
    }

    public Point getP2() {
        return p2;
    }

    public Point getP3() {
        return p3;
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setPaint(Color.GREEN);
        g2.drawLine((int) getP1().getX(), (int) getP1().getY(),(int) getP2().getX(), (int) getP2().getY());
        g2.drawLine((int) getP2().getX(), (int) getP2().getY(),(int) getP3().getX(), (int) getP3().getY());
        g2.drawLine((int) getP3().getX(), (int) getP3().getY(),(int) getP1().getX(), (int) getP1().getY());
    }
}
