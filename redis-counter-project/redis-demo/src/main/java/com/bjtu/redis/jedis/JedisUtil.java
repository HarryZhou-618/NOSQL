package com.bjtu.redis.jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Tuple;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

public class JedisUtil {    //封装jedis工具类

    public static void incrNum(String key, int value){  //计数增加
        Jedis jedis = null;
        try {
            jedis = JedisInstance.getInstance().getResource();  //获得jedis资源
            jedis.incrBy(key,Long.parseLong(String.valueOf(value)));    //调用incrBy实现计数增加
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (jedis != null) {
                jedis.close();  //关闭jedispool
            }
        }
    }

    public static String getNum(String key){    //读取计数
        Jedis jedis = null;
        String num = null;

        try {
            jedis = JedisInstance.getInstance().getResource();  //获得jedis资源
            num = jedis.get(key);   //调用get方法通过key获得value
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (jedis != null) {
                jedis.close();  //关闭jedispool
            }
        }
        return num;
    }

    public static void setFreq(String key, int score, String member){   //记录时间信息

        Jedis jedis = null;
        try {
            jedis = JedisInstance.getInstance().getResource();  //获得jedis资源
            jedis.zadd(key,score,member);   //调用zadd方法通过zset记录时间信息
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (jedis != null) {
                jedis.close();  //关闭jedispool
            }
        }
    }

    public static long getFreq(String beginTime, String endTime, String key){   //统计freq区间
        long count = 0;
        Jedis jedis = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            jedis = JedisInstance.getInstance().getResource();  //获得jedis资源

            Date bTime = simpleDateFormat.parse(beginTime);     //将string类型时间通过format转化为Date
            long bt = bTime.getTime()/1000;     //将开始时间转为秒数
            Date eTime = simpleDateFormat.parse(endTime);
            long et = eTime.getTime()/1000;     //将结束时间转为秒数

            return jedis.zcount(key,bt,et); //根据zset有序的特性，统计区间内个数

        }catch (Exception e){
            e.printStackTrace();
        }

        return count;
    }

    public static void setDesc(String key, String desc){    //设置description
        Jedis jedis = null;
        try {
            jedis = JedisInstance.getInstance().getResource();  //获得jedis资源
            jedis.set(key,desc);    //通过set方法设置string值
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (jedis != null) {
                jedis.close();  //关闭jedispool
            }
        }
    }

    public static String getDesc(String key){   //读取description
        Jedis jedis = null;
        String desc = null;
        try {
            jedis = JedisInstance.getInstance().getResource();  //获得jedis资源
            desc = jedis.get(key);  //通过get方法根据key值读取value
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (jedis != null) {
                jedis.close();  //关闭jedispool
            }
        }
        return desc;
    }

    public static void setLog(String key,String action){    //增加log
        Jedis jedis = null;
        try {
            jedis = JedisInstance.getInstance().getResource();  //获得jedis资源
            jedis.rpush(key,action);    //通过list的rpush方法写入列表
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (jedis != null) {
                jedis.close();  //关闭jedispool
            }
        }
    }

    public static String getLog(String key){    //加载log
        Jedis jedis = null;
        String log = null;
        try {
            jedis = JedisInstance.getInstance().getResource();  //获得jedis资源
            for(int i=0;i<jedis.llen(key);i++){ //通过lindex通过索引加载log
                log += jedis.lindex(key,i);     //将log拼接成字符串return
                log += "\n";
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (jedis != null) {
                jedis.close();  //关闭jedispool
            }
        }
        return log;
    }
}
