package com.liuyang19900520.tetris.controller;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;

import com.liuyang19900520.tetris.Config;
import com.liuyang19900520.tetris.R;
import com.liuyang19900520.tetris.utils.ScreenUtils;
import com.liuyang19900520.tetris.model.BoxModel;
import com.liuyang19900520.tetris.model.MapModel;

/**
 * Created by LiuYang on 2017/9/12.
 */

public class GameController {

    //模型
    public BoxModel boxModel;
    public MapModel mapModel;

    //自动下落线程
    Thread downThread;

    //暂停状态
    public boolean isPause;
    //结束状态
    public boolean isOver;

    //用Activity交互的上线文
    public Context context;
    //用Activity交互的handler
    public Handler handler;

    public GameController(Context c, Handler handler) {
        this.context = c;
        this.handler = handler;
        initData();
    }

    /**
     * 初始化数据
     */
    public void initData() {
        //获取屏幕宽度与高度
        int width = ScreenUtils.getScreenWidth((Activity) context);
        int height = ScreenUtils.getScreenHeight((Activity) context);
        //获取底边的代销
        int ll_bottom = ScreenUtils.dip2px(context, Config.LL_BOTTOM);
        //设置游戏控制画面的大小
        Config.FL_HEIGHT = height - ll_bottom;
        Config.FL_WIDTH = Config.FL_HEIGHT / 2;

        //初始化方塊大小  游戲區域/10
        int boxSize = Config.FL_WIDTH / Config.MAP_X;

        //实例化地图模型
        mapModel = new MapModel(context, boxSize, Config.FL_WIDTH, Config.FL_HEIGHT);
        //实例化方块
        boxModel = new BoxModel(context, boxSize);
    }

    /**
     * 开始游戏
     */

    public void startGame() {
        //清除所有方块
        for (int x = 0; x < mapModel.maps.length; x++) {
            for (int y = 0; y < mapModel.maps[0].length; y++) {
                mapModel.maps[x][y] = false;
            }
        }
        //设置暂停与结束状态
        isPause = false;
        isOver = false;

        //开启下落线程
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
                        //判断当前状态或者结束状态
                        if (isPause || isOver) {
                            continue;
                        }

                        //执行一次下落
                        moveBottom();
                        //发送重绘的msg
                        Message msg = handler.obtainMessage();
                        msg.what = Config.MSG_INVALIDATE;
                        handler.sendMessage(msg);
                    }
                }
            };
            downThread.start();
        }
        //创建新方块
        boxModel.newBoxes();
    }


    //游戏结束判断
    public boolean checkOver() {

        //地图上的点与方块的点重合了的时候，这里面的方块应该是最新的
        for (int i = 0; i < boxModel.boxs.length; i++) {
            if (mapModel.maps[boxModel.boxs[i].x][boxModel.boxs[i].y]) {
                return true;
            }
        }
        return false;
    }

    /*下落方法*/
    public boolean moveBottom() {
        //1,移动成功，不做处理
        if (boxModel.move(0, 1, mapModel)) {
            return true;
        }
        //2,移动失败，堆积处理
        for (int i = 0; i < boxModel.boxs.length; i++) {
            mapModel.maps[boxModel.boxs[i].x][boxModel.boxs[i].y] = true;
        }

        //消行处理
        mapModel.cleanLine();

        //3，生成新的方块
        if (!isOver && !isPause) {
            boxModel.newBoxes();
        }

        //判断游戏结束
        isOver = checkOver();

        //游戏未结束
        return false;
    }

    public void setPause() {
        //前提是游戏未结束
        if (!isOver) {
            if (isPause) {
                isPause = false;
            } else {
                isPause = true;
            }
        }
    }

    /**
     * 绘制方法，绘制地图，方块，辅助线
     *
     * @param canvas
     */
    public void drawGameLayout(Canvas canvas) {
        //绘制地图
        mapModel.drawMap(canvas);
        //绘制方块
        boxModel.drawBox(canvas);
        //绘制辅助线
        mapModel.drawLine(canvas);
        //绘制状态
        mapModel.drawState(canvas, isOver, isPause);
    }

    /**
     * 繪製預覽區域
     * @param canvas
     * @param width
     */
    public void drawNextLayout(Canvas canvas, int width) {
        boxModel.drawNextBox(canvas,width);
    }

    public void onclick(int id) {
        switch (id) {
            case R.id.btn_left:
                if (isPause) {
                    return;
                }
                boxModel.move(-1, 0, mapModel);
                break;

            case R.id.btn_top:
                if (isPause) {
                    return;
                }
                boxModel.rotate(mapModel);
                break;

            case R.id.btn_right:
                if (isPause) {
                    return;
                }
                boxModel.move(1, 0, mapModel);
                break;

            case R.id.btn_down:
                if (isPause) {
                    return;
                }
                moveBottom();
                break;

            case R.id.btn_quickdown:
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
    }


}
