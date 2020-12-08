package com.bjtu.redis.jsonLoader;

import ch.qos.logback.core.util.FileUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjtu.redis.action.MyAction;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class ActionJsonLoader { //action.json加载类

    private static HashMap myActionMap = new HashMap(); //actionmap，用于将action.json数据存储至内存

    public static void load(){
        myActionMap.clear();
        try {
            String content = FileUtils.readFileToString(new File("src/main/resources/action.json"), StandardCharsets.UTF_8);

            JSONObject jsonObject = JSON.parseObject(content);
            JSONArray jsonArray = jsonObject.getJSONArray("action");

            for (int i = 0; i < jsonArray.size(); i++) {
                MyAction action = jsonArray.getJSONObject(i).toJavaObject(MyAction.class);  //将json对象加载至MyAction对象中

                myActionMap.put(action.getName(), action);  //将MyAction对象载入map
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static MyAction getAction(String name){
        return (MyAction)myActionMap.get(name);
    }
}
