package com.abc.draw.geometry;

import com.abc.draw.Drawable;

import java.awt.*;

public class Square implements Drawable {
    private final Point p1;
    private double width;

    public Point getP1() {
        return p1;
    }

    public Square(Point p1, Double width) {
        this.p1 = p1;
        this.width = width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setPaint(Color.BLACK);
        g2.drawRect((int) getP1().getX(), (int) getP1().getY(), (int) width, (int) width);
    }
}
