package com.surroundthecat.app.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.surroundthecat.app.R;
import com.surroundthecat.app.game.GameLoopLinster;
import com.surroundthecat.app.game.GameView;

//实现接口OnClickListener和GameLoopLinster（自定义接口）
public class MainActivity extends Activity implements View.OnClickListener, GameLoopLinster {

    GameView gameView;

    Button startButton;
    RelativeLayout relativeLayout;

    Button button1;
    RelativeLayout relativeLayout1;

    Button button2;
    RelativeLayout relativeLayout2;

    TextView infoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Activity生命週期的開始
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);

        gameView = (GameView) findViewById(R.id.gameView1);
        gameView.setGameLoopLinster(this);//自定义了一个GameLoopLinster接口

        startButton = (Button) findViewById(R.id.button1);//“开始抓猫”
        //中间两只猫和“开始抓猫”按钮
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout1);

        button1 = (Button) findViewById(R.id.button2);//“再抓一次”
        //抓到猫了和“再抓一次”按钮
        relativeLayout1 = (RelativeLayout) findViewById(R.id.success);

        button2 = (Button) findViewById(R.id.button3);//“再抓一次”
        //没有抓到和“再抓一次”按钮
        relativeLayout2 = (RelativeLayout) findViewById(R.id.haha);

        //抓到猫后显示的对话框
        infoTextView = (TextView) findViewById(R.id.info);

        startButton.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
    }

    @Override
    public void onClick(View arg0) {
        relativeLayout1.setVisibility(View.INVISIBLE);//抓到猫了和“再抓一次”按钮
        relativeLayout2.setVisibility(View.INVISIBLE);//没有抓到和“再抓一次”按钮
        relativeLayout.setVisibility(View.INVISIBLE);//中间两只猫和“开始抓猫”按钮
        gameView.reset();//重置游戏
    }

    @Override
    //Activity重启
    public void onState(int state) {
        switch (state) {
            //猫在边界点的情况
            case 0:
                //没有抓到和“再抓一次”按钮
                relativeLayout2.setVisibility(View.VISIBLE);
                break;
            //抓到猫的情况
            case 1:
                //抓到猫后显示的对话框
                String format = "<h1>你用了 %d步 抓住了贱贱猫!</h1><br> 我还会回来的！";
                // Android中的TextView，本身就支持部分的Html格式标签。
                // 这其中包括常用的字体大小颜色设置，文本链接等。
                // 使用起来也比较方便，只需要使用Html类转换一下即可。
                // 比如：
                // textView.setText(Html.fromHtml(str));
                infoTextView.setText(Html.fromHtml(String.format(format, gameView.getStepNum())));
                //抓到猫了和“再抓一次”按钮
                relativeLayout1.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onStop() {
        super.onStop();
    }
}
