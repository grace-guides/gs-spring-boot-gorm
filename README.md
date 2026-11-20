[![Main branch build status](https://github.com/grace-guides/gs-spring-boot-gorm/workflows/Grace%20CI/badge.svg?style=flat)](https://github.com/grace-guides/gs-spring-boot-gorm/actions?query=workflow%3A%Grace+CI%22)
[![Apache 2.0 license](https://img.shields.io/badge/License-APACHE%202.0-green.svg?logo=APACHE&style=flat)](https://opensource.org/licenses/Apache-2.0)
[![Grace on X](https://img.shields.io/twitter/follow/graceframework?style=social)](https://twitter.com/graceframework)

[![Groovy Version](https://img.shields.io/badge/Groovy-5.0.8-blue?style=flat&color=4298b8)](https://groovy-lang.org/releasenotes/groovy-5.0.html)
[![Grace Version](https://img.shields.io/badge/Grace-2025.0.0-blue?style=flat&color=f49b06)](https://github.com/graceframework/grace-framework/releases/tag/v2025.0.0-M3)
[![Spring Boot Version](https://img.shields.io/badge/Spring_Boot-4.0.9-blue?style=flat&color=6db33f)](https://github.com/spring-projects/spring-boot/releases/tag/v4.0.9)


# Spring Boot with Grace GORM

Learn how to build a Spring Boot application using GORM.

### Versions

* Spring Boot 4.0.9
* Grace Framework 2025.0.0-M3
* Groovy 5.0.8

### Adding Grace Dependencies

```gradle
plugins {
    id 'groovy'
    id 'org.springframework.boot'
    id 'io.spring.dependency-management'
    id 'org.graceframework.grace-core'
}

dependencies {
    implementation 'org.apache.groovy:groovy'
    implementation 'org.graceframework:grace-boot'
    implementation 'org.graceframework:grace-boot-hibernate'
    implementation 'org.graceframework:grace-boot-persistence'
    runtimeOnly 'com.h2database:h2'
}
```

### Configure Hibernate Dialect and Datasource

Update `src/main/resources/application.yml`,

```yml
hibernate:
    show_sql: true
    dialect:  org.hibernate.dialect.H2Dialect
dataSource:
    dbCreate: create-drop
    pooled: true
    jmxExport: true
    driverClassName: org.h2.Driver
    url: jdbc:h2:file:./build/boot_dev;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE
    username: sa
    password:
```

### Creating Post Domain and GORM Service

Creating `Post.groovy` and place it in `app/domain`,

```groovy
class Post {

    String title

    static constraints = {
        title(blank: false, nullable: false, maxSize: 255)
    }

    String toString() {
        "#$id $title"
    }
}
```

> [!NOTE]  
> After Grace 2023.1.0, you can also place GORM Entity in the `src/main/groovy`.

Creating `PostService.groovy` and place it in `app/services`,

```groovy
@grails.gorm.services.Service(Post)
interface PostService {

    List<Post> findAll()

    Post save(Post post)

}
```

Now, GORM will generate `$PostServiceImplementation` for your application,
then you can use it in your Spring Boot application just like in Grace,

```groovy
// Post methods
new Post(title: 'Grace 2022.2.8 is released').save()
Post.list()
Post.where()

// PostService methods
postService.findAll()
```

### Running the App

In the `src/main/groovy/grace/guides/GraceApplication`, if you use Grace before `2023.1.0`, remember that, exculding the AutoConfigurations, `HibernateJpaAutoConfiguration`, `DataSourceAutoConfiguration`, `TransactionAutoConfiguration` in `GraceApplication`, now in Grace `2023.3.0`, there is no need to do this.

```groovy
@SpringBootApplication
class GraceApplication implements CommandLineRunner {

    @Autowired
    BootStrap bootStrap

    @Autowired
    PostService postService

    static void main(String[] args) {
        SpringApplication.run(GraceApplication, args)
    }

    @Override
    void run(String... args) throws Exception {
        println 'Prepare Sample Data '.padRight(94, '>')
        bootStrap.init()

        println()
        println 'Load All Posts '.padRight(94, '>')
        postService.findAll().each {
            println it 
        }

    }

}
```

### Running the App

Staring the App by execute the following Gradle task,

```bash
~/gs-spring-boot-gorm ./gradlew bootRun

> Task :bootRun

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/

 :: Spring Boot ::                (v4.0.9)

2026-11-20T17:28:43.718+08:00  INFO 17707 --- [  restartedMain] grace.guides.GraceApplication            : Starting GraceApplication using Java 17.0.20 with PID 17707 (/Users/grace/grace-guides/gs-spring-boot-gorm/build/classes/groovy/main started by rain in /Users/grace/grace-guides/gs-spring-boot-gorm)
2026-11-20T17:28:43.719+08:00  INFO 17707 --- [  restartedMain] grace.guides.GraceApplication            : No active profile set, falling back to 1 default profile: "default"
2026-11-20T17:28:43.738+08:00  INFO 17707 --- [  restartedMain] .e.DevToolsPropertyDefaultsPostProcessor : Devtools property defaults active! Set 'spring.devtools.add-properties' to 'false' to disable
2026-11-20T17:28:43.739+08:00  INFO 17707 --- [  restartedMain] .e.DevToolsPropertyDefaultsPostProcessor : For additional web related logging consider setting the 'logging.level.web' property to 'DEBUG'
2026-11-20T17:28:44.207+08:00  INFO 17707 --- [  restartedMain] g.plugins.DefaultGrailsPluginManager     : Total 4 plugins loaded successfully, take in 36 ms
2026-11-20T17:28:44.408+08:00  INFO 17707 --- [  restartedMain] o.s.boot.tomcat.TomcatWebServer          : Tomcat initialized with port 8080 (http)
2026-11-20T17:28:44.416+08:00  INFO 17707 --- [  restartedMain] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2026-11-20T17:28:44.416+08:00  INFO 17707 --- [  restartedMain] o.apache.catalina.core.StandardEngine    : Starting Servlet engine: [Apache Tomcat/11.0.27]
2026-11-20T17:28:44.433+08:00  INFO 17707 --- [  restartedMain] b.w.c.s.WebApplicationContextInitializer : Root WebApplicationContext: initialization completed in 693 ms
2026-11-20T17:28:44.623+08:00  INFO 17707 --- [  restartedMain] org.hibernate.Version                    : HHH000412: Hibernate ORM core version 5.6.15.Final
2026-11-20T17:28:44.776+08:00  INFO 17707 --- [  restartedMain] o.hibernate.annotations.common.Version   : HCANN000001: Hibernate Commons Annotations {5.1.2.Final}
2026-11-20T17:28:44.818+08:00  INFO 17707 --- [  restartedMain] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
2026-11-20T17:28:44.911+08:00  INFO 17707 --- [  restartedMain] com.zaxxer.hikari.pool.HikariPool        : HikariPool-1 - Added connection conn0: url=jdbc:h2:file:./build/boot_dev user=SA
2026-11-20T17:28:44.912+08:00  INFO 17707 --- [  restartedMain] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Start completed.
2026-11-20T17:28:44.923+08:00  INFO 17707 --- [  restartedMain] org.hibernate.dialect.Dialect            : HHH000400: Using dialect: org.hibernate.dialect.H2Dialect
Hibernate: drop table if exists comment CASCADE
Hibernate: drop table if exists post CASCADE
Hibernate: create table comment (id bigint generated by default as identity, version bigint not null, text varchar(255) not null, primary key (id))
Hibernate: create table post (id bigint generated by default as identity, version bigint not null, title varchar(255) not null, primary key (id))
2026-11-20T17:28:45.374+08:00  WARN 17707 --- [  restartedMain] JpaBaseConfiguration$JpaWebConfiguration : spring.jpa.open-in-view is enabled by default. Therefore, database queries may be performed during view rendering. Explicitly configure spring.jpa.open-in-view to disable this warning
2026-11-20T17:28:45.585+08:00  INFO 17707 --- [  restartedMain] o.s.b.a.e.web.EndpointLinksResolver      : Exposing 14 endpoints beneath base path '/actuator'
2026-11-20T17:28:45.635+08:00  INFO 17707 --- [  restartedMain] o.s.boot.tomcat.TomcatWebServer          : Tomcat started on port 8080 (http) with context path '/'
2026-11-20T17:28:45.659+08:00  INFO 17707 --- [  restartedMain] grace.guides.GraceApplication            : Started GraceApplication in 2.144 seconds (process running for 2.487)
2026-11-20T17:28:45.663+08:00 DEBUG 17707 --- [  restartedMain] PluginsInfoApplicationContextInitializer :
----------------------------------------------------------------------------------------------------------
Order      Plugin Name                        Plugin Version                                       Enabled
----------------------------------------------------------------------------------------------------------
    1      Core                               2025.0.0-M3                                                Y
    2      DataSource                         2025.0.0-M3                                                Y
    3      DomainClass                        2025.0.0-M3                                                Y
    4      Hibernate                          2025.0.0-M3                                                Y
----------------------------------------------------------------------------------------------------------

Prepare Sample Data >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
Hibernate: insert into post (id, version, title) values (default, ?, ?)
Hibernate: insert into post (id, version, title) values (default, ?, ?)
Hibernate: insert into post (id, version, title) values (default, ?, ?)
Hibernate: insert into post (id, version, title) values (default, ?, ?)
Hibernate: insert into post (id, version, title) values (default, ?, ?)
Hibernate: insert into post (id, version, title) values (default, ?, ?)
Hibernate: insert into post (id, version, title) values (default, ?, ?)
Hibernate: insert into post (id, version, title) values (default, ?, ?)
Hibernate: insert into post (id, version, title) values (default, ?, ?)
Hibernate: insert into post (id, version, title) values (default, ?, ?)
Hibernate: insert into post (id, version, title) values (default, ?, ?)
Hibernate: insert into post (id, version, title) values (default, ?, ?)
Hibernate: insert into post (id, version, title) values (default, ?, ?)
Hibernate: insert into post (id, version, title) values (default, ?, ?)
Hibernate: insert into post (id, version, title) values (default, ?, ?)
Hibernate: insert into post (id, version, title) values (default, ?, ?)
Hibernate: insert into post (id, version, title) values (default, ?, ?)
Hibernate: insert into post (id, version, title) values (default, ?, ?)
Hibernate: insert into post (id, version, title) values (default, ?, ?)
Hibernate: insert into comment (id, version, text) values (default, ?, ?)

Load All Posts >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
Hibernate: select this_.id as id1_1_0_, this_.version as version2_1_0_, this_.title as title3_1_0_ from post this_
#1 Grace 2022.2.0 is released
#2 Grace 2022.2.1 is released
#3 Grace 2022.2.2 is released
#4 Grace 2022.2.3 is released
#5 Grace 2022.2.4 is released
#6 Grace 2022.2.5 is released
#7 Grace 2022.2.6 is released
#8 Grace 2022.2.7 is released
#9 Grace 2022.2.8 is released
#10 Grace 2022.2.9 is released
#11 Grace 2023.0.0 is released
#12 Grace 2023.1.0 is released
#13 Grace 2023.2.0 is released
#14 Grace 2023.3.0 is released
#15 Grace 2024.0.0 is released
#16 Grace 2024.1.0 is released
#17 Grace 2024.2.0 is released
#18 What's new in Grace 2024.2.0?
#19 Grace 2025.0.0-M3 is also available now!

Load All Comments >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
Hibernate: select comment0_.id as id1_0_, comment0_.version as version2_0_, comment0_.text as text3_0_ from comment comment0_
Wow, Grace is awesome!
```

# Links

* [Grace Framework](https://github.com/graceframework/grace-framework)
* [Spring Boot with Plugins](https://github.com/grace-guides/gs-spring-boot)
* [Spring Boot with GSP](https://github.com/grace-guides/gs-spring-boot-gsp)
