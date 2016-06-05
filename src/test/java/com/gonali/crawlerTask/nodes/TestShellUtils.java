package com.gonali.crawlerTask.nodes;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by TianyuanPan on 6/4/16.
 */
public class TestShellUtils {

    static ShellUtils shell;

    public static void main(String[] args) {

        shell = ShellUtils.getShellUtils("TianyuanPan", "jkl", "127.0.0.1", 22);
//        shell = ShellUtils.getShellUtils("crawler", "crawler@#AD900", "121.40.76.156", 22);

        BufferedReader in;
        String cmd = "";
        String s = "";
        in = new BufferedReader(new InputStreamReader(System.in));
        InputStream inputStream;


        try {
/*

            shell.openPerformShell();
            while (true) {

                System.out.println("--------------------------");
                System.out.println("$ input << ");


                cmd = in.readLine();


                inputStream = shell.performShell(cmd);

                int testTimes = 10;

                while (testTimes > 0) {

                    int len = inputStream.available();
                    if (len > 0) {
                        byte[] data = new byte[len];
                        inputStream.read(data);

                        s += new String(data, 0, len, "utf-8");
                    } else {
                        try {
                            Thread.sleep(200);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        --testTimes;
                    }

                }


                System.out.println(s);
                s = "";


                if (cmd.equals("exit"))
                    break;
            }

            in.close();

            shell.closePerformShell();

*/


            while (true) {

                System.out.println("--------------------------");
                System.out.printf("$ input >> ");


                cmd = in.readLine();


                s = shell.doExecuteShell(cmd);


                System.out.println(s);


                if (cmd.equals("exit"))
                    break;
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }
}
