package com.gonali.crawlerTask.deamon;

/**
 * Created by TianyuanPan on 6/5/16.
 */
public class AppMainEntry {

    static MainDeamon mainDeamon;

    static {

        mainDeamon = MainDeamon.create();
    }


    public static void main(String[] args) {

        mainDeamon.appStart();

    }
}
