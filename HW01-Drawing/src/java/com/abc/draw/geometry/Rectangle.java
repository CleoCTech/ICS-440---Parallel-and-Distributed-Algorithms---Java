package com.abc.draw.geometry;

import com.abc.draw.Drawable;

import java.awt.*;

public class Rectangle implements Drawable {
    private final Point p1;
    private double width;
    private double height;
    public Rectangle(Point p1, double width, double height) {
        this.p1 = p1;
        this.width = width;
        this.height = height;
    }
    public Point getP1() {
        return p1;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setHeight(double height) {
        this.height = height;
    }
    @Override
    public void draw(Graphics2D g2) {
        g2.setPaint(Color.CYAN);
        g2.drawRect((int) getP1().getX(), (int) getP1().getY(), (int) width, (int) height);
    }
}
