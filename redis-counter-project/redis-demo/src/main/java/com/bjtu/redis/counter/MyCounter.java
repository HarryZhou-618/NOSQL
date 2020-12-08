package com.bjtu.redis.counter;

public class MyCounter {    //counter类
    private String name;    //counter名称
    private String key;     //counter的key值
    private int value;      //counter的value值
    private String type;    //counter的种类（num, zset, string, list）
    private String beginTime;   //开始时间（用于freq计数）
    private String endTime;     //结束时间（用于freq计数）
    public MyCounter(){

    }

    public MyCounter(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
