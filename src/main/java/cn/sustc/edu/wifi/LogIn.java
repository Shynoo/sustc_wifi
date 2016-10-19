package cn.sustc.edu.wifi;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("deprecated")
public final class LogIn {
    static String username;
    static String password;
    private static CloseableHttpClient httpClient = HttpClients.createDefault();
    private static String location=null;
    private static final String mo="**********";

    private static volatile boolean hasConnected=false;

    public synchronized static void setDefaultHeader(HttpRequestBase httpGet){
        HttpParams params = new BasicHttpParams();
        params.setParameter("http.protocol.handle-redirects", false);
        httpGet.setParams(params);
    }

    public synchronized static boolean isNetWorking() {
        try {
            HttpGet httpget = new HttpGet("http://www.baidu.com");
            setDefaultHeader(httpget);
            CloseableHttpResponse response=null;
            try {
                response= httpClient.execute(httpget);
                if (response.getStatusLine().getStatusCode()==200){
                    if (hasConnected){

                    }
                    else {
                        System.out.println(time() + "Connected");
                        hasConnected=true;
                    }
                    return true;
                }
                else{
                    hasConnected=false;
                    if (response.getFirstHeader("Location")!=null){
                        location=response.getFirstHeader("Location").getValue();
                        if (location.contains("baidu.com")){
                            System.out.println(time()+"Connected");
                        }
                        else
                            if (location.contains("http://enet.10000.gd.cn")){
                                System.out.println(time()+"LogIn...");
                                logIn();
                            }
                            else {
                                System.out.println(time()+"Not in Students' Network!");
                            }
                    }
                    else {
                        System.out.println(time()+"Not in Students' Network!");
                    }
                }
            } catch (UnknownHostException e) {
                System.out.println(time()+"No Networking!");
            }
            catch (SocketException se){
                System.out.println(time()+"Networking Error!");
            }
            finally {
                if (response!=null)
                    response.close();
                }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
    public static HttpGet newGetRequest(String url){
        HttpGet httpGet=new HttpGet(url);
        return httpGet;
    }
    private static void logIn(){
        logIn(username,password.toCharArray());
    }

//    public static void post(){
//        location="http://172.18.1.77/loginAction.do";
//        HttpPost httppost = new HttpPost(location);
//        httppost.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9," +
//            "image/webp,*/*;q=0.8");
//        httppost.setHeader("Accept-Encoding", "gzip, deflate, sdch");
//        httppost.setHeader("Connection", "keep-alive");
//        httppost.setHeader("Upgrade-Insecure-Requests", "1");
//        httppost.setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.86 Safari/537.36");
//        httppost.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
//        httppost.setHeader("DNT", "1");
//        httppost.setHeader("Cookie","JSESSIONID=ceb5NT92-THQudLGAu9ov");
//        httppost.setHeader("Refere","http://172.18.1.77/loginAction.do");
//        System.out.println("executing request " + httppost.getURI());
//        try {
//            CloseableHttpResponse response = httpClient.execute(httppost);
//            System.out.println(text(response));
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//    }
//
    public synchronized static void logIn(String username,char[] password){
        HttpGet httpGet=newGetRequest(location);
        String data;
        try {
            data=text(httpClient.execute(httpGet));
            String s1="action=\"(.*?)\"";
            String s2="<input type=\"hidden\" name=\"lt\" .*?value=\"(.*?)\"";
            String s3="<input type=\"hidden\" name=\"execution\" .*?value=\"(.*?)\"";
            String s4="jsessionid=(.*?)type=";
            Pattern pattAction = Pattern.compile(s1);
            Pattern pattLt = Pattern.compile(s2);
            Pattern pattExec = Pattern.compile(s3);
            Pattern pattJsession=Pattern.compile(s4);
            Matcher matcherAction = pattAction.matcher(data);
            Matcher matcherLt = pattLt.matcher(data);
            Matcher matcherJsession=pattJsession.matcher(data);
            Matcher matcherExec = pattExec.matcher(data);
            if (matcherAction.find()&&matcherLt.find()&&matcherExec.find()&&matcherJsession.find()){
                String action=matcherAction.group(0);
                String lt= matcherLt.group(0);
                String execution= matcherExec.group(0);
                action=action.substring(8,action.length()-1);
                System.out.println("action:"+action);
                lt=lt.substring(38,lt.length()-1);
                execution=execution.substring(45,execution.length()-1);

                HttpPost httpPost = new HttpPost("http://weblogin.sustc.edu.cn"+action);
                List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
                list.add(new BasicNameValuePair("username",username));
                list.add(new BasicNameValuePair("password", new String(password)));
                list.add(new BasicNameValuePair("lt",lt));
                list.add(new BasicNameValuePair("execution",execution));
                list.add(new BasicNameValuePair("_eventId","submit"));
                list.add(new BasicNameValuePair("submit","LOGIN"));
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list);
                httpPost.setEntity(entity);

                System.out.println("~~~~~~~~~executing request~~~~~~~~~~" + httpPost.getURI());
                httpClient.execute(httpPost);
            }
            else {
                System.err.println("\nError");
//                System.err.println(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String text(CloseableHttpResponse response){
        StringBuffer sb=new StringBuffer();
        try {
            BufferedReader br=new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String d;
            while ((d=br.readLine())!=null){
                sb.append(d+"\n");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String time(){
        return TimeLog.timeInfo();
    }




}
