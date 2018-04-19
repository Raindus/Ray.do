package com.raindus.raydo.tomato;

import java.util.Date;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by Raindus on 2018/4/13.
 */

@Entity
public class TomatoEntity {

    @Id
    public long id;

    /**
     * 开始时间
     */
    public long startTime;

    /**
     * 结束时间 与 开始时间组成工作时段
     */
    public long endTime;

    /**
     * 番茄钟时长，单位min
     */
    public int tomatoTime;

    /**
     * 短休息时长
     */
    public int shortRestTime;

    /**
     * 长休息时长
     */
    public int longRestTime;

    /**
     * 长休息间隔番茄个数
     */
    public int longRestIntervalNum;

    /**
     * 收获番茄个数
     */
    public int tomatoNum;

    public TomatoEntity() {

    }

    /**
     * @param t 番茄钟时长
     * @param s 短休息时长
     * @param l 长休息时长
     * @param i 长休息间隔
     */
    public TomatoEntity(int t, int s, int l, int i) {
        tomatoTime = t;
        shortRestTime = s;
        longRestTime = l;
        longRestIntervalNum = i;
        tomatoNum = 0;
        startTime = new Date().getTime();
    }

}
