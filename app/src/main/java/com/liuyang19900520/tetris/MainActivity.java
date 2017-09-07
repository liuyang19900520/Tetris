package com.liuyang19900520.tetris;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.liuyang19900520.tetris.R;
import com.liuyang19900520.tetris.ScreenUtils;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //游戏区域宽高
    int xWidth, xHeight;


    //声明游戏区域
    private View view;
    //地图
    boolean[][] maps;

    //方塊的類型
    int boxType;
    //方块
    Point[] boxs;
    //方块大小(格子)
    int boxSize;
    //辅助线画笔
    Paint linePaint;
    //f方块画笔
    Paint boxPainnt;
    //f地圖画笔
    Paint mapPainnt;


    //自动下落线程
    Thread downThread;
    //暂停状态
    public boolean isPause;
    //结束状态
    public boolean isOver;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            view.invalidate();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initData();
        initView();
        initListener();
    }

    private void initListener() {

        findViewById(R.id.btn_left).setOnClickListener(this);
        findViewById(R.id.btn_top).setOnClickListener(this);
        findViewById(R.id.btn_right).setOnClickListener(this);
        findViewById(R.id.btn_down).setOnClickListener(this);
        findViewById(R.id.btn_start).setOnClickListener(this);
        findViewById(R.id.btn_pause).setOnClickListener(this);

    }

    /**
     * 初始化数据
     */
    private void initData() {
        //获取屏幕宽度与高度
        int width = ScreenUtils.getScreenWidth(this);

        xWidth = width * 2 / 3;
        xHeight = xWidth * 2;

        //初始化地圖
        maps = new boolean[10][20];
        //初始化方塊
//        boxs = new Point[]{new Point(4, 1), new Point(3, 0), new Point(1, 1), new Point(5, 1)};
        //初始化方塊大小  游戲區域/10
        boxSize = xWidth / maps.length;
        boxs = new Point[]{};

    }


    public void newBoxs() {
        //随机数生成方块
        Random random = new Random();
        boxType = random.nextInt(7);

        switch (boxType) {
            //田
            case 0:
                //初始化方塊
                boxs = new Point[]{new Point(4, 0), new Point(5, 0), new Point(4, 1), new Point(5, 1)};
                break;
            //L
            case 1:
                //初始化方塊
                boxs = new Point[]{new Point(4, 1), new Point(5, 0), new Point(3, 1), new Point(5, 1)};
                break;
            //J
            case 2:
                //初始化方塊
                boxs = new Point[]{new Point(4, 1), new Point(3, 0), new Point(3, 1), new Point(5, 1)};
                break;
            //s
            case 3:
                boxs = new Point[]{new Point(4, 1), new Point(3, 1), new Point(4, 0), new Point(5, 0)};
                break;
            //z
            case 4:
                boxs = new Point[]{new Point(4, 1), new Point(3, 0), new Point(4, 0), new Point(5, 1)};
                break;
            //I
            case 5:
                boxs = new Point[]{new Point(4, 1), new Point(4, 0), new Point(4, 2), new Point(4, 3)};
                break;
            //倒T
            case 6:
                boxs = new Point[]{new Point(4, 1), new Point(3, 1), new Point(5, 1), new Point(4, 0)};
                break;

        }


    }

    /**
     * 初始化视图
     */
    private void initView() {
        //初始化画笔
        linePaint = new Paint();
        linePaint.setColor(0xff666666);
        linePaint.setAntiAlias(true);

        boxPainnt = new Paint();
        boxPainnt.setColor(0xf0000000);
        boxPainnt.setAntiAlias(true);

        mapPainnt = new Paint();
        mapPainnt.setColor(0xf0222000);
        mapPainnt.setAntiAlias(true);
        //1，得到父容器
        FrameLayout layoutGame = (FrameLayout) findViewById(R.id.fl_game);
        //2，实例化游戏区域
        view = new View(this) {
            @Override
            protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                //绘制地图
                for (int i = 0; i < maps.length; i++) {
                    for (int j = 0; j < maps[0].length; j++) {
                        if (maps[i][j] == true) {
                            canvas.drawRect(i * boxSize, j * boxSize, i * boxSize + boxSize, j * boxSize + boxSize, mapPainnt);
                        }
                    }
                }


                //方块绘制(不*boxSize的話就只能繪畫出一個小方塊)
                for (int i = 0; i < boxs.length; i++) {
                    canvas.drawRect(boxs[i].x * boxSize,
                            boxs[i].y * boxSize,
                            boxs[i].x * boxSize + boxSize,
                            boxs[i].y * boxSize + boxSize,
                            boxPainnt);
                }


                //画辅助线
                for (int x = 0; x < maps.length; x++) {
                    canvas.drawLine(x * boxSize, 0, x * boxSize, view.getHeight(), linePaint);
                }
                for (int y = 0; y < maps[0].length; y++) {
                    canvas.drawLine(0, y * boxSize, view.getWidth(), y * boxSize, linePaint);
                }
            }
        };
        //3，设置游戏区域大小
        view.setLayoutParams(new FrameLayout.LayoutParams(xWidth, xHeight));
        //設置背景顔色
        view.setBackgroundColor(0x10000000);
        //4，添加到父容器内
        layoutGame.addView(view);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                if (isPause) {
                    return;
                }
                move(-1, 0);
                break;
            case R.id.btn_top:
                if (isPause) {
                    return;
                }
                rotate();
                break;
            case R.id.btn_right:
                if (isPause) {
                    return;
                }
                move(1, 0);
                break;
            case R.id.btn_down:
                if (isPause) {
                    return;
                }
                //快速下落
                while (true) {
                    if (!moveBottom()) {
                        break;
                    }
                }

                break;
            case R.id.btn_start:

                startGame();

                break;
            case R.id.btn_pause:
                setPause();
                break;

        }
        //重绘View
        view.invalidate();
    }


    public void startGame() {
        if (downThread == null) {
            downThread = new Thread() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            //休眠500
                            sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //判断当前状态
                        if (isPause) {
                            continue;
                        }
                        //执行一次下落
                        moveBottom();
                        handler.sendEmptyMessage(0);
                    }
                }
            };
            downThread.start();
        }
        newBoxs();
    }

    public void setPause() {
        if (isPause) {
            isPause = false;
        } else {
            isPause = true;
        }
    }

    //游戏结束判断
    public boolean checkOver() {

        //地图上的点与方块的点重合了的时候，这里面的方块应该是最新的
        for (int i = 0; i < boxs.length; i++) {
            if (maps[boxs[i].x][boxs[i].y]) {
                return true;
            }
        }
        return false;
    }

    /*下落方法*/
    public boolean moveBottom() {
        //1,移动成功，不做处理
        if (move(0, 1)) {
            return true;
        }
        //2,移动失败，堆积处理
        for (int i = 0; i < boxs.length; i++) {
            maps[boxs[i].x][boxs[i].y] = true;
        }

        //消行处理
        cleanLine();
        //3，生成新的方块
        newBoxs();

        //判断游戏结束
        isOver = checkOver();
        //游戏未结束
        return false;


        //2,移动成功，

    }

    //执行消行（主方法）
    public void cleanLine() {
        boolean isClean;
        for (int y = maps[0].length - 1; y > 0; y--) {
            //判断消行
            if (checkLine(y)) {
                //执行消行
                deleteLine(y);
                //从消的哪一行重新开始便利
                y++;
            }

        }
    }

    //消行判断，判断行的执行在上级方法中，所以这里就只判断列
    //子方法
    public boolean checkLine(int y) {
        //maps.length是10，也就是说现在判断一列的内容
        for (int x = 0; x < maps.length; x++) {
            if (!maps[x][y]) {
                return false;
            }
        }
        return true;
    }


    /*
    delete方法
     */
    public void deleteLine(int dy) {
        //map[0].length==20,也就数现在才开始判断横行开始判断
        for (int y = maps[0].length - 1; y > 0; y--) {
            for (int x = 0; x < maps.length; x++) {
                maps[x][y]= maps[x][y - 1];
            }
        }
    }


    /*
      移动
     */
    public boolean move(int x, int y) {
        Log.e("before", boxs[0].x + ":" + boxs[0].y);
        //方块数组，每一个方块的坐标都要加上便宜的量

        for (int i = 0; i < boxs.length; i++) {
            if (checkBoundary(boxs[i].x + x, boxs[i].y + y)) {
                return false;
            }
        }


        for (int i = 0; i < boxs.length; i++) {
            boxs[i].x += x;
            boxs[i].y += y;
        }
        Log.e("after", boxs[0].x + ":" + boxs[0].y);
        return true;
    }

    /*
    旋转
     */
    public boolean rotate() {
        //
        if (boxType == 0) {
            return false;
        }
        for (int i = 0; i < boxs.length; i++) {
            //旋转算法(笛卡尔公式)
            int checkX = -boxs[i].y + boxs[0].y + boxs[0].x;
            int checkY = boxs[i].x - boxs[0].x + boxs[0].y;
            if (checkBoundary(checkX, checkY)) {
                return false;
            }
        }

        //遍历方块数组，每一个都要围绕着中心点旋转90度
        for (int i = 0; i < boxs.length; i++) {
            //旋转算法(笛卡尔公式)
            int checkX = -boxs[i].y + boxs[0].y + boxs[0].x;
            int checkY = boxs[i].x - boxs[0].x + boxs[0].y;
            boxs[i].x = checkX;
            boxs[i].y = checkY;
        }
        return true;
    }

    /**
     * 判断边界，判断传入的xy是否再界外
     *
     * @param x 方块x坐标
     * @param y 方块y坐标
     * @return true 出界面  false 未出界
     */

    //如果這個邊界和地圖上面的點是true，那麽就是代表出街了。
    public boolean checkBoundary(int x, int y) {
        return (x < 0 || y < 0 || x >= maps.length || y >= maps[0].length || maps[x][y]);
    }


}
