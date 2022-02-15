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
│   │     ├── application-prod.properties         # Production env static configuration file (override file config mặc định)
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
[Spring Data JPA](https://spring.io/projects/spring-data-jpa) (Java Persistence API) là a library of group `Spring Data`, belong to `Spring Framework`. It help 
the datasource interaction in code more simplier and easier for maintain. You don't need to implement every unique data access instance for each type of difference datasources. 
JPA provides a data query language similar with `SQL`, called `JPAQL`. In execution, `JPAQL` will be transformed to correct native SQL query of each used datasource

This document will only show JPA setup instructions with MySQL datasource

###### 1. Components
````
 - Started Project
 - Spring Data JPA
```` 

###### 2. Package structure
````
.   .  .     .
│   │  │     ├── utils                           
│   │  │     .  └── jpa                       # (NEW) Add util support pagination query      
.   .  .    .
│   │  │     ├── swagger                      
│   │  │     │  └── demo_jpa                  # (NEW)(REMOVABLE) Thêm các swagger model cho module [demo_jpa]
.   .  .    .
│   │  │    └── modules  
.   .  .        .                       
│   │  │        └── demo_jpa                  # (CÓ THỂ XÓA) Code demo Spring Data JPA                   
│   │  │           ├── controllers                                
│   │  │           ├── repositories                
│   │  │           ├── services                   
│   │  │           └── models                      
│   │  │              ├── dtos                     
│   │  │              └── entities                
│   │  │                                        
│   │  └── resources/                          
.   .     .
│   │     ├── application.properties          # (MODIFIED) Thêm config datasource và Hibernate ORM
│   .      .
│                                                                     
├── build.gradle                              # (MODIFIED) Thêm dependency
.
````

###### 3. Các thành phần có thể xóa  
- Xem tại cấu trúc thư mục  

###### 4. Gradle dependency  
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

###### 5. Configuration  
Cấu hình JPA được đặt trong `application.properties`  
````
...
# [jpa] JPA configuration
spring.jpa.hibernate.ddl-auto=(create|create-drop|update|none|validate)
spring.jpa.hibernate.naming.implicit-strategy=org.hibernate.boot.model.naming.ImplicitNamingStrategyLegacyJpaImpl
spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL55Dialect
spring.datasource.url=jdbc:mysql://(datasource host):(datasource port)/(database name)?useUnicode=yes&characterEncoding=UTF-8&autoReconnect=true&useSSL=false&createDatabaseIfNotExist=true
spring.datasource.username=(datasource username)     # username truy cập datasource
spring.datasource.password=(datasource password)     # password truy cập datasource

# [jpa] JPA query logging configuration
spring.jpa.show-sql=(true|false)     # Log trên console native query được tranform từ JPAQL mỗi khi một truy vấn được thực thi
spring.jpa.properties.hibernate.format_sql=(true|false)     # Làm đẹp native query được log trên console
...
````

###### 6. Hướng tiếp cận  
###### Code first  
Module `demo_jpa` được phát triển theo hướng tiếp cận này. Tư tưởng của `Code first` là sử dụng code để có thể kiểm soát 
mọi hoạt động của database, từ việc tạo, cập nhật schema, các quan hệ cho đến thực hiện các truy vấn. Như vậy developer 
sẽ không cần phải làm việc trực tiếp với dbms

Để enable chế độ này, cần set lại giá trị cho biến config sau trong `application.properties`
````
...
spring.jpa.hibernate.ddl-auto=(create|update|create-drop)
...
````

###### Database first  
Trái ngược với `Code first`, tư tưởng của `Database first` là việc database phải được tạo xong từ trước, sau đó JPA 
chỉ đóng vai trò mapping giữa các table trong databse vào các thực thể `Entity` trong code. `Database first` nên được 
sử dụng khi dữ liệu cần được ràng buộc chặt chẽ ở tầng cơ sở dữ liệu

Để enable chế độ này, cần set lại giá trị cho biến config sau trong `application.properties`
````
...
spring.jpa.hibernate.ddl-auto=none
...
````

Và chắc chắn rằng schema đã được tạo, nếu không JPA sẽ báo lỗi binding tại thời điểm runtime

###### 7. JPA Dynamic Query
JPA mặc định hỗ trợ một cách thức tao JPAQL, đó định nghĩa trực tiến query bên trong interface `JPARepository`. 
Tuy nhiên cách thức này lại mang đến một sự hạn chế, đó là khả năng tùy chỉnh các query dựa theo từng trường hợp khác nhau.

Ex: Tạo api lấy sách nhân viên, có hỗ trợ lọc theo `Họ`, `Tên`, `Ngày sinh`, `Chức vụ`. Vì là lọc nên các thông tin 
trên đều ở trạng thái optional, có thể có mà cũng có thể không được truyền lên. Do đó query truy vấn cũng sẽ thay đổi 
truy theo số lượng tham số được truyền lên. Việc định nghĩa query trong `JPARepository` lúc này không còn khả quan

Thật may là JPA có hỗ trợ việc tạo các JPAQL dưới dạng raw string và truyền vào một đối tượng Executor để thực thi 
truy vấn, truy nhiên làm việc với raw string query thì thực sự rất phiền phức, dễ lỗi cú pháp, ngoài ra còn tiểm tàng 
nguy cơ dính phải SQLInjection nếu thiếu cẩn thận

`JPA Dynamic Query` là một tool được tạo ra để giải quyết các vấn đề trên, tool chỉ có nhiện vụ hỗ trợ sinh raw query string 
tự động theo ý lập tình viên, giảm thiểu lỗi cú pháp và lỗi bảo mật.

Tool được đặt tại `utils/jpa` và chỉ gồm hai thành phần: 
- `JPAQueryBuilder` giúp sinh raw query string
- `JPAQueryExecutor` giúp thực thi raw query được sinh ra bởi `JPAQueryBuilder`. Có hỗ trợ truy vấn phân trang

Tài liệu hướng dẫn các sử dụng có thể tham khảo tại [đây](https://gitlab.com/worksvn-dev-team/developer-tools/jpa-query-builder)

###### 8. Demo JPA  

![](readme_assets/demo_jpa_schema.png)

**Product** Sản phầm, bao gồm: mã định danh (id), tên (name), loại size (product_size), thẻ (tags), 
product_type_id (mã loại sản phẩm) , mô tả (description), ngày tạo (created_date)  

Cấu trúc của một `Product` trong JPA
````java
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")// uuid tự sinh
    @GeneratedValue(generator = "uuid")
    @Column(name = "id", length = 36)
    private String id;
    @Column(name = "name", nullable = false)
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(name = "product_size", columnDefinition = "TEXT")// sử dụng enum
    private ProductSize productSize;
    @Column(name = "created_date")
    private Date createdDate;
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Convert(converter = JsonListConverter.class)
    private List<String> tags;

    @OneToOne( // quan hệ 1 - 1 với ProductType
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

**ProductType** Loại sản phẩm, bao gồm: mã định danh (id), tên (name)  

Cấu trúc của một `ProductType` trong JPA
````java
@Entity
@Table(name = "product_type")
public class ProductType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)// id tự động tăng
    @Column(name = "id")
    private int id;
    @Column(name = "name", nullable = false)
    private String name;
}
````

Truy cập swagger-ui để xem danh sách cách api demo  

###### 9. Uninstall  
- Xóa các nhóm được comment `[jpa]` trong 02 file `application.properties` và `build.gradle`
- Xóa 03 package:
 + `utils/jpa`
 + `modules/demo_jpa`
 + `swagger/demo_jpa`

##### III. Spring Security + OAuth2 + JWT  
[OAuth2](https://oauth.net) là một phương thức chứng thực. Nhờ nó, một web service hay một application 
bên thứ 3 (third-party) có thể đại diện cho người dùng để truy cập vào tài nguyên của họ nằm trên một dịch vụ nào đó  

![Mô hình OAuth2](readme_assets/oauth2.png)

[JWT](https://jwt.io) là một chuẩn mở (RFC 7519) định nghĩa một cách nhỏ gọn và khép kín để truyền một cách an toàn 
thông tin giữa các bên dưới dạng đối tượng JSON. Thông tin này có thể được xác minh và đáng tin cậy vì nó có chứa 
chữ ký số. JWTs có thể được ký bằng một thuật toán bí mật (với thuật toán HMAC) hoặc một public / private key 
sử dụng mã hoá RSA.

Module này yêu cầu Spring Data JPA, do cần lưu trữ dữ liệu người dùng

###### 1. Thành phần   
````
 - Started Project
 - Spring Data JPA (for storing user info)
 - Spring starter security
 - OAuth2
 - JWT (Json Web Token)
```` 

###### 2. Cấu trúc thư mục  
````
.   .  .    .
│   │  │    ├── annotations                           
│   │  │    │  └── auth                      # (NEW) Thêm annotation @AuthorizationRequired
│   │  │    │
│   │  │    ├── components                           
│   │  │    │  └── auth                      # (NEW) Thêm hai component custom swagger-ui
│   │  │    │
│   │  │    ├── utils                           
│   │  │    │  └── auth                      # (NEW) Thêm util hỗ trợ scan các mapping route 
│   │  │    │
│   │  │    ├── configs                           
│   │  │    │  └── auth                      # (NEW) Thêm các runtime config cho OAuth2, JWT và Swagger   
│   │  │    │
│   │  │    ├── exceptions                           
│   │  │    │  └── auth                      # (NEW) Thêm một số custom exception của OAuth2       
│   │  │    │
│   │  │    ├── swagger    
│   │  │    │  ├── auth                      # (NEW) Thêm các swagger model cho module [auth]                  
│   │  │    │  └── demo_auth                 # (NEW)(CÓ THỂ XÓA) Thêm các swagger model cho module [demo_auth]
│   │  │    │
│   │  │    └── modules  
│   │  │        └── auth                     # (NEW) Module xác thực (authentication) người dùng                
│   │  │        │   ├── controllers                                
│   │  │        │   ├── repositories                
│   │  │        │   ├── services                   
│   │  │        │   └── models                      
│   │  │        │      ├── dtos                     
│   │  │        │      └── entities   
│   │  │        │              
│   │  │        └── demo_auth                # (CÓ THỂ XÓA) Code demo api lấy thông tin người dùng yêu cầu xác thực               
│   │  │           ├── controllers                                
│   │  │           ├── repositories                
│   │  │           ├── services                   
│   │  │           └── models                      
│   │  │              ├── dtos                     
│   │  │              └── entities                
│   │  │                                        
│   │  └── resources/                          
│   │     └── application.properties        # (MODIFIED) Thêm config OAuth2, JWT
│   . 
.   .  
│                                                                     
├── build.gradle                            # (MODIFIED) Thêm dependency
.
````

###### 3. Các thành phần có thể xóa  
- Xem tại cấu trúc thư mục  

###### 4. Gradle dependency  
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

###### 5. Configuration  
Cấu hình OAuth2 + JWT được đặt trong `application.properties`  
````
...
# [auth] OAuth2 + JWT for Authentication and Authorization configuration
application.oauth2.resource-server.id=oauth2_resource_server_id        # id của resource server trong mô hình OAuth2
application.oauth2.authorization-server.trusted-client.web.id=trusted_client                 # id của client application trong mô hình OAuth2
application.oauth2.authorization-server.trusted-client.web.secret=trusted_client_secret      # mã bí mật của client application trong mô hình OAuth2
application.oauth2.authorization-server.access-token.validity-seconds=604800     # thời gian hiệu lực (giây) của access token được sinh
application.oauth2.authorization-server.refresh-token.validity-seconds=5184000   # thời gian hiệu lực (giây) của refresh token được sinh
application.oauth2.authorization-server.token-signing-key=secret_sgn_key         # mã bí mật dùng để làm token signature
application.security.password-hashing=noop      # thuật toán hash password được áp dụng (bcypt, pbkdf2, scrypt, sha256). giá trị `noop` là không áp dụng thuật toán hashing
...
````

###### 6. Tính năng
`Note`: Xem chi mô tả chi tiết tại swagger-ui  
Module `auth` cung cấp các tính năng  
**I. Đăng ký (Registration)**  
1. Đăng ký người dùng mới
    ````
        PATH: /api/users/registration
        METHOD: POST
        REQUEST BODY:
        {
            "username": <tên đăng nhập người dùng>,
            "password": <mật khẩu người dùng>,
            "roleID": <quyền của người dùng (chưa impliment), để null>
        }
    ````
**II. Xác thực người dùng (Authentication)**  
1. Xác thực bằng username password
    ````
        PATH: /api/authentication/username-password
        METHOD: POST
        REQUEST HEADER: 
            - Authorization: Basic <Base64(<id của client application>:<mã bí mật của client application>)>
        REQUEST BODY:
        {
            "username": <tên đăng nhập người dùng>,
            "password": <mật khẩu người dùng>
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
    Response khi xác thực thành công sẽ có dạng  
    ````
    {
        "code": 200,
        "msg": "thành công",
        "data": {
            "userID": <id của người dùng>,
            "tokenType": <loại của token>,
            "jti": <một mã uuid định danh của token>,
            "accessToken": <access token của người dùng>
            "refreshToken": <refresh token của người dùng>
            "accessTokenExpSecs": <thời gian hiệu lực của access token (giây)>,
            "refreshTokenExpSecs": <thời gian hiệu lực của refresh token (giây)>
        }
    }
    ````    

2. Xác thực bằng refresh token  
    ````
        PATH: /api/authentication/refresh-token
        METHOD: POST
        REQUEST HEADER: 
            - Authorization: Basic <Base64(<id của client application>:<mã bí mật của client application>)>
        REQUEST BODY:
        {
            "refreshToken": <refresh token còn hiệu lực của người dùng>
        }
    ````
    Response khi xác thực thành công có dạng giống như `Xác thực bằng username password`  
    
**III. Truy cập yêu cầu xác thực người dùng**
1. Lấy thông tin tài khoản người dùng (demo_auth)   
    Api này trả về thông tin tài khoản người dùng, yêu cầu xác thực người dùng trước trước khi trả về dữ liệu  
    ````
        PATH: /api/auth-demo/users/info
        METHOD: GET
        REQUEST HEADER: 
            - Authorization: <loại của token> <access token của người dùng>
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
    

###### 7. Dành cho người phát triển
**I. Sử dụng annotation `@AuthorizationRequired`**  

Annotation này được sử dụng để dánh dấu các api hay một nhóm api nào đó là `Truy cập yêu cầu xác thưc người dùng`
````java

@AuthorizationRequired    // <- annotate vào controller để đánh dấu tất cả các api bên trong controller này 
@RestController           //    đêu yêu cầu xác thực người dùng khi truy cập
@RequestMapping("/api/auth-demo/users")
@Api(description = "Thông tin tài khoản")
public class UserProfileController extends BaseRESTController {

    @Autowired
    private UserProfileService userProfileService;

    @ApiOperation(value = "Lấy thông tin tài khoản",
            notes = "Trả về toàn bộ các thông tin tài khoản của người dùng, " +
                    "thực hiện xác thực người dùng bằng access token",
            response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = ResponseValue.SUCCESS, responseBody = UserDtoSwagger.class),
            @Response(responseValue = ResponseValue.USER_NOT_FOUND)
    })
    @GetMapping("/info")
    public BaseResponse getUserProfile() {
        return userProfileService.getUserProfile(getAuthorizedUser().getUserID()); // <- lấy thông tin của người dùng 
                                                                                   //    sau khi token đã được xác minh
    }
    
                             //     annotate vào method để đánh dấu api này yêu cầu xác thực người dùng, 
                             //     trường hợp nếu đã annotate ở controller rồi thì không cần thiết phải 
    @AuthorizationRequired   // <-  annoate ở method
    @GetMapping("/foo/bar")
    public BaseResponse getFooBar() {
        return new BaseResponse(ResponseValue.SUCCESS);
    }
}
````

**II. Gọi các api yêu cầu xác thực trên swagger-ui**

Đối với các api `Truy cập yêu cầu xác thưc người dùng`, khi được visualize trên swagger-ui sẽ có ký hiệu cảnh báo để 
thông báo cần xác thực người dùng trước khi thực hiện gọi

![](readme_assets/swagger-ui-auth-warning.png)

Để gọi được những api loại này trên swagger-ui, làm theo hướng dẫn sau

![](readme_assets/swagger-ui-auth-pass-warning.png)

###### 8. Uninstall  
- Xóa các nhóm được comment `[auth]` trong 02 file `application.properties` và `build.gradle`
- Xóa 09 package:
  + `modules/auth`
  + `modules/demo_auth`
  + `swagger/auth`
  + `swagger/demo_auth`
  + `configs/demo_auth`
  + `utils/auth`
  + `exceptions/auth`
  + `annotations/auth`
  + `components/auth`
  
##### IV. Điều khiển truy cập trên cơ sở vai trò - Role-base Access Control  
[Điều khiển truy cập trên cơ sở vai trò](https://vi.wikipedia.org/wiki/Điều_khiển_truy_cập_trên_cơ_sở_vai_trò) 
(Role-Based Access Control - viết tắt là RBAC) là một trong số các phương pháp điều khiển và đảm bảo quyền sử dụng 
cho người dùng

Trong nội bộ một tổ chức, các vai trò (các Role) được kiến tạo để đảm nhận các chức năng công việc khác nhau. Mỗi vai trò 
được gắn liền với một số quyền hạn cho phép nó thao tác một số hoạt động cụ thể (các Api). Những người dùng trong hệ thống (các User) 
được phân phối một vai trò riêng, và thông qua việc phân phối vai trò này mà họ tiếp thu được một số những quyền hạn cho 
phép họ thi hành những chức năng cụ thể trong hệ thống.

![](readme_assets/RBAC-diagram.png)

RBAC trong project này được phát triển dựa trên module `Spring Security + OAuth2 + JWT`

###### 1. Thành phần   
````
 - Started Project
 - Spring Data JPA (for storing role - api)
 - Spring Security + OAuth2 + JWT  
 - Role base access control
```` 

###### 2. Cấu trúc thư mục  
````
.   .  .    .
│   │  │    ├── annotations                           
│   │  │    │  └── rbac                      # (NEW) Thêm annotation @RoleBaseAccessControl
│   │  │    │
│   │  │    ├── components                           
│   │  │    │  └── rbac                      # (NEW) Thêm một số component hỗ trợ cấu hình và validation RBAC
│   │  │    │
│   │  │    ├── events_handle                           
│   │  │    │  └── rbac                      # (NEW) Thêm các runtime event triger để xử lý các tác vụ cần thiết để khởi tạo RBAC  
│   │  │    │
│   │  │    ├── exceptions                           
│   │  │    │  └── rbac                      # (NEW) Thêm một số custom exception của RBAC       
│   │  │    │
│   │  │    ├── swagger    
│   │  │    │  ├── rbac                      # (NEW) Thêm các swagger model cho module [rbac]                  
│   │  │    │  └── demo_rbac                 # (NEW)(CÓ THỂ XÓA) Thêm các swagger model cho module [demo_rbac]
│   │  │    │
│   │  │    └── modules  
│   │  │        └── rbac                     # (NEW) Core module của RBAC               
│   │  │        │   ├── controllers                                
│   │  │        │   ├── repositories                
│   │  │        │   ├── services                   
│   │  │        │   └── models                      
│   │  │        │      ├── dtos                     
│   │  │        │      └── entities   
│   │  │        │              
│   │  │        └── demo_rbac                # (CÓ THỂ XÓA) Code demo tính năng của RBAC            
│   │  │           ├── controllers                                
│   │  │           ├── repositories                
│   │  │           ├── services                   
│   │  │           └── models                      
│   │  │              ├── dtos                     
│   │  │              └── entities                
│   │  │                                        
│   │  └── resources/  
│   │     ├── rbac/                         # (MODIFIED) Thêm các additional config file cho RBAC                         
│   │     └── application.properties        # (MODIFIED) Thêm config cho RBAC
│   . 
.   .  
````

###### 3. Các thành phần có thể xóa  
- Xem tại cấu trúc thư mục  

###### 4. Gradle dependency  
- Không

###### 5. Configuration  
Cấu hình RBAC được đặt trong `application.properties`  
Xem thêm mô tả chi tiết tại mục `7. Dành cho người phát triển`  

````
...
# [rbac] Role-base access control configuration
application.rbac.refresh=false    # flag để RBAC xác định được có tiến hành update lại dữ liệu hay không
application.rbac.http-methods.path=rbac/http-methods.json    # đường dẫn đến file config http-methods.json của RBAC
application.rbac.rbac-started-roles-users.path=rbac/rbac-started-roles-users.json    # đường dẫn đến file config rbac-started-roles-users.json  của RBAC
application.rbac.rbac-modules-description.path=rbac/rbac-modules-description.json    # đường dẫn đến file config rbac-modules-description.json  của RBAC
...
````

###### 6. Tính năng
**I. Khởi tạo RBAC**
Scan và persist các api yêu cầu RBAC
    Khi mối khi service được start, RBAC sẽ tiến hành scan toàn bộ các package `controllers` của tất cả các module để tìm 
    kiếm các api yêu cầu RBAC và sau đó persist vào database
    
    `LƯU Ý`:
    - Một `@Controller` hay `@RestController` class nếu đặt ngoài package `controllers` của module sẽ *không* được RBAC 
    scan và persist. Do đó cần lưu ý đặt các Controller class đúng vị trí  
  
**II. RBAC core module**  
Truy cập `swagger-ui` để xem mô tả chi tiết
1. Mô hình
  RBAC trong project này được tổ chức thành mô hình: User - Role - Api  
    
  Trong đó:
  - Một user chỉ có một role (1 - 1)
  - Một role có thể truy cập được nhiều api (1 - n)
  - Một api có thể được truy cập bởi nhiều role (1 - n)  

  Database schema  
  
  ![](readme_assets/RBAC-schema.png)
  
  Trong RBAC, tồn tại một loại Role đặc biệt được gọi là ROOT. ROOT được phép truy cập mọi Api của hệ thống mà không cần 
  phân quyền, và phân quyền cũng không có tác dụng với ROOT
  
2. Liệt kê danh sách các api cần thực hiện RBAC của hệ thống
    Api yêu cầu token của người dùng bất kì và trả về danh sách tất cả các api yêu cầu RBAC
    ````
        PATH: /api/auth/rbac/apis
        METHOD: GET
        REQUEST HEADER: 
            - Authorization: <loại của token> <access token của người dùng bất kỳ>
    ````  
    
3. Liệt kê danh sách các api được phép truy cập của một quyền
    Api yêu cầu token của người dùng có quyền được phép truy cập, mặc định chỉ user có quyền ROOT mới được phép truy cập
    ````
        PATH: /api/auth/rbac/roles/{id của quyền}/apis
        METHOD: GET
        REQUEST HEADER: 
            - Authorization: <loại của token> <access token của người dùng có quyền hạn>
    ````
    
    Response khi user không có quyền truy cập
    ````
    HTTP STATUS: 401 Unauthorized
    RESPONSE BODY
    {
        "code": 4017,
        "msg": "quyền hiện tại không được phép truy cập",
        "data": null
    }
    ````

4. Phân/thu hồi danh sách api được phép truy cập cho một quyền (Phân/thu hồi quyền truy cập)
    Api yêu cầu token của người dùng có quyền được phép truy cập, mặc định chỉ user có quyền ROOT mới được phép truy cập  
    Sau khi phân/thu hồi quyền thành công, user có quyền đó có thể/không thể truy cập các api được phân/thu hồi mà không cần 
    tiến hành xác minh lại người dùng
    
     ````
        PATH: /api/auth/rbac/roles/{id của quyền}/apis
        METHOD: PUT
        REQUEST HEADER: 
            - Authorization: <loại của token> <access token của người dùng có quyền hạn>
        REQUEST BODY:
        [id của của api được phép truy cập]
     ````

###### 7. Dành cho người phát triển  
**I. RoleType để làm gì?** 
`RoleType` là một enum class định nghĩa các giá trị dùng để nhận dạng và đặc trưng cho một nhóm các quyền có cùng một 
số lượng quyền truy cập giống nhau vao thời điểm khoải tạo, RBAC dựa vào RoleType để có thể tự động cập nhập các api được 
phép truy cập mặc định cho các quyên đó

Ex: 
Nhóm quyền RoleType `ADMIN` mặc định được phép truy cập vào api `/foo` và `/bar`. Role `admin1` và `admin2` đều thuộc 
nhóm `ADMIN`. Do đó khi RBAC được khởi tạo sẽ tự động add thêm 2 api `/foo` và `/bar` vào danh sách các api được phép 
truy cập của hai role `admin1` và `admin2`

**II. Flag `application.rbac.refresh` trong `application.properties` để làm gì?** 
`scan và persist các api yêu cầu RBAC` là một quá trình yêu cầu dịch vụ phải tiến hành rà soát toàn bộ các module và 
thực hiện các database operation để persist dữ liệu. Do đó đối với project lớn, database thì được remote, với với độ trễ cao 
thì việc scan và persist các api RBAC sẽ trở thành vấn đề cảm trở đến quá trình phát triển  

Bên cạnh đó, công việc `scan và persist` bên trên cũng không đồi hỏi phải thực hiện thường xuyên mà hầu hết chỉ cần thực 
hiện mỗi khi lập trình viên định nghĩa thêm/sửa/xóa một route mapping trong `@Controller` hay thay đổi cấu trúc module  
dẫn đến sự thay đổi các api route  
 
Do đó, Flag `application.rbac.refresh` được tạo ra để giải quyết vấn đề trên. RBAC sẽ dựa vào giá trị của flag này để 
xác định xem có nên tiến hành `scan và persist` lại các api hay không

Mặc định giá trị của `application.rbac.refresh` là `false`

Cách sử dụng: Thêm `application.rbac.refresh` như một program environment variable khi start ứng dụng Spring Boot
Ex:
```bash
$ java -jar build/libs/spring-base-project.jar --application.rbac.refresh=true
hoặc
$ gradle bootRun build/libs/spring-base-project.jar --application.rbac.refresh=true
```

Hoặc có thể cấu hình trên IDE Intelliji như sau

![](readme_assets/inteliji-run-rbac-config.png)

**III. Các file cấu hình trong `resources/rbac` để làm gì?** 
Các file cấu hình đặt trong `rousources/rbac` là các file cấu hình khởi tạo dữ liệu cho RBAC, bao gồm
- `http-methods.json` đây là file cấu hình mức độ ưu tiên của các Http method, dùng để sorting  

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

- `rbac-modules-description.json` đây là file cấu hình các thông tin bổ xung về các module, bao gồm mô tả (description) 
và mức độ ưu tiên (priority)  

```json
{
  "rbac": {
    "description": "Điều khiển truy cập trên cơ sở vai trò",
    "priority": 1
  },
  "demo_rbac": {
    "description": "Demo tính năng điều khiển truy cập trên cơ sở vai trò",
    "priority": 2
  }
}
```
    
- `rbac-started-roles-users.json` đây là file cấu hình các quyền (Role) và người dùng (User) khởi tạo bna đầu của RBAC, 
bao gồm 1 quyền thuộc nhóm `ROOT`, 1 quyên thuộc nhóm `ADMIN` và một user có quyền `ROOT`  

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

**IV. Sử dụng annotation `@RoleBaseAccessControl`**  

Annotation được kế thừa từ annotation `@AuthorizationRequired` nên nó sẽ **bao gồm cả tính năng** của `@AuthorizationRequired`

`@AuthorizationRequired` là mức low level so với `@RoleBaseAccessControl`

Annotation này được sử dụng để dánh dấu các api hay một nhóm api nào đó là `truy cập yêu cầu RBAC`
````java

@RoleBaseAccessControl    // <- annotate vào controller để đánh dấu tất cả các api bên trong controller này 
@RestController           //    đêu yêu cầu RBAC
@RequestMapping("/api/auth/rbac")
@Api(description = "Quản lý quyền truy cập")
public class ApiController extends BaseRESTController {
    
                             //     annotate vào method để đánh dấu api này yêu cầu RBAC, 
                             //     trường hợp nếu đã annotate ở controller rồi thì không cần thiết phải 
    @RoleBaseAccessControl   // <-  annoate ở method
    @GetMapping("/foo")
    public BaseResponse getFoo() {
        return new BaseResponse(ResponseValue.SUCCESS);
    }
    
    // Có thể định nghĩa thêm các RoleType mặc định được phép truy cập (ngoài ROOT), 
    // nếu để trống khi có nghĩa là api này mặc định chỉ có ROOT được phép truy cập
    // ↓ 
    @RoleBaseAccessControl(defaultAccess = {RoleType.ADMIN}) 
    @GetMapping("/bar")
    public BaseResponse getFooBar() {
        return new BaseResponse(ResponseValue.SUCCESS);
    }
}
````

**II. Gọi các api yêu cầu xác thực trên swagger-ui**

Đối với các api yêu cầu RBAC trên swagger cũng có cảnh báo giống như các api `Truy cập yêu cầu xác thưc người dùng`

###### 8. Uninstall  
- Xóa các nhóm được comment `[rbac]` trong 02 file `application.properties` và `build.gradle`
- Xóa 09 package:
  + `modules/rbac`
  + `modules/demo_rbac`
  + `swagger/rbac`
  + `swagger/demo_rbac`
  + `annotations/rbac`
  + `components/rbac`
  + `exceptions/rbac`
  + `events_handle/rbac`
  + `resources/rbac`

##### V. Firebase
[Firebase](https://firebase.google.com) Firebase là một dịch vụ hệ thống backend được Google cung cấp sẵn cho nền tảng 
Mobile. Firebase giúp lập trình viên rút ngắn thời gian phát triển, triển khai và thời gian mở rộng quy mô của ứng dụng.

![](readme_assets/firebase-logo.png)

###### 1. Thành phần   
````
 - Started Project
 - Firebase admin
```` 

###### 2. Cấu trúc thư mục  
````
.   .  .    .
│   │  │    ├── configs                           
│   │  │    │  └── firebase                  # (NEW) Thêm firebase connection config    
│   │  │    │
│   │  │    ├── swagger    
│   │  │    │  ├── demo_firebase             # (NEW)(CÓ THỂ XÓA) Thêm các swagger model cho module [demo_firebase]
│   │  │    │
│   │  │    └── modules  
│   │  │        └── firebase                 # (NEW) Module core chứa các service hỗ trợ tương tác với firebase
│   │  │        └── demo_firebase            # (NEW)(CÓ THỂ XÓA) Thêm module demo upload file lên firebase storage            
│   │  │            ├── controllers                                
│   │  │            ├── repositories                
│   │  │            ├── services                   
│   │  │            └── models                      
│   │  │               ├── dtos                     
│   │  │               └── entities                       
│   │  │                                        
│   │  └── resources/                          
│   │     └── application.properties        # (MODIFIED) Thêm config firebase
│   . 
.    
│                                                                     
├── build.gradle                            # (MODIFIED) Thêm dependency
.
````

###### 3. Các thành phần có thể xóa  
- Xem tại cấu trúc thư mục  

###### 4. Gradle dependency  
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

###### 5. Configuration  
Cấu hình firebase được đặt trong `application.properties`  
````
...
# [firebase] Firebase admin configuration
application.firebase.google-services.path=base/google-services.json   # đường đẫn đến file config google-services.json
application.firebase.fcm.legacy-server-key=AIzaSyDiJ9DLhe-BA_2W0mQElnqELlYl89wVbz0   # legancy server key của firebase project
application.firebase.fcm.api=https://fcm.googleapis.com/fcm/send   # api send push notification của firebase
application.firebase.database.url=https://base-firebase-project-d8945.firebaseio.com   # database url của firebase project
application.firebase.storage.bucket=base-firebase-project-d8945.appspot.com   # storage bucket url của firebase project
application.firebase.storage.api=http://storage.googleapis.com   # api google storage
...
````

![Lấy firebase legacy server key](readme_assets/firebase-console-legacy-server-key.png)

![Lấy firebase config file và database url](readme_assets/firebase-console-service-accounts.png)

![Lấy firebase storage bucket](readme_assets/firebase-console-storage-bucket.png)

###### 6. Tính năng
1. Firebase authentication
2. Firebase storage
3. Firebase real-time database / Firestore
4. Firebase cloud messaging  

###### 7. Demo firebase  
Truy cập swagger-ui, module `demo_firebase`  

###### 8. Uninstall  
- Xóa các nhóm được comment `[firebase]` trong 02 file `application.properties` và `build.gradle`
- Xóa `google-service.json`
- Xóa 04 package:
  + `configs/firebase`
  + `modules/firebase`
  + `modules/demo_firebase`
  + `swagger/demo_firebase`
  



