package com.gonali.crawlerTask.deamon;

import com.gonali.crawlerTask.scheduler.TaskScheduler;
import com.gonali.crawlerTask.scheduler.rulers.Ruler;

/**
 * Created by TianyuanPan on 6/5/16.
 */
public class TaskDeamon implements Runnable{

    private TaskScheduler scheduler;

    public TaskDeamon(){

        scheduler = TaskScheduler.getTaskScheduler();
    }


    public TaskDeamon setRuler(Ruler ruler){

        scheduler.registerRuler(ruler);
        return this;
    }

    @Override
    public void run() {

        scheduler.schedulerStart();
    }

}
