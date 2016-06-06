package com.gonali.crawlerTask.deamon;

import com.gonali.crawlerTask.scheduler.rulers.Ruler;
import com.gonali.crawlerTask.scheduler.rulers.SimpleLongTimeFirstRuler;

/**
 * Created by TianyuanPan on 6/5/16.
 */
public class AppMainEntry {

    private static MainDeamon mainDeamon;


    static {

        mainDeamon = MainDeamon.create();
    }


    public static void main(String[] args) {

        Ruler ruler;
        ruler = new SimpleLongTimeFirstRuler();
        mainDeamon.appStart(ruler);

    }
}
