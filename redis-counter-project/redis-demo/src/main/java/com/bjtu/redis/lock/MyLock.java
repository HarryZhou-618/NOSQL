package com.bjtu.redis.lock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MyLock {   //静态类，封装ReentrantReadWriteLock

    public static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public static void getReadLock(){
        lock.readLock().lock();
    }   //获得读锁

    public static void freeReadLock(){
        lock.readLock().unlock();
    }   //释放读锁

    public static void getWriteLock(){
        lock.writeLock().lock();
    }   //获得写锁

    public static void freeWriteLock(){
        lock.writeLock().unlock();
    }   //释放写锁
}
