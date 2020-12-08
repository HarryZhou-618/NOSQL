package com.bjtu.redis.action;

public class ActionCounter {
    private String cname;

    public ActionCounter(){ //ActionCounter类，用于存储action需要使用的Counter信息

    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }
}
