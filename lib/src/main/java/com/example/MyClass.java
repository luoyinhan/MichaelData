package com.example;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 一般测试 类
 */
public class MyClass {

    public static void main(String[] args) {
//      30 * 24 * 60 * 60 * 1000
        String end_time = getDate("yyyy-MM-dd", System.currentTimeMillis() + 30l * 24 * 60 * 60 * 1000);//添加 后30天数据
//      System.out.println(end_time + "  " + Integer.MIN_VALUE);
        int a = 0x55;
        System.out.println("a=" + intToByte4(a)[0]);
        int i = 3;
        System.out.println(i++ + "1");
        float fuel_cost = 0;
        float fuel_left = 4.5f;
        float fuel_left1 = 5.6f;
        fuel_cost += (fuel_left - fuel_left1);
        System.out.print(fuel_cost);
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
