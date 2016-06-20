package com.gonali.crawlerTask.rulers;

import com.gonali.crawlerTask.scheduler.CurrentTask;
import com.gonali.crawlerTask.scheduler.TaskScheduler;
import com.gonali.crawlerTask.scheduler.rulers.Ruler;
import com.gonali.crawlerTask.scheduler.rulers.RulerBase;

/**
 * Created by TianyuanPan on 6/20/16.
 */
public class WholeSiteSimpleTimeFirstRuler extends RulerBase{


    @Override
    public void writeBack(TaskScheduler scheduler) {

    }

    @Override
    public CurrentTask doSchedule(TaskScheduler scheduler) {
        return null;
    }
}
