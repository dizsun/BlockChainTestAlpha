package com.dizsun.block;

import com.dizsun.util.*;
import com.dizsun.service.*;

import java.io.*;
import java.util.ArrayList;

public class Main {
    private static String Drivder = "org.sqlite.JDBC";
    static int N = 4;

    public static void main(String[] args) {

        TimeCenter timeCenter = new TimeCenter(9090);
        timeCenter.start();

        File dbFileFolder = new File("./db");
        if (!dbFileFolder.exists()) {
            System.out.println("[Main]db文件夹不存在!");
            if (dbFileFolder.mkdir()) {
                System.out.println("[Main]db文件夹创建成功!");
            } else {
                System.out.println("[Main]db文件夹创建失败!");
                return;
            }
        }
        File dbFile = new File("./db/blocks.db");
        if (!dbFile.exists()) {
            System.out.println("[Main]db文件不存在!");
            try {
                SQLUtil sqlUtil = new SQLUtil();
                sqlUtil.initBlocks(null);
                System.out.println("[Main]db文件创建成功!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (args != null && args.length == 1) {
            N = Integer.valueOf(args[0]);
            if (N <= 2) return;
        } else {
            System.out.println("传入参数错误，默认4个节点");
            System.out.println("usage: java -jar BlockChainTest.jar 4");
        }

        Node node0 = new Node(0, 10000, 20000);
        Thread thread0 = new Thread(new Runnable() {
            @Override
            public void run() {
                node0.start(new ICallback() {
                    @Override
                    public void callback() {
                        for (int i = 1; i <= N - 1; i++) {
                            Node node = new Node(i, 10000 + i, 20000 + i);
                            Thread thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    node.start(new ICallback() {
                                        @Override
                                        public void callback() {
                                            System.out.println("[Main][nonce " + node.getNonce() + "] begin connect " + node.getP2pPort());
                                            node.conntectTo(20000);
                                        }
                                    });
                                }
                            });
                            thread.start();
                        }
                    }
                });
            }
        });
        thread0.start();

    }

}