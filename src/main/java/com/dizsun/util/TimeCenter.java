package com.dizsun.util;

import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class TimeCenter {
    private int port=9090;

    public TimeCenter(int port) {
        this.port = port;
    }

    public void start(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ServerSocket ss = new ServerSocket(port);
                    System.out.println("Time Center:开始监听!");
                    while (true) {
                        Socket s = ss.accept();
                        System.out.println("Time Center:有一个用户进入!");
                        long date=new Date().getTime();
                        DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                        dos.writeLong(date);
                        dos.flush();
                        s.close();
                        System.out.println("Time Center:有一个用户离开!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
