# 基于JAVA-Redis的Counter项目

## 1. 项目基本信息

- 作者：周子涵（Harry Zhou）
- 完成时间：2020-12-8
- 开发语言：Java
- 框架：Spring boot

## 2. 任务完成情况

- 完成对num的封装，实现num（计数）
- 完成对zset的封装，实现freq（区间计数）
- 完成对string的封装，实现desc（简介）
- 完成对list的封装，实现write action log（写操作日志）
- 实现json文件存储action、counter数据，使用alibaba.fastjson加载json数据
- 通过commons io Monitor实现对json文件修改的动态监听与加载
- 通过ReentrantReadWriteLock实现MyLock静态类，对读写操作加锁

## 3. 项目结构简介

com.bjtu.redis目录下主要包括以下文件夹：

- action
- counter
- jedis
- jsonLoader
- lock
- monitor

其中还有RedisDemoApplication.java文件（main函数所在文件）

json文件存储于项目的resources文件夹下

### 3.1 action

该文件夹下为MyAction class，ActionCounter class，ActionExecutor class。MyAction class是action的Java类。ActionCounter类是用于存储action需要使用的counter，这里只需要存储counter名称，将counter名称作为参数可以通过CounterJsonLoader的map获得对应counter对象。ActionExecutor类为action的执行类，根据传入的action对象执行对应操作。

action的执行逻辑主要为：用户输入操作-->转化为对应action名称-->通过actionmap获取对应MyAction对象-->将MyAction对象传入ActionExecutor中-->加载MyAction对象所需counter-->执行操作

### 3.2 counter

该文件夹下为MyCounter class，是counter的Java类

### 3.3 jedis

该文件夹下为JedisInstance class和JedisUtil class。JedisInstance类通过getInstance() 获得jedis对象。JedisUtil类是封装的jedis工具类，将需要用到的对jedis操作的方法封装于此。

### 3.4 jsonLoader

该文件夹下为ActionJsonLoader class和CounterJsonLoader class，两个类分别用于action.json和counter.json的数据加载操作。加载json数据，通过map存储在内存中。

### 3.5 lock

该文件夹下为MyLock class，使用ReentrantReadWriteLock封装了读写锁，用于数据加载时使用。

### 3.6 monitor

该文件夹下为MyJsonMonitor class，是实现文件变化监听的类，继承 FileAlterationListener，实现对json文件修改的监听与重新加载

### 3.7 json数据

action和counter数据都使用json存储，两种数据的格式示例如下：

**action：**

```json
"action": [
    {
      "name": "incrcount",
      "index": 1,
      "operation": "write",
      "counter": [
        {"cname": "numcount"},
        {"cname": "freqcount"},
        {"cname": "logcount"}
      ]
    },
    ...
]
```

最外层为action数组，内部存储action对象。每一个action对象包含名称（name）、索引（index）、操作类型（operation）、所需counter（counter）。

**counter：**

```json
"counter": [
    {
      "name": "numcount",
      "index": 1,
      "key": "count",
      "value": 1,
      "type": "num"
    },
    ...
]
```

最外层为counter数组，内部存储counter对象。每一个counter对象包含名称（name）、索引（index）、键（key）、值（value）、种类（type）（个别counter还会包含一些其他数据，如freqcount包含开始结束时间）。

## 4. 用户操作

### 4.1 启动程序

用户通过idea打开程序，右键单击RedisDemoApplication，点击 Run RedisDemoApplication即可运行。

<img src="C:\Users\zhou\AppData\Roaming\Typora\typora-user-images\image-20201208165536306.png" alt="image-20201208165536306" style="zoom:50%;" />

### 4.2 基本功能

运行程序后，控制台输出以下信息即加载成功：

<img src="C:\Users\zhou\AppData\Roaming\Typora\typora-user-images\image-20201208165631527.png" alt="image-20201208165631527" style="zoom: 80%;" />

用户可以根据菜单列表通过输入对应字母编号（大小写皆可）进行操作。主要操作为：

- increase count（计数器计数）
- get count（获得当前数值）
- get freq（获得区间数值）
- get desc（获得简介）
- set desc（修改简介）
- get write action log（获得写入操作日志）
- quit（退出）

### 4.3 json文件修改

用户可以通过修改json文件调整action与counter的配置。本项目已实现监听文件修改与自动加载。

若要修改每次计数的数量，则打开counter.json，修改numcount中的“value”值即可。

```json
{
      "name": "numcount",
      "index": 1,
      "key": "count",
      "value": 1,		//计数数量
      "type": "num"
},
```

若要修改freq的区间，则打开则打开counter.json，修改freqcount中的“beginTime”值、“endTime”值即可。时间格式要求按照“yyyy-MM-dd HH-mm-ss”。

```json
{
      "name": "freqcount",
      "index": 2,
      "key": "freq",
      "value": 1,
      "type": "zset",
      "beginTime": "2020-12-8 00:00:00",	//开始时间
      "endTime": "2020-12-9 00:00:00"		//结束时间
},
```

