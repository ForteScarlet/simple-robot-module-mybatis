# mod-mybatis

[![](https://img.shields.io/badge/simple--robot-module-green)](https://github.com/ForteScarlet/simple-robot-core) [![](https://img.shields.io/maven-central/v/love.forte.simple-robot-module/mod-mybatis)](https://mvnrepository.com/artifact/love.forte.simple-robot-module/mod-mybatis)

针对于mybatis的模组实现。

注：默认使用的是`3.4.6`版本的Mybatis.

## 导入依赖

[![](https://img.shields.io/maven-central/v/love.forte.simple-robot-module/mod-mybatis)](https://mvnrepository.com/artifact/love.forte.simple-robot-module/mod-mybatis)

```xml
<dependency>
    <groupId>love.forte.simple-robot-module</groupId>
    <artifactId>mod-mybatis</artifactId>
    <version>${version}</version>
</dependency>
```

## 配置文件
```properties
# suppress inspection "UnusedProperty" for whole file

# 此处指的是mapper.xml的扫描路径。
# 暂时不支持通配符，只支持整个文件夹的扫描。
# 此配置不受 simbot.mybatis.config 的影响，会一直生效。
# 一般xml指定了namespace的话就用不着指定mapperScan了
simbot.mapper.mapperResourceScan=mappers
# mybatis的mapper接口的扫描路径。
simbot.mapper.mapperScan=

# mybatis的xml配置文件的资源路径。此配置文件需要使用UTF-8编码
# 如果存在此配置，则以下其他的配置将会被忽略
# 似乎会存在jar包情况下mapper.package失效的问题，如果出现此问题，则需要配置上面的配置信息.
simbot.mybatis.config=conf.xml
# 数据库驱动. 驱动相关的依赖需要自己导入
simbot.db.driver=
# 连接路径
simbot.db.url=
# 连接用户名
simbot.db.name=
# 连接密码
simbot.db.password=
```

## 使用方法
因为使用的是原生的mybatis，首先我还是建议你写一个mybatis的xml配置文件，例如：

> db.properties: 

```properties
# db.properties
driver=com.mysql.cj.jdbc.Driver
url=jdbc:mysql://127.0.0.1:3306/test_db1?serverTimezone=UTC&useUnicode=true&amp&characterEncoding=UTF-8&useSSL=false
name=root
password=123456
```

> conf.xml: 

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>

    <!-- 引用db.properties配置文件 -->
    <properties resource="db.properties"/>
    <!--
    environments:开发模式
    work:工作模式
    default="development"，id="development"，两个的属性值必须一致
     -->
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <!-- value属性值引用db.properties配置文件中配置的值 -->
                <property name="driver" value="${driver}"/>
                <property name="url" value="${url}"/>
                <property name="username" value="${name}"/>
                <property name="password" value="${password}"/>
            </dataSource>
        </environment>
    </environments>
</configuration>
```

> simple-robot-conf.properties: 

```properties
simbot.mybatis.config=conf.xml
simbot.mapper.mapperResourceScan=mappers
simbot.mapper.mapperScan=
```

当然，假如你不使用mybatis的xml配置，你依然可以使用简化版的配置：

> simple-robot-conf.properties: 

```properties
# mapper.xml的扫描路径
simbot.mapper.mapperResourceScan=mappers
# mybatis的mapper的扫描路径
simbot.mapper.scanPackage=com.forte.mapper

# 数据库驱动. 驱动相关的依赖需要自己导入
simbot.db.driver=com.mysql.cj.jdbc.Driver
# 连接路径
simbot.db.url=jdbc:mysql://127.0.0.1:3306/test_db1?serverTimezone=UTC&useUnicode=true&amp&characterEncoding=UTF-8&useSSL=false
# 连接用户名
simbot.db.name=root
# 连接密码
simbot.db.password=123456
```