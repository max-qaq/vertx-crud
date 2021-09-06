# vertx-crud
简单的用vertx框架写了个crud
https://vertx-china.gitee.io/
这是官方文档，写的都很到位也很全面
目前我就看了这么多。。以后学懂了并发编程再继续了解一下，蛮有趣的
最大的坑就是MysqlVerticle中的List<JsonObject>的输出，就是req.end()一定要在success的代码块下，因为vertx不同方法是非阻塞的，然后查询时间又慢，就会排在后面
  挺有趣的一个东西（）
同时还参考了https://www.bilibili.com/video/BV1Ep4y1Y7Do?from=search&seid=8564176791779772552&spm_id_from=333.337.0.0
  之后看缘分能不能再和你见面啦vertx 有点有趣
