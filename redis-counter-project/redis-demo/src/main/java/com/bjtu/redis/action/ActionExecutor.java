package com.bjtu.redis.action;

import com.bjtu.redis.action.ActionCounter;
import com.bjtu.redis.action.MyAction;
import com.bjtu.redis.counter.MyCounter;
import com.bjtu.redis.jedis.JedisUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ActionExecutor {   //action执行类

    private MyAction myAction = null;   //action对象
    private HashMap myCounterMap;   //加载的countermap
    private ArrayList<MyCounter> counterList;   //action使用的counter列表
    private int incrNum;    //每次增加的数量（可通过json文件设置）

    private String desc;    //用于获取用户输入description



    public ActionExecutor(){

    }

    public ActionExecutor(MyAction myAction, HashMap myCounterMap){ //初始化
        this.incrNum=0;
        this.myAction = myAction;
        this.myCounterMap = myCounterMap;
        this.counterList = new ArrayList<MyCounter>();
    }

    public void execute(){
        //加载执行action所需counter
        for (ActionCounter ac:myAction.getCounter()){   //通过action的counter执行列表从map中加载对应counter
            MyCounter myCounter = (MyCounter) myCounterMap.get(ac.getCname());
            counterList.add(myCounter);
        }

        switch (this.myAction.getOperation()){  //根据操作调用封装jedisUtil类方法实现操作
            case "read":    //读操作
                for(MyCounter myCounter:counterList)
                {
                    if (myCounter.getType().equals("num")){ //读取num
                        System.out.println("The current count is :" + JedisUtil.getNum(myCounter.getKey())); //调用jedis工具类读取计数
                        System.out.println();
                    }else if(myCounter.getType().equals("zset")){   //读取zset，实现freq统计
                        long ans = JedisUtil.getFreq(myCounter.getBeginTime(),myCounter.getEndTime(),myCounter.getKey());
                        System.out.println("The count number between " + myCounter.getBeginTime() + " and "
                                + myCounter.getEndTime() + " is: "+ ans);
                        System.out.println();
                    }else if(myCounter.getType().equals("string")){ //读取string，加载description
                        System.out.println("The description is : "+JedisUtil.getDesc(myCounter.getKey()));
                        System.out.println();
                    }else if (myCounter.getType().equals("list")){  //读取list，加载write action log

                        System.out.println("Data Write Action Log: ");
                        System.out.println(JedisUtil.getLog(myCounter.getKey()).replaceAll("null",""));
                        System.out.println();
                    }
                }
                break;
            case "write":   //写操作
                for(MyCounter myCounter:counterList)
                {
                    if (myCounter.getType().equals("num")){ //写入num
                        incrNum = myCounter.getValue();
                        JedisUtil.incrNum(myCounter.getKey(),myCounter.getValue());//调用jedis工具类增加计数
                        System.out.println("The count successfully increased.");
                        System.out.println();
                    }else if(myCounter.getType().equals("zset")){   //写入zset，记录时间信息
                        Date date = new Date();
                        long nowTime = date.getTime()/1000;
                        Integer nowTimeInt = new Long(nowTime).intValue();

                        for (int i = 0; i < incrNum ; i++){ //根据每次incrnum的个数写入相同数量的时间信息
                            JedisUtil.setFreq(myCounter.getKey(),nowTimeInt,String.valueOf(System.currentTimeMillis()+ i ));
                        }
                    }else if (myCounter.getType().equals("string")){    //修改description信息
                        JedisUtil.setDesc(myCounter.getKey(),this.getDesc());
                        System.out.println("The desc successfully modified.");
                        System.out.println();
                    }else if(myCounter.getType().equals("list")){   //写入log
                        Date date = new Date();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String log = simpleDateFormat.format(date);
                        log += " ";
                        log += this.myAction.getName();
                        JedisUtil.setLog(myCounter.getKey(),log);
                    }
                }
                break;
            default:
                break;

        }
    }

    public MyAction getMyAction() {
        return myAction;
    }

    public void setMyAction(MyAction myAction) {
        this.myAction = myAction;
    }

    public HashMap getMyCounterMap() {
        return myCounterMap;
    }

    public void setMyCounterMap(HashMap myCounterMap) {
        this.myCounterMap = myCounterMap;
    }

    public ArrayList<MyCounter> getCounterList() {
        return counterList;
    }

    public void setCounterList(ArrayList<MyCounter> counterList) {
        this.counterList = counterList;
    }

    public int getIncrNum() {
        return incrNum;
    }

    public void setIncrNum(int incrNum) {
        this.incrNum = incrNum;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
