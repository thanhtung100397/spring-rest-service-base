# Spring Boot REST service base project
`Develop by tungtt`  
`Email: thanhtung100397@gmail.com`

### GHI CHÚ
- Project này yêu cầu thiết lập 'allow-bean-definition-overriding=true', do đó cần lưu ý KHÔNG tạo @Bean trùng lặp
bởi chúng sẽ replace lẫn nhau, điều này có thể gây ra các bug hoặc nhầm lẫn khó hiểu

### PROJECT SETUP
`LƯU Ý`: Đảm bảo `git`, `java 8` và `gradle v4.9` đã được cài đặt  

###### 1. Clone project  
- Download project với định dạng nén phù hợp từ gitlab về máy cá nhân và giải nén

###### 2. Install dependencies  
```bash
$ gradle dependencies
```

###### 3. Run project (mục đích phát triển)
```bash
$ cd gradle bootRun
```

###### 4. Xem Doc API  
- Truy cập địa chỉ `http://<host>:<port>/swagger-ui.html` bằng trình duyệt
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

`LƯU Ý`:  
- Trong quá trình build, gradle sẽ tiến hành chạy tất cả các test case của project (nếu có). 
Quá trình build chỉ thành công khi tất cả các test case đều được pass  
- Sau khi build thành công, file `.jar` sẽ được sinh ra tại đường dẫn 
`<project root folder>/build/libs` 

###### 7. Execute .jar (mục đích triển khai trên production)   
```bash
$ java -jar <path/to/.jar>
```

### PROJECT MODIFY 
###### 1. Set git remote repository cho project 
```bash
$ git init
$ git remote add origin <your git remote repository>
$ git remote -v
```

###### 2. Thay đổi tên package name
- Đổi package `src/main/java/com/spring/baseproject` thành `src/main/java/<tên/package/mới>`  
- Cập nhật `rootProject.name` trong `settings.gradle`  
- Cập nhật giá trị `group` trong `build.gradle`  
- Cập nhật package name mới cho `BASE_PACKAGE_NAME` trong class `./constants/ApplicationConstants`  
- Nếu project có sử dụng Spring Data JPA, cần rà soát lại toàn bộ các phương thức @Query có trong JpaRepository để 
đổi lại package name mới, ex:  
````java
public interface ProductRepository extends JpaRepository<Product, String> {
     @Query("select new com.spring.baseproject" +  // <-- cần đổi lại tên package name
            ".modules.demo_jpa.models.dtos.ProductPreviewDto(p.id, p.name, p.createdDate, p.tags, pt.id, pt.name) " +
            "from Product p " +
            "left join p.productType pt")
     Page<ProductPreviewDto> getPageProductPreviewDtos(Pageable pageable);
 }
````

### PROJECT STRUCTURE  
##### I. STARTED  
###### 1. Thành phần  
````
 - Spring started web (for REST)
 - Mockito JUnitTest (for unit test)
 - Springfox Swagger 2 (for Doc API)
 - Docker
 - Demo REST API
 - Demo unit test cho phần business logic @Service
````

###### 2. Cấu trúc thư mục
````
.    
├── readme_assets/                              # (CÓ THỂ XÓA) Folder chứa static resource (image,...) của README.md
├── src/                                   
│   └── main/                                
│   │  ├── java/
│   │  │  └── com/spring/baseproject/           # Package java source code của toàn bộ project
│   │  │     ├── annotations                     # Chứa toàn bộ các custom @Annotation của project
│   │  │     ├── base                            # Chứa toàn bộ các class base hoặc toàn cục của project
│   │  │     │  ├── controllers                   # Chứa base controller class của project
│   │  │     │  └── models                        # Chứa các model class base hoặc toàn cục của project
│   │  │     ├── components                      # Chứa toàn bộ các custom @Component của project
│   │  │     ├── configs                         # Chứa toàn bộ các runtime config của project
│   │  │     ├── constants                       # Chứa toàn bộ các static constant value (ex: các giá trị text response, response code) của project
│   │  │     ├── events_handle                   # Chứa toàn bộ các event trigger và handler (ex: ContextRefreshedEvent, ContextStartedEvent,...) của project
│   │  │     ├── exceptions                      # Chứa toàn bộ các custom exception(s) của project
│   │  │     ├── utils                           # Chứa toàn bộ các tools sử dụng trong project
│   │  │     ├── swagger                         # Chứa toàn bộ các swagger model(s)
│   │  │     │  ├── base
│   │  │     │  └── demo                         # (CÓ THỂ XÓA) Chứa toàn bộ các swagger models của module `demo`
│   │  │     │ 
│   │  │     └── modules                         # (Core) Chứa toàn bộ các core business module trong project
│   │  │        └── demo                         # (CÓ THỂ XÓA) Package được đặt theo tên module trong project
│   │  │           ├── controllers                   
│   │  │           ├── repositories                
│   │  │           ├── services                   
│   │  │           └── models                      
│   │  │              ├── dtos                     
│   │  │              └── entities                
│   │  │                                        
│   │  └── resources/                           # Package chứa toàn bộ các static resources và các config của project
│   │     ├── base/                               # Chứa các static resources mặc định
│   │     ├── dev/                               # Chứa các static resources cho môi trường Development
│   │     ├── prod/                              # Chứa các static resources cho môi trường Production
│   │     ├── application.properties             # File config mặc định của project
│   │     ├── application-dev.properties         # File config cho môi trường Developement (override file config mặc định)
│   │     ├── application-prod.properties        # File config cho môi trường Production (override file config mặc định)
│   │     └── swagger-info.json                  # File tùy chỉnh chứa một số info hiển thị trên trang swagger-ui.html
│   │
│   └── test/                                                               
│      └── java/                              
│         └── com/spring/baseproject/           # Package chứa toàn bộ các unit test của project        
│            └── demo                           # (CÓ THỂ XÓA) Package được đặt theo tên module tương ứng                   
│               ├── controllers                   # Chứa tất cả các unit test(s) cho @RestController của module    
│               ├── repositories                  # Chứa tất cả các unit test(s) cho @Repository của module  
│               └── services                      # Chứa tất cả các unit test(s) cho @Service của module 
│                                                                               
├── build.gradle                               # File khai báo dependencies và build config của gradle
├── Dockerfile                                 # File Dockerfile để build Docker Image
└── README.md   (CÓ THỂ XÓA)                   # File README 
````

###### 3. Các thành phần có thể xóa  
- Xem tại cấu trúc thư mục

###### 4. Code structure  
Project được thiết kế theo kiến trúc phân tẩng  

![](readme_assets/project-structure.png)  

**Controller** Định nghĩa các route mapping, viết Doc API (Swagger), input validation. `Controller` tương tác với `Service` để 
xử lý nghiệp vụ và nhận kết quả trả về response cho client. Một `Controller` chỉ tương tác với một `Service`  
**Service** Xử lý business logic của nghiệp vụ, tương tác với `Repository` để truy vấn dữ liệu từ database và trả kết quả  
xử lý về `Controller`. Một `Service` có thể tương tác với nhiều `Repository`  
**Repository** Tương tác với database để truy vấn hoặc persist data  

Do đó, cấu trúc package của một module trong project được tổ chức như sau
````
.   .  .     .
│   │  │     └── modules                         # (Core) Chứa toàn bộ các core business module trong project
│   │  │        └── <tên module>                  # Một module trong project
│   │  │           ├── controllers                 # Chứa toàn bộ các @RestController của module     
│   │  │           ├── repositories                # Chứa toàn bộ các @Repository (ex: JPARepository, CrudRepository, MongoRepository, ...) của module
│   │  │           ├── services                    # Chứa toàn bộ các @Service của module
│   │  │           └── models                      # Chứa toàn bộ các model class của module
│   │  │              ├── dtos                      # Chứa toàn bộ các DTO model class
│   │  │              └── entities                  # Chứa toàn bộ các ORM model class (ex: @Entity của Spring Data JPA, @Document của Spring Data MongoDB,...)
│   │  │  
.   .  .
````

###### 5. Swagger 2 - Code-gen Doc API  
[Swagger](https://swagger.io) là một công cụ tạo API Document. Document được sinh tự động dựa trên code nên giúp tiết 
kiệm thời gian viết tài liệu, dễ sửa đổi. Swagger sử dụng các `@Annotation` để scan toàn bộ source code trong project, 
từ đó sẽ visualize Doc Api dưới dạng 1 web page gọi là swagger ui. Ngoài hiển thị, swagger ui còn cho phép tương tác 
trực tiếp với các API, tuy nhiên các công cụ hỗ trợ thao tác thì còn khá hạn chế

Sau khi run project, truy cập địa chỉ `http://<host>:<port>/swagger-ui.html` để vào swagger ui của project 
 
![](readme_assets/swagger-ui.png)  

Project đã modify một số phần của Swagger để thuận tiện hơn cho quá trình phát triển, điều này chỉ làm thay đổi một 
số chi tiết nhỏ so với tài liệu gốc của swagger. Dưới đây là cách sử dụng
````java
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.spring.baseproject.annotations.swagger.Response;//Custom annotation
import com.spring.baseproject.annotations.swagger.Responses;//Custom annotation

@RestController
@RequestMapping("/api/foo")
@Api(description = "Mô tả ngắn gọn về nhóm api")
class FooController extends BaseRESTController {
    @ApiOperation(value = "Tên gọi/mô tả nghiệp vụ ngắn gọn của api",
                notes = "Chú thích/giải thích/mô tả chi tiết về api (nếu cần)",
                response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = ResponseValue.SUCCESS, responseBody = ResponseModelClass.class),//<- Loại class sẽ được api trả về (nếu cần)
            @Response(responseValue = ResponseValue.FOO_NOT_FOUND)
    })
    @GetMapping()
    public BaseResponse getFoos() {
        
    }
}
````

`LƯU Ý`:
- Swagger đã được tùy chỉnh để chỉ scan và visualize các @RestController nằm trong package `modules/<tên module>/controllers`, 
do đó nên lưu ý đặt các @Controller vào đúng vị trí
- Các `ResponseValue` có cùng `specialCode` sẽ replace lẫn nhau khi hiển thị trên swagger ui, do đó nên lưu ý không tạo 
hai `ResponseValue` khác nhau có cùng chung một `specialCode`  

##### II. SPRING DATA JPA  
[Spring Data JPA](https://spring.io/projects/spring-data-jpa) (Java Persistence API) là một thư viên của `Spring Data`, thuộc `Spring Framework`. Với mục đích là đơn giản hóa 
việc thao tác với datasource từ code. Hạn chế việc phải implement nhiều tầng data access khác nhau với mỗi datasource khác nhau. 
JPA cũng cấp một ngôn ngữ truy vấn khá tương đồng với `SQL`, được gọi là `JPAQL`. Khi thực thi, `JPAQL` sẽ tự động được transform sang native 
query phù hợp với mỗi datasource được sử dụng

Trong tài liệu này chỉ hướng dẫn cách setup JPA với datasource là MySQL

###### 1. Thành phần  
````
 - Started Project
 - Spring Data JPA
```` 

###### 2. Cấu trúc thư mục  
````
.   .  .     .
│   │  │     ├── utils                           
│   │  │     .  └── jpa                       # (NEW) Thêm util hỗ trợ phân trang query      
.   .  .    .
│   │  │     ├── swagger                      
│   │  │     │  └── demo_jpa                  # (NEW)(CÓ THỂ XÓA) Thêm các swagger model cho module [demo_jpa]
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

###### 7. Demo JPA  

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

###### 6. Uninstall  
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
    

###### 6. Dành cho người phát triển
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

###### 6. Uninstall  
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

##### III. Firebase
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
  



