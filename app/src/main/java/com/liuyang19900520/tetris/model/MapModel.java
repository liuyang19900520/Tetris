package com.liuyang19900520.tetris.model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.liuyang19900520.tetris.Config;

/**
 * Created by liuya on 2017/9/11.
 */

public class MapModel {


    //辅助线画笔
    Paint linePaint;

    //地圖画笔
    Paint mapPainnt;

    //声明一个状态框画笔
    Paint statePainnt;

    private Context context;

    public boolean[][] maps;

    public int x;
    public int y;

    //方块大小(格子)
    public int boxSize;

    public MapModel(Context context, int boxSize, int w, int h) {

        this.context = context;
        this.boxSize = boxSize;
        this.x = w;
        this.y = h;

        //初始化地圖
        maps = new boolean[Config.MAP_X][Config.MAP_Y];
        //初始化画笔
        linePaint = new Paint();
        linePaint.setColor(0xff666666);
        linePaint.setAntiAlias(true);

        //初始化地图画笔
        mapPainnt = new Paint();
        mapPainnt.setColor(0xff696969);
        mapPainnt.setAntiAlias(true);

        statePainnt = new Paint();
        statePainnt.setColor(0xffff0000);
        statePainnt.setAntiAlias(true);
        statePainnt.setTextSize(150);
    }

    public void drawMap(Canvas canvas) {
        //绘制地图
        for (int i = 0; i < maps.length; i++) {
            for (int j = 0; j < maps[0].length; j++) {
                if (maps[i][j] == true) {
                    canvas.drawRect(i * boxSize, j * boxSize, i * boxSize + boxSize, j * boxSize + boxSize, mapPainnt);
                }
            }
        }
    }

    public void drawLine(Canvas canvas) {
        //画辅助线
        for (int x = 0; x < maps.length; x++) {
            canvas.drawLine(x * boxSize, 0, x * boxSize, y, linePaint);
        }
        for (int y = 0; y < maps[0].length; y++) {
            canvas.drawLine(0, y * boxSize, x, y * boxSize, linePaint);
        }
    }

    public void drawState(Canvas canvas, boolean isOver, boolean isPause) {

        if (isOver) {
            canvas.drawText("游戏结束", x / 2 - statePainnt.measureText("游戏结束") / 2, y / 2, statePainnt);
        }

        //暂停状态
        if (isPause) {
            canvas.drawText("暂停", x / 2 - statePainnt.measureText("暂停") / 2, y / 2, statePainnt);
        }
    }


    //执行消行（主方法）
    public int cleanLine() {

        int lines = 0;

        for (int y = maps[0].length - 1; y > 0; y--) {
            //判断消行
            if (checkLine(y)) {
                //执行消行
                deleteLine(y);
                //从消的哪一行重新开始便利
                y++;
                lines++;
            }

        }
        return lines;
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
                maps[x][y] = maps[x][y - 1];
            }
        }
    }
}
