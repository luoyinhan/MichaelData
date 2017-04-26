package com.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.lang.Thread.sleep;

/**
 * 描述：多线程 测试工具
 * 作者：Micheal
 * 修改时间：2017/3/13
 */

public class MultiThreadTest {
    private static boolean ready;
    private static int number;
    private static ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);//指定并行执行 最大 5个线程
    private static ExecutorService cachedThreadPool = Executors.newCachedThreadPool();//无限制线程  比较耗内存
    private static ExecutorService cachedThreadPool2 = Executors.newFixedThreadPool(5);// 固定线程池大小
    private static ExecutorService cachedThreadPool3 = Executors.newSingleThreadExecutor();// 固定线程池大小
    private static Object obj = new Object();
    public static boolean wait = false;

    public static void main(String[] args) throws InterruptedException {
//        scheduledThreadPool.execute(new Runnable() {
//            int a = 0;
//
//            @Override
//            public void run() {
//                while (true) {
//                    System.out.println("ThreadId 1=    " + Thread.currentThread().getId() + " a=" + a);
//                    if (a == 10) {
//                        break;
//                    } else {
//                        a++;
//                    }
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//        MyThread1 thread = new MyThread1();
//        thread.start();

//        scheduledThreadPool.scheduleAtFixedRate(new Runnable() {
//            int a = 0;
//
//            @Override
//            public void run() {
//                if (a == 5) {
////                    scheduledThreadPool.shutdown();//完成已提交的任务后封闭办事，不再接管新任务
////                    scheduledThreadPool.shutdownNow();//停止所有正在履行的任务并封闭办事
////                    scheduledThreadPool.isTerminated();//是否所有任务都履行完毕了
//                }
//                a++;
//                System.out.println("ThreadId 2=    " + Thread.currentThread().getId() + " a=" + a);
//            }
//        }, 5000, 1000, TimeUnit.MILLISECONDS);
        synchronized (obj) {
            MyThread thread = new MyThread();
            thread.start();
            for (int i = 0; i < 5; i++) {
                sleep(1000);
                System.out.println(i);
            }
        }
    }

    static class TaskRunable implements Runnable {
        private String name = "";

        public TaskRunable(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            System.out.println("ThreadId =    " + Thread.currentThread().getId() + " name=" + name);
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 线程终止的方法一  exit 标记位   2  interrupt()  3 stop();
     */
    public static class MyThread extends Thread {

        @Override
        public void run() {
            int i = 0;
            while (true) {
                synchronized (obj) {
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                    if (i == 5) {
                        try {
                            obj.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println(i++ + "   thread id=" + Thread.currentThread().getId());
                }
            }
        }
    }

    /**
     * 线程终止的方法一
     */
    public static class MyThread1 extends Thread {
        public volatile boolean exit = false;   //volatile  同一时间只能一个线程修改  可见性

        @Override
        public void run() {
            int i = 0;
            while (true) {
                if (i == 5) {
                    break;
                }
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(i++);
            }
        }
    }

}
