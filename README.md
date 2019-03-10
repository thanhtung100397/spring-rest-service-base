# Spring Boot REST service base project
`Develop by tungtt`  
`Email: thanhtung100397@gmail.com`

### GHI CHÚ
 - project này yêu cầu thiết lập 'allow-bean-definition-overriding=true', do đó cần lưu ý KHÔNG tạo @Bean trùng lặp
bởi chúng sẽ replace lẫn nhau, điều này có thể gây ra các bug hoặc nhầm lẫn khó hiểu

### PROJECT SETUP
`LƯU Ý`: Đảm bảo `git`, `java 8` và `gradle v4.9` đã được cài đặt  

1. Clone project  
- Download project với định dạng nén phù hợp từ gitlab về máy cá nhân và giải nén

2. Install dependencies  
```bash
$ gradle dependencies
```

3. Run project (mục đích phát triển)
```bash
$ cd gradle bootRun
```

4. Xem Doc API  
- Truy cập địa chỉ `http://<host>:<port>/swagger-ui.html` bằng trình duyệt
(ex: `http://localhost:8080/swagger-ui.html`)

5. Run all test cases
```bash
$ gradle test
```

6. Build .jar
```bash
$ cd spring-boot-rest-service
$ gradle build
```  

`LƯU Ý`:  
- Trong quá trình build, gradle sẽ tiến hành chạy tất cả các test case của project (nếu có). 
Quá trình build chỉ thành công khi tất cả các test case đều được pass  
- Sau khi build thành công, file `.jar` sẽ được sinh ra tại đường dẫn 
`<project root folder>/build/libs` 

7. Execute .jar (mục đích triển khai trên production)   
```bash
$ java -jar <path/to/.jar>
```

### PROJECT MODIFY 
1. Set git remote repository cho project 
```bash
$ git init
$ git remote add origin <your git remote repository>
$ git remote -v
```

2. Thay đổi tên package name
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
1. Thành phần  
````
 - Spring started web (for REST)
 - Mockito JUnitTest (for unit test)
 - Springfox Swagger 2 (for Doc API)
 - Docker
 - Demo REST API
 - Demo unit test cho phần business logic @Service
````

2. Cấu trúc thư mục
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
│   │     ├── dev/                               # Chứa các static resources mặc định
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

3. Các thành phần có thể xóa  
- `readme_assets`  
- `README.md`  
- Toàn bộ package `demo` trong `java /modules`, `java /swagger` và `test`, package này được tạo ra chỉ với
mục đích demo

4. Module structure  
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

5. Swagger 2 - Codegen Doc API  
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



