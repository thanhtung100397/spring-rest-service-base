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
Quá trình build chỉ SUCCESSFUL khi tất cả các test case đều được pass  
- Sau khi build SUCCESSFUL, file `.jar` sẽ được sinh ra tại đường dẫn 
`<project root folder>/build/libs`   

### PROJECT MODIFY 
1. Link project đến git remote repository  
```bash
$ git init
$ git remote add origin <your git remote repository>
$ git remote -v
```

2. Thay đổi package name:
- Đổi package `src/main/java/com/spring/baseproject` thành `src/main/java/<tên/package/mới>`  
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
##### I. STARTED PROJECT  
2. Thành phần  
````
 - Spring started web (for REST)
 - Mockito JUnitTest (for unit test)
 - Springfox Swagger 2 (for Doc API)
 - Docker
 - Demo REST API - CÓ THỂ XÓA
 - Demo unit test cho phần business logic @Service - CÓ THỂ XÓA
````

3. Cấu trúc thư mục
````
.              
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
│   │  │     │  └── demo   (CÓ THỂ XÓA)
│   │  │     │ 
│   │  │     └── modules                         # (Core) Chứa toàn bộ các core business module trong project
│   │  │        └── demo  (CÓ THỂ XÓA)            # Package được đặt theo tên module trong project
│   │  │           ├── controllers                 # Chứa toàn bộ các @RestController của module     
│   │  │           ├── repositories                # Chứa toàn bộ các @Repository (ex: JPARepository, CrudRepository, MongoRepository, ...) của module
│   │  │           ├── services                    # Chứa toàn bộ các @Service của module
│   │  │           └── models                      # Chứa toàn bộ các model class của module
│   │  │              ├── dtos                      # Chứa toàn bộ các DTO model class
│   │  │              └── entities                  # Chứa toàn bộ các ORM model class (ex: @Entity của Spring Data JPA, @Document của Spring Data MongoDB,...)
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
│            └── demo   (REMOVABLE)              # Package được đặt theo tên module tương ứng                   
│               ├── controllers                   # Chứa tất cả các unit test(s) cho @RestController của module    
│               ├── repositories                  # Chứa tất cả các unit test(s) cho @Repository của module  
│               └── services                      # Chứa tất cả các unit test(s) cho @Service của module 
│                                                                               
├── build.gradle                               # File khai báo dependencies và build config của gradle
├── Dockerfile                                 # File Dockerfile để build Docker Image
└── README.md                                  # File README 
````