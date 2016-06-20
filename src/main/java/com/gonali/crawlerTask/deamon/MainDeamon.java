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


    private MainDeamon() {

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


        Thread heartbeatDeamon = new Thread(this.hearbeatDeamon);
        Thread taskDeamon = new Thread(this.taskDeamon.setRuler(ruler));
        Thread htmlDeamon = new Thread(this.htmlDeamon);

/*        heartbeatDeamon.setDaemon(true);
        taskDeamon.setDaemon(true);
        htmlDeamon.setDaemon(true);*/

        heartbeatDeamon.start();
        taskDeamon.start();
        htmlDeamon.start();


        System.out.println("HtmlDeamon Id : " + heartbeatDeamon.getId());
        System.out.println("HtmlDeamon Id : " + htmlDeamon.getId());
        System.out.println("Hi! MainDeamon");
    }
}
