package com.surroundthecat.app.model;

import android.graphics.Rect;

/**
 * Created by Jiang Meng on 2016/7/28.
 */
public class Point {
    /**
     * 点的坐标
     */
    private int xIndex=0;
    private int yIndex=0;

    /**
     * 是否为石头
     */
    private boolean isStone = false;

    private Rect rect;

    public Point(int xIndex,int yIndex) {
        this.xIndex = xIndex;
        this.yIndex = yIndex;
    }

    public int getxIndex() {
        return xIndex;
    }
    public void setxIndex(int xIndex){
        this.xIndex = xIndex;
    }

    public int getyIndex() {
        return yIndex;
    }
    public void setyIndex(int yIndex){
        this.yIndex = yIndex;
    }

    public boolean isStone() {
        return isStone;
    }
    public void setStone(boolean isStone) {
        this.isStone = isStone;
    }

    public Rect getRect() {
        return rect;
    }
    public void setRect(Rect rect) {
        this.rect = rect;
    }

}
