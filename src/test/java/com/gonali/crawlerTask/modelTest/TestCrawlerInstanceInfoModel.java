package com.gonali.crawlerTask.modelTest;

import com.gonali.crawlerTask.model.TaskCrawlerInstanceInfoModel;
import com.gonali.crawlerTask.model.TaskStatus;

/**
 * Created by TianyuanPan on 6/3/16.
 */
public class TestCrawlerInstanceInfoModel {

    private static TaskCrawlerInstanceInfoModel model;


    public static void main(String[] args) {

        model = new TaskCrawlerInstanceInfoModel();

        model.addInstance("192.168.1.1", 4512, TaskStatus.UNCRAWL);
        model.addInstance("192.168.1.10", 104512, TaskStatus.CRAWLED);
        model.addInstance("192.168.1.100", 45102, TaskStatus.CRAWLING);

        System.out.println(model.getCrawlerInstanceInfoJsonString());

        model.removeInstance("192.168.1.100", 45102);

        System.out.println(model.getCrawlerInstanceInfoJsonString());

        model.setCrawlerInstanceInfo("[\n" +
                "  {\n" +
                "    \"host\": \"110.110.20.10\",\n" +
                "    \"pid\":45612,\n" +
                "    \"taskStatus\": \"CRAWLING\",\n" +
                "    \"heartbeat\": 142055551\n" +
                "  },\n" +
                "  {\n" +
                "    \"host\": \"110.110.20.20\",\n" +
                "    \"pid\":45612,\n" +
                "    \"taskStatus\": \"CRAWLING\",\n" +
                "    \"heartbeat\": 142055551\n" +
                "  },\n" +
                "  {\n" +
                "    \"host\": \"110.110.20.30\",\n" +
                "    \"pid\":45612,\n" +
                "    \"taskStatus\": \"CRAWLING\",\n" +
                "    \"heartbeat\": 142055551\n" +
                "  },\n" +
                "  {\n" +
                "    \"host\": \"110.110.20.40\",\n" +
                "    \"pid\":45612,\n" +
                "    \"taskStatus\": \"CRAWLING\",\n" +
                "    \"heartbeat\": 142055551\n" +
                "  }\n" +
                "]");
        System.out.println(model.getCrawlerInstanceInfoJsonString());
    }

}
