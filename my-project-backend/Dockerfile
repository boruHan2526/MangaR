# 基础镜像
FROM eclipse-temurin:21
# 作者
MAINTAINER boru
# 工作目录
WORKDIR /usr/local/java
# 同步docker内部的时间
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
# 设置时区
ENV TZ=Asia/Tokyo
EXPOSE 8081
# 复制jar包和配置文件到容器
ARG JAR_FILE
ADD ${JAR_FILE} ./manga.jar
ADD application.yml ./application.yml

ENTRYPOINT ["nohup","java","-jar","/usr/local/java/manga.jar",">","/usr/local/java/manga.log","&>","&"]

# 手顺

# 1. 重装docker （为什么重装？因为购买服务器选择的docker版本低，运行java容器会报内存不足。）
# 按照以下顺序执行即可
# 卸载：
# 1. `sudo yum install -y yum-utils`
# 2. `sudo yum remove docker-ce docker-ce-cli containerd.io`
# 3. `sudo rm -rf /var/lib/docker`
# 4. `sudo rm -rf /var/lib/containerd`
# 安装：
# 1. `sudo yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo`
# 2. `sudo yum install docker-ce docker-ce-cli containerd.io`
# 3. 设置开机自启：`systemctl enable docker.service`

# 2. 创建docker网络：
# docker network manga

# 3. 创建mysql服务
#docker run -d \
#  --name mysql \
#  -v mysqldata:/var/lib/mysql \
#  -v mysqlconf:/etc/mysql/conf.d \
#  -v mysqllog:/var/log/mysql \
#  -p 3306:3306 \
#  --network manga \
#  -e MYSQL_ROOT_PASSWORD=123456 \
#  mysql:8

# 4. 创建redis服务
# docker run --name redis --network manga -p 6379:6379 -d redis:latest

# java应用镜像构建：
# 1.在服务器中创建 /usr/local/java 文件夹，并cd到该目录下
# 2.将Dockerfile，相关jar包，yml配置文件全部上传到Java文件夹
# 3.执行build命令 -> docker build -t manga:v1.0 .
# 4.运行后端 -> docker run -d -it --name manga --network manga -v /usr/local/java:/usr/local/java -p 8081:8081 manga:v1.0

