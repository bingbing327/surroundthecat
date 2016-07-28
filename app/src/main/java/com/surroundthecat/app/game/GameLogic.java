package com.surroundthecat.app.game;

import android.util.Log;

import com.surroundthecat.app.model.Cat;
import com.surroundthecat.app.model.Maps;
import com.surroundthecat.app.model.Point;

import java.util.Random;

/**
 * Created by Jiang Meng on 2016/7/28.
 */
public class GameLogic {
    //随机产生18个以內的石头
    public static void maps(Maps maps, GameContext gameContext) {
        int max = 9;
        /*
        * java.util.Random random=new java.util.Random();// 定义随机类
        * int result=random.nextInt(10);// 返回[0,10)集合中的整数，注意不包括10
        * return result+1;              // +1后，[0,10)集合变为[1,11)集合，满足要求
        * */
        Random random = new Random();
        max += random.nextInt(10);
        for (int i = 0; i < max; i++) {
            int xIndex = random.nextInt(GameContext.X_MAX);
            int yIndex = random.nextInt(GameContext.Y_MAX);
            //除去猫所在的点
            if (xIndex == 4 && yIndex == 4) {
                continue;
            }
            maps.getPoints()[xIndex][yIndex].setStone(true);
        }
    }

    public static void step(Cat cat, Maps maps, GameContext gameContext) {
        int[] temps = new int[6];
        int weight = 3;
        //circle():猫的下一步的所有坐标
        Point[] points = maps.circle(cat.getxIndex(), cat.getyIndex());

        for (Point point : points) {
            if (point == null)
                continue;
            //猫的下一步优先选择是边界点的
            //若point.isStone()为false且point为边界点
            if (!point.isStone() && maps.isEdge(point)) {
                cat.moveTo(point);
                return;
            }
        }

        temps[0] = weightTest(points[0], maps, gameContext, weight, 1);
        temps[1] = weightTest(points[1], maps, gameContext, weight, 2);
        temps[2] = weightTest(points[2], maps, gameContext, weight, 3);
        temps[3] = weightTest(points[3], maps, gameContext, weight, 4);
        temps[4] = weightTest(points[4], maps, gameContext, weight, 5);
        temps[5] = weightTest(points[5], maps, gameContext, weight, 6);

        for (int i : temps) {
            Log.d("temps", i + "@");
        }

        int temp = -1;
        int index = 0;
        for (int i = 0; i < temps.length; i++) {
            int j = temps[i];
            if (j > temp) {
                temp = j;
                index = i;
            }
        }

        cat.moveTo(points[index]);

        Log.d("index", index + "@");

    }

    /**
     * 猫的算法分析
     */
    public static int weightTest(Point point, Maps maps, GameContext gameContext, int step, int d) {
        if (step == 0 || point == null)
            return 0;
        int weight = 0;

        //若point为石头，猫不可以走，weight = 0
        if (point.isStone()) {
            weight = 0;
            //若point为边界点，weight置为很大的值
        } else if (maps.isEdge(point)) {
            weight = 5000;
            //其他情况，weight = 1，机会均等
        } else {
            weight = 1;
            Point[] points = maps.circle(point.getxIndex(), point.getyIndex());
            for (Point point2 : areaStone(points, d)) {
                weight += weightTest(point2, maps, gameContext, step - 1, d);//???????
            }
        }
        return weight * step;//????????
    }

    public static int check(Cat cat, Maps maps, GameContext gameContext) {
        int xIndex = cat.getxIndex();
        int yIndex = cat.getyIndex();
        //若猫在边界点，check()返回0
        if (xIndex == 0 || xIndex == GameContext.X_MAX - 1 ||
                yIndex == 0 || yIndex == GameContext.Y_MAX - 1) {
            return 0;
        }
        //points为猫的下一步的所有坐标
        Point[] points = maps.circle(cat.getxIndex(), cat.getyIndex());
        boolean b = false;
        for (Point point : points) {
            //若point不是石头，b = true
            if (!point.isStone()) {
                b = true;
                break;
            }
        }
        //若b为false，check()返回1
        if (!b) {
            return 1;
        }
        //若猫的周围有石头，check()返回2？？？？？？
        if (testCircle(maps.getPoint(xIndex, yIndex), maps, gameContext)) {
            return 2;
        }
        //其他情况，check()返回3
        return 3;
    }

    /**
     * 检查猫的周围是否有石头
     */
    public static boolean testCircle(Point test, Maps maps, GameContext gameContext) {
        Point[] points = maps.circle(test.getxIndex(), test.getyIndex());
        return testArea(test, points, maps, 1)
                && testArea(test, points, maps, 2)
                && testArea(test, points, maps, 3)
                && testArea(test, points, maps, 4)
                && testArea(test, points, maps, 5)
                && testArea(test, points, maps, 6);
    }

    //传进来d
    public static boolean testArea(Point test, Point[] points, Maps maps, int d) {
        //根据d的六种情况，将points[]的三个点赋给points2[]
        Point[] points2 = areaStone(points, d);//d是什么？？？

        boolean b = true;

        for (Point point : points2) {
            if (point == null) {
                b = false;
                break;
            }
            //若point不是石头，猫的下一步的所有坐标赋给tempPoints[]
            if (!point.isStone()) {
                Point[] tempPoints = maps.circle(point.getxIndex(), point.getyIndex());
                //将point、tempPoints和d全部传入testArea(),返回值赋给b
                b = testArea(point, tempPoints, maps, d);
                if (!b) {
                    break;
                }
            }
        }

        return b;
    }

    //传进来d
    public static Point[] areaStone(Point[] points, int d) {
        //定义points2为长度为3的一维数组
        Point[] points2 = new Point[3];
        //d的六种情况
        switch (d) {
            case 1:
                points2[0] = points[0];//?????????????????
                points2[1] = points[1];
                points2[2] = points[5];
                break;
            case 2:
                points2[0] = points[1];
                points2[1] = points[2];
                points2[2] = points[0];
                break;
            case 3:
                points2[0] = points[2];
                points2[1] = points[3];
                points2[2] = points[1];
                break;
            case 4:
                points2[0] = points[3];
                points2[1] = points[4];
                points2[2] = points[2];
                break;
            case 5:
                points2[0] = points[4];
                points2[1] = points[5];
                points2[2] = points[3];
                break;
            case 6:
                points2[0] = points[5];
                points2[1] = points[0];
                points2[2] = points[4];
                break;
            default:
                break;
        }
        return points2;
    }
}
