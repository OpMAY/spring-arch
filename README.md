# Back-end Architecture

Okiwi의 Back-end Library 개편입니다. 해당 버전의 필요한 점은 아래와 같습니다.

## Updates

### 2023-01-02

1. pom.xml이 수정되었습니다. (Installation -> Maven 파트 참고)
2. Websocket 로직이 추가되었습니다. (이 로직은 기존 서비스에 영향을 미치지 않습니다.)
3. Websocket 로직이 추가에 따른 디렉토리 구조가 /websocket/ 이 추가되었습니다.
4. Websocket 로직이 추가에 따른 ChatMessage.class, ChatRoom.class, SessionHandShakeHandler.class,
   SessionHandShakeInerceptor.class, SocketQueueConfig.class, WebsocketConfig.class, WebsocketHandler.class,
   WebsocketService.class, websocket-test.jsp, websocket.js 등이 추가되었습니다.
5. 기존의 Websocket에서 HandShake Interceptor를 사용하여 Session의 로그인 정보를 가져올 수 있게 수정하였습니다.
6. 기존의 Websocket 로직에서 ChatRoom 개념을 도입하여 서버의 퍼포먼스를 획기적으로 높였습니다.
7. 현재 로직에서 createHandShake와 ModifyHandShake, Queue Managing System, 보안 설정 등이 업데이트 예정 사항입니다.

### 2022-12-30

1. 각종 Tags(formatDayOfWeek(요일 추출), formatFileSize(사이즈 시각변환), formatPrice(가격 포맷으로 변환))가 추가되었습니다. 해당 로직은 아래와 같은 선언을 통해
   사용하실 수 있습니다.
    ```html
   <%@ taglib prefix="custom" tagdir="/WEB-INF/tags" %>
    ```
2. CryptoException 클래스가 추가되었습니다. EncryptionService 클래스에서 사용됩니다.
3. CryptoException 클래스가 추가됨에 따라 EncryptionService가 추가로 변경되었습니다.
4. EncryptionService 클래스에서 예외처리가 추가되었습니다.
5. AuthInterceptor 클래스가 추가되었습니다.
6. FileRestController 클래스가 추가되었습니다. 해당 클래스에서 Download와 Upload 로직을 수행할 수 있습니다. 이에따라 api.js에 아래와 같은 로직이 추가되었습니다.
   ```js
   async function apiFileUpload(file) {
    function apiFetch(file) {
        const formData = new FormData();
        formData.append('file', file);
        let requestOptions = {
            method: 'POST',
            body: formData,
        };
        const response = fetch(`/file/upload`, requestOptions);
        return response.then(res => res.json());
    }

    let result;
    try {
        result = await apiFetch(file);
        return result;
    } catch (error) {
        console.log(error);
    }
   }
   ```
7. file-test.jsp가 추가되었습니다.
8. sample.jsp가 변경되었습니다.

### 2022-12-29

1. Utility 클래스가 추가되었습니다.
2. TokenGenerator 클래스가 Utility 클래스에 병합되었습니다.
3. Time 클래스가 TimeFormatter 클래스로 변경되었습니다.
4. 각종 Custom Exception 클래스가 추가되었습니다. 그에 따라 BusinessExceptionType이 GlobalExceptionType으로 변경되었습니다.
5. LoginAPI, NaverAPI, GoogleAPI, KakaoAPI, MailBuilder, ProtocolBuilder, DownloadBuilder 클래스의 각종 에러 처리를 변경하였습니다.
6. Build 시점에서 로그의 내용을 Build Process에 맞춰서 설정값을 추가하여 각종 로그를 보기 쉽게 바꾸었습니다.
7. Exception Advice 클래스의 구조를 Rest Exception Advice를 따로 분할 했습니다.
8. Rest Exception Advice 클래스의 구조를 변경 하였습니다.
9. FileUploadUtility 클래스를 보기 쉽게 바꾸었습니다.
10. GRANT_TYPE Enum 클래스를 GrantType Enum 클래스로 변경하였습니다.
11. RootUser 클래스와 상속받은 User 클래스의 겹치는 변수를 제거하였습니다.
12. Normalize 클래스를 제거하였습니다.
13. Instagram 관련 클래스(InstagramAPI, IGConstant, IGData, IGResponse)를 제거하였습니다. (사유 : 레거시)
14. AOP 관련 클래스(LogAop, RequestLogger)를 제거하였습니다. (사유 : 레거시)
15. 사업자 파악 관련 클래스(BusinessRegistrationAPI, Data, Response)를 제거하였습니다. (사유 : 레거시)
16. Sort 클래스를 제거하였습니다.
17. MailBuilder와 관련된 ResponseEnum Enum 클래스를 제거하였습니다. (사유 : 레거시 및 단일 사용으로 인한 Exception 구조로 통합)

## Installation

깃허브를 클론하거나 다운로드 하여 바로 실행

### Maven

#### 2022-01-02

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>application</groupId>
    <artifactId>webapplication</artifactId>
    <version>2.a-SNAPSHOT</version>
    <properties>
        <java-version>1.8</java-version> <!--Java SDK Version-->
        <mybatis-version>3.5.5</mybatis-version>
        <org.springframework-version>5.2.6.RELEASE</org.springframework-version> <!--Spring Version-->
        <org.aspectj-version>1.9.7</org.aspectj-version> <!--Spring AOP Version-->
        <mybatis-spring-version>2.0.5</mybatis-spring-version> <!--Mybatis Version-->
        <mysql-connector-version>8.0.19</mysql-connector-version> <!--Connector Version-->
        <aws-sdk-version>1.11.327</aws-sdk-version> <!--AWS Version-->
        <org.projectlombok>1.18.0</org.projectlombok>
        <org.slf4j-version>1.7.33</org.slf4j-version>
        <log4j-version>2.17.1</log4j-version> <!--Logger Version-->
    </properties>

    <dependencies>
        <!--Spring Framework 설정-->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-web</artifactId>
            <version>${org.springframework-version}</version>
        </dependency>

        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>${org.springframework-version}</version>
            <exclusions>
                <exclusion>
                    <groupId>commons-logging</groupId>
                    <artifactId>commons-logging</artifactId>
                </exclusion>
            </exclusions>
        </dependency>

        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-webmvc</artifactId>
            <version>${org.springframework-version}</version>
        </dependency>

        <!-- Spring Websocket-->
        <!-- https://mvnrepository.com/artifact/org.springframework/spring-websocket -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-websocket</artifactId>
            <version>${org.springframework-version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-messaging</artifactId>
            <version>${org.springframework-version}</version>
        </dependency>

        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-test</artifactId>
            <version>${org.springframework-version}</version>
        </dependency>

        <!--JDBC 연동 모듈 설정-->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-jdbc</artifactId>
            <version>5.2.6.RELEASE</version>
        </dependency>

        <!--Spring AOP 설정-->
        <dependency>
            <groupId>org.aspectj</groupId>
            <artifactId>aspectjweaver</artifactId>
            <version>${org.aspectj-version}</version>
        </dependency>
        <dependency>
            <groupId>org.aspectj</groupId>
            <artifactId>aspectjtools</artifactId>
            <version>${org.aspectj-version}</version>
        </dependency>

        <!--Lombok Data binding-->
        <!-- https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind -->
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>2.10.3</version>
        </dependency>

        <!-- https://mvnrepository.com/artifact/org.codehaus.jackson/jackson-mapper-asl -->
        <dependency>
            <groupId>org.codehaus.jackson</groupId>
            <artifactId>jackson-mapper-asl</artifactId>
            <version>1.9.13</version>
        </dependency>

        <!--Encoding & Decoding-->
        <!-- https://mvnrepository.com/artifact/commons-codec/commons-codec -->
        <dependency>
            <groupId>commons-codec</groupId>
            <artifactId>commons-codec</artifactId>
            <version>1.10</version>
        </dependency>

        <!--Database & Mybatis Maven-->
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>${mybatis-version}</version>
        </dependency>
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis-spring</artifactId>
            <version>${mybatis-spring-version}</version>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>${mysql-connector-version}</version>
        </dependency>
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-dbcp2</artifactId>
            <version>2.7.0</version>
        </dependency>

        <!--Model JSON 설정-->
        <dependency>
            <groupId>com.google.code.gson</groupId>
            <artifactId>gson</artifactId>
            <version>2.8.5</version>
        </dependency>

        <!--Lombok Modeling 설정-->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>${org.projectlombok}</version>
            <scope>provided</scope>
        </dependency>

        <!--Amazon CDN-->
        <dependency>
            <groupId>com.amazonaws</groupId>
            <artifactId>aws-java-sdk-s3</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-aws</artifactId>
            <version>2.2.1.RELEASE</version>
            <type>pom</type>
        </dependency>
        <dependency>
            <groupId>commons-io</groupId>
            <artifactId>commons-io</artifactId>
            <version>2.6</version>
        </dependency>

        <!-- 파일업로드 관련 : 이미지 썸네일 생성 라이브러리-->
        <dependency>
            <groupId>org.imgscalr</groupId>
            <artifactId>imgscalr-lib</artifactId>
            <version>4.2</version>
        </dependency>

        <!-- 파일업로드 관련-->
        <dependency>
            <groupId>commons-io</groupId>
            <artifactId>commons-io</artifactId>
            <version>2.5</version>
        </dependency>
        <dependency>
            <groupId>commons-fileupload</groupId>
            <artifactId>commons-fileupload</artifactId>
            <version>1.3.3</version>
        </dependency>

        <!--로그 설정-->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>${org.slf4j-version}</version>
        </dependency>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>jcl-over-slf4j</artifactId>
            <version>${org.slf4j-version}</version>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-slf4j-impl</artifactId>
            <version>${log4j-version}</version>
        </dependency>
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-core</artifactId>
            <version>${log4j-version}</version>
            <exclusions>
                <exclusion>
                    <groupId>javax.mail</groupId>
                    <artifactId>mail</artifactId>
                </exclusion>
                <exclusion>
                    <groupId>javax.jms</groupId>
                    <artifactId>jms</artifactId>
                </exclusion>
                <exclusion>
                    <groupId>com.sun.jdmk</groupId>
                    <artifactId>jmxtools</artifactId>
                </exclusion>
                <exclusion>
                    <groupId>com.sun.jmx</groupId>
                    <artifactId>jmxri</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <!--JSP 관련 설정-->
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>javax.servlet-api</artifactId>
            <version>3.1.0</version>
        </dependency>
        <dependency>
            <groupId>javax.servlet.jsp</groupId>
            <artifactId>jsp-api</artifactId>
            <version>2.1</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>jstl</artifactId>
            <version>1.2</version>
        </dependency>

        <!--Schedule Lock Maven-->
        <dependency>
            <groupId>net.javacrumbs.shedlock</groupId>
            <artifactId>shedlock-spring</artifactId>
            <version>2.2.0</version>
        </dependency>
        <dependency>
            <groupId>net.javacrumbs.shedlock</groupId>
            <artifactId>shedlock-provider-jdbc-template</artifactId>
            <version>2.1.0</version>
        </dependency>

        <!--JWT Token Authentication-->
        <!-- https://mvnrepository.com/artifact/com.auth0/java-jwt -->
        <dependency>
            <groupId>com.auth0</groupId>
            <artifactId>java-jwt</artifactId>
            <version>3.10.3</version>
        </dependency>

        <!--Java Json-->
        <!-- https://mvnrepository.com/artifact/org.json/json -->
        <dependency>
            <groupId>org.json</groupId>
            <artifactId>json</artifactId>
            <version>20210307</version>
        </dependency>

        <!--Bootpay REST Maven-->
        <dependency>
            <groupId>org.apache.httpcomponents</groupId>
            <artifactId>httpclient</artifactId>
            <version>LATEST</version>
        </dependency>

        <!-- https://mvnrepository.com/artifact/com.sun.mail/javax.mail -->
        <dependency>
            <groupId>com.sun.mail</groupId>
            <artifactId>javax.mail</artifactId>
            <version>1.6.2</version>
        </dependency>
    </dependencies>

    <!--Amazon CDN Maven-->
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>com.amazonaws</groupId>
                <artifactId>aws-java-sdk-bom</artifactId>
                <version>${aws-sdk-version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <!--Maven Build 설정-->
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-war-plugin</artifactId>
                <version>2.3</version>
                <configuration>
                    <webResources>
                        <resource>
                            <directory>web</directory>
                        </resource>
                    </webResources>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <source>${java-version}</source>
                    <target>${java-version}</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

#### 2022-??-??

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>application</groupId>
    <artifactId>webapplication</artifactId>
    <version>2.a-SNAPSHOT</version>
    <properties>
        <java-version>1.8</java-version> <!--Java SDK Version-->
        <mybatis-version>3.5.5</mybatis-version>
        <org.springframework-version>5.2.6.RELEASE</org.springframework-version> <!--Spring Version-->
        <org.aspectj-version>1.9.7</org.aspectj-version> <!--Spring AOP Version-->
        <mybatis-spring-version>2.0.5</mybatis-spring-version> <!--Mybatis Version-->
        <mysql-connector-version>8.0.19</mysql-connector-version> <!--Connector Version-->
        <aws-sdk-version>1.11.327</aws-sdk-version> <!--AWS Version-->
        <org.projectlombok>1.18.0</org.projectlombok>
        <org.slf4j-version>1.7.33</org.slf4j-version>
        <log4j-version>2.17.1</log4j-version> <!--Logger Version-->
    </properties>

    <dependencies>
        <!--Spring Framework 설정-->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-web</artifactId>
            <version>${org.springframework-version}</version>
        </dependency>

        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>${org.springframework-version}</version>
            <exclusions>
                <exclusion>
                    <groupId>commons-logging</groupId>
                    <artifactId>commons-logging</artifactId>
                </exclusion>
            </exclusions>
        </dependency>

        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-webmvc</artifactId>
            <version>${org.springframework-version}</version>
        </dependency>

        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-test</artifactId>
            <version>${org.springframework-version}</version>
        </dependency>

        <!--JDBC 연동 모듈 설정-->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-jdbc</artifactId>
            <version>5.2.6.RELEASE</version>
        </dependency>

        <!--Spring AOP 설정-->
        <dependency>
            <groupId>org.aspectj</groupId>
            <artifactId>aspectjweaver</artifactId>
            <version>${org.aspectj-version}</version>
        </dependency>
        <dependency>
            <groupId>org.aspectj</groupId>
            <artifactId>aspectjtools</artifactId>
            <version>${org.aspectj-version}</version>
        </dependency>

        <!--Lombok Data binding-->
        <!-- https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind -->
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>2.10.3</version>
        </dependency>

        <!-- https://mvnrepository.com/artifact/org.codehaus.jackson/jackson-mapper-asl -->
        <dependency>
            <groupId>org.codehaus.jackson</groupId>
            <artifactId>jackson-mapper-asl</artifactId>
            <version>1.9.13</version>
        </dependency>

        <!--Encoding & Decoding-->
        <!-- https://mvnrepository.com/artifact/commons-codec/commons-codec -->
        <dependency>
            <groupId>commons-codec</groupId>
            <artifactId>commons-codec</artifactId>
            <version>1.10</version>
        </dependency>

        <!--Database & Mybatis Maven-->
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>${mybatis-version}</version>
        </dependency>
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis-spring</artifactId>
            <version>${mybatis-spring-version}</version>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>${mysql-connector-version}</version>
        </dependency>
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-dbcp2</artifactId>
            <version>2.7.0</version>
        </dependency>

        <!--Model JSON 설정-->
        <dependency>
            <groupId>com.google.code.gson</groupId>
            <artifactId>gson</artifactId>
            <version>2.8.5</version>
        </dependency>

        <!--Lombok Modeling 설정-->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>${org.projectlombok}</version>
            <scope>provided</scope>
        </dependency>

        <!--Amazon CDN-->
        <dependency>
            <groupId>com.amazonaws</groupId>
            <artifactId>aws-java-sdk-s3</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-aws</artifactId>
            <version>2.2.1.RELEASE</version>
            <type>pom</type>
        </dependency>
        <dependency>
            <groupId>commons-io</groupId>
            <artifactId>commons-io</artifactId>
            <version>2.6</version>
        </dependency>

        <!-- 파일업로드 관련 : 이미지 썸네일 생성 라이브러리-->
        <dependency>
            <groupId>org.imgscalr</groupId>
            <artifactId>imgscalr-lib</artifactId>
            <version>4.2</version>
        </dependency>

        <!-- 파일업로드 관련-->
        <dependency>
            <groupId>commons-io</groupId>
            <artifactId>commons-io</artifactId>
            <version>2.5</version>
        </dependency>
        <dependency>
            <groupId>commons-fileupload</groupId>
            <artifactId>commons-fileupload</artifactId>
            <version>1.3.3</version>
        </dependency>

        <!--로그 설정-->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>${org.slf4j-version}</version>
        </dependency>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>jcl-over-slf4j</artifactId>
            <version>${org.slf4j-version}</version>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-slf4j-impl</artifactId>
            <version>${log4j-version}</version>
        </dependency>
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-core</artifactId>
            <version>${log4j-version}</version>
            <exclusions>
                <exclusion>
                    <groupId>javax.mail</groupId>
                    <artifactId>mail</artifactId>
                </exclusion>
                <exclusion>
                    <groupId>javax.jms</groupId>
                    <artifactId>jms</artifactId>
                </exclusion>
                <exclusion>
                    <groupId>com.sun.jdmk</groupId>
                    <artifactId>jmxtools</artifactId>
                </exclusion>
                <exclusion>
                    <groupId>com.sun.jmx</groupId>
                    <artifactId>jmxri</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <!--JSP 관련 설정-->
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>javax.servlet-api</artifactId>
            <version>3.1.0</version>
        </dependency>
        <dependency>
            <groupId>javax.servlet.jsp</groupId>
            <artifactId>jsp-api</artifactId>
            <version>2.1</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>jstl</artifactId>
            <version>1.2</version>
        </dependency>

        <!--Schedule Lock Maven-->
        <dependency>
            <groupId>net.javacrumbs.shedlock</groupId>
            <artifactId>shedlock-spring</artifactId>
            <version>2.2.0</version>
        </dependency>
        <dependency>
            <groupId>net.javacrumbs.shedlock</groupId>
            <artifactId>shedlock-provider-jdbc-template</artifactId>
            <version>2.1.0</version>
        </dependency>

        <!--JWT Token Authentication-->
        <!-- https://mvnrepository.com/artifact/com.auth0/java-jwt -->
        <dependency>
            <groupId>com.auth0</groupId>
            <artifactId>java-jwt</artifactId>
            <version>3.10.3</version>
        </dependency>

        <!--Java Json-->
        <!-- https://mvnrepository.com/artifact/org.json/json -->
        <dependency>
            <groupId>org.json</groupId>
            <artifactId>json</artifactId>
            <version>20210307</version>
        </dependency>

        <!--Bootpay REST Maven-->
        <dependency>
            <groupId>org.apache.httpcomponents</groupId>
            <artifactId>httpclient</artifactId>
            <version>LATEST</version>
        </dependency>

        <!-- https://mvnrepository.com/artifact/com.sun.mail/javax.mail -->
        <dependency>
            <groupId>com.sun.mail</groupId>
            <artifactId>javax.mail</artifactId>
            <version>1.6.2</version>
        </dependency>

        <!-- 채팅 위한 socket -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-websocket</artifactId>
            <version>${org.springframework-version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-messaging</artifactId>
            <version>${org.springframework-version}</version>
        </dependency>
    </dependencies>

    <!--Amazon CDN Maven-->
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>com.amazonaws</groupId>
                <artifactId>aws-java-sdk-bom</artifactId>
                <version>${aws-sdk-version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <!--Maven Build 설정-->
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-war-plugin</artifactId>
                <version>2.3</version>
                <configuration>
                    <webResources>
                        <resource>
                            <directory>web</directory>
                        </resource>
                    </webResources>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <source>${java-version}</source>
                    <target>${java-version}</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

### Dispatcher-Servlet.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       https://www.springframework.org/schema/context/spring-context.xsd">
    <!-- Annotation Uses-->
    <!--<mvc:annotation-driven>
        <mvc:message-converters>
            &lt;!&ndash; @ResponseBody로 String 처리할때 한글처리 &ndash;&gt;
            <bean class="org.springframework.http.converter.StringHttpMessageConverter">
                <property name="supportedMediaTypes" value="text/html;charset=UTF-8"/>
            </bean>
        </mvc:message-converters>
    </mvc:annotation-driven>-->

    <!-- Base Back java -->
    <context:component-scan base-package="com"/>
</beans>
```

### Mybatis-Config.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <settings>
        <setting name="cacheEnabled" value="true"/>
        <setting name="callSettersOnNulls" value="true"/>
        <setting name="jdbcTypeForNull" value="NULL"/>
    </settings>
    <typeAliases>
        <typeAlias alias="JsonArrayObjectTypeHandler" type="com.middleware.JsonArrayObjectTypeHandler"/>
        <typeAlias alias="JsonObjectTypeHandler" type="com.middleware.JsonObjectTypeHandler"/>
        <typeAlias alias="StringArrayListTypeHandler" type="com.middleware.StringArrayListTypeHandler"/>
    </typeAliases>
</configuration>
```

### Log4j2.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<log4j:configuration xmlns:log4j="http://jakarta.apache.org/log4j/">
    <!-- console log -->
    <Appenders>
        <Console name="console" target="SYSTEM_OUT">
            <PatternLayout pattern="[%-5p][%d{yyyy-MM-dd HH:mm:ss}]%m%n"/>
        </Console>
    </Appenders>

    <Loggers>
        <Logger name="org.springframework.core" level="INFO" additivity="false">
            <AppenderRef ref="console"/>
        </Logger>
        <Logger name="org.springframework.beans" level="INFO" additivity="false">
            <AppenderRef ref="console"/>
        </Logger>
        <Logger name="org.springframework.context" level="INFO" additivity="false">
            <AppenderRef ref="console"/>
        </Logger>
        <Logger name="org.springframework.web" level="INFO" additivity="false">
            <AppenderRef ref="console"/>
        </Logger>
        <Logger name="com.mapper" level="INFO" additivity="false">
            <AppenderRef ref="console"/>
        </Logger>
        <Logger name="org.mybatis.spring.SqlSessionUtils" level="INFO" additivity="false">
            <AppenderRef ref="console"/>
        </Logger>
        <Logger name="org.springframework.jdbc" level="INFO" additivity="false">
            <AppenderRef ref="console"/>
        </Logger>
        <Logger name="org.mybatis.spring.transaction.SpringManagedTransaction" level="INFO" additivity="false">
            <AppenderRef ref="console"/>
        </Logger>
        <Logger name="org.springframework.web.servlet.DispatcherServlet" level="INFO" additivity="false">
            <AppenderRef ref="console"/>
        </Logger>
        <Logger name="org.springframework.web.servlet.view.InternalResourceView" level="INFO" additivity="false">
            <AppenderRef ref="console"/>
        </Logger>
        <Logger name="com.interceptor.BaseInterceptor" level="INFO" additivity="false">
            <AppenderRef ref="console"/>
        </Logger>
        <Logger name="org.springframework.web.servlet.view.JstlView" level="INFO" additivity="false">
            <AppenderRef ref="console"/>
        </Logger>
        <Root level="INFO">
            <AppenderRef ref="console"/>
        </Root>
    </Loggers>
</log4j:configuration>
```

## Warn

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License

1. [OKIWI](https://avouch.co.kr)
2. [MIT](https://choosealicense.com/licenses/mit/)
