# neo4j-louvain算法实现

## 1. 向数据库导入数据

```
CREATE
  (nAlice:User {name: 'Alice', seed: 42}),
  (nBridget:User {name: 'Bridget', seed: 42}),
  (nCharles:User {name: 'Charles', seed: 42}),
  (nDoug:User {name: 'Doug'}),
  (nMark:User {name: 'Mark'}),
  (nMichael:User {name: 'Michael'}),

  (nAlice)-[:LINK {weight: 1}]->(nBridget),
  (nAlice)-[:LINK {weight: 1}]->(nCharles),
  (nCharles)-[:LINK {weight: 1}]->(nBridget),

  (nAlice)-[:LINK {weight: 5}]->(nDoug),

  (nMark)-[:LINK {weight: 1}]->(nDoug),
  (nMark)-[:LINK {weight: 1}]->(nMichael),
  (nMichael)-[:LINK {weight: 1}]->(nMark);
```

![image-20201209003956608](C:\Users\zhou\AppData\Roaming\Typora\typora-user-images\image-20201209003956608.png)

## 2. 使用match验证导入数据

```
match (u:User) return *
```

![image-20201209004029100](C:\Users\zhou\AppData\Roaming\Typora\typora-user-images\image-20201209004029100.png)

## 3. **创建图并将其存储在图目录**

```
CALL gds.graph.create(
    'myGraph',
    'User',
    {
        LINK: {
            orientation: 'UNDIRECTED'
        }
    },
    {
        nodeProperties: 'seed',
        relationshipProperties: 'weight'
    }
)
```

![image-20201209004102590](C:\Users\zhou\AppData\Roaming\Typora\typora-user-images\image-20201209004102590.png)

## 4. **估计运行该算法的内存资源要求**

```
CALL gds.louvain.write.estimate('myGraph', { writeProperty: 'community' })
YIELD nodeCount, relationshipCount, bytesMin, bytesMax, requiredMemory
```

![image-20201209004131858](C:\Users\zhou\AppData\Roaming\Typora\typora-user-images\image-20201209004131858.png)

## 5. 运行louvain算法

### 5.1 **运行算法并返回流式结果**

```
CALL gds.louvain.stream('myGraph')
YIELD nodeId, communityId, intermediateCommunityIds
RETURN gds.util.asNode(nodeId).name AS name, communityId, intermediateCommunityIds
ORDER BY name ASC
```

![image-20201209004208225](C:\Users\zhou\AppData\Roaming\Typora\typora-user-images\image-20201209004208225.png)

### 5.2 **运行算法并以统计值和测量值的形式返回结果（返回社区数）**

```
CALL gds.louvain.stats('myGraph')
YIELD communityCount
```

![image-20201209004230433](C:\Users\zhou\AppData\Roaming\Typora\typora-user-images\image-20201209004230433.png)

### 5.3 运行算法并将结果存储在myGraph（返回模块度）

```
CALL gds.louvain.mutate('myGraph', { mutateProperty: 'communityId' })
YIELD communityCount, modularity, modularities
```

![image-20201209004250632](C:\Users\zhou\AppData\Roaming\Typora\typora-user-images\image-20201209004250632.png)

### 5.4 **使用write模式运行算法并返回结果**

```
CALL gds.louvain.write('myGraph', { writeProperty: 'community' })
YIELD communityCount, modularity, modularities
```

![image-20201209004309944](C:\Users\zhou\AppData\Roaming\Typora\typora-user-images\image-20201209004309944.png)

### 5.5 **在加权图上运行算法并返回流式结果**

```
CALL gds.louvain.stream('myGraph', { relationshipWeightProperty: 'weight' })
YIELD nodeId, communityId, intermediateCommunityIds
RETURN gds.util.asNode(nodeId).name AS name, communityId, intermediateCommunityIds
ORDER BY name ASC
```

![image-20201209004337484](C:\Users\zhou\AppData\Roaming\Typora\typora-user-images\image-20201209004337484.png)

### 5.6 **通过seed属性运行算法并返回流式结果**

```
CALL gds.louvain.stream('myGraph', { seedProperty: 'seed' })
YIELD nodeId, communityId, intermediateCommunityIds
RETURN gds.util.asNode(nodeId).name AS name, communityId, intermediateCommunityIds
ORDER BY name ASC
```

![image-20201209004413115](C:\Users\zhou\AppData\Roaming\Typora\typora-user-images\image-20201209004413115.png)

### 5.7 流中间社区

#### 5.7.1 创建更复杂的表

```
CREATE (a:Node {name: 'a'})
CREATE (b:Node {name: 'b'})
CREATE (c:Node {name: 'c'})
CREATE (d:Node {name: 'd'})
CREATE (e:Node {name: 'e'})
CREATE (f:Node {name: 'f'})
CREATE (g:Node {name: 'g'})
CREATE (h:Node {name: 'h'})
CREATE (i:Node {name: 'i'})
CREATE (j:Node {name: 'j'})
CREATE (k:Node {name: 'k'})
CREATE (l:Node {name: 'l'})
CREATE (m:Node {name: 'm'})
CREATE (n:Node {name: 'n'})
CREATE (x:Node {name: 'x'})

CREATE (a)-[:TYPE]->(b)
CREATE (a)-[:TYPE]->(d)
CREATE (a)-[:TYPE]->(f)
CREATE (b)-[:TYPE]->(d)
CREATE (b)-[:TYPE]->(x)
CREATE (b)-[:TYPE]->(g)
CREATE (b)-[:TYPE]->(e)
CREATE (c)-[:TYPE]->(x)
CREATE (c)-[:TYPE]->(f)
CREATE (d)-[:TYPE]->(k)
CREATE (e)-[:TYPE]->(x)
CREATE (e)-[:TYPE]->(f)
CREATE (e)-[:TYPE]->(h)
CREATE (f)-[:TYPE]->(g)
CREATE (g)-[:TYPE]->(h)
CREATE (h)-[:TYPE]->(i)
CREATE (h)-[:TYPE]->(j)
CREATE (i)-[:TYPE]->(k)
CREATE (j)-[:TYPE]->(k)
CREATE (j)-[:TYPE]->(m)
CREATE (j)-[:TYPE]->(n)
CREATE (k)-[:TYPE]->(m)
CREATE (k)-[:TYPE]->(l)
CREATE (l)-[:TYPE]->(n)
CREATE (m)-[:TYPE]->(n);
```

![image-20201209004826051](C:\Users\zhou\AppData\Roaming\Typora\typora-user-images\image-20201209004826051.png)

#### 5.7.2 **加载示例图，运行算法并返回流式（包括中间社区在内）结果**

```
CALL gds.louvain.stream({
    nodeProjection: 'Node',
    relationshipProjection: {
        TYPE: {
            type: 'TYPE',
            orientation: 'undirected',
            aggregation: 'NONE'
        }
    },
    includeIntermediateCommunities: true
}) YIELD nodeId, communityId, intermediateCommunityIds
RETURN gds.util.asNode(nodeId).name AS name, communityId, intermediateCommunityIds
ORDER BY name ASC
```

```
╒══════╤═════════════╤══════════════════════════╕
│"name"│"communityId"│"intermediateCommunityIds"│
╞══════╪═════════════╪══════════════════════════╡
│"a"   │14           │[3,14]                    │
├──────┼─────────────┼──────────────────────────┤
│"b"   │14           │[3,14]                    │
├──────┼─────────────┼──────────────────────────┤
│"c"   │14           │[14,14]                   │
├──────┼─────────────┼──────────────────────────┤
│"d"   │14           │[3,14]                    │
├──────┼─────────────┼──────────────────────────┤
│"e"   │14           │[14,14]                   │
├──────┼─────────────┼──────────────────────────┤
│"f"   │14           │[14,14]                   │
├──────┼─────────────┼──────────────────────────┤
│"g"   │7            │[7,7]                     │
├──────┼─────────────┼──────────────────────────┤
│"h"   │7            │[7,7]                     │
├──────┼─────────────┼──────────────────────────┤
│"i"   │7            │[7,7]                     │
├──────┼─────────────┼──────────────────────────┤
│"j"   │12           │[12,12]                   │
├──────┼─────────────┼──────────────────────────┤
│"k"   │12           │[12,12]                   │
├──────┼─────────────┼──────────────────────────┤
│"l"   │12           │[12,12]                   │
├──────┼─────────────┼──────────────────────────┤
│"m"   │12           │[12,12]                   │
├──────┼─────────────┼──────────────────────────┤
│"n"   │12           │[12,12]                   │
├──────┼─────────────┼──────────────────────────┤
│"x"   │14           │[14,14]                   │
└──────┴─────────────┴──────────────────────────┘
```