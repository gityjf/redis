package com.yjf;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @program: redis
 * @author: yjf
 * @create: 2019-01-25 13:45
 **/
public class LockTest {

    public static ReentrantLock reenT = new ReentrantLock();//参数默认false，不公平锁
    private static ReentrantLock lock = new ReentrantLock(true); //公平锁  速度慢与不公平锁

    private static Integer count = 50;

    public static void tryLockTest()  {
        try{
            if (reenT.tryLock(1,TimeUnit.SECONDS)) {//如果已经被lock，则立即返回false不会等待，
                //达到忽略操作的效果 ,当执行1000线程时，有些未获得对象锁的线程，会自动跳过
                try {
                    //操作
                    System.out.println("aaaa" + Thread.currentThread().getName());
                } finally {
                    reenT.unlock();
                }
            }
        }catch (Exception e){
            System.out.println("获取锁失败!");
        }

    }
    public static void main(String[] args) throws Exception{
        for (int i = 0; i < 5; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (reenT.tryLock()){
                        reenT.lock();
                    }
                    System.out.println(Thread.currentThread().getName());
                    tryLockTest();
                }
            }).start();
        }


    }





}
