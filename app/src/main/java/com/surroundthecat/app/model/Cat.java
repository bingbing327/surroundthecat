package com.surroundthecat.app.model;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jiang Meng on 2016/7/28.
 */
public class Cat {
    /**
     * 猫的坐标
     */
    private int xIndex = 0;
    private int yIndex = 0;

    /**
     * 猫的动作图的序号
     */
    private int index = 0;

    /**
     * 猫被围住了
     */
    private boolean surrounded = false;

    /**
     * 猫的动作控制
     */
    private List<Bitmap> cat1 = new ArrayList<Bitmap>();
    private List<Bitmap> cat2 = new ArrayList<Bitmap>();

    public Cat() {}

    public Bitmap getBitMap() {
        // 利用index置为0作动作循环
        if (index == cat1.size()) {
            index = 0;
        }
        Bitmap bitmap = cat1.get(index);
        //如果猫被围住了，bitmap取cat2中动作图
        if (surrounded) {
            bitmap = cat2.get(index);
        }
        index++;
        return bitmap;
    }

    public int getyIndex() {
        return yIndex;
    }
    public void setyIndex(int yIndex) {
        this.yIndex = yIndex;
    }

    public int getxIndex() {
        return xIndex;
    }
    public void setxIndex(int xIndex) {
        this.xIndex = xIndex;
    }

    public List<Bitmap> getCat1() {
        return cat1;
    }
    public void setCat1(List<Bitmap> cat1) {
        this.cat1 = cat1;
    }

    public List<Bitmap> getCat2() {
        return cat2;
    }
    public void setCat2(List<Bitmap> cat2) {
        this.cat2 = cat2;
    }

    public boolean isSurrounded() {
        return surrounded;
    }
    public void setSurrounded(boolean haha) {
        this.surrounded = haha;
    }

    /**
     * 猫的初始位置设定在（4,4）
     */
    public void reset() {
        this.xIndex = 4;
        this.yIndex = 4;
        this.index = 0;
        this.surrounded = false;
    }

    /**
     * 猫移动的位置
     * @param point
     */
    public void moveTo(Point point){
        if(point != null){
            this.xIndex = point.getxIndex();
            this.yIndex = point.getyIndex();
        }
    }
}
