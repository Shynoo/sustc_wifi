package cn.sustc.edu.wifi;

import java.util.concurrent.TimeUnit;

import static cn.sustc.edu.wifi.LogIn.isNetWorking;
import static cn.sustc.edu.wifi.LogIn.username;
import static cn.sustc.edu.wifi.LogIn.password;
/**
 * Created by Darain on 2016/10/19.
 */
public class Main {
    private static final String mo="**********";
    public static void main(String[] args) {
        LogIn.username=Config.WIFI.get("username");
        LogIn.password=Config.WIFI.get("password");
        if (username.length()==8&&username.compareTo("11110000")>0&&password.length()>=6) {
            System.out.println(mo+"SUSTC_WIFI v0.1.2_beta "+mo);
            System.out.println(TimeLog.timeInfo() + "Starting...");
            long delay;
            try {
                delay=Long.valueOf(Config.WIFI.get("delay"));
            }catch (Exception e){
                System.out.println(e.getMessage());
                delay=2000;
            }
            while (true) {
                try {
                    isNetWorking();
                    TimeUnit.MILLISECONDS.sleep(delay);
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
