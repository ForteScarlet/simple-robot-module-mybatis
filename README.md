# mod-mybatis

针对于mybatis的模组实现。

注：默认使用的是`3.4.6`版本的Mybatis.

## 配置文件
```properties
# suppress inspection "UnusedProperty" for whole file

# mybatis的xml配置文件的资源路径。此配置文件需要使用UTF-8编码
# 如果存在此配置，则以下其他的配置将会被忽略
simbot.mybatis.config=conf.xml

# 数据库驱动. 驱动相关的依赖需要自己导入
simbot.db.driver=

# 连接路径
simbot.db.url=

# 连接用户名
simbot.db.name=

# 连接密码
simbot.db.password=

# mybatis的mapper的扫描路径
simbot.mapper.scanPackage=
```

