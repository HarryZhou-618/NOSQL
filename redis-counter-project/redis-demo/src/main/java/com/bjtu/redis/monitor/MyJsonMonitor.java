package com.bjtu.redis.monitor;

import com.bjtu.redis.jsonLoader.ActionJsonLoader;
import com.bjtu.redis.jsonLoader.CounterJsonLoader;
import com.bjtu.redis.lock.MyLock;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;



public class MyJsonMonitor implements FileAlterationListener{   //文件修改监听类
    @Override
    public void onStart(FileAlterationObserver fileAlterationObserver) {

    }


    @Override
    public void onDirectoryCreate(File file) {

    }


    @Override
    public void onDirectoryChange(File file) {

    }


    @Override
    public void onDirectoryDelete(File file) {

    }


    @Override
    public void onFileCreate(File file) {

    }


    @Override
    public void onFileChange(File file) {

        MyLock.getWriteLock();  //加载写锁

        /*根据修改文件调用对应方法，重新加载json数据至内存中的map*/
        if (file.getName().equals("counter.json")){
            CounterJsonLoader.load();
        }else if (file.getName().equals("action.json")){
            ActionJsonLoader.load();
        }
        System.out.print("(" + file.getName() + " changed. Update successfully.) >>");
        MyLock.freeWriteLock(); //释放写锁

    }


    @Override
    public void onFileDelete(File file) {

    }


    @Override
    public void onStop(FileAlterationObserver fileAlterationObserver) {

    }
}
