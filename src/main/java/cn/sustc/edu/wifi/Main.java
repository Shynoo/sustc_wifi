package cn.sustc.edu.wifi;

import java.util.concurrent.TimeUnit;

import static cn.sustc.edu.wifi.LogIn.isNetWorking;

/**
 * Created by Darain on 2016/10/19.
 */
public class Main {
    private static String username;
    private static String password;
    private static final String mo="**********";
    public static void main(String[] args) {
        if (args.length==2&&args[0].length()==8&&args[0].compareTo("11110000")>0&&args[1].length()>=6) {
            username=args[0];
            password=args[1];
            System.out.println(mo+"SUSTC_WIFI v0.1.2_beta "+mo);
            System.out.println(TimeLog.timeInfo() + "Starting...");
            while (true) {
                try {
                    isNetWorking();
                    TimeUnit.MILLISECONDS.sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            System.out.println("请输入你的学号和密码,我们绝不会以任何方式记录你的密码!");
            System.out.println("一个合法的输入示例为: java -jar C:\\\\sustc_wifi.jar 11310001 qwer1234");
        }
    }
}
