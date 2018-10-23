package com.dizsun.util;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.DataInputStream;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    Date date;
    SimpleDateFormat sdf;
    String time;
    boolean state;
    private String host;
    private static DateUtil dateUtil;

    private DateUtil() {
        host="127.0.0.1";
    }

    public static DateUtil newDataUtil(){
        if(dateUtil==null){
            dateUtil=new DateUtil();
        }
        return dateUtil;
    }

    public int getCurrentMinute(){
        sdf = new SimpleDateFormat("mm");
        date=new Date();
        return Integer.valueOf(sdf.format(date));
    }

    public int getCurrentSecond(){
        sdf = new SimpleDateFormat("ss");
        date=new Date();
        return Integer.valueOf(sdf.format(date));
    }

    public String getTimeFromRC(){
        time="-1";
        try {
            Socket socket=new Socket(host,9090);
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            time=""+dis.readLong();
            socket.close();
            return time;
        }catch (Exception e){
            e.printStackTrace();
        }
        return time;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
