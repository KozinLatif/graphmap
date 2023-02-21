package com.example.graphmap;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Place extends Circle {
    private final double x;
    private final double y;
    private final String name;
    private boolean selected;

    public Place(double x, double y,  String name){
        super(x, y, 8, Color.BLUE);
        this.x = x;
        this.y = y;
        this.name = name;
    }

    public void setSelected(boolean selected){
        this.selected = selected;

        if(selected){
            setFill(Color.RED);
        } else {
            setFill(Color.BLUE);
        }
    }

    public boolean getSelected(){
        return selected;
    }

    public String getName() {
        return name;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public String toString() {
        return name;
    }
}