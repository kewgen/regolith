package com.geargames.regolith;

import com.geargames.common.util.Lock;
import com.geargames.platform.util.JavaLock;

/**
 * @author Mikhail_Kutuzov
 *         created: 25.05.12  13:23
 */
public class ThreadTester {

    public static void main(String[] args) throws InterruptedException {
        test();
    }

    public static void test() throws InterruptedException {
        final Lock lock = new JavaLock();

        Thread thread1 = new Thread(new Runnable() {
            public void run() {
                lock.lock();
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            public void run() {
                long tmp1  =  System.currentTimeMillis();
                lock.lock();
                System.out.println("thread2 is going through the lock");
                System.out.println("thread2 stuck time = " + (System.currentTimeMillis() - tmp1));
            }
        });

        Thread thread3 = new Thread(new Runnable() {
                    public void run() {
                        System.out.println("thread3 is going to release the lock");
                        lock.release();
                    }
                });

        thread1.start();
        Thread.sleep(100);

        thread2.start();
        Thread.sleep(2000);
        thread3.start();

        thread2.join();
        thread3.join();

        lock.lock();

        System.out.println("and once more");
        thread2 = new Thread(new Runnable() {
                    public void run() {
                        long tmp1  =  System.currentTimeMillis();
                        lock.lock();
                        System.out.println("thread2 is going through the lock");
                        System.out.println("thread2 stuck time = " + (System.currentTimeMillis() - tmp1));
                    }
                });
        thread2.start();
        Thread.sleep(2000);
        thread3 = new Thread(new Runnable() {
                            public void run() {
                                System.out.println("thread3 is going to release the lock");
                                lock.release();
                            }
                        });
        thread3.start();

    }

}
