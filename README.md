# Spring Boot REST service base project
`Develop by tungtt`  
`Email: thanhtung100397@gmail.com`

### NOTED
- This project require 'allow-bean-definition-overriding=true' configuration, so you should NOT create duplicated @Bean, due to the fact that they will replace each other, this may lead to some unexpected bugs

### PROJECT SETUP
`NOTE`: This project required `git`, `java 8` and `gradle v4.9`  

###### 1. Clone project

###### 2. Install dependencies  
```bash
$ gradle dependencies
```

###### 3. Run project (for development purpose)
```bash
$ cd gradle bootRun
```

###### 4. Visit API web page (Swagger UI)
- Please visit `http://<host>:<port>/swagger-ui.html` by your prefered browser
(ex: `http://localhost:8080/swagger-ui.html`)

###### 5. Run all test cases
```bash
$ gradle test
```

###### 6. Build .jar
```bash
$ cd spring-boot-rest-service
$ gradle build
```  

- After successful build, the output `.jar` file will be stored in `<project root folder>/build/libs` dir

###### 7. Execute .jar (for production deployment purpose)   
```bash
$ java -jar <path/to/.jar>
```

### PROJECT MODIFY 
###### 1. Edit new git remote repository for project 
```bash
$ git init
$ git remote add origin <your git remote repository>
$ git remote -v
```

###### 2. Edit package name
- Rename package `src/main/java/com/spring/baseproject` to `src/main/java/<your/new/package/name>`  
- Edit `rootProject.name` in `settings.gradle` file to your new package name
- Edit `group` in `build.gradle` file to your new package name 
- Edit `BASE_PACKAGE_NAME` in class `./constants/ApplicationConstants` file to your new package name  
- If this project using Spring Data JPA, you need to re-check all @Query annoation inside JpaRepository to find and replace by your package name, 
for example:  
````java
public interface ProductRepository extends JpaRepository<Product, String> {
     @Query("select new com.spring.baseproject" +  // <-- need to edit to your new package name
            ".modules.demo_jpa.models.dtos.ProductPreviewDto(p.id, p.name, p.createdDate, p.tags, pt.id, pt.name) " +
            "from Product p " +
            "left join p.productType pt")
     Page<ProductPreviewDto> getPageProductPreviewDtos(Pageable pageable);
 }
````

### PROJECT STRUCTURE  
##### I. STARTED  
###### 1. Components 
````
 - Spring started web (for REST)
 - Mockito JUnitTest (for unit test)
 - Springfox Swagger 2 (for Doc API)
 - Docker
 - Demo REST API
 - Demo unit test all services
````

###### 2. Cấu trúc thư mục
````
.    
├── readme_assets/                              # (REMOVABLE) Folder contains static resources (image,...) of README.md
├── src/                                   
│   └── main/                                
│   │  ├── java/
│   │  │  └── com/spring/baseproject/           # Package source of project
│   │  │     ├── annotations                     # Contains all project custom @Annotation
│   │  │     ├── base                            # Contains all project base class
│   │  │     │  ├── controllers                   # Contains all project base controller
│   │  │     │  └── models                        # Contains all project base pojo model
│   │  │     ├── components                      # Contains all project custom @Component
│   │  │     ├── configs                         # Contains all project runtime configurations
│   │  │     ├── constants                       # Contains all project static constant value (ex: text response, response code)
│   │  │     ├── events_handle                   # Contains all project event trigger and handler (ex: ContextRefreshedEvent, ContextStartedEvent,...)
│   │  │     ├── exceptions                      # Contains all project custom exceptions
│   │  │     ├── utils                           # Contains all project tools, ultilities,...
│   │  │     └── modules                         # (Core) Contains all project core business module
│   │  │        └── demo                         # (REMOVABLE) Package for demo module
│   │  │           ├── controllers                   
│   │  │           ├── repositories                
│   │  │           ├── services                   
│   │  │           └── models                      
│   │  │              ├── dtos                     
│   │  │              └── entities                
│   │  │                                        
│   │  └── resources/                            # Contains all project static resources
│   │     ├── base/                               # Contains all project static resources in default
│   │     ├── dev/                                # Contains all project static resources for Development env
│   │     ├── prod/                               # Contains all project static resources for Production env
│   │     ├── application.properties              # Default project static configuration file
│   │     ├── application-dev.properties          # Developement env static configuration file (override default config file)
│   │     ├── application-prod.properties         # Production env static configuration file (override default config file)
│   │     └── swagger-info.json                   # Static displayed info on swagger ui page configuration
│   │
│   └── test/                                                               
│      └── java/                              
│         └── com/spring/baseproject/           # Package contains all project unit test  
│            └── demo                           # (REMOVABLE) Package for demo module unit test         
│               ├── controllers                   # Contains all project unit tests for module @RestController   
│               ├── repositories                  # Contains all project unit tests for module @Repository
│               └── services                      # Contains all project unit tests for module @Service
│                                                                               
├── build.gradle                               # Dependencies and build configuration file by Gradle
├── Dockerfile                                 # Dockerfile by Docker
└── README.md   (CÓ THỂ XÓA)                   # README File 
````

###### 3. Project code structure  

![](readme_assets/project-structure.png)  

**Controller** declares all project route mapping, provide Doc API (Swagger), input validation. `Controller` should interact with `Service` for main bussiness processing then response to client. One `Controller` should be coupled with one `Service`  

**Service** is the main business logic processor of project, which should interact with `Repository` to get and query data from datasource. One `Service` can interact with multiple other services, but should be with only one `Repository`  

**Repository** interact with datasource for get, query or save data

Therefore, the project package structure of one module will be
````
.   .  .     .
│   │  │     └── modules                         # (Core) Contains all project core business module
│   │  │        └── <Module name>                  # Package with name by module name
│   │  │           ├── controllers                 # Contains all project @RestController     
│   │  │           ├── repositories                # Contains all project @Repository (ex: JPARepository, CrudRepository, MongoRepository, ...)
│   │  │           ├── services                    # Contains all project @Service
│   │  │           └── models                      # Contains all project pojo model class
│   │  │              ├── dtos                      # Contains all project DTO model class
│   │  │              └── entities                  # Contains all project ORM model class (ex: @Entity of Spring Data JPA, @Document of Spring Data MongoDB,...)
│   │  │  
.   .  .
````

###### 5. Swagger 2 - Code-gen Doc API  
[Swagger](https://swagger.io) is an API Document auto-generated tool. The document will be generated by code automatically, therefore it's good for maintain and make your life easier. Swagger using java annotation to scan and find all declared route path of all project controller, then it will visualize Doc Api to one ui web page

After starting this project, visit `http://<host>:<port>/swagger-ui.html` on your browser to get access to swagger ui page
 
![](readme_assets/swagger-ui.png)  

This project has been modified some aspect of Swagger for more practical development purpose, it made some minor changes to  
some components behavior compare with the original swagger documentation. Here is the detail usage example
````java
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.spring.baseproject.annotations.swagger.Response;//Custom annotation
import com.spring.baseproject.annotations.swagger.Responses;//Custom annotation

@RestController
@RequestMapping("/api/foo")
@Api(description = "Simple description about this api group")
class FooController extends BaseRESTController {
    @ApiOperation(value = "Name of this api",
                notes = "Note/explaination/detailed description about this api (if necessary)",
                response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = ResponseValue.SUCCESS, responseBody = ResponseModelClass.class),//<- POJO Class of api response (if have)
            @Response(responseValue = ResponseValue.FOO_NOT_FOUND) //<- Enum response value (httpStatus, specialCode, message)
    })
    @GetMapping()
    public BaseResponse getFoos() {
        
    }
}
````

`LƯU Ý`:
- IN THIS PROJECT, Swagger was modified to ONLY SCAN ALL @RestController inside project every package `modules/<Module name>/controllers`, 
therefore you must be aware to put all @RestController to correct package position (if not, you will not see your api displayed on swagger ui page)
- Each `ResponseValue` with the same `specialCode` will replace each other when displayed on swagger ui, so you should not  
create 2 difference `ResponseValue` with the same `specialCode` value  

##### II. SPRING DATA JPA  
[Spring Data JPA](https://spring.io/projects/spring-data-jpa) (Java Persistence API) is a library of group `Spring Data`, which belong to `Spring Framework`. It help 
the datasource interaction in your code more simplier and life easier. With it, you won't need to implement manually every single data access instance for each type of difference datasources. To archive this, JPA provides its own data query language, quite similar with `SQL`, which is called `JPAQL`. In execution, `JPAQL` will be transformed to correct native SQL query of each used datasource

This document will only show JPA setup instructions with MySQL datasource

###### 1. Components
````
 - Started Project
 - Spring Data JPA
```` 

###### 2. Package structure
````
.   .  .    .
│   │  │    ├── utils                           
│   │  │    |   └── jpa                       # (NEW) Add util support pagination query      
.   .  .    .
│   │  │    └── modules  
.   .  .        .                       
│   │  │        └── demo_jpa                  # (REMOVABLE) Module demo for Spring Data JPA                   
│   │  │           ├── controllers                                
│   │  │           ├── repositories                
│   │  │           ├── services                   
│   │  │           └── models                      
│   │  │              ├── dtos                     
│   │  │              └── entities                
│   │  │                                        
│   │  └── resources/                          
.   .     .
│   │     ├── application.properties          # (MODIFIED) Add some configs for datasource and Hibernate ORM
│   .      .
│                                                                     
├── build.gradle                              # (MODIFIED) Add Spring Data dependencies
.
````

###### 3. Gradle dependency  
 ````
 ...
 dependencies {
     ...
     // [jpa] Spring Data JPA - Connect SQL DBMS
     implementation('org.springframework.boot:spring-boot-starter-data-jpa')
     runtime('mysql:mysql-connector-java')
     ...
 }
 ````

###### 4. Configuration  
All The JPA configurations were in `application.properties` file  
````
...
# [jpa] JPA configuration
spring.jpa.hibernate.ddl-auto=(create|create-drop|update|none|validate)
spring.jpa.hibernate.naming.implicit-strategy=org.hibernate.boot.model.naming.ImplicitNamingStrategyLegacyJpaImpl
spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL55Dialect
spring.datasource.url=jdbc:mysql://(datasource host):(datasource port)/(database name)?useUnicode=yes&characterEncoding=UTF-8&autoReconnect=true&useSSL=false&createDatabaseIfNotExist=true
spring.datasource.username=(datasource username)     # username access datasource
spring.datasource.password=(datasource password)     # password access datasource

# [jpa] JPA query logging configuration
spring.jpa.show-sql=(true|false)     # Enable console log for native query which was the transformation result of JPAQL every query execution
spring.jpa.properties.hibernate.format_sql=(true|false)     # Enable native query beautifier on console log
...
````

###### 5. Datasource Approaching Method
###### Code first  
This module `demo_jpa` was currently implemented by this approaching method. The main concept of `Code first` method is using only code to control all actions, structures of datasource interaction, from create, update schema, entity relationships to query execution. This abstraction will help developer focus to only main bussiness of application than care about used datasource or dbms

To enable this `Code first` approaching method, you should edit this config value below inside (in `application.properties` file) to like this
````
...
spring.jpa.hibernate.ddl-auto=(create|update|create-drop)
...
````

###### Database first  
Opposite from `Code first`, the key concept of `Database first` method is the database design must be done at first, then JPA 
is just used as a mapping method between all data unit of datasource to every `Entity` instance in code.

To enable this approaching method, just edit this config value below (in `application.properties` file) to like this
````
...
spring.jpa.hibernate.ddl-auto=none
...
````

And please make sure your datasouorce schema was created from start

###### 6. Demo JPA  

![](readme_assets/demo_jpa_schema.png)

**Product** identify code (id),  product name (name), size type (product_size), hashtag (tags), 
type id (product_type_id) , product description (description), product created date (created_date)  

This is the sample of `Product` Entity class in JPA
````java
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")// auto generated uuid
    @GeneratedValue(generator = "uuid")
    @Column(name = "id", length = 36)
    private String id;
    @Column(name = "name", nullable = false)
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(name = "product_size", columnDefinition = "TEXT")// using enum for product size
    private ProductSize productSize;
    @Column(name = "created_date")
    private Date createdDate;
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Convert(converter = JsonListConverter.class)
    private List<String> tags;

    @OneToOne( // 1 - 1 relationship with ProductType entity
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "product_type_id")
    private ProductType productType;
}

public enum ProductSize {
    SM, M, L, XL, XXL
}
````

**ProductType** identity code (id), product type name (name)  

This is the sample of `ProductType` Entity class in JPA
````java
@Entity
@Table(name = "product_type")
public class ProductType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)// auto increasement id
    @Column(name = "id")
    private int id;
    @Column(name = "name", nullable = false)
    private String name;
}
````

Please visit swagger ui for api demo  

###### 7. How to remove
- Remove all group with prefix `[jpa]` in `application.properties` and `build.gradle` file
- Remove all these packages:
 + `utils/jpa`
 + `modules/demo_jpa`

##### III. Spring Security + OAuth2 + JWT  
[OAuth2](https://oauth.net) is one of popular authentication method. With it, a web service or an third-party application can be on behalf of end users to access to every their owned resources on server belong to other service providers, to archive this, your application need to acquire authorization grants from end users via target service provider. 

![OAuth2](readme_assets/oauth2.png)

[JWT](https://jwt.io) `JSON Web Token (JWT) is an open standard (RFC 7519) that defines a compact and self-contained way for securely transmitting information between parties as a JSON object. This information can be verified and trusted because it is digitally signed. JWTs can be signed using a secret (with the HMAC algorithm) or a public/private key pair using RSA or ECDSA.`

Ths module requires Spring Data JPA to save user data

###### 1. Components   
````
 - Started Project
 - Spring Data JPA (for save user data)
 - Spring starter security
 - OAuth2
 - JWT (Json Web Token)
```` 

###### 2. Package structure
````
.   .  .    .
│   │  │    ├── annotations                           
│   │  │    │  └── auth                      # (NEW) Add annotation @AuthorizationRequired
│   │  │    │
│   │  │    ├── components                           
│   │  │    │  └── auth                      # (NEW) Add 2 custom swagger-ui components
│   │  │    │
│   │  │    ├── utils                           
│   │  │    │  └── auth                      # (NEW) Add util to support scan all mapping api route 
│   │  │    │
│   │  │    ├── configs                           
│   │  │    │  └── auth                      # (NEW) Add some runtime configs for OAuth2, JWT and Swagger   
│   │  │    │
│   │  │    ├── exceptions                           
│   │  │    │  └── auth                      # (NEW) Add some custom exceptions for OAuth2       
│   │  │    │
│   │  │    └── modules  
│   │  │        └── auth                     # (NEW) Module authentication/authorization for user                
│   │  │        │   ├── controllers                                
│   │  │        │   ├── repositories                
│   │  │        │   ├── services                   
│   │  │        │   └── models                      
│   │  │        │      ├── dtos                     
│   │  │        │      └── entities   
│   │  │        │              
│   │  │        └── demo_auth                # (REMOVABLE) Module demo for get user perfonal info which is required user authorization               
│   │  │           ├── controllers                                
│   │  │           ├── repositories                
│   │  │           ├── services                   
│   │  │           └── models                      
│   │  │              ├── dtos                     
│   │  │              └── entities                
│   │  │                                        
│   │  └── resources/                          
│   │     └── application.properties        # (MODIFIED) Add config for OAuth2, JWT
│   . 
.   .  
│                                                                     
├── build.gradle                            # (MODIFIED) Add Spring Security, JWT, OAuth2 dependency
.
````

###### 3. Gradle dependency  
 ````
 ...
 dependencies {
     ...
     // [auth] OAuth2 + JWT for Authentication and Authorization
     implementation('org.springframework.boot:spring-boot-starter-security')
     testImplementation('org.springframework.security:spring-security-test')
     implementation('org.springframework.security.oauth:spring-security-oauth2:2.0.8.RELEASE')
     implementation('org.springframework.security:spring-security-jwt:1.0.0.RELEASE')
     implementation('io.jsonwebtoken:jjwt:0.9.0')
     ...
 }
 ````

###### 4. Configuration  
All the confgurations for OAuth2 + JWT is in `application.properties` file  
````
...
# [auth] OAuth2 + JWT for Authentication and Authorization configuration
application.oauth2.resource-server.id=oauth2_resource_server_id        # id of resource server
application.oauth2.authorization-server.trusted-client.web.id=trusted_client                 # client id of client application
application.oauth2.authorization-server.trusted-client.web.secret=trusted_client_secret      # client secret of client application
application.oauth2.authorization-server.access-token.validity-seconds=604800     # the validity duration in seconds of generated access token
application.oauth2.authorization-server.refresh-token.validity-seconds=5184000   # the validity duration in seconds of generated refresh token
application.oauth2.authorization-server.token-signing-key=secret_sgn_key         # the secret code using for create token signature
application.security.password-hashing=noop      # the password hashing algorithm (bcypt, pbkdf2, scrypt, sha256) was applied. the value `noop` is mean: no password hashing                                                         algorithm (for development/testing purpose - should not using on production)
...
````

###### 5. Features
`Note`: Please visit swagger ui page for more api detail and try out 
The module `auth` will provide some features below

**I. User registration**  
1. New user registration
    ````
        PATH: /api/users/registration
        METHOD: POST
        REQUEST BODY:
        {
            "username": <user username>,
            "password": <user password>
        }
    ````
**II. User authentication**  
1. User authentication by username password
    ````
        PATH: /api/authentication/username-password
        METHOD: POST
        REQUEST HEADER: 
            - Authorization: Basic <Base64(<client id>:<client secret>)>
        REQUEST BODY:
        {
            "username": <user username>,
            "password": <user password>
        }
    ````
    Ex:  
    ````
        URL: POST http://locahost:8080/api/authentication/username-password
        REQUEST HEADER: 
            - Authorization: Basic c3dhZ2dlcl91aV9jbGllbnQ6c3dhZ2dlcl91aV9zZWNyZXQ=
        REQUEST BODY:
        {
            "username": "tungtt",
            "password": "123456"
        }
    ````
    The response body after successful verification will be  
    ````
    {
        "code": 200,
        "msg": "success",
        "data": {
            "userID": <id of user>,
            "tokenType": <type of generated token>,
            "jti": <id of generated token>,
            "accessToken": <user access token>
            "refreshToken": <user refresh token>
            "accessTokenExpSecs": <access token validity duration in seconds>,
            "refreshTokenExpSecs": <refresh token validity duration in seconds>
        }
    }
    ````    

2. User authentication by refresh token  
    ````
        PATH: /api/authentication/refresh-token
        METHOD: POST
        REQUEST HEADER: 
            - Authorization: Basic <Base64(<client id>:<client secret>)>
        REQUEST BODY:
        {
            "refreshToken": <user active refresh token>
        }
    ````
    The response after successful verification will be the same as `User authentication by username password`
    
**III. Access required user authorization**
1. Get user personal info (demo_auth)   
    This Api will return user personal info, but it required user authorization (by access token)
    ````
        PATH: /api/auth-demo/users/info
        METHOD: GET
        REQUEST HEADER: 
            - Authorization: <token type> <user access token>
    ````
    Ex:
    ````
        URL: GET http://localhost:8080/api/auth-demo/users/info
        REQUEST HEADER: 
         - Authorization: bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOlsib2F1dGgyX3Jlc291cmNlX3NlcnZlcl9pZCJdLCJ1c2VyX2lkIjoiNW
                                 ZkZDM5MTMtMzBhNS00OTE1LWFhNWUtMTJiMjM0NTFkZGUzIiwidXNlcl9uYW1lIjoidHVuZ3R0Iiwic2NvcGUiO
                                 lsicmVhZCIsIndyaXRlIl0sImV4cCI6MTU1MzA5OTE4OCwianRpIjoiZjUzZmJlMjAtNGQzMi00ZmU0LTljMzMt
                                 NDdlYzIzYzJkMzg3IiwiY2xpZW50X2lkIjoic3dhZ2dlcl91aV9jbGllbnQifQ.jjPulNOK_ibmd6rZYlK0Cm9M
                                 qVe67pSvz2bKd2agIAw
    ````
    

###### 7. For developer
**I. Using `@AuthorizationRequired` annotation**  

This annotation was used to mark one api / api group as `access is required user authorization`
````java

@AuthorizationRequired    // <- annotate to controller class to apply to all api inside this controller 
@RestController           //    which required user authorization for access
@RequestMapping("/api/auth-demo/users")
@Api(description = "User account information")
public class UserProfileController extends BaseRESTController {

    @Autowired
    private UserProfileService userProfileService;

    @ApiOperation(value = "Get user personal info",
            notes = "Return user all personal information, " +
                    "Required user authorization by access token",
            response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = ResponseValue.SUCCESS, responseBody = UserDtoSwagger.class),
            @Response(responseValue = ResponseValue.USER_NOT_FOUND)
    })
    @GetMapping("/info")
    public BaseResponse getUserProfile() {
        return userProfileService.getUserProfile(getAuthorizedUser().getUserID());
    }
    
    @AuthorizationRequired   // <-  or you can annotate to every single api
    @GetMapping("/foo/bar")
    public BaseResponse getFooBar() {
        return new BaseResponse(ResponseValue.SUCCESS);
    }
}
````

**II. How to verify user authorization on swagger-ui**

For all user authorization required apis, when visualized on swagger ui page, it will have a red warning icon on the top right of api detail

![](readme_assets/swagger-ui-auth-warning.png)

To verify user authorization by token, please take a look at the instruction on the image below step by step

![](readme_assets/swagger-ui-auth-pass-warning.png)

###### 8. How to remove  
- Remove all group with comment prefix `[auth]` in `application.properties` and `build.gradle` file
- Remove all packages:
  + `modules/auth`
  + `modules/demo_auth`
  + `configs/demo_auth`
  + `utils/auth`
  + `exceptions/auth`
  + `annotations/auth`
  + `components/auth`
  
##### IV. Role-base Access Control  
[Role-base Access Control  ](https://en.wikipedia.org/wiki/Role-based_access_control) 
`(Role-Based Access Control - RBAC)  is an approach to restricting system access to authorized users`

In an organization, there are multiple Role which was created to control and limit actions of each person. Every role was binding to some specific permissions, each permission will represent to one action. Every person will be assigned to one role, so they can do some in-controled action in one system.

![](readme_assets/RBAC-diagram.png)

RBAC in this project was implemented using the base of module `Spring Security + OAuth2 + JWT`

###### 1. Components  
````
 - Started Project
 - Spring Data JPA (for save role - api)
 - Spring Security + OAuth2 + JWT  
 - Role base access control
```` 

###### 2. Package structure
````
.   .  .    .
│   │  │    ├── annotations                           
│   │  │    │  └── rbac                      # (NEW) Add annotation @RoleBaseAccessControl
│   │  │    │
│   │  │    ├── components                           
│   │  │    │  └── rbac                      # (NEW) Add some component support config and validate RBAC
│   │  │    │
│   │  │    ├── events_handle                           
│   │  │    │  └── rbac                      # (NEW) Add some runtime event trigers to handle some initial of RBAC  
│   │  │    │
│   │  │    ├── exceptions                           
│   │  │    │  └── rbac                      # (NEW) Add some custom exceptions of RBAC       
│   │  │    │
│   │  │    └── modules  
│   │  │        └── rbac                     # (NEW) Core module of RBAC               
│   │  │        │   ├── controllers                                
│   │  │        │   ├── repositories                
│   │  │        │   ├── services                   
│   │  │        │   └── models                      
│   │  │        │      ├── dtos                     
│   │  │        │      └── entities   
│   │  │        │              
│   │  │        └── demo_rbac                # (REMOVABLE) Module demo of RBAC            
│   │  │           ├── controllers                                
│   │  │           ├── repositories                
│   │  │           ├── services                   
│   │  │           └── models                      
│   │  │              ├── dtos                     
│   │  │              └── entities                
│   │  │                                        
│   │  └── resources/  
│   │     ├── rbac/                         # (MODIFIED) Add some configuration config files to RBAC                         
│   │     └── application.properties        # (MODIFIED) Add config of RBAC
│   . 
.   .  
````

###### 5. Gradle dependency  
- None

###### 6. Configuration  
The configuration of RBAC is in `application.properties` file

````
...
# [rbac] Role-base access control configuration
application.rbac.refresh=false    # enable RBAC re-scan and update new added api (set = true when there are any  added/updated/deleted apis)
application.rbac.http-methods.path=rbac/http-methods.json    # path to config file http-methods.json
application.rbac.rbac-started-roles-users.path=rbac/rbac-started-roles-users.json    # path to config file rbac-started-roles-users.json
application.rbac.rbac-modules-description.path=rbac/rbac-modules-description.json    # path to config file rbac-modules-description.json
...
````

###### 7. Features
**I. Initial RBAC**
Scan and save all apis which is required RBAC
    When service started, RBAC will scan all project package `controllers` of each module find every apis that required RBAC, then save to database
    
    `NOTE`:
    - All `@Controller` or `@RestController` class if placed outside package `controllers` of module WILL BE IGNORED BY RBAC. So please make sure to place your Controller class to correct package 
  
**II. RBAC core module**  
Visit swagger ui for api detail and try out
1. RBAC Structure
  RBAC was build base on the relationship between 3 entity: User - Role - Api  
  - 1 user only has 1 role (1 - 1)
  - 1 role can contains multiple allowed api (1 - n)
  - 1 api can be allowed by multiple role (1 - n)  

  Database schema  
  
  ![](readme_assets/RBAC-schema.png)
  
  In this project RBAC, there is a special Role, called `ROOT`. The `ROOT` Role will get access to all available apis without any limitation, 
  and you CAN NOT assign permissions to `ROOT` Role
  
2. List all RBAC api
    Api required RBAC Authorization by access token
    ````
        PATH: /api/auth/rbac/apis
        METHOD: GET
        REQUEST HEADER: 
            - Authorization: <token type> <user access token>
    ````  
    
3. List all allowed apis of assigned to one role
    Api required RBAC Authorization by access token. By default, only `ROOT` user has access right
    ````
        PATH: /api/auth/rbac/roles/{role id}/apis
        METHOD: GET
        REQUEST HEADER: 
            - Authorization: <token type> <user access token>
    ````
    
    The response when user has no access permission
    ````
    HTTP STATUS: 401 Unauthorized
    RESPONSE BODY
    {
        "code": 4017,
        "msg": "access denied",
        "data": null
    }
    ````

4. Assign/Revoke assigned apis of one role (Assign/Revoke access right)
    Api required RBAC Authorization by access token. By default, only `ROOT` user has access right
    There is no need to re-authentication after assign/revoke api of role
    
     ````
        PATH: /api/auth/rbac/roles/{role id}/apis
        METHOD: PUT
        REQUEST HEADER: 
            - Authorization: <token type> <user access token>
        REQUEST BODY:
        [id của của api được phép truy cập]
     ````

###### 8. For developer 

**I. Configuration file in `resources/rbac`** 
Every files in `rousources/rbac` are both initial configuration files for RBAC, which includes:
- `http-methods.json` this config file will provide HTTP METHOD default ordering by priority (ASC) 

```json
{
  "GET": 1,
  "HEAD": 2,
  "POST": 3,
  "PUT": 4,
  "PATCH": 5,
  "DELETE": 6,
  "OPTIONS": 7,
  "TRACE": 8
}
```

- `rbac-modules-description.json` this config file will provide module metadata, include modulle description 
and priority (for ordering)  

```json
{
  "rbac": {
    "description": "Role-base Access Control",
    "priority": 1
  },
  "demo_rbac": {
    "description": "Demo features of Role-base Access Control",
    "priority": 2
  }
}
```
    
- `rbac-started-roles-users.json` this config file will provide all started Role and User of RBAC, 
include 1 role of group `ROOT`, 1 role of group `ADMIN` and 1 user with role = `ROOT`  

```json
{
  "started_roles": {
    "Root": {
      "type": "ROOT"
    },
    "Admin": {
      "type": "ADMIN"
    }
  },
  "started_users": {
    "root": {
      "password": "123456",
      "role_name": "Root"
    }
  }
}
```

**II. Using `@RoleBaseAccessControl` annotation**  

This annotation was extended from `@AuthorizationRequired` annotation. Therefore, it will **include all available features** of `@AuthorizationRequired`

This annotation was used to mark one api / api group as `access is required user RBAC authorization`
````java

@RoleBaseAccessControl    // <- annotate to controller class to apply to all api inside this controller 
@RestController           //    which required user RBAC authorization for access
@RequestMapping("/api/auth/rbac")
@Api(description = "Quản lý quyền truy cập")
public class ApiController extends BaseRESTController {
    
    @RoleBaseAccessControl   // <-  or you can annotate to every single api
    @GetMapping("/foo")
    public BaseResponse getFoo() {
        return new BaseResponse(ResponseValue.SUCCESS);
    }
    
    // You can declare default access for role groups (without ROOT), 
    // ↓ 
    @RoleBaseAccessControl(defaultAccess = {RoleType.ADMIN}) 
    @GetMapping("/bar")
    public BaseResponse getFooBar() {
        return new BaseResponse(ResponseValue.SUCCESS);
    }
}
````

**III. Try out RBAC api on swagger-ui**

All RBAC required api on swagger can by try out with the same approach as `How to verify user authorization on swagger-ui`

###### 8. Hot to remove  
- Remove all groups which have comment prefix `[rbac]` in `application.properties` and `build.gradle` file
- Remove all packages:
  + `modules/rbac`
  + `modules/demo_rbac`
  + `annotations/rbac`
  + `components/rbac`
  + `exceptions/rbac`
  + `events_handle/rbac`
  + `resources/rbac`

##### V. Firebase
[Firebase](https://firebase.google.com) `Firebase is a platform developed by Google for creating mobile and web applications`

![](readme_assets/firebase-logo.png)

###### 1. Components   
````
 - Started Project
 - Firebase admin
```` 

###### 2. Package structure  
````
.   .  .    .
│   │  │    ├── configs                           
│   │  │    │  └── firebase                  # (NEW) Add firebase connection config    
│   │  │    │
│   │  │    └── modules  
│   │  │        └── firebase                 # (NEW) Module core include all interaction service with firebase
│   │  │        └── demo_firebase            # (NEW)(REMOVABLE) Add module demo upload file to firebase storage            
│   │  │            ├── controllers                                
│   │  │            ├── repositories                
│   │  │            ├── services                   
│   │  │            └── models                      
│   │  │               ├── dtos                     
│   │  │               └── entities                       
│   │  │                                        
│   │  └── resources/                          
│   │     └── application.properties        # (MODIFIED) Add firebase config
│   . 
.    
│                                                                     
├── build.gradle                            # (MODIFIED) Add firebase dependencies
.
````

###### 2. Gradle dependency  
 ````
 ...
 dependencies {
     ...
     // [firebase] Firebase
     implementation ('com.google.api-client:google-api-client:1.23.0')
     implementation ('com.google.firebase:firebase-admin:5.11.0')
     implementation('com.google.guava:guava:20.0')
     ...
 }
 ````

###### 3. Configuration  
All firebase configuration is in `application.properties` file
````
...
# [firebase] Firebase admin configuration
application.firebase.google-services.path=base/google-services.json   # path to config file google-services.json
application.firebase.fcm.legacy-server-key=AIzaSyDiJ9DLhe-BA_2W0mQElnqELlYl89wVbz0   # legacy server key of firebase project
application.firebase.fcm.api=https://fcm.googleapis.com/fcm/send   # push notification api of firebase
application.firebase.database.url=https://base-firebase-project-d8945.firebaseio.com   # database url of firebase project
application.firebase.storage.bucket=base-firebase-project-d8945.appspot.com   # storage bucket url of firebase project
application.firebase.storage.api=http://storage.googleapis.com   # google storage api
...
````

![How to get firebase legacy server key](readme_assets/firebase-console-legacy-server-key.png)

![How to get firebase config file and database url](readme_assets/firebase-console-service-accounts.png)

![How to get firebase storage bucket](readme_assets/firebase-console-storage-bucket.png)

###### 6. Features
1. Firebase authentication
2. Firebase storage
3. Firebase real-time database / Firestore
4. Firebase cloud messaging  

###### 7. Demo firebase 
Visit swagger ui page, section `demo_firebase`  

###### 8. How to remove  
- Remove all groups which have comment prefix `[firebase]` in `application.properties` and `build.gradle` file
- Remove `google-service.json`
- Remove all packages:
  + `configs/firebase`
  + `modules/firebase`
  + `modules/demo_firebase`
  



