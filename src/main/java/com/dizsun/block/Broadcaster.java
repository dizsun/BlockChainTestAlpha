package com.dizsun.block;

import com.dizsun.util.DateUtil;
import com.dizsun.util.ISubscriber;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 广播时间变化事件
 */
public class Broadcaster {
    private Timer timer;
    private ArrayList<ISubscriber> subscribers;
    private DateUtil dateUtil;
//    private int sec=0;
//    private int min=0;

    /**
     * 假设允许的时间最大误差为每小时1s，那么1=cumulativeDrift=3600*rateOfDrift*Drift
     * 所以rateOfDrift=1/(3600*Drift)，令rateOfDrift=0.01，则Drift=28ms
     */
    private float rateOfDrift;
    private int cumulativeDrift;

    private  int Drift=28;//1000ms

    public Broadcaster() {
        timer = new Timer();
        subscribers = new ArrayList<>();
        dateUtil = DateUtil.newDataUtil();
        rateOfDrift=0.01f;
        cumulativeDrift=0;
    }

    public void broadcast() {
//        sec=dateUtil.getCurrentSecond();
//        min=dateUtil.getCurrentMinute();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
//                if(dateUtil.getCurrentMinute()%5==1){
//                    for (ISubscriber s : subscribers) {
//                        s.doPerHour45();
//                    }
//                }
//                else if(dateUtil.getCurrentMinute()%5==2){
//                    for (ISubscriber s : subscribers) {
//                        s.doPerHour59();
//                    }
//                }
//                else if(dateUtil.getCurrentMinute()%5==3){
//                    for (ISubscriber s : subscribers) {
//                        s.doPerHour00();
//                    }
//                }
//                else if(dateUtil.getCurrentMinute()%5==4){
//                    for (ISubscriber s : subscribers) {
//                        s.doPerHour01();
//                    }
//                }
                switch (dateUtil.getCurrentMinute()%5) {
                    case 3:
                        for (ISubscriber s : subscribers) {
                            s.doPerHour00();
                        }
                        break;
                    case 4:
                        for (ISubscriber s : subscribers) {
                            s.doPerHour01();
                        }
                        break;
                    case 1:
                        for (ISubscriber s : subscribers) {
                            s.doPerHour45();
                        }
                        break;
                    case 2:
                        for (ISubscriber s : subscribers) {
                            s.doPerHour59();
                        }
                        break;
                    default:
                        break;
                }
//                switch (dateUtil.getCurrentMinute()) {
//                    case 0:
//                    case 20:
//                    case 40:
//                        for (ISubscriber s : subscribers) {
//                            s.doPerHour00();
//                        }
//                        break;
//                    case 5:
//                    case 25:
//                    case 45:
//                        for (ISubscriber s : subscribers) {
//                            s.doPerHour01();
//                        }
//                        break;
//                    case 10:
//                    case 30:
//                    case 50:
//                        for (ISubscriber s : subscribers) {
//                            s.doPerHour45();
//                        }
//                        break;
//                    case 15:
//                    case 35:
//                    case 55:
//                        for (ISubscriber s : subscribers) {
//                            s.doPerHour59();
//                        }
//                        break;
//                    default:
//                        break;
//                }

//                if(Math.random()<rateOfDrift)
//                    cumulativeDrift+=Drift;
//                if(dateUtil.getCurrentMinute()==0&&dateUtil.getCurrentSecond()==0){
//                    Drift=0-Drift;
//                }

            }
        }, 1, 60*1000);
    }

    public void subscribe(ISubscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void unSubscribe(ISubscriber subscriber) {
        subscribers.remove(subscriber);
    }

    public int getCumulativeDrift() {
        return cumulativeDrift;
    }
}
