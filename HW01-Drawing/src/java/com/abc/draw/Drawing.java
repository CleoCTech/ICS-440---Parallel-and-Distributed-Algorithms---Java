package com.abc.draw;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// TODO - add code to the methods, fields, and constructor below to allow multiple Drawable shapes
// to be held and drawn.

public class Drawing extends Object implements Drawable {


    List<Drawable> drawableList = new ArrayList<>();

    public Drawing() {

    }

    public void drawAll(Graphics2D g2) {
        // call draw(g2) on each of the Drawable's.
        draw(g2);
    }

    public void append(Drawable drawable) {
        // add the specified drawable to an array/list to be able to call draw() later
        drawableList.add(drawable);

    }


    @Override
    public void draw(Graphics2D g2) {
        for (Drawable drawable:drawableList) {
            drawable.draw(g2);
        }

    }
}