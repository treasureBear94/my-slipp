# my-slipp
youtub 박재성님 https://www.youtube.com/playlist?list=PLqaSEyuwXkSppQAjwjXZgKkjWbFoUdNXC

### 배포 방법
```
$ ./mvnw clean package 
$ java -jar target/my-slipp.jar &
```

### mustache 파일 인식 못하는 에러
mustache 에서 ~~{{ > /include/header }}~~ 가 아니라 `{{ > include/header }}` 로 표시해야 제대로 인식됩니다.  
 
`UserController.java`에서도 마찬가지입니다. 파일 경로를 표시할 때, ~~"/user/list"~~ 가 아니라 `"user/list"` 로 작성해야 합니다.
 
 
### maven 에서 spring boot 를 실행하는 방법.(jar 로 압축하지 않는 방법)
```
./mvnw spring-boot:run &
```

### 리펙토링
가능한 데이터를 꺼내지 말고(getId..), 객체한테 메시지 보내며 물어봐. 

### 외부 Tomcat을 이용한 war 배포
1. pom.xml 에 아래 내용 추가

https://docs.spring.io/spring-boot/docs/2.1.5.RELEASE/reference/html/build-tool-plugins-maven-plugin.html
```xml
<packaging>war</packaging>
<!-- ... -->
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-tomcat</artifactId>
        <scope>provided</scope>
    </dependency>
    <!-- ... -->
</dependencies>
```

2. tomcat 설치 

설치 url 은 아파치 톰캣 홈페이지에 가서 맞는 거 받으면 됨.
```
curl -O https://downloads.apache.org/tomcat/tomcat-9/v9.0.39/bin/apache-tomcat-9.0.39.tar.gz
```
받은 파일 압축 해제
```
tar -xvf apache-tomcat-9.0.39.tar.gz
```
(옵션) 링크 걸기 
위 파일을 압축해제 하면, 이름이 길고 복잡하니까 간단한 이름으로 링크 거는 것.
```
ln -s apache-tomcat-9.0.39 tomcat
```
apache-tomcat-9.0.39 를 tomcat 이라는 이름으로 접근 가능. 

tomcat 실행 
```
cd tomcat/bin
./startup.sh
```

tomcat 종료
```
./shutdown.sh
```

(옵션) tomcat 포트 변경
```
cd tomcat/conf
vi server.xml
```
아래 port 를 변경해주면 됨.
```
...
<Connector port="8080" protocol="HTTP/1.1"
           connectionTimeout="20000"
           redirectPort="8443" />
...
```

3. war 로 빌드
```
./mvnw clean package 
```

### 데이터 전달
객체에서 값을 꺼내서 전달하는 것보다 객체로 전달하는 것이 더 낫다. 

~~sessionUser.getId()~~ -> sessionUser
  
### swagger-ui
아래 URL로 swagger-ui 접근 가능. 간단한 수동 테스트도 가능

http://localhost:8081/swagger-ui.html

### 배포 순서
- git pull
- mvnw clean package
- ./shutdown.sh (tomcat 서버 종료)
- tomcat/webapps/ROOT 삭제
- 빌드한 산출물 tomcat/webapps/ROOT로 이동
- ./startup.sh (tomcat 서버 시작)

### 쉘 스크립트를 만들어서 배포 자동화
```
~ $ vi deploy.sh
```

잘 되는지 확인
```
#!/bin/bash

echo "Welcome My Shell!!"
pwd
```

deploy.sh 에 실행권한 주기
```shell
$ chmod 755 deploy.sh
```

deploy.sh 실행
```shell
$ ./deploy.sh
```

실행 결과 
```shell
Welcome My Shell!!
/home/yj
```

소스 수정
```shell
#!/bin/bash

echo "Welcome My Shell!!"

# 변수 생성
TOMCAT_HOME=~/tomcat

cd ~/my-slipp
git pull

./mvnw clean package

cd $TOMCAT_HOME/bin
./shutdown.sh

cd $TOMCAT_HOME/webapps
rm -rf ROOT

mv ~/my-slipp/target/my-slipp-1.0/ $TOMCAT_HOME/webapps/ROOT

cd $TOMCAT_HOME/bin
./startup.sh

tail -500f $TOMCAT_HOME/logs/catalina.out
```

Ite's over.