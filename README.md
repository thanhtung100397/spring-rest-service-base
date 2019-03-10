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
 @Query("select new com.spring.baseproject" +  // <-- cần đổi lại tên package name
        ".modules.demo_jpa.models.dtos.ProductPreviewDto(p.id, p.name, p.createdDate, p.tags, pt.id, pt.name) " +
        "from Product p " +
        "left join p.productType pt")
 Page<ProductPreviewDto> getPageProductPreviewDtos(Pageable pageable);
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

###### 5. Swagger 2 - Codegen Doc API  
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
Spring Data JPA (Java Persistence API) là một thư viên của `Spring Data`, thuộc `Spring Framework`. Với mục đích là đơn giản hóa 
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
│   │  │     └── modules  
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
│   │     ├── application.properties             # (MODIFIED) Thêm config datasource và Hibernate ORM
│   .      .
│                                                                     
├── build.gradle                               # (MODIFIED) Thêm dependency
.
````

###### 3. Các thành phần có thể xóa  
- Xem tại cấu trúc thư mục  

###### 4. Gradle dependency  
 ````
 ...
 dependencies {
     ...
     // Spring Data JPA - Connect SQL DBMS
     implementation('org.springframework.boot:spring-boot-starter-data-jpa')
     runtime('mysql:mysql-connector-java')
     ...
 }
 ````

###### 4. Configuration
Cấu hình JPA được đặt trong `application.properties`  
````
...
# JPA configuration
spring.jpa.hibernate.ddl-auto=(create|create-drop|update|none|validate)
spring.jpa.hibernate.naming.implicit-strategy=org.hibernate.boot.model.naming.ImplicitNamingStrategyLegacyJpaImpl
spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL55Dialect
spring.datasource.url=jdbc:mysql://(datasource host):(datasource port)/(database name)?useUnicode=yes&characterEncoding=UTF-8&autoReconnect=true&useSSL=false&createDatabaseIfNotExist=true
spring.datasource.username=(datasource username)     # username truy cập datasource
spring.datasource.password=(datasource password)     # password truy cập datasource

# JPA query logging configuration
spring.jpa.show-sql=(true|false)     # Log trên console native query được tranform từ JPAQL mỗi khi một truy vấn được thực thi
spring.jpa.properties.hibernate.format_sql=(true|false)     # Làm đẹp native query được log trên console
...
````

###### 5. Hướng tiếp cận
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

###### 6. Demo JPA
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

###### 6. Uninstalling
Xóa `Configuration`, `Gradle dependency` và `demo_jpa`



