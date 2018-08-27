package com.raji.logrssiapplication.model;

import java.io.Serializable;

public class Point implements Serializable {
    public float x;
    public float y;
    public String name;

    public Point() {
    }

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Point(String name, float x, float y) {
        this.x = x;
        this.y = y;
        this .name = name;
    }

    public Point(Point src) {
        this.x = src.x;
        this.y = src.y;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }



}
