# SpringCloud之Eureka

## 前言  
spring cloud为互联企业构建微服务提供了一整套的技术组件，其中Eureka是Spring Cloud体系中的核心。Netfix不是一个技术概念，它原本是国外一个视频网站的名称。这个视频网站的技术团队在微服务方向做了大量实践，并提供了很多的技术组件，Eureka就是其中之一。笔者也是Spring Cloud初学者，本文从创建项目工程开始，一步一步开始讲解如何创建eureka服务端和客户端，一起学习，共同进步。  

欢迎访问我的技术博客：http://51think.net
### 一、什么是eureka  
我们经常看到一些互联网企业在描述其技术架构时会使用到Eureka一词，也听说过Eureka用于服务注册发现，并不清楚它是如何整合到应用层的。从代码结构上来看，Eureka就是一个大jar包集合，maven引入这个jar包集合，并在应用层做简单配置即可实现服务发现功能。也就是说，Spring Cloud已经将相关功能封装的很好了，直接引用就行了，这也符合各种流行框架的宗旨，最大程度的降低非业务性的工作量，让程序员更加专注实现自己的业务功能。Eureka这个组件是不能单独运行的，需要以Springboot作为应用载体，真正的部署到虚机上面才能运行。  

### 二、创建Eureka注册中心  
注册中心，顾名思义，类似于zookeeper一样，提供服务注册发现功能，即服务端的服务地址通过注册中心全部暴露给客户端，由客户端实现负载均衡。下面我们使用idea工具创建相关项目。  
1、创建一个maven主工程  
2、在主工程下创建一个model,本例中命名为spring-cloud-eureka  
![](http://51think.net:80/upload/2019/07/ttk7db4jomgdso2872qd2ud7qp.jpg)

3、填写Group和Artifact  
![](http://51think.net:80/upload/2019/07/4hlg8t48uog8vrtj9fvs58hqf2.jpg)

4、勾选Eureka Server  
![](http://51think.net:80/upload/2019/07/uggeg2l3kag3tpfskl7ms4880h.jpg)

5、创建完成之后的pom文件  

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.6.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.example</groupId>
    <artifactId>spring-cloud-eureka</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>spring-cloud-eureka</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
        <spring-cloud.version>Greenwich.SR2</spring-cloud.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>
```
pom文件中只有一个关键的spring-cloud-starter-netflix-eureka-server包，其实这个包下面依赖了很多子包，如下图：  
![](http://51think.net:80/upload/2019/07/tiabgrid2mh4lpmc5jhm431e06.jpg)

6、找到Springboot的启动类，加上@EnableEurekaServer注解  
@EnableEurekaServer代表这个springboot应用是一个注册中心。   
```
@EnableEurekaServer
@SpringBootApplication
public class EurekaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }

}
```
7、配置application.yml   
```
server:
  port: 8010

eureka:
  instance:
    hostname: localhost
  client:
    registerWithEureka: false
    fetchRegistry: false
    serviceUrl:
      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/

```
  其中两个属性registerWithEureka: false  fetchRegistry: false表明这个应用是Eureka  Server端，而不是Client端。defaultZone用来申明这个注册中心的地址，后面创建Eureka Client端时也要申明这个地址，以便向注册中心注册。  

### 三、创建一个服务生产者应用  
这里我们要创建一个springboot应用作为服务的生产者，并且能够将服务注册到注册中心。对于整个系统而言，我们创建的是一个服务端应用供客户端调用，对于Eureka注册中心而言，除了注册中心是Server角色，其他都是Eureka Client角色。具体过程如下：   
1、创建model过程与创建Eureka  Server类似,名称为spring-cloud-eureka-myservice   
2、pom文件   
```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.6.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.example</groupId>
    <artifactId>spring-cloud-eureka-myservice</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>spring-cloud-eureka-myservice</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
        <spring-cloud.version>Greenwich.SR2</spring-cloud.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>

```
大家可以比较一下这个pom文件与之前的pom文件的差别，spring-cloud-starter-netflix-eureka-clien组件和之前注册中心pom文件中的spring-cloud-starter-netflix-eureka-server相对应。spring-boot-starter-web组件用来提供web访问能力，我们可以通过浏览器来访问后台服务。   
3、使用@EnableEurekaClient来标注自己的身份  
```
@EnableEurekaClient
@SpringBootApplication
public class MyServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyServiceApplication.class, args);
    }

}
```
4、配置application.yml  
```
eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8010/eureka/
server:
  port: 8011
spring:
  application:
    name: myservice
```
这个配置项spring.application.name很重要，表明这个应用在微服务架构中的应用名称，后面的案例中我们可以通过这个名称来访问这个服务。   

5、启动注册中心应用spring-cloud-eureka  
6、启动服务生产者应用spring-cloud-eureka-myservice  
7、访问Eureka面板  
Eureka提供一个web访问页面，通过这个页面我们可以看到已注册的服务列表以及注册中心应用的状态。浏览器访问http://localhost:8010，会展现如下页面：  
![](http://51think.net:80/upload/2019/07/rsush4ucfggkto38mbr8n27drn.jpg)

红框标注的部分即我们刚刚启动的spring-cloud-eureka-myservice应用，服务名称为myservice。  
至此，Eureka Server端和Client端已经部署成功。
