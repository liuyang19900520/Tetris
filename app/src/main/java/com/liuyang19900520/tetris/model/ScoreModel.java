package com.liuyang19900520.tetris.model;

import android.content.Context;

import com.liuyang19900520.tetris.Config;
import com.liuyang19900520.tetris.utils.SharedPreferencesUtils;

/**
 * 分数
 * Created by LiuYang on 2017/9/14.
 */

public class ScoreModel {
    public int score = 0;

    public int highScore;

    private Context context;

    public ScoreModel(Context context) {
        this.context = context;

    }

    /***
     * 加分逻辑
     * @param lines
     * @return
     */
    public int score(int lines) {
        int thisScore = 0;

        switch (lines) {
            case 0:
                thisScore = 0;
                break;
            case 1:
                thisScore = 1;
                break;
            case 2:
                thisScore = 3;
                break;
            case 3:
                thisScore = 6;
                break;
            case 4:
                thisScore = 10;
                break;
        }
        return score + thisScore;
    }

    /**
     * 更新最高分
     */

    public void updateHighScore() {
        if (highScore == 0) {
            highScore = (int) SharedPreferencesUtils.get(context, "scoreMax", 0);
        }
        //取得最大值
        if (score > highScore) {
            //存入最大值
            highScore = score;
            SharedPreferencesUtils.put(context, "scoreMax", highScore);
        }


    }
}
