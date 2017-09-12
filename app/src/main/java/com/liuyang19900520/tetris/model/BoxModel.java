package com.liuyang19900520.tetris.model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

import com.liuyang19900520.tetris.R;

import java.util.Random;

/**
 * Created by liuya on 2017/9/11.
 */

public class BoxModel {

    //方塊的類型
    public int boxType;
    //方块
    public Point[] boxs;
    //方块大小(格子)
    public int boxSize;

    //方块画笔
    public Paint boxPainnt;

    //上下文
    private Context context;

    public BoxModel(Context context, int boxSize) {
        this.boxSize = boxSize;
        this.context = context;
        boxPainnt = new Paint();
        boxPainnt.setColor(0xf0000000);
        boxPainnt.setAntiAlias(true);
    }

    /**
     * 绘制方块
     *
     * @param canvas
     */
    public void drawBox(Canvas canvas) {
        if (boxs != null) {
            //方块绘制(不*boxSize的話就只能繪畫出一個小方塊)
            for (int i = 0; i < boxs.length; i++) {
                canvas.drawRect(boxs[i].x * boxSize,
                        boxs[i].y * boxSize,
                        boxs[i].x * boxSize + boxSize,
                        boxs[i].y * boxSize + boxSize,
                        boxPainnt);
            }
        }
    }

    /**
     * 生成方块
     */
    public void newBoxes() {
        //随机数生成方块
        Random random = new Random();
        boxType = random.nextInt(7);

        switch (boxType) {
            //田
            case 0:
                //初始化方塊
                boxPainnt.setColor(context.getResources().getColor(R.color.boxRed));
                boxs = new Point[]{new Point(4, 0), new Point(5, 0), new Point(4, 1), new Point(5, 1)};
                break;

            //L
            case 1:
                //初始化方塊
                boxPainnt.setColor(context.getResources().getColor(R.color.boxOrange));
                boxs = new Point[]{new Point(4, 1), new Point(5, 0), new Point(3, 1), new Point(5, 1)};
                break;

            //J
            case 2:
                //初始化方塊
                boxPainnt.setColor(context.getResources().getColor(R.color.boxYellow));
                boxs = new Point[]{new Point(4, 1), new Point(3, 0), new Point(3, 1), new Point(5, 1)};
                break;

            //s
            case 3:
                boxPainnt.setColor(context.getResources().getColor(R.color.boxGreen));
                boxs = new Point[]{new Point(4, 1), new Point(3, 1), new Point(4, 0), new Point(5, 0)};
                break;

            //z
            case 4:
                boxPainnt.setColor(context.getResources().getColor(R.color.boxBlue));
                boxs = new Point[]{new Point(4, 1), new Point(3, 0), new Point(4, 0), new Point(5, 1)};
                break;

            //I
            case 5:
                boxPainnt.setColor(context.getResources().getColor(R.color.boxDarkBlue));
                boxs = new Point[]{new Point(4, 1), new Point(4, 0), new Point(4, 2), new Point(4, 3)};
                break;

            //倒T
            case 6:
                boxPainnt.setColor(context.getResources().getColor(R.color.boxPurple));
                boxs = new Point[]{new Point(4, 1), new Point(3, 1), new Point(5, 1), new Point(4, 0)};
                break;
        }
    }


    /**
     * 移动
     *
     * @param x        横轴方向
     * @param y        纵轴方向
     * @param mapModel
     * @return 移动成功否
     */
    public boolean move(int x, int y, MapModel mapModel) {
        Log.e("before", boxs[0].x + ":" + boxs[0].y);
        //方块数组，每一个方块的坐标都要加上便宜的量
        for (int i = 0; i < boxs.length; i++) {
            if (checkBoundary(boxs[i].x + x, boxs[i].y + y, mapModel)) {
                return false;
            }
        }
        //坐标变化
        for (int i = 0; i < boxs.length; i++) {
            boxs[i].x += x;
            boxs[i].y += y;
        }
        Log.e("after", boxs[0].x + ":" + boxs[0].y);
        return true;
    }

    /**
     * 旋转方法
     *
     * @param mapModel
     * @return 是否能够旋转成功
     */
    public boolean rotate(MapModel mapModel) {
        //方块无法旋转
        if (boxType == 0) {
            return false;
        }
        //判断边界值
        for (int i = 0; i < boxs.length; i++) {
            //旋转算法(笛卡尔公式)
            int checkX = -boxs[i].y + boxs[0].y + boxs[0].x;
            int checkY = boxs[i].x - boxs[0].x + boxs[0].y;
            //碰到边界时无法旋转
            if (checkBoundary(checkX, checkY, mapModel)) {
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
    public boolean checkBoundary(int x, int y, MapModel mapModel) {
        //如果這個邊界和地圖上面的點是true，那麽就是代表出街了。
        return (x < 0 || y < 0 || x >= mapModel.maps.length || y >= mapModel.maps[0].length || mapModel.maps[x][y]);
    }
}
