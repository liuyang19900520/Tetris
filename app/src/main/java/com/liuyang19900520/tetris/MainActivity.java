package com.liuyang19900520.tetris;

import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;

import com.liuyang19900520.tetris.controller.GameController;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //声明游戏区域
    private View view;

    //声明游戏控制器
    public GameController gameController;

    //主页Handler
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //重绘
                case Config.MSG_INVALIDATE:
                    view.invalidate();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //NO_TITLE设置
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        //初始化控制器
        gameController = new GameController(this, handler);
        //初始化视图
        initView();
        //初始化监听器
        initListener();
    }


    /**
     * 初始化视图
     */
    private void initView() {
        //1，得到父容器
        FrameLayout layoutGame = (FrameLayout) findViewById(R.id.fl_game);
        //2，实例化游戏区域
        view = new View(this) {
            @Override
            protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                //绘制画布
                gameController.draw(canvas);
            }
        };
        //3，设置游戏区域大小
        view.setLayoutParams(new FrameLayout.LayoutParams(Config.FL_WIDTH, Config.FL_HEIGHT));
        //3，設置背景顔色
        view.setBackgroundColor(0x10000000);
        //4，添加到父容器内
        layoutGame.addView(view);
    }

    /**
     * 初始化监听器
     */
    private void initListener() {
        findViewById(R.id.btn_left).setOnClickListener(this);
        findViewById(R.id.btn_top).setOnClickListener(this);
        findViewById(R.id.btn_right).setOnClickListener(this);
        findViewById(R.id.btn_down).setOnClickListener(this);
        findViewById(R.id.btn_quickdown).setOnClickListener(this);
        findViewById(R.id.btn_start).setOnClickListener(this);
        findViewById(R.id.btn_pause).setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        gameController.onclick(v.getId());
        //重绘View
        view.invalidate();
    }


}
