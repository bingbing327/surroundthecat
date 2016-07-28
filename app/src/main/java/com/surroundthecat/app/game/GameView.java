package com.surroundthecat.app.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.surroundthecat.app.R;
import com.surroundthecat.app.model.Cat;
import com.surroundthecat.app.model.Maps;
import com.surroundthecat.app.model.Point;

/**
 * Created by Jiang Meng on 2016/7/28.
 */
public class GameView extends View {

    GameContext gameContext = new GameContext();

    Maps maps;

    Cat cat;

    private GameLoopLinster gameLoopLinster;

    public GameView(Context context) {
        super(context);
        initGame();
    }
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGame();
    }

    public GameLoopLinster getGameLoopLinster() {
        return gameLoopLinster;
    }
    public void setGameLoopLinster(GameLoopLinster gameLoopLinster) {
        this.gameLoopLinster = gameLoopLinster;
    }

    public int getStepNum() {
        return stepNum;
    }
    public void setStepNum(int stepNum) {
        this.stepNum = stepNum;
    }

    // 用Canvas绘制位图的情况。
    // 在用Canvas绘制位图时，一般地，我们使用drawBitmap函数家族，
    // 在这些函数中，都有一个Paint参数，要做到防止锯齿，我们就要使用到这个参数。
    // 如下：首先在你的构造函数中，需要创建一个Paint。
    // Paint mPaint = new Paint（）；
    // 然后，您需要设置两个参数:
    // 1)mPaint.setAntiAlias(); //第一个函数是用来防止边缘的锯齿,
    // 2)mPaint.setBitmapFilter(true); //第二个函数是用来对位图进行滤波处理。
    // 最后，在画图的时候，调用drawBitmap函数，只需要将整个Paint传入即可。

    public void initGame(){
        paint.setAntiAlias(true);// 防止边缘的锯齿
        maps = new Maps(GameContext.X_MAX, GameContext.Y_MAX);
        maps.init();
        cat = new Cat();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize =2;//图片宽高都为原来的二分之一，即图片为原来的四分之一

        // BitmapFactory.decodeResource（？，？）这个带两个参数的方法：
        // 第一个参数是包含你要加载的位图资源文件的对象（一般写成 getResources（）就ok了）；
        // 第二个是你需要加载的位图资源的Id。
        //
        // BitmapFactory.decodeResource（？，？，？）带三个参数的方法：
        // 前两个和上面的方法一样。
        // 第三个参数应该是对你要加载的位图是否需要完整显示，如果你只需要部分，可以在这里定制。

        cat.getCat1().add(BitmapFactory.decodeResource(getResources(), R.drawable.cat12,options));
        cat.getCat1().add(BitmapFactory.decodeResource(getResources(), R.drawable.cat11,options));
        cat.getCat1().add(BitmapFactory.decodeResource(getResources(), R.drawable.cat13,options));
        cat.getCat1().add(BitmapFactory.decodeResource(getResources(), R.drawable.cat11,options));

        cat.getCat2().add(BitmapFactory.decodeResource(getResources(), R.drawable.cat21,options));
        cat.getCat2().add(BitmapFactory.decodeResource(getResources(), R.drawable.cat22,options));
        cat.getCat2().add(BitmapFactory.decodeResource(getResources(), R.drawable.cat23,options));
        cat.getCat2().add(BitmapFactory.decodeResource(getResources(), R.drawable.cat24,options));
    }

    /**
     * 重置游戏
     */
    public void reset(){
        maps.clearStone();//清除所有的石头
        GameLogic.maps(maps, gameContext);//随机产生18个以内的石头
        cat.reset();//设定猫的初始位置
        setStepNum(0);//玩家步数置为0
        handler.postDelayed(runnable, delay);//delay毫秒后开始播放动画
        gameContext.put("OldPath", maps.getPoint(4, 4));//猫的初始位置加到map中
    }

    /**
     * 地图的坐标
     */
    int mapX = 0;
    int mapY = 0;

    /**
     * 圆的半径
     */
    int pointRadius = 0;

    /**
     * 玩家的步数
     */
    private int stepNum = 0;

    /**
     * 计算会用到的常量
     */
    public void calConst() {
        // 获得屏幕的宽和高
        int w = this.getWidth();
        int h = this.getHeight();
        // 适应屏幕，求出圆的半径
        pointRadius = (w - 2 * GameContext.MARGIN - GameContext.X_MAX * GameContext.SPACE) / (2 * GameContext.X_MAX + 1);
        // 定义地图的坐标
        mapX = GameContext.MARGIN;
        mapY = h-w+20;

        Point[][] points = this.maps.getPoints();
        // step为两个圆心之间的距离，即圆的直径加圆的间隙
        int step = pointRadius * 2 + GameContext.SPACE;
        int y = mapY;
        for (int i = 0; i < points.length; i++) {
            int x = mapX;
            // 双数排时（0,2,4…）地图向右推进一个Margin的像素，单数排时（1,3,5…）推进一个半径的像素
            // 单数排的情况
            if(i%2 == 1){
                x += pointRadius;
            }
            Point[] points2 = points[i];
            for (Point point : points2) {
                // 绘出一个圆所在的矩形
                Rect rect = new Rect();
                rect.left = x - GameContext.SPACE / 2;// 地圖的左邊留白像素（17）
                rect.top = y;// 地圖的顶部留白像素
                rect.right = x + 2*pointRadius + GameContext.SPACE/2;//地圖的右邊留白像素
                rect.bottom = y + 2*pointRadius;// 地圖的底部留白像素
                point.setRect(rect);

                // 累加向右計算下一個圓的中心x位置
                x += step;
            }
            // 從屏幕的這個高度開始畫地圖,每次累加計算下一行的圓的中心y位置
            y += pointRadius*2 - 2;
        }
    }

    // onLayout方法是ViewGroup中子View的布局方法，用于放置子View的位置。
    // 放置子View很简单，只需在重写onLayout方法，然后获取子View的实例，调用子View的layout方法实现布局。
    // 在实际开发中，一般要配合onMeasure测量方法一起使用。
    //
    // onLayout方法：
    // @Override
    // protected abstract void onLayout(boolean changed,int l, int t, int r, int b);
    //
    // 该方法在ViewGroup中定义是抽象函数，继承该类必须实现onLayout方法，而ViewGroup的onMeasure并非必须重写的。
    // View的放置都是根据一个矩形空间放置的，
    // onLayout传下来的l,t,r,b分别是放置父控件的矩形可用空间（除去margin和padding的空间）的左上角的left、top
    // 以及右下角right、bottom值。
    //
    // 解析onLayout(boolean, int, int, int, int)方法:
    // void android.view.ViewGroup.onLayout(boolean changed, int l, int t, int r, int b)
    // 调用场景：在view给其孩子设置尺寸和位置时被调用。
    // 子view，包括孩子在内，必须重写onLayout(boolean, int, int, int, int)方法，
    // 并且调用各自的layout(int, int, int, int)方法。
    //
    // 参数说明：
    // 参数changed表示view有新的尺寸或位置；
    // 参数l表示相对于父view的Left位置；
    // 参数t表示相对于父view的Top位置；
    // 参数r表示相对于父view的Right位置；
    // 参数b表示相对于父view的Bottom位置。

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        this.calConst();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawMap(canvas);
        drawCat(canvas);
    }

    //
    // android开发中将十六进制颜色代码转换为int类型数值的方法：
    // Color.parseColor("#00CCFF")
    // 返回int数值;

    /**
     * 颜色定义
     */
    int pointColor = Color.parseColor("#b5b5b5");
    int checkColor = Color.parseColor("#ff845e");
    Paint paint  = new Paint();

    /**
     * 用画笔绘制地图
     */
    protected void drawMap(Canvas canvas){
        Point[][] points = this.maps.getPoints();
        for (int i = 0; i < points.length; i++) {
            Point[] points2 = points[i];
            for (Point point : points2) {
                // 若point为石头，则设置画笔为checkColor色
                if(point.isStone()){
                    paint.setColor(checkColor);
                    // 反之，则设置画笔为pointColor色
                }else {
                    paint.setColor(pointColor);
                }
                Rect rect = point.getRect();
                // drawCircle(float cx, float cy, float radius,Paint paint);
                // 绘制圆，参数一是中心点的x轴，参数二是中心点的y轴，参数三是半径，参数四是paint对象；
                canvas.drawCircle(rect.left + pointRadius + GameContext.SPACE/2,
                        rect.top + pointRadius,
                        pointRadius,
                        paint);
            }
        }
    }

    // Rect类主要用于表示坐标系中的一块矩形区域，并可以对其做一些简单操作。
    // 这块矩形区域，需要用左上右下两个坐标点表示（left,top,right,bottom）,
    // 你也可以获取一个Rect实例的Width和Height。
    //
    // 但是，如果你这么声明一个Rect类：
    // Rect rect=new Rect(100,50,300,500);
    // 那么右下角(300,500)其实是不在这个矩形里面的，但是左上角(100,50)在，
    // 也就是说，这个矩形实际表示的区域是：(100,50,299,499)。
    // 另外，Rect计算出的Height和Width倒是对的。
    //
    //所以，在涉及Rect运算的时候，尽量不要使用它的右下角坐标，即right和bottom，因为他们是错的。
    // 当然，在你调用android自己的函数时，是可以使用的，因为Android里面一直保持这么奇葩的思维。

    /**
     * 画猫
     */
    protected void drawCat(Canvas canvas){
        Rect rect = this.maps.getPoints()[cat.getxIndex()][cat.getyIndex()].getRect();
        //rect.bottom:矩形底边的y轴值。如果矩形为空，则该值为 NegativeInfinity。
        //猫站在每个圆底部向上10个像素的位置
        int bottom = rect.bottom - 10;
        //获取猫的图片
        Bitmap bitmap = cat.getBitMap();
        //猫的图片所在位置的左上角的x坐标
        int left = rect.left - (bitmap.getWidth() - rect.right + rect.left)/2;
        // drawBitmap(Bitmap bitmap, Rect src, Rect dst, Paint paint);
        // 贴图，在指定点绘制从源位图中"挖取"的一块。
        // 参数一就是我们常规的Bitmap对象，
        // 参数二是源区域(这里是bitmap)，
        // 参数三是目标区域(应该在canvas的位置和大小)，
        // 参数四是Paint画刷对象，因为用到了缩放和拉伸的可能，当原始Rect不等于目标Rect时性能将会有大幅损失。
        //
        // public void drawBitmap (Bitmap bitmap, float left, float top, Paint paint)
        // Parameters
        // bitmap The bitmap to be drawn
        // left The position of the left side of the bitmap being drawn
        // top The position of the top side of the bitmap being drawn
        // paint The paint used to draw the bitmap (may be null)
        canvas.drawBitmap(bitmap, left, bottom - bitmap.getHeight(), paint);
    }

    /**
     * 触摸事件
     */
    public boolean onTouchEvent(MotionEvent event) {
        // ACTION_DOWN: 表示用户开始触摸
        // ACTION_MOVE: 表示用户在移动(手指或者其他)
        // ACTION_UP:表示用户抬起了手指
        // ACTION_CANCEL:表示手势被取消了
        if (event.getAction() == MotionEvent.ACTION_UP) {
            // getX() 获得事件发生时,触摸的中间区域在屏幕的X轴.
            // getY() 获得事件发生时,触摸的中间区域在屏幕的Y轴.
            gameLoop(event.getX(), event.getY());
        }
        return true;
    }

    /**
     * 游戏主循环
     */
    public void gameLoop(float px, float py) {
        Point[][] points = this.maps.getPoints();
        //定义一个bool变量has，has为true时表示玩家成功点击，猫需要移动了
        boolean has = false;
        // 在使用二维数组对象时，注意length所代表的长度，
        // 数组名后直接加上length(如arr.length)，所指的是有几行(Row);
        // 指定索引后加上length(如arr[0].length)，指的是该行所拥有的元素，也就是列(Column)数目。
        for (int i = 0; i < points.length; i++) {
            Point[] points2 = points[i];
            for (Point point : points2) {
                //遍历数组
                Rect rect = point.getRect();
                /*
                * Rect对象rect有一个contains方法，
                * 只要我们把坐标传进去，就可以通过返回值来得到该坐标是否在该rect对象所表示的矩形区域了。
                * */
                //若玩家点击的位置在此point的rect内
                if(rect.contains((int)px, (int)py)){
                    //若玩家点击到石头，则直接return
                    if(point.isStone()){
                        return;
                    }
                    //玩家点击的点置为石头
                    point.setStone(true);
                    //has置为true，跳出循环
                    has = true;
                    break;
                }
            }
            //只要has变为true，就跳出循环
            if(has){
                break;
            }
        }
        if(!has)
            return;

        //玩家的步数+1
        setStepNum(getStepNum() + 1);
        //判断猫的check()值
        int state = GameLogic.check(cat, maps, gameContext);
        switch (state) {
            case 0://猫在边界点
                /*
                * 方法postDelayed的作用是延迟多少毫秒后开始运行，
                * 而removeCallbacks方法是删除指定的Runnable对象，使线程对象停止运行。
                * */
                handler.removeCallbacks(runnable);
                break;
            case 1:
                handler.removeCallbacks(runnable);
                break;
            case 2://猫的周围有石头
                cat.setSurrounded(true);
            case 3:
                //猫走下一步
                GameLogic.step(cat, maps, gameContext);
                invalidate();
                break;
            default:
                //移动
                break;
        }

        if(this.gameLoopLinster != null){
            this.gameLoopLinster.onState(state);//传回state值
        }
    }

    int delay = 150;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            invalidate();
            handler.postDelayed(runnable, delay);
        }
    };
}
