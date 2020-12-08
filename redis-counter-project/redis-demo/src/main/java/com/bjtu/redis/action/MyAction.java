package com.bjtu.redis.action;

import java.util.ArrayList;

public class MyAction { //action类
    private String name;    //action名称
    private String operation = null;    //action操作类型（‘read’or‘write’）
    private int index;  //action索引号
    private ArrayList<ActionCounter> counter = null;    //action使用的counter列表

    public MyAction(){

    }

    public MyAction(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public ArrayList<ActionCounter> getCounter() {
        return counter;
    }

    public void setCounter(ArrayList<ActionCounter> counter) {
        this.counter = counter;
    }
}
