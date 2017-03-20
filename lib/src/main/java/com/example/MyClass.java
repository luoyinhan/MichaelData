package com.example;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyClass {

    public static void main(String[] args) {
//        30 * 24 * 60 * 60 * 1000
        String end_time = getDate("yyyy-MM-dd", System.currentTimeMillis() + 30l * 24 * 60 * 60 * 1000);//添加 后30天数据
//        System.out.println(end_time + "  " + Integer.MIN_VALUE);
        int a = 0x55;
        System.out.println("a=" + intToByte4(a)[0]);
        int i = 3;
        System.out.println(i++ + "1");

        for (int j = 1; j < 30; j++) {
            System.out.print(j % 2);
        }
        try {
            DatagramSocket socket=new DatagramSocket(8080);        } catch (SocketException e) {
            e.printStackTrace();
        }

    }

    public static String getDate(String style, long time) {
        if (style == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat(style);
        return sdf.format(new Date((time)));
    }

    /**
     * int整数转换为4字节的byte数组
     *
     * @param i 整数
     * @return byte数组
     */
    public static byte[] intToByte4(int i) {
        byte[] targets = new byte[1];
        targets[0] = (byte) (i & 0xFF);
        return targets;
    }

}
