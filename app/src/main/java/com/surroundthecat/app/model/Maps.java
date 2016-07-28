package com.surroundthecat.app.model;

import android.util.Log;

/**
 * Created by Jiang Meng on 2016/7/28.
 */
public class Maps {
    /**
     * 使用二维数组存放地图的每个点的坐标
     */
    private Point[][] points;
    private int xMax = 0;
    private int yMax = 0;

    public Maps(int xMax,int yMax) {
        this.xMax = xMax;
        this.yMax = yMax;
    }

    public Point getPoint(int xIndex,int yIndex){
        //若此点不在范围内，则返回空值
        if(xIndex < 0 || xIndex >= xMax ||
                yIndex < 0 || yIndex >= yMax){
            return null;
        }
        return points[xIndex][yIndex];//取得判斷后傳回值
    }

    public Point[][] getPoints() {
        return points;
    }

    /**
     * 初始化地图
     */
    public void init(){
        this.points = new Point[xMax][yMax];
        for (int i = 0; i < xMax; i++) {
            for (int j = 0; j < yMax; j++) {
                Point point = new Point(i,j);
                this.points[i][j] = point;
            }
        }
        for (int i = 0; i < xMax; i++) {
            for (int j = 0; j < yMax; j++) {
                Point point  = this.points[i][j] ;
                // 如果getxIndex()返回的值不为i，
                // 或者getyIndex()返回的值不为j，
                if(point.getxIndex() != i || point.getyIndex() != j){
                    Log.d("error", i + " " + j);
                }
            }
        }
        Log.d("error",this.points[3][5]+"");
        Log.d("error","-end");
    }

    /**
     * 清除石头，重新开始
     */
    public void clearStone(){
        //取得points[][]的行数，并将i个一维数组赋值给sub[]
        for (int i = 0; i < points.length; i++) {
            Point[] sub = points[i];
            //取得sub[]的元素数，并将points[i][j]赋值给point[][]，
            //然后将point.setStone()置为false。
            for (int j = 0; j < sub.length; j++) {
                Point point = points[i][j];
                point.setStone(false);
            }
        }
    }

    /**
     * 判断point是否为边界点
     */
    public boolean isEdge(Point point){
        if(point == null)
            return false;
        return point.getxIndex() == 0 || point.getxIndex() == (xMax-1) ||
                point.getyIndex() == 0 || point.getyIndex() == (yMax-1);
    }

    /**
     * 猫下一步的所有坐标
     */
    public Point[] circle(int xIndex,int yIndex){
        Point [] points = new Point[6];
        //猫在单双行时周围6个可能移动的位置坐标
        //单行
        if(xIndex%2==1){
            points[0] = getPoint(xIndex - 1, yIndex);
            points[1] = getPoint(xIndex - 1, yIndex +1);
            points[2] = getPoint(xIndex , yIndex + 1);
            points[3] = getPoint(xIndex+1 , yIndex +1);
            points[4] = getPoint(xIndex + 1  , yIndex  );
            points[5] = getPoint(xIndex , yIndex -1);
            //双行
        }else {
            points[0] = getPoint(xIndex - 1, yIndex - 1);
            points[1] = getPoint(xIndex - 1, yIndex);
            points[2] = getPoint(xIndex , yIndex + 1);
            points[3] = getPoint(xIndex + 1, yIndex );
            points[4] = getPoint(xIndex + 1  , yIndex -1 );
            points[5] = getPoint(xIndex , yIndex -1);
        }
        return points;
    }

}
