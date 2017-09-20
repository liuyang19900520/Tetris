package com.liuyang19900520.tetris;

import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liuyang19900520.tetris.controller.GameController;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //声明游戏区域
    private View gameView;

    //声明下一塊預覽区域
    private View nextView;

    //声明游戏控制器
    public GameController gameController;

    private TextView tvHigh, tvNow;

    //主页Handler
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //重绘
                case Config.MSG_INVALIDATE:
                    gameView.invalidate();
                    nextView.invalidate();
                    break;
                case Config.MSG_SCORE:
                    tvNow.setText((Integer) msg.getData().getInt("score") + "分");
                    tvHigh.setText((Integer) msg.getData().getInt("highScore") + "分");
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
        //初始化下一塊佈局
        initNextView();
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
        gameView = new View(this) {
            @Override
            protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                gameController.drawGameLayout(canvas);
            }
        };
        //3，设置游戏区域大小
        gameView.setLayoutParams(new FrameLayout.LayoutParams(Config.FL_WIDTH, Config.FL_HEIGHT));
        gameView.setPadding(Config.PADDING_X, Config.PADDING_X, Config.PADDING_X, Config.PADDING_X);
        //3，設置背景顔色
        gameView.setBackgroundColor(0x10000000);
        //4，添加到父容器内
        layoutGame.addView(gameView);

        //设置信息区域
        LinearLayout ll_right = (LinearLayout) findViewById(R.id.ll_right);
        ll_right.setPadding(0, Config.PADDING_X, Config.PADDING_X, Config.PADDING_X);

        tvHigh = (TextView) findViewById(R.id.tv_high_score);
        tvNow = (TextView) findViewById(R.id.tv_now_score);
    }

    /**
     * 實例化下一塊預覽區域
     */
    public void initNextView() {
        //1，得到父容器
        FrameLayout layoutNext = (FrameLayout) findViewById(R.id.fl_next);
        //2，实例化下一塊區域
        nextView = new View(this) {
            @Override
            protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                gameController.drawNextLayout(canvas, nextView.getWidth());
            }
        };
        //3，设置下一塊區域大小
        nextView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200));
        //3，設置背景顔色
        nextView.setBackgroundColor(0x10000000);
        //4，添加到父容器内
        layoutNext.addView(nextView);
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
        gameView.invalidate();
        nextView.invalidate();
    }


}
