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
        heartbeatDeamon.setDaemon(true);
        System.out.println("HtmlDeamon Id : " + heartbeatDeamon.getId());
        heartbeatDeamon.start();
        new Thread(this.taskDeamon.setRuler(ruler)).start();
        Thread htmlDeamon = new Thread(this.htmlDeamon);
        htmlDeamon.setDaemon(true);
        System.out.println("HtmlDeamon Id : " + htmlDeamon.getId());
        htmlDeamon.start();


        System.out.println("Hi! MainDeamon");
    }
}
