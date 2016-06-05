package com.gonali.crawlerTask.deamon;

/**
 * Created by TianyuanPan on 6/5/16.
 */
public class MainDeamon {

    private static volatile MainDeamon mainDeamon;
    private HearbeatDeamon hearbeatDeamon;


    private MainDeamon(){

        hearbeatDeamon = HearbeatDeamon.create();
    }


    public static MainDeamon create() {

        if (mainDeamon == null)
            mainDeamon = new MainDeamon();
        return mainDeamon;

    }

    public void appStart() {


        new Thread(this.hearbeatDeamon).start();

        System.out.println("Hi! MainDeamon");
    }
}
