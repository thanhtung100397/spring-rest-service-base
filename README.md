# Spring Boot REST service base  
`Develop by tungtt`  
`Email: thanhtung100397@gmail.com`

### NOTES ABOUT THIS PROJECT  
 - This project is enabled 'allow-bean-definition-overriding' configuration, so make sure DO NOT CREATE DUPLICATE BEAN (SAME NAME) 
 in this project because two bean will replace each other. Therefore, it will cause some unexpected behavior

### Project Setup  
`NOTE`: (Make sure `gradle v4.9` installed)    
1. Clone project  
(Make sure `git` installed)  
```bash
$ git clone git@gitlab.com:worksvn-dev-team/base-project/spring-boot-rest-service.git (ssh)
or
$ git clone https://gitlab.com/worksvn-dev-team/base-project/spring-boot-rest-service.git (https)
```

2. Install dependencies  
```bash
$ cd spring-boot-rest-service
$ gradle dependencies
```

3. Run project  
```bash
$ cd spring-boot-rest-service
$ cd gradle bootRun
```

4. View Doc API  
- Visit `http://<service host>:<service port>/swagger-ui.html` by using any browsers
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
.jar file will be store in directory `spring-boot-rest-service/build/libs`  
`NOTE`: `gradle build` will compile and run all project test cases, if only one test case fail, build will fail immediately. 
Therefore, please run all project test cases before build to make sure there is no build issues

### Project Modifying  
1. Change main package name:
````
- Change main package name directory (src/main/java/<any/package/name>)
- Update new package name to `application.base-package-name` in `application.properties`
````

### Project Features  
##### I. Started project  
1. Install  
```bash
$ git checkout master
```

2. Project Components  
````
 - Spring started web (for REST)
 - Mockito JUnitTest (for testing)
 - Springfox Swagger 2 (for Doc API)
 - Docker
````

3. Project structure  
````
.              
├── src/                                   
│   └── main/                                
│   │  ├── java/
│   │  │  └── com/spring/baseproject/           # Project source code directory
│   │  │     ├── annotations                     # Contain all project custom annotation(s)
│   │  │     ├── base                            # Contain all project base and global class(es)
│   │  │     │  ├── controllers                   # Contain project base controller
│   │  │     │  └── models                        # Contain all project global model(s)
│   │  │     ├── components                      # Contain all Spring component(s)
│   │  │     ├── configs                         # Contain all application internal configuration(s)
│   │  │     ├── constants                       # Contain all project constant value(s) (ex: texts, special codes)
│   │  │     ├── events_handle                   # Contain all Spring eventType handler(s) (ex: ContextRefreshedEvent, ContextStartedEvent,...)
│   │  │     ├── exceptions                      # Contain all project custom exception(s)
│   │  │     ├── utils                           # Contain all project utilitiy(s)
│   │  │     ├── swagger                         # Contain all swagger model(s)
│   │  │     └── modules                         # (Core) Contain all project modules core bussiness source codes
│   │  │        └── demo                          # Directory is named by project module name
│   │  │           ├── controllers                 # Contain all module's Spring REST Controller(s)     
│   │  │           ├── repositories                # Contain all module's Spring Repository(s) (ex: JPARepository, CrudRepository,...) 
│   │  │           ├── services                    # Contain all module's bussiness service(s)
│   │  │           └── models                      # Contain all module's data model(s)  
│   │  │              ├── dtos                      # Contain all module's DTO model(s) 
│   │  │              └── entities                  # Contain all module's JPA Entity model(s)
│   │  │                                        
│   │  └── resources/                           # Project resources and external configuration(s) directory
│   │     ├── dev/                               # Contain all configuration(s) and resource(s) for project Development environment
│   │     ├── prod/                              # Contain all configuration(s) and resource(s) for project Product environment
│   │     ├── application.properties             # Base configuration file for project
│   │     ├── application-dev.properties         # Overidden-base configuration file for project Development environemnt
│   │     ├── application-prod.properties        # Overidden-base configuration file for project Product environemnt
│   │     └── ValidationMessages.properties      # Validation error messages file
│   │
│   └── test/                                                               
│      └── java/                              
│         └── com/spring/baseproject/           # Project unit test directory          
│            └── demo                            # Directory is named by project module name                         
│               ├── controllers                   # Contain all unit test(s) for modules's controllers    
│               ├── repositories                  # Contain all unit test(s) for modules's repositories 
│               └── services                      # Contain all unit test(s) for modules's services 
│                                                                               
├── build.gradle                               # Project dependencies file
├── Dockerfile                                 # Project Dockerfile 
└── README.md                                  # Project README file 
````