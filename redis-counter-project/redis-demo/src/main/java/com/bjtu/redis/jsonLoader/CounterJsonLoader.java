package com.bjtu.redis.jsonLoader;

import ch.qos.logback.core.util.FileUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjtu.redis.counter.MyCounter;
import com.bjtu.redis.jedis.JedisUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class CounterJsonLoader {    //counter.json加载类

    private static HashMap myCounterMap = new HashMap();    //countermap，用于将counter.json数据存储至内存

    public static void load(){
        myCounterMap .clear();
        try {
            String content = FileUtils.readFileToString(new File("src/main/resources/counter.json"), StandardCharsets.UTF_8);

            JSONObject jsonObject = JSON.parseObject(content);
            JSONArray jsonArray = jsonObject.getJSONArray("counter");

            for (int i = 0; i < jsonArray.size(); i++) {
                MyCounter counter = jsonArray.getJSONObject(i).toJavaObject(MyCounter.class);   //将json对象加载至MyCounter对象中

                myCounterMap.put(counter.getName(), counter);   //将MyCounter对象载入map
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static MyCounter getCounter(String name){
        return (MyCounter) myCounterMap.get(name);
    }

    public static HashMap getMyCounterMap() {
        return myCounterMap;
    }
}
