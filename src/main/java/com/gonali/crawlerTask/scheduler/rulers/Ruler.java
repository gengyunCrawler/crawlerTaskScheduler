package com.gonali.crawlerTask.scheduler.rulers;

import com.gonali.crawlerTask.model.TaskModel;
import com.gonali.crawlerTask.scheduler.TaskScheduler;

/**
 * Created by TianyuanPan on 6/6/16.
 */
public interface Ruler {

    public TaskModel  doSchedule(TaskScheduler scheduler);
}
