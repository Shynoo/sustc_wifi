package cn.sustc.edu.wifi;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
public enum Config{
    WIFI("wifi.properties");
    private volatile Properties props = new Properties();
    Config(String path){
        try{
            InputStream inputStream = Thread.currentThread()
                    .getContextClassLoader().getResourceAsStream(path);
            if (inputStream == null){
                System.out.printf("[Warning] cannot find '%s'.\n", path);
                return;
            }
            props.load(inputStream);
            inputStream.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    public String get(String key){
        String property = props.getProperty(key);
        return property != null ? property.trim() : null;
    }
}
