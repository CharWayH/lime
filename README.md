新项目

## 整体架构

前端使用低代码前端框架amis



从github上将下载https://github.com/aisuda/amis-react-starter，后在解压后的项目路径中(/Users/charwayh/front-end1/amis-react-starter-main)执行

```shell
# 安装相关模块和依赖
npm i
# 运行项目
npm run server
```

```shell
npm run vite
```





### 模块

lime-common:普通模块

lime-service-api:api模块

lime-service-api-impl:api实现类模块

lime-cron:定时任务模块

lime-support





**swagger访问地址**

默认为

[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html#/)

端口号与springboot配置的服务端口相同





nginx

http://localhost:6565









### **docker**

#### mysql-db

项目的mysql数据库

进入到docker/mysql路径下执行

```
docker-compose up
```



#### amis(低代码前端框架)

进入到docker/amis路径下执行

```
docker-compose up
```



#### apollo

进入到docker/apollo路径下执行

```
docker-compose up
```

连接数据库127.0.0.1:13306

执行分别执行docker/apollo/sqlsql下的sql脚本。

- apolloconfigdb.sql
- apolloportaldb.sql

**阿波罗页面**

http://localhost:8071/

用户名:apollo

密码:admin



#### xxl-job

连接数据库127.0.0.1:23306

执行docker/apollo/sqlsql下的sql脚本

tables_xxl_job.sql



进入到docker/xxl-job-admin路径下，修改docker-compose.yml中的mysql地址(不能为127.0.0.1或localhost)

```
docker-compose up
```



启动成功后访问

http://localhost:8001/xxl-job-admin/

用户名:admin
密码:123456




任务

1.登录成功跳转至功能页

所有消息展示



2.邮件发送











