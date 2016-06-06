package com.gonali.crawlerTask.deamon;

import com.gonali.crawlerTask.htmlServer.HtmlServer;

/**
 * Created by TianyuanPan on 6/6/16.
 */
public class HtmlDeamon implements Runnable {

    private static volatile HtmlDeamon htmlDeamon;
    private HtmlServer htmlServer;


    private HtmlDeamon() {

        htmlServer = HtmlServer.createHtmlServer();
    }

    @Override
    public void run() {

        htmlServer.serverStart();
    }

    public static HtmlDeamon createDeamon() {

        if (htmlDeamon == null)
            htmlDeamon = new HtmlDeamon();
        return htmlDeamon;
    }
}
