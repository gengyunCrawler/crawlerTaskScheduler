package com.gonali.crawlerTask.scheduler.rulers;

import com.gonali.crawlerTask.model.TaskModel;
import com.gonali.crawlerTask.scheduler.TaskScheduler;

/**
 * Created by TianyuanPan on 6/6/16.
 */
public class SimpleLongTimeFirstRuler implements Ruler {

    private TaskModel currentTask;

    public SimpleLongTimeFirstRuler(){


    }

    @Override
    public TaskModel doSchedule(TaskScheduler scheduler) {
        return null;
    }


}
