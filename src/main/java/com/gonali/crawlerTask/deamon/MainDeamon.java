package com.gonali.crawlerTask.deamon;

import com.gonali.crawlerTask.scheduler.rulers.Ruler;

/**
 * Created by TianyuanPan on 6/5/16.
 */
public class MainDeamon {

    private static volatile MainDeamon mainDeamon;
    private HearbeatDeamon hearbeatDeamon;
    private TaskDeamon taskDeamon;
    private HtmlDeamon htmlDeamon;


    private MainDeamon(){

        hearbeatDeamon = HearbeatDeamon.createDeamon();
        taskDeamon = TaskDeamon.createDeamon();
        htmlDeamon = HtmlDeamon.createDeamon();
    }


    public static MainDeamon createDeamon() {

        if (mainDeamon == null)
            mainDeamon = new MainDeamon();
        return mainDeamon;

    }

    public void appStart(Ruler ruler) {


        new Thread(this.hearbeatDeamon).start();
//        new Thread(this.taskDeamon.setRuler(ruler)).start();
        new Thread(this.htmlDeamon).start();

        System.out.println("Hi! MainDeamon");
    }
}
