package com.liuyang19900520.tetris.model;

/**
 * 分数
 * Created by LiuYang on 2017/9/14.
 */

public class ScoreModel {
    public int score = 0;

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
}
